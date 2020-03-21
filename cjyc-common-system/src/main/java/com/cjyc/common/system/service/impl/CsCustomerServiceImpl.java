package com.cjyc.common.system.service.impl;

import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.ReturnMsg;
import com.cjkj.common.redis.template.StringRedisUtil;
import com.cjkj.usercenter.dto.common.*;
import com.cjyc.common.model.constant.Constant;
import com.cjyc.common.model.dao.ICustomerDao;
import com.cjyc.common.model.dto.salesman.customer.SalesCustomerDto;
import com.cjyc.common.model.entity.Driver;
import com.cjyc.common.model.entity.Role;
import com.cjyc.common.model.dto.salesman.mine.AppCustomerIdDto;
import com.cjyc.common.model.enums.PayModeEnum;
import com.cjyc.common.model.enums.SendNoTypeEnum;
import com.cjyc.common.model.enums.customer.CustomerSourceEnum;
import com.cjyc.common.model.enums.role.DeptTypeEnum;
import com.cjyc.common.model.enums.role.RoleNameEnum;
import com.cjyc.common.model.util.JsonUtils;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.salesman.customer.SalesCustomerListVo;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.enums.customer.CustomerTypeEnum;
import com.cjyc.common.model.exception.ParameterException;
import com.cjyc.common.model.exception.ServerException;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.YmlProperty;
import com.cjyc.common.model.vo.salesman.customer.SalesCustomerVo;
import com.cjyc.common.model.vo.salesman.customer.SalesKeyCustomerListVo;
import com.cjyc.common.model.vo.salesman.customer.SalesKeyCustomerVo;
import com.cjyc.common.model.vo.salesman.mine.AppContractVo;
import com.cjyc.common.system.feign.ISysRoleService;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.system.feign.ISysUserService;
import com.cjyc.common.system.service.ICsCustomerService;
import com.cjyc.common.system.service.ICsRoleService;
import com.cjyc.common.system.service.ICsSendNoService;
import com.cjyc.common.system.service.ICsUserRoleDeptService;
import com.cjyc.common.system.util.ResultDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 客户公用业务
 * @author JPG
 */
@Service
@Slf4j
@Transactional(rollbackFor = RuntimeException.class)
public class CsCustomerServiceImpl implements ICsCustomerService {

    private static final String CUSTOMER_FIXED_PWD = YmlProperty.get("cjkj.customer.password");
    private static final String CUSTOMER_FIXED_DEPTID = YmlProperty.get("cjkj.dept_customer_id");

    @Resource
    private ICustomerDao customerDao;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private ISysRoleService sysRoleService;
    @Resource
    private StringRedisUtil redisUtil;
    @Resource
    private ICsRoleService csRoleService;
    @Resource
    private ICsCustomerService csCustomerService;
    @Resource
    private ICsSendNoService sendNoService;
    @Resource
    private ICsUserRoleDeptService csUserRoleDeptService;



    @Override
    public Customer getByUserId(Long userId, boolean isSearchCache) {
        return customerDao.findByUserId(userId);
    }

    /**
     * 根据手机号查询客户
     *
     * @param customerPhone
     * @param isCache
     * @author JPG
     * @since 2019/11/5 9:13
     */
    @Override
    public Customer getByPhone(String customerPhone, boolean isCache) {
        return customerDao.findByPhone(customerPhone);
    }

    @Override
    public ResultData<Long> addCustomerToPlatform(Customer customer) {
        ResultData<UserResp> accountRd = sysUserService.getByAccount(customer.getContactPhone());
        if (!ReturnMsg.SUCCESS.getCode().equals(accountRd.getCode())) {
            return ResultData.failed("获取用户信息失败，原因：" + accountRd.getMsg());
        }
        if (accountRd.getData() != null) {
            //存在，则直接返回已有用户userId信息
            return ResultData.ok(accountRd.getData().getUserId());
        }
        //不存在，需要重新添加
        AddUserReq user = new AddUserReq();
        user.setName(customer.getName());
        user.setAccount(customer.getContactPhone());
        user.setMobile(customer.getContactPhone());
        user.setDeptId(Long.parseLong(YmlProperty.get("cjkj.dept_customer_id")));
        user.setPassword(YmlProperty.get("cjkj.customer.password"));
        if (CustomerTypeEnum.COOPERATOR.code == customer.getType()) {
            user.setRoleIdList(Arrays.asList(
                    Long.parseLong(YmlProperty.get("cjkj.customer.partner_role_id"))));
        }
        ResultData<AddUserResp> saveRd = sysUserService.save(user);
        if (!ReturnMsg.SUCCESS.getCode().equals(saveRd.getCode())) {
            return ResultData.failed("保存客户信息失败，原因：" + saveRd.getMsg());
        }
        return ResultData.ok(saveRd.getData().getUserId());
    }

