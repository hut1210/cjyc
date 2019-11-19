package com.cjyc.common.system.service.impl;

import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.ReturnMsg;
import com.cjkj.common.redis.template.StringRedisUtil;
import com.cjkj.usercenter.dto.common.AddUserReq;
import com.cjkj.usercenter.dto.common.AddUserResp;
import com.cjkj.usercenter.dto.common.UpdateUserReq;
import com.cjyc.common.model.dao.ICustomerDao;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.exception.ServerException;
import com.cjyc.common.model.util.YmlProperty;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.system.feign.ISysUserService;
import com.cjyc.common.system.service.ICsCustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 客户公用业务
 * @author JPG
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class CsCustomerServiceImpl implements ICsCustomerService {

    private static final String CUSTOMER_FIXED_PWD = YmlProperty.get("cjkj.customer.password");
    private static final String CUSTOMER_FIXED_DEPTID = YmlProperty.get("cjkj.dept_customer_id");

    @Resource
    private ICustomerDao customerDao;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private StringRedisUtil redisUtil;

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
    public Customer save(Customer customer) {
        //添加架构组数据
        AddUserReq addUserReq = new AddUserReq();
        addUserReq.setAccount(customer.getContactPhone());
        addUserReq.setPassword(CUSTOMER_FIXED_PWD);
        addUserReq.setDeptId(Long.valueOf(CUSTOMER_FIXED_DEPTID));
        addUserReq.setMobile(customer.getContactPhone());
        addUserReq.setName(customer.getName());
        ResultData<AddUserResp> resultData = sysUserService.save(addUserReq);

        if(resultData == null || resultData.getData() == null || resultData.getData().getUserId() == null){
            throw new ServerException(resultData == null ? "添加用户失败" : resultData.getMsg());
        }
        customer.setUserId(resultData.getData().getUserId());
        customerDao.insert(customer);
        return customer;
    }

    @Override
    public ResultData<Long> addCustomerToPlatform(Customer customer) {
        ResultData<AddUserResp> accountRd =
                sysUserService.getByAccount(customer.getContactPhone());
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
            ResultData<AddUserResp> accountRd = sysUserService.getByAccount(newPhone);
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
}
