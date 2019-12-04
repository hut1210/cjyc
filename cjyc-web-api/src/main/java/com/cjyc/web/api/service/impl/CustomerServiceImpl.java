package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.ReturnMsg;
import com.cjkj.usercenter.dto.common.AddUserReq;
import com.cjkj.usercenter.dto.common.AddUserResp;
import com.cjkj.usercenter.dto.common.UpdateUserReq;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.customer.*;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.*;
import com.cjyc.common.model.enums.customer.CustomerPayEnum;
import com.cjyc.common.model.enums.customer.CustomerSourceEnum;
import com.cjyc.common.model.enums.customer.CustomerStateEnum;
import com.cjyc.common.model.enums.customer.CustomerTypeEnum;
import com.cjyc.common.model.exception.ServerException;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.util.RandomUtil;
import com.cjyc.common.model.util.YmlProperty;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.customer.*;
import com.cjyc.common.model.vo.web.coupon.CustomerCouponSendVo;
import com.cjyc.common.system.feign.ISysUserService;
import com.cjyc.common.system.service.ICsCustomerService;
import com.cjyc.common.system.service.ICsSendNoService;
import com.cjyc.web.api.service.ICustomerContractService;
import com.cjyc.web.api.service.ICustomerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

/**
 *  @author: zj
 *  @Date: 2019/9/29 15:20
 *  @Description: 移动用户
 */
@Service
@Slf4j
public class CustomerServiceImpl extends ServiceImpl<ICustomerDao,Customer> implements ICustomerService{

    @Resource
    private ICustomerDao customerDao;

    @Resource
    private ICustomerContractDao customerContractDao;

    @Resource
    private ICustomerContractService customerContractService;

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private ICustomerPartnerDao customerPartnerDao;

    @Resource
    private IBankCardBindDao bankCardBindDao;

    @Resource
    private ICouponDao couponDao;

    @Resource
    private ICouponSendDao couponSendDao;

    @Resource
    private IAdminDao adminDao;

    @Resource
    private IDriverDao driverDao;

    @Resource
    private ICustomerCountDao customerCountDao;

    @Resource
    private ICsSendNoService sendNoService;

    @Resource
    private ICsCustomerService csCustomerService;