    @Override
    public ResultData<Boolean> updateCustomerToPlatform(Customer customer, String newPhone) {
        String oldPhone = customer.getContactPhone();
        if (!oldPhone.equals(newPhone)) {
            //新旧账号不相同需要替换手机号
            ResultData<UserResp> accountRd = sysUserService.getByAccount(newPhone);
            if (!ReturnMsg.SUCCESS.getCode().equals(accountRd.getCode())) {
                return ResultData.failed("用户信息获取失败，原因：" + accountRd.getMsg());
            }
            if (accountRd.getData() != null) {
                return ResultData.failed("用户账号不允许修改，预修改账号：" + newPhone + " 已存在");
            }
            UpdateUserReq user = new UpdateUserReq();
            user.setUserId(customer.getUserId());
            user.setAccount(newPhone);
            user.setMobile(newPhone);
            ResultData rd = sysUserService.updateUser(user);
            if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
                return ResultData.failed("用户信息修改失败，原因：" + rd.getMsg());
            }
            return ResultData.ok(true);
        }
        return ResultData.ok(false);
    }

    @Override
    public Customer getById(Long userId, boolean isSearchCache) {
        return customerDao.selectById(userId);
    }

    @Override
    public Customer validate(Long loginId) {
        Customer customer = getById(loginId, true);
        if(customer == null){
            throw new ParameterException("用户不存在");
        }
        return customer;
    }

    @Override
    public ResultVo<SalesCustomerListVo> findCustomer(SalesCustomerDto dto) {
        List<SalesCustomerVo> customerVos = customerDao.findCustomerPhoneName(dto);
        SalesCustomerListVo listVo = new SalesCustomerListVo();
        listVo.setList(customerVos);
        return BaseResultUtil.success(listVo);
    }

    @Override
    public ResultVo<Long> findRoleId(List<SelectRoleResp> roleResps) {
        Long roleId = null;
        if(!CollectionUtils.isEmpty(roleResps)){
            for(SelectRoleResp roleResp : roleResps){
                //合伙人
                if(roleResp.getRoleName().equals(RoleNameEnum.PARTNER.getName())){
                    roleId = roleResp.getRoleId();
                    break;
                }
            }
        }
        return BaseResultUtil.success(roleId);
    }

    @Override
    public ResultVo<SalesKeyCustomerListVo> findKeyCustomer(SalesCustomerDto dto) {
        log.info("大客户查询请求json数据 :: "+ JsonUtils.objectToJson(dto));
        List<SalesKeyCustomerVo> salesKeyCustomter = customerDao.findSalesKeyCustomter(dto);
        SalesKeyCustomerListVo listVo = new SalesKeyCustomerListVo();
        listVo.setList(salesKeyCustomter);
        return BaseResultUtil.success(listVo);
    }

    @Override
    public ResultVo<AppContractVo> findCustomerContract(AppCustomerIdDto dto) {
        Long now = LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now());
        List<Map<String,Object>> contractList = customerDao.getContractByCustomerId(dto.getCustomerId(),now);
        AppContractVo vo = new AppContractVo();
        vo.setList(contractList);
        return BaseResultUtil.success(vo);
    }


    /************************************韵车集成改版 st***********************************/
    @Override
    public ResultData<Long> addUserToPlatform(String phone, String name, Role role) {
        ResultData<UserResp> rd = sysUserService.getByAccount(phone);
        if(!ResultDataUtil.isSuccess(rd)){
            return ResultData.failed("获取用户信息失败，原因：" + rd.getMsg());
        }
        if(rd.getData() != null){
            //用户存在，需要判断是否需要将用户、角色关系维护
            ResultData<List<SelectRoleResp>> rolesRd =
                    sysRoleService.getListByUserId(rd.getData().getUserId());
            if(!ResultDataUtil.isSuccess(rolesRd)){
                return ResultData.failed("查询用户下角色列表错误，原因：" + rolesRd.getMsg());
            }
            UpdateUserReq updateUserReq = null;
            if(!CollectionUtils.isEmpty(rolesRd.getData())){
                //存在角色
                List<Long> roleIdList = rolesRd.getData().stream()
                        .map(r -> r.getRoleId()).collect(Collectors.toList());
                if(roleIdList.contains(role.getRoleId())){
                    return ResultData.ok(rd.getData().getUserId());
                }else{
                    roleIdList.add(role.getRoleId());
                    updateUserReq = new UpdateUserReq();
                    updateUserReq.setUserId(rd.getData().getUserId());
                    updateUserReq.setRoleIdList(roleIdList);
                    ResultData updateUserRd = sysUserService.update(updateUserReq);
                    if (!ResultDataUtil.isSuccess(updateUserRd)) {
                        return ResultData.failed("更新用户信息错误，原因：" + updateUserRd.getMsg());
                    }else {
                        return ResultData.ok(rd.getData().getUserId());
                    }
                }
            }else{
                //不存在角色
                updateUserReq = new UpdateUserReq();
                updateUserReq.setUserId(rd.getData().getUserId());
                updateUserReq.setRoleIdList(Arrays.asList(role.getRoleId()));
                ResultData updateUserRd = sysUserService.update(updateUserReq);
                if(!ResultDataUtil.isSuccess(updateUserRd)){
                    return ResultData.failed("更新用户信息错误，原因：" + updateUserRd.getMsg());
                }else{
                    return ResultData.ok(rd.getData().getUserId());
                }
            }
        }else{
            //不存在，需要重新添加
            AddUserReq user = new AddUserReq();
            user.setName(name);
            user.setAccount(phone);
            user.setMobile(phone);
            user.setDeptId(Long.parseLong(YmlProperty.get("cjkj.dept_admin_id")));
            user.setPassword(YmlProperty.get("cjkj.customer.password"));
            user.setRoleIdList(Arrays.asList(role.getRoleId()));
            ResultData<AddUserResp> saveRd = sysUserService.save(user);
            if(!ReturnMsg.SUCCESS.getCode().equals(saveRd.getCode())){
                return ResultData.failed("保存客户信息失败，原因：" + saveRd.getMsg());
            }
            return ResultData.ok(saveRd.getData().getUserId());
        }
    }

    @Override
    public ResultData<Boolean> updateUserToPlatform(Customer customer, Driver driver, String newPhone,String newName) {
        String oldPhone = null;
        if(customer != null){
            oldPhone = customer.getContactPhone();
        }else{
            oldPhone = driver.getPhone();
        }
        if (!oldPhone.equals(newPhone)) {
            //新旧账号不相同需要替换手机号
            ResultData<UserResp> accountRd = sysUserService.getByAccount(newPhone);
            if (!ReturnMsg.SUCCESS.getCode().equals(accountRd.getCode())) {
                return ResultData.failed("用户信息获取失败，原因：" + accountRd.getMsg());
            }
            if (accountRd.getData() != null) {
                return ResultData.failed("用户账号不允许修改，预修改账号：" + newPhone + " 已存在");
            }
            ResultData rd = updateData(customer, driver, newPhone, newName);
            if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
                return ResultData.failed("用户信息修改失败，原因：" + rd.getMsg());
            }
            return ResultData.ok(true);
        }else if(oldPhone.equals(newPhone)){
            if((customer != null && !customer.getName().equals(newName))
                || (driver != null && !driver.getName().equals(newName))){
                UpdateUserReq user = new UpdateUserReq();
                if(customer != null){
                    user.setUserId(customer.getUserId());
                }else{
                    user.setUserId(driver.getUserId());
                }
                user.setName(newName);
                ResultData rd = sysUserService.updateUser(user);
                if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
                    return ResultData.failed("用户姓名修改失败，原因：" + rd.getMsg());
                }
            }
            return ResultData.ok(true);
        }
        return ResultData.ok(false);
    }

    @Override
    public ResultVo<Customer> saveCustomer(String customerPhone,String customerName,Long loginId) {
        Role role = csRoleService.getByName(YmlProperty.get("cjkj.customer_client_role_name"), DeptTypeEnum.CUSTOMER.code);
        if(role == null){
            return BaseResultUtil.fail("C端客户角色不存在，请先添加");
        }
        //新增个人用户信息到物流平台
        ResultData<Long> rd = csCustomerService.addUserToPlatform(customerPhone,customerName,role);
        if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
            return BaseResultUtil.fail(rd.getMsg());
        }
        if(rd.getData() == null){
            return BaseResultUtil.fail("获取架构组userId失败");
        }
        Customer customer = new Customer();
        customer.setUserId(rd.getData());
        customer.setCustomerNo(sendNoService.getNo(SendNoTypeEnum.CUSTOMER));
        customer.setAlias(customerName);
        customer.setName(customerName);
        customer.setContactMan(customerName);
        customer.setContactPhone(customerPhone);
        customer.setType(CustomerTypeEnum.INDIVIDUAL.code);
        customer.setPayMode(PayModeEnum.COLLECT.code);
        customer.setSource(CustomerSourceEnum.WEB.code);
        customer.setCreateUserId(loginId);
        customer.setCreateTime(System.currentTimeMillis());
        customer.setCheckUserId(loginId);
        customer.setCheckTime(System.currentTimeMillis());
        customerDao.insert(customer);
        //保存用户角色机构关系
        csUserRoleDeptService.saveCustomerToUserRoleDept(customer, role.getId(), loginId);
        return BaseResultUtil.success(customer);
    }

    @Override
    public boolean validateActive(Long id) {
        Customer customer = customerDao.findActive(id);
        return customer != null;
    }

    @Override
    public ResultVo<Customer> validateAndGetActive(Long id) {
        Customer customer = customerDao.findActive(id);
        if(customer == null){
            return BaseResultUtil.fail("用户状态不可用");
        }
        return BaseResultUtil.success(customer);
    }

    private ResultData updateData(Customer customer,Driver driver,String newPhone,String newName){
        UpdateUserReq user = new UpdateUserReq();
        if(customer != null){
            user.setUserId(customer.getUserId());
        }else{
            user.setUserId(driver.getUserId());
        }
        user.setName(newName);
        user.setAccount(newPhone);
        user.setMobile(newPhone);
        ResultData rd = sysUserService.updateUser(user);
        return rd;
    }
}