    private static final Long NOW = LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now());

    @Override
    public ResultVo<CustomerInfoVo> findCustomerInfo(ExistCustomreDto dto) {
        Customer customer = customerDao.findByPhone(dto.getPhone());
        CustomerInfoVo infoVo = new CustomerInfoVo();
        if(customer != null){
            infoVo.setCustomerId(customer.getId());
            infoVo.setContactMan(customer.getContactMan());
            infoVo.setContactPhone(customer.getContactPhone());
            return BaseResultUtil.success(infoVo);
        }
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo existCustomer(ExistCustomreDto dto) {
        Customer c = this.getOne(new QueryWrapper<Customer>().lambda()
                        .eq(Customer::getContactPhone, dto.getPhone())
                        .ne((dto.getCustomerId() != null && dto.getCustomerId() != 0),Customer::getId,dto.getCustomerId()));
        if(c != null){
            return BaseResultUtil.fail("该客户已存在，请检查");
        }
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo saveCustomer(CustomerDto dto) {
        //判断该手机号是否在库中存在
        Customer cust = customerDao.selectOne(new QueryWrapper<Customer>().lambda().eq(Customer::getContactPhone, dto.getContactPhone()));
        if(cust != null){
            return BaseResultUtil.fail("该客户已存在，请检查");
        }
        Customer customer = new Customer();
        BeanUtils.copyProperties(dto,customer);
        customer.setCustomerNo(sendNoService.getNo(SendNoTypeEnum.CUSTOMER));
        customer.setAlias(dto.getContactMan());
        customer.setName(dto.getContactMan());
        customer.setIsDelete(DeleteStateEnum.NO_DELETE.code);
        customer.setType(CustomerTypeEnum.INDIVIDUAL.code);
        customer.setState(CustomerStateEnum.CHECKED.code);
        customer.setPayMode(CustomerPayEnum.TIME_PAY.code);
        customer.setSource(CustomerSourceEnum.WEB.code);
        customer.setRegisterTime(NOW);
        customer.setCreateUserId(dto.getLoginId());
        customer.setCreateTime(NOW);
        //新增个人用户信息到物流平台
        ResultData<Long> rd = csCustomerService.addCustomerToPlatform(customer);
        if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
            return BaseResultUtil.fail(rd.getMsg());
        }
        customer.setUserId(rd.getData());
        super.save(customer);
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo modifyCustomer(CustomerDto dto) {
        //判断该手机号是否在库中存在
        Customer cust = customerDao.selectOne(new QueryWrapper<Customer>().lambda().eq(Customer::getContactPhone, dto.getContactPhone()));
        if(cust != null && !cust.getId().equals(dto.getCustomerId())){
            return BaseResultUtil.fail("该客户已存在，请检查");
        }
        Customer customer = customerDao.selectById(dto.getCustomerId());
        if(null != customer){
            ResultData<Boolean> updateRd = csCustomerService.updateCustomerToPlatform(customer, dto.getContactPhone());
            if (!ReturnMsg.SUCCESS.getCode().equals(updateRd.getCode())) {
                log.error("修改用户信息失败，原因：" + updateRd.getMsg());
                return BaseResultUtil.fail("修改用户信息失败，原因：" + updateRd.getMsg());
            }
            if (updateRd.getData()) {
                //需要同步手机号信息
                syncPhone(customer.getContactPhone(), dto.getContactPhone());
            }
            BeanUtils.copyProperties(dto,customer);
            customer.setName(dto.getContactMan());
            customer.setAlias(dto.getContactMan());
            customer.setCheckUserId(dto.getLoginId());
            customer.setCheckTime(NOW);
            super.updateById(customer);
        }
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo findCustomer(SelectCustomerDto dto) {
        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        List<CustomerVo> customerVos = customerDao.findCustomer(dto);
        if(!CollectionUtils.isEmpty(customerVos)){
            for(CustomerVo vo : customerVos){
                CustomerCountVo count = customerCountDao.count(vo.getCustomerId());
                if(count != null){
                    vo.setTotalOrder(count.getTotalOrder());
                    vo.setTotalCar(count.getTotalCar());
                    vo.setTotalAmount(count.getTotalAmount());
                }
            }
        }
        PageInfo<CustomerVo> pageInfo =  new PageInfo<>(customerVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo saveOrModifyKey(KeyCustomerDto dto) {
        //判断该手机号是否在库中存在
        Customer cust = customerDao.selectOne(new QueryWrapper<Customer>().lambda().eq(Customer::getContactPhone, dto.getContactPhone()));
        if((cust != null && dto.getCustomerId() == null) || (cust != null && !cust.getId().equals(dto.getCustomerId()))){
            return BaseResultUtil.fail("该客户已存在，请检查");
        }
        if(dto.getCustomerId() == null){
            //新增大客户
            Customer customer = new Customer();
            BeanUtils.copyProperties(dto,customer);
            customer.setCustomerNo(sendNoService.getNo(SendNoTypeEnum.CUSTOMER));
            customer.setAlias(dto.getName());
            customer.setIsDelete(DeleteStateEnum.NO_DELETE.code);
            customer.setType(CustomerTypeEnum.ENTERPRISE.code);
            customer.setState(CustomerStateEnum.WAIT_LOGIN.code);
            customer.setSource(CustomerSourceEnum.WEB.code);
            customer.setRegisterTime(NOW);
            customer.setCreateTime(NOW);
            customer.setCreateUserId(dto.getLoginId());

            //保存大客户信息到物流平台
            ResultData<Long> rd = csCustomerService.addCustomerToPlatform(customer);
            if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
                log.error("保存大客户信息失败，原因：" + rd.getMsg());
                return BaseResultUtil.fail("保存大客户信息失败，原因：" + rd.getMsg());
            }
            customer.setUserId(rd.getData());
            super.save(customer);
            //合同集合
            List<CustomerContractDto> customerConList = dto.getCustContraVos();
            List<CustomerContract> list = encapCustomerContract(customer.getId(),customerConList);
            customerContractService.saveBatch(list);
        }else{
            return modifyKey(dto);
        }
        return BaseResultUtil.success();
    }

    private ResultVo modifyKey(KeyCustomerDto dto){
        Customer customer = customerDao.selectById(dto.getCustomerId());
        if(null != customer){
            //判断手机号是否存在
            ResultData<Boolean> updateRd = csCustomerService.updateCustomerToPlatform(customer, dto.getContactPhone());
            if (!ReturnMsg.SUCCESS.getCode().equals(updateRd.getCode())) {
                log.error("修改用户信息失败，原因：" + updateRd.getMsg());
                return BaseResultUtil.fail("修改用户信息失败，原因：" + updateRd.getMsg());
            }
            if (updateRd.getData()) {
                //需要同步手机号信息
                syncPhone(customer.getContactPhone(), dto.getContactPhone());
            }
            BeanUtils.copyProperties(dto,customer);
            customer.setId(dto.getCustomerId());
            customer.setAlias(dto.getName());
            super.updateById(customer);

            List<CustomerContractDto> contractDtos = dto.getCustContraVos();
            List<CustomerContract> list = null;
            if(!CollectionUtils.isEmpty(contractDtos)){
                //批量删除
                customerContractDao.removeKeyContract(dto.getCustomerId());
                list = encapCustomerContract(customer.getId(),contractDtos);
                customerContractService.saveBatch(list);
            }
        }
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo verifyCustomer(OperateDto dto) {
        Customer customer = customerDao.selectById(dto.getId());
        if(customer != null){
            if(FlagEnum.DELETE.code == dto.getFlag()){
               //假删除
                customer.setIsDelete(DeleteStateEnum.YES_DELETE.code);
            }else if(FlagEnum.AUDIT_PASS.code == dto.getFlag()){
                //审核通过
                customer.setState(CustomerStateEnum.CHECKED.code);
            }else if(FlagEnum.AUDIT_REJECT.code == dto.getFlag()){
                //审核拒绝
                customer.setState(CustomerStateEnum.REJECT.code);
            }
            customer.setCheckTime(NOW);
            customer.setCheckUserId(dto.getLoginId());
            super.updateById(customer);
        }
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo<ShowKeyCustomerVo> showKeyCustomer(Long customerId) {
        ShowKeyCustomerVo sKeyCustomerDto = new ShowKeyCustomerVo();
        //根据主键id查询大客户
        Customer customer = customerDao.selectById(customerId);
        if(customer != null){
            BeanUtils.copyProperties(customer,sKeyCustomerDto);
        }
        //根据customer_id查询大客户的合同
        List<CustomerContractVo> contractVos = customerContractDao.getCustContractByCustId(customer.getId());
        if(!CollectionUtils.isEmpty(contractVos)){
            sKeyCustomerDto.setCustContraVos(contractVos);
        }
        return BaseResultUtil.success(sKeyCustomerDto);
    }

    @Override
    public ResultVo<PageVo<ListKeyCustomerVo>> findKeyCustomer(SelectKeyCustomerDto dto) {
        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        List<ListKeyCustomerVo> keyCustomerList = customerDao.findKeyCustomter(dto);
        if(!CollectionUtils.isEmpty(keyCustomerList)){
            for(ListKeyCustomerVo vo : keyCustomerList){
                CustomerCountVo count = customerCountDao.count(vo.getCustomerId());
                if(count != null){
                    vo.setTotalOrder(count.getTotalOrder());
                    vo.setTotalCar(count.getTotalCar());
                    vo.setTotalAmount(count.getTotalAmount());
                }
            }
        }
        PageInfo<ListKeyCustomerVo> pageInfo = new PageInfo<>(keyCustomerList);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public boolean save(Customer customer) {
        //添加架构组数据
        AddUserReq addUserReq = new AddUserReq();
        addUserReq.setAccount(customer.getContactPhone());
        addUserReq.setPassword(YmlProperty.get("cjkj.web.password"));
        addUserReq.setDeptId(Long.valueOf(YmlProperty.get("cjkj.dept_customer_id")));
        addUserReq.setMobile(customer.getContactPhone());
        addUserReq.setName(customer.getName());
        ResultData<AddUserResp> resultData = sysUserService.save(addUserReq);

        if(resultData == null || resultData.getData() == null || resultData.getData().getUserId() == null){
            throw new ServerException("添加用户失败");
        }
        customer.setUserId(resultData.getData().getUserId());
        return super.save(customer);
    }

    @Override
    public Customer selectById(Long customerId) {
        return customerDao.selectById(customerId);
    }

    @Override
    public ResultVo saveOrModifyPartner(PartnerDto dto) {
        //新增/修改时，验证在大客户或者合伙人中是否存在
        Customer c = customerDao.selectOne(new QueryWrapper<Customer>().lambda()
                .eq(Customer::getContactPhone,dto.getContactPhone())
                .ne(Customer::getType,1)
                .ne((dto.getCustomerId() != null),Customer::getId,dto.getCustomerId()));
        if(c != null){
            return BaseResultUtil.getVo(ResultEnum.EXIST_CUSTOMER.getCode(),ResultEnum.EXIST_CUSTOMER.getMsg());
        }
        //新增/修改时，验证在C端用户中是否存在
        c =  customerDao.selectOne(new QueryWrapper<Customer>().lambda()
                .eq(Customer::getContactPhone,dto.getContactPhone())
                .eq(Customer::getType,1));
        if(c != null){
            if(dto.getFlag()){
                if(c.getIsDelete() == DeleteStateEnum.YES_DELETE.code){
                    //已删除
                    return BaseResultUtil.getVo(ResultEnum.DELETE_CUSTOMER.getCode(),ResultEnum.DELETE_CUSTOMER.getMsg());
                }
                //前端重置为true，升级为合伙人
                Customer cu = customerDao.selectById(dto.getCustomerId());
                if(cu != null){
                    cu.setIsDelete(DeleteStateEnum.YES_DELETE.code);
                    super.updateById(cu);
                }
                BeanUtils.copyProperties(dto,c);
                c.setAlias(dto.getName());
                c.setType(CustomerTypeEnum.COOPERATOR.code);
                c.setSource(CustomerSourceEnum.UPGRADE.code);
                c.setState(CommonStateEnum.WAIT_CHECK.code);
                c.setIsDelete(DeleteStateEnum.NO_DELETE.code);
                c.setCreateUserId(dto.getLoginId());
                c.setCreateTime(NOW);
                super.updateById(c);
                //合伙人附加信息
                encapPartner(dto,c,NOW);
                return BaseResultUtil.success();
            }else{
                //返回前端，flag重置为true
                return BaseResultUtil.getVo(ResultEnum.UPGRADE_CUSTOMER.getCode(),ResultEnum.UPGRADE_CUSTOMER.getMsg());
            }
        }
        if(dto.getCustomerId() == null){
            return addPartner(dto);
        }else{
            //修改
           return modifyPartner(dto);
        }
    }

    /**
     * 新增合伙人
     * @param dto
     * @return
     */
    private ResultVo addPartner(PartnerDto dto){
        //新增c_customer
        Customer customer = new Customer();
        BeanUtils.copyProperties(dto,customer);
        customer.setAlias(dto.getName());
        customer.setCustomerNo(sendNoService.getNo(SendNoTypeEnum.CUSTOMER));
        customer.setIsDelete(DeleteStateEnum.NO_DELETE.code);
        customer.setSource(CustomerSourceEnum.WEB.code);
        customer.setType(CustomerTypeEnum.COOPERATOR.code);
        customer.setState(CommonStateEnum.WAIT_CHECK.code);
        customer.setRegisterTime(NOW);
        customer.setCreateTime(NOW);
        customer.setCreateUserId(dto.getLoginId());

        //新增用户信息到物流平台
        ResultData<Long> rd = csCustomerService.addCustomerToPlatform(customer);
        if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
            return BaseResultUtil.fail(rd.getMsg());
        }
        customer.setUserId(rd.getData());
        customerDao.insert(customer);
        encapPartner(dto,customer,NOW);
        return BaseResultUtil.success();
    }

    /**
     * 修改合伙人
     * @param dto
     * @return
     */
    private ResultVo modifyPartner(PartnerDto dto){
        Customer customer = customerDao.selectById(dto.getCustomerId());
        if(customer != null){
            //判断手机号是否存在
            ResultData<Boolean> updateRd = csCustomerService.updateCustomerToPlatform(customer, dto.getContactPhone());
            if (!ReturnMsg.SUCCESS.getCode().equals(updateRd.getCode())) {
                log.error("修改用户信息失败，原因：" + updateRd.getMsg());
                return BaseResultUtil.fail("修改用户信息失败，原因：" + updateRd.getMsg());
            }
            if (updateRd.getData()) {
                //需要同步手机号信息
                syncPhone(customer.getContactPhone(), dto.getContactPhone());
            }
            BeanUtils.copyProperties(dto,customer);
            customer.setAlias(dto.getName());
            super.updateById(customer);
            //删除合伙人附加信息
            customerPartnerDao.removeByCustomerId(customer.getId());
            //删除合伙人银行卡信息
            bankCardBindDao.removeBandCarBind(customer.getId());
            encapPartner(dto,customer,NOW);
        }
        return BaseResultUtil.success();
    }
    @Override
    public ResultVo<PageVo<CustomerPartnerVo>> findPartner(CustomerPartnerDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<CustomerPartnerVo> partnerVos = customerDao.getPartnerByTerm(dto);
        if(!CollectionUtils.isEmpty(partnerVos)){
            for(CustomerPartnerVo partnerVo : partnerVos){
                CustomerCountVo count = customerCountDao.count(partnerVo.getCustomerId());
                if(count != null){
                    partnerVo.setTotalOrder(count.getTotalOrder());
                    partnerVo.setTotalCar(count.getTotalCar());
                    partnerVo.setTotalAmount(count.getTotalAmount());
                }
            }
        }
        PageInfo<CustomerPartnerVo> pageInfo = new PageInfo<>(partnerVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<ShowPartnerVo> showPartner(Long customerId) {
        ShowPartnerVo partnerVo = customerDao.showPartner(customerId);
        return BaseResultUtil.success(partnerVo);
    }

    @Override
    public Customer selectByPhone(String customerPhone) {
        return customerDao.findByPhone(customerPhone);
    }

    @Override
    public boolean updateById(Customer customer) {
        return super.updateById(customer);
    }

    @Override
    public ResultVo getAllCustomerByKey(String keyword) {
        List<Map<String,Object>> customerList = customerDao.getAllCustomerByKey(keyword);
        return BaseResultUtil.success(customerList);
    }

    @Override
    public ResultVo getContractByCustomerId(Long customerId) {
        //获取当前时间戳
        Long now = LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now());
        List<Map<String,Object>> contractList = customerDao.getContractByCustomerId(customerId,now);
        return BaseResultUtil.success(contractList);
    }

    @Override
    public ResultVo customerCoupon(CustomerCouponDto dto) {
        PageInfo<CustomerCouponVo> pageInfo = null;
        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        List<CustomerCouponVo> voList = couponDao.getCustomerCouponByTerm(dto);
        List<CustomerCouponVo> disCouponVos = new ArrayList<>(10);
        List<CustomerCouponVo> couponVos = new ArrayList<>(10);
        Map<String, Object> count = new HashMap<>();
        if (!CollectionUtils.isEmpty(voList)) {
            //不可用的优惠券(使用过的或者是过期的)
            for (CustomerCouponVo couponVo : voList) {
                if (couponVo.getIsUse() == 1 || (couponVo.getIsForever() == 0 && couponVo.getEndPeriodDate() > NOW)) {
                    disCouponVos.add(couponVo);
                } else {
                    couponVos.add(couponVo);
                }
            }
            count.put("isUse_0", couponVos.size());
            count.put("isUse_1", disCouponVos.size());
        }
        if(UseStateEnum.DISABLED.code == dto.getIsUsable()){
            //不可用
            pageInfo = new PageInfo<>(disCouponVos);
        }else{
            pageInfo = new PageInfo<>(couponVos);
        }
        return BaseResultUtil.success(pageInfo,count);
    }
    @Override
    public ResultVo<List<CustomerCouponSendVo>> getCouponByCustomerId(Long customerId) {
        List<CustomerCouponSendVo> sendVoList = null;
        //根据客户id查看发放的优惠券
        List<CustomerCouponSendVo> couponVos = couponSendDao.getCustomerCoupon(customerId);
        if(!CollectionUtils.isEmpty(couponVos)){
            sendVoList = new ArrayList<>();
            for(CustomerCouponSendVo sendVo : couponVos){
                if(sendVo.getEndPeriodDate() != null){
                    if(sendVo.getEndPeriodDate() > NOW){
                        sendVoList.add(sendVo);
                    }
                }else{
                    sendVoList.add(sendVo);
                }
            }
        }
        return BaseResultUtil.success(sendVoList);
    }

    /**
     * 封装合伙人附加信息&银行卡信息
     * @param dto
     * @param customer
     * @param now
     * @return
     */
    private void encapPartner(PartnerDto dto,Customer customer,Long now){
        //新增合伙人附加信息c_customer_partner
        CustomerPartner cp = new CustomerPartner();
        BeanUtils.copyProperties(dto,cp);
        cp.setCustomerId(customer.getId());
        customerPartnerDao.insert(cp);
        //新增绑定银行卡信息s_bank_card_bind
        BankCardBind bcb = new BankCardBind();
        BeanUtils.copyProperties(dto,bcb);
        bcb.setUserId(customer.getId());
        bcb.setCardPhone(customer.getContactPhone());
        bcb.setUserType(UserTypeEnum.CUSTOMER.code);
        bcb.setCardColour(RandomUtil.getIntRandom());
        bcb.setState(UseStateEnum.USABLE.code);
        bcb.setCreateTime(now);
        bankCardBindDao.insert(bcb);
    }

    /**
     * 封装大客户合同
     * @param customerId
     * @param customerConList
     * @return
     */
    private List<CustomerContract> encapCustomerContract(Long customerId,List<CustomerContractDto> customerConList){
        List<CustomerContract> list = null;
        if(!CollectionUtils.isEmpty(customerConList)){
            list = new ArrayList<>(10);
            for(CustomerContractDto dto : customerConList){
                CustomerContract custCont = new CustomerContract();
                BeanUtils.copyProperties(dto,custCont);
                custCont.setCustomerId(customerId);
                list.add(custCont);
            }
        }
        return list;
    }

    /**
     * 将手机号同步到系统用户及司机用户
     * @param oldPhone
     * @param newPhone
     */
    private void syncPhone(String oldPhone, String newPhone) {
        Admin admin = adminDao.selectOne(new QueryWrapper<Admin>()
                .eq("phone", oldPhone));
        if (null != admin) {
            admin.setPhone(newPhone);
            adminDao.updateById(admin);
        }
        Driver driver = driverDao.selectOne(new QueryWrapper<Driver>()
                .eq("phone", oldPhone));
        if (null != driver) {
            driver.setPhone(newPhone);
            driverDao.updateById(driver);
        }
    }

    /**
     * 校验：手机号是否在Customer中存在
     * @param phone
     * @return
     */
    /*private boolean phoneExistsInCustomer(String phone) {
        List<Customer> existList = customerDao.selectList(new QueryWrapper<Customer>()
                .eq("contact_phone", phone));
        if (!CollectionUtils.isEmpty(existList)) {
            log.error("手机号已存在，请检查");
            return true;
        }
        return false;
    }*/
}
