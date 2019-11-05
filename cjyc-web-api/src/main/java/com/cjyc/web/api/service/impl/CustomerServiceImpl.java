package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.ReturnMsg;
import com.cjkj.usercenter.dto.common.AddUserReq;
import com.cjkj.usercenter.dto.common.AddUserResp;
import com.cjkj.usercenter.dto.common.UpdateUserReq;
import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.customer.*;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.*;
import com.cjyc.common.model.enums.coupon.CouponLifeTypeEnum;
import com.cjyc.common.model.enums.customer.CustomerPayEnum;
import com.cjyc.common.model.enums.customer.CustomerSourceEnum;
import com.cjyc.common.model.enums.customer.CustomerStateEnum;
import com.cjyc.common.model.enums.customer.CustomerTypeEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.util.YmlProperty;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.customer.*;
import com.cjyc.common.model.vo.web.coupon.CustomerCouponSendVo;
import com.cjyc.web.api.exception.CommonException;
import com.cjyc.web.api.exception.ServerException;
import com.cjyc.web.api.feign.ISysUserService;
import com.cjyc.web.api.service.ICustomerContractService;
import com.cjyc.web.api.service.ICustomerService;
import com.cjyc.web.api.service.ISendNoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
    private ISendNoService sendNoService;

    @Override
    public boolean saveCustomer(CustomerDto dto) {
        Long now = LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now());
        Customer customer = new Customer();
        BeanUtils.copyProperties(dto,customer);
        customer.setCustomerNo(sendNoService.getNo(SendNoTypeEnum.CUSTOMER));
        customer.setAlias(dto.getName());
        customer.setIsDelete(DeleteStateEnum.NO_DELETE.code);
        customer.setContactMan(dto.getName());
        customer.setType(CustomerTypeEnum.INDIVIDUAL.code);
        customer.setState(CustomerStateEnum.CHECKED.code);
        customer.setPayMode(CustomerPayEnum.TIME_PAY.code);
        customer.setSource(CustomerSourceEnum.WEB.code);
        customer.setCreateTime(now);
        customer.setRegisterTime(now);
        customer.setCreateUserId(dto.getUserId());
        //用户手机号在C端不能重复
        if (phoneExistsInCustomer(customer.getContactPhone())) {
            log.error("手机号已存在，请检查");
            return false;
        }
        //注册时间
        //新增个人用户信息到物流平台
        ResultData<Long> rd = addCustomerToPlatform(customer);
        if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
            throw new CommonException(rd.getMsg());
        }
        customer.setUserId(rd.getData());
        return super.save(customer);
    }


    @Override
    public boolean modifyCustomer(CustomerDto customerDto) {
        Customer customer = customerDao.selectById(customerDto.getId());
        if(null != customer){
            ResultData<Boolean> updateRd = updateCustomerToPlatform(customer, customerDto.getContactPhone());
            if (!ReturnMsg.SUCCESS.getCode().equals(updateRd.getCode())) {
                log.error("修改用户信息失败，原因：" + updateRd.getMsg());
                return false;
            }
            if (updateRd.getData()) {
                //需要同步手机号信息
                syncPhone(customer.getContactPhone(), customerDto.getContactPhone());
            }
            customer.setName(customerDto.getName());
            customer.setAlias(customerDto.getName());
            customer.setContactMan(customerDto.getName());
            customer.setContactPhone(customerDto.getContactPhone());
            customer.setIdCard(customerDto.getIdCard());
            customer.setIdCardFrontImg(customerDto.getIdCardFrontImg());
            customer.setIdCardBackImg(customerDto.getIdCardBackImg());
        }
        return super.updateById(customer);
    }

    @Override
    public ResultVo findCustomer(SelectCustomerDto customerDto) {
        PageHelper.startPage(customerDto.getCurrentPage(), customerDto.getPageSize());
        List<CustomerVo> customerVos = customerDao.findCustomer(customerDto);
        if(!CollectionUtils.isEmpty(customerVos)){
            for(CustomerVo vo : customerVos){
                CustomerCountVo count = customerCountDao.count(vo.getUserId());
                Admin admin = adminDao.findByUserId(vo.getCreateUserId());
                if(admin != null){
                    vo.setCreateUserName(StringUtils.isNotBlank(admin.getName()) == true ? admin.getName():"");
                }
                if(count != null){
                    vo.setTotalOrder(count.getTotalOrder() == null ? 0:count.getTotalOrder());
                    vo.setTotalCar(count.getTotalCar() == null ? 0:count.getTotalCar());
                    vo.setTotalAmount(count.getTotalAmount() == null ? BigDecimal.ZERO:count.getTotalAmount().divide(new BigDecimal(100)));
                }
                if(StringUtils.isNotBlank(vo.getRegisterTime())){
                    Long registerTime = Long.parseLong(vo.getRegisterTime());
                    vo.setRegisterTime(LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(registerTime),TimePatternConstant.COMPLEX_TIME_FORMAT));
                }
            }
        }
        PageInfo<CustomerVo> pageInfo =  new PageInfo<>(customerVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public boolean saveKeyCustomer(KeyCustomerDto keyCustomerDto) {
        //新增大客户
        Long now = LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now());
        Customer customer = new Customer();
        BeanUtils.copyProperties(keyCustomerDto,customer);
        customer.setCustomerNo(sendNoService.getNo(SendNoTypeEnum.CUSTOMER));
        customer.setAlias(keyCustomerDto.getName());
        customer.setIsDelete(DeleteStateEnum.NO_DELETE.code);
        customer.setType(CustomerTypeEnum.ENTERPRISE.code);
        customer.setState(CustomerStateEnum.WAIT_LOGIN.code);
        customer.setSource(CustomerSourceEnum.WEB.code);
        customer.setRegisterTime(now);
        customer.setCreateTime(now);
        customer.setCreateUserId(keyCustomerDto.getUserId());
        //
        //客户端信息不能重复
        if (phoneExistsInCustomer(keyCustomerDto.getContactPhone())) {
            return false;
        }
        //保存大客户信息到物流平台
        ResultData<Long> rd = addCustomerToPlatform(customer);
        if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
            log.error("保存大客户信息失败，原因：" + rd.getMsg());
            return false;
        }
        customer.setUserId(rd.getData());
        super.save(customer);
        //合同集合
        List<CustomerContractDto> customerConList = keyCustomerDto.getCustContraVos();
        List<CustomerContract> list = encapCustomerContract(customer.getId(),customerConList);
        return customerContractService.saveBatch(list);
    }

    @Override
    public boolean verifyCustomer(OperateDto dto) {
        Customer customer = customerDao.selectById(dto.getId());
        if(customer != null){
            Long now = LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now());
            if(FlagEnum.DELETE.code == dto.getFlag()){
               //假删除
                customer.setIsDelete(DeleteStateEnum.YES_DELETE.code);
            }else if(FlagEnum.AUDIT_PASS.code == dto.getFlag()){
                //审核通过
                customer.setState(CustomerStateEnum.CHECKED.code);
                customer.setCheckTime(now);
                customer.setCheckUserId(dto.getUserId());
            }else if(FlagEnum.AUDIT_REJECT.code == dto.getFlag()){
                //审核拒绝
                customer.setState(CustomerStateEnum.REJECT.code);
                customer.setCheckTime(now);
                customer.setCheckUserId(dto.getUserId());
            }
            return super.updateById(customer);
        }
        return false;
    }

    @Override
    public ResultVo showKeyCustomer(Long id) {
        ShowKeyCustomerVo sKeyCustomerDto = new ShowKeyCustomerVo();
        //根据主键id查询大客户
        Customer customer = customerDao.selectById(id);
        if(customer != null){
            BeanUtils.copyProperties(customer,sKeyCustomerDto);
        }
        //根据customer_user_id查询大客户的合同
        List<CustomerContractVo> contractVos = customerContractDao.getCustContractByCustId(customer.getId());
        if(!CollectionUtils.isEmpty(contractVos)){
            for(CustomerContractVo vo : contractVos){
                if(StringUtils.isNotBlank(vo.getContractLife())){
                    vo.setContractLife(LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(Long.parseLong(vo.getContractLife())),TimePatternConstant.SIMPLE_DATE_FORMAT));
                }
                if(StringUtils.isNotBlank(vo.getProjectEstabTime())){
                    vo.setProjectEstabTime(LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(Long.parseLong(vo.getProjectEstabTime())),TimePatternConstant.SIMPLE_DATE_FORMAT));
                }
                vo.setProTraVolume(vo.getProTraVolume() == null ? BigDecimal.ZERO:vo.getProTraVolume());
                vo.setAvgMthTraVolume(vo.getAvgMthTraVolume() == null ? BigDecimal.ZERO:vo.getAvgMthTraVolume());
            }
            sKeyCustomerDto.setCustContraVos(contractVos);
        }
        return BaseResultUtil.success(sKeyCustomerDto == null ? new SelectKeyCustomerDto():sKeyCustomerDto);
    }

    @Override
    public boolean modifyKeyCustomer(KeyCustomerDto keyCustomerDto) {
        boolean flag = false;
        Customer customer = customerDao.selectById(keyCustomerDto.getId());
        if(null != customer){
            //判断手机号是否存在
            ResultData<Boolean> updateRd = updateCustomerToPlatform(customer, keyCustomerDto.getContactPhone());
            if (!ReturnMsg.SUCCESS.getCode().equals(updateRd.getCode())) {
                log.error("修改用户信息失败，原因：" + updateRd.getMsg());
                return false;
            }
            if (updateRd.getData()) {
                //需要同步手机号信息
                syncPhone(customer.getContactPhone(), keyCustomerDto.getContactPhone());
            }
            customer.setName(keyCustomerDto.getName());
            customer.setAlias(keyCustomerDto.getName());
            customer.setContactMan(keyCustomerDto.getContactMan());
            customer.setSocialCreditCode(keyCustomerDto.getSocialCreditCode());
            customer.setContactPhone(keyCustomerDto.getContactPhone());
            customer.setContactAddress(keyCustomerDto.getContactAddress());
            customer.setCustomerNature(keyCustomerDto.getCustomerNature());
            flag = super.updateById(customer);
            if(!flag){
                throw new CommonException("更新大客户失败");
            }
            List<CustomerContractDto> contractDtos = keyCustomerDto.getCustContraVos();
            List<CustomerContract> list = null;
            if(!CollectionUtils.isEmpty(contractDtos)){
                //批量删除
                int n = customerContractDao.removeKeyContract(keyCustomerDto.getId());
                if(n <= 0){
                    throw new CommonException("删除合同失败");
                }
                list = encapCustomerContract(customer.getId(),contractDtos);
                return customerContractService.saveBatch(list);
            }
        }
        return true;
    }

    @Override
    public ResultVo findKeyCustomer(SelectKeyCustomerDto keyCustomerDto) {
        PageHelper.startPage(keyCustomerDto.getCurrentPage(), keyCustomerDto.getPageSize());
        List<ListKeyCustomerVo> keyCustomerList = customerDao.findKeyCustomter(keyCustomerDto);
        if(!CollectionUtils.isEmpty(keyCustomerList)){
            for(ListKeyCustomerVo vo : keyCustomerList){
                CustomerCountVo count = customerCountDao.count(vo.getUserId());
                Admin admin = adminDao.findByUserId(vo.getCreateUserId());
                if(admin != null){
                    vo.setCreateUserName(StringUtils.isNotBlank(admin.getName()) == true ? admin.getName():"");
                }
                if(count != null){
                    vo.setTotalOrder(count.getTotalOrder() == null ? 0:count.getTotalOrder());
                    vo.setTotalCar(count.getTotalCar() == null ? 0:count.getTotalCar());
                    vo.setTotalAmount(count.getTotalAmount() == null ? BigDecimal.ZERO:count.getTotalAmount().divide(new BigDecimal(100)));
                }
                if(StringUtils.isNotBlank(vo.getRegisterTime())){
                    vo.setRegisterTime(LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(Long.valueOf(vo.getRegisterTime())),TimePatternConstant.COMPLEX_TIME_FORMAT));
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
        //customer.setUserId(resultData.getData().getUserId());
        return super.save(customer);
    }

    @Override
    public Customer selectById(Long customerId) {
        return customerDao.selectById(customerId);
    }

    @Override
    public ResultVo savePartner(PartnerDto dto) {
        boolean result = false;
        Long now = LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now());
        //新增c_customer
        Customer customer = new Customer();
        BeanUtils.copyProperties(dto,customer);
        customer.setAlias(dto.getName());
        customer.setCustomerNo(sendNoService.getNo(SendNoTypeEnum.CUSTOMER));
        customer.setIsDelete(DeleteStateEnum.NO_DELETE.code);
        customer.setSource(CustomerSourceEnum.WEB.code);
        customer.setType(CustomerTypeEnum.COOPERATOR.code);
        customer.setState(CommonStateEnum.WAIT_CHECK.code);
        customer.setCreateTime(now);
        customer.setRegisterTime(now);
        customer.setCreateUserId(dto.getUserId());
        //用户手机号在C端不能重复
        if (phoneExistsInCustomer(customer.getContactPhone())) {
            log.error("手机号已存在，请检查");
            return BaseResultUtil.fail("手机号已存在，请检查");
        }
        //新增用户信息到物流平台
        ResultData<Long> rd = addCustomerToPlatform(customer);
        if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
            throw new CommonException(rd.getMsg());
        }
        customer.setUserId(rd.getData());

        result = super.save(customer);
        if(!result){
            throw new CommonException("新增合伙人失败");
        }
        encapPartner(dto,customer,now);
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo modifyPartner(PartnerDto dto) {
        boolean result = false;
        Long now = LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now());
        Customer customer = customerDao.selectById(dto.getId());
        if(customer != null){
            //判断手机号是否存在
            ResultData<Boolean> updateRd = updateCustomerToPlatform(customer, dto.getContactPhone());
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
           result = super.updateById(customer);
            if(!result){
                throw new CommonException("更新合伙人失败");
            }
            //删除合伙人附加信息
            customerPartnerDao.removeByCustomerId(customer.getId());
            //删除合伙人银行卡信息
            bankCardBindDao.removeBandCarBind(customer.getUserId(),UserTypeEnum.CUSTOMER.code);
            encapPartner(dto,customer,now);
        }
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo findPartner(CustomerPartnerDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<CustomerPartnerVo> partnerVos = customerDao.getPartnerByTerm(dto);
        if(!CollectionUtils.isEmpty(partnerVos)){
            for(CustomerPartnerVo partnerVo : partnerVos){
                CustomerCountVo count = customerCountDao.count(partnerVo.getUserId());
                Admin admin = adminDao.findByUserId(partnerVo.getCreateUserId());
                if(admin != null){
                    partnerVo.setCreateUserName(StringUtils.isNotBlank(admin.getName()) == true ? admin.getName():"");
                }
                if(count != null){
                    partnerVo.setTotalOrder(count.getTotalOrder() == null ? 0:count.getTotalOrder());
                    partnerVo.setTotalCar(count.getTotalCar() == null ? 0:count.getTotalCar());
                    partnerVo.setTotalAmount(count.getTotalAmount() == null ? BigDecimal.ZERO:count.getTotalAmount().divide(new BigDecimal(100)));
                }
            }
        }
        PageInfo<CustomerPartnerVo> pageInfo = new PageInfo<>(partnerVos);
        return BaseResultUtil.success(pageInfo == null ? new PageInfo<>():pageInfo);
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
        return BaseResultUtil.success(customerList == null ? Collections.EMPTY_LIST:customerList);
    }

    @Override
    public ResultVo getCustContractByUserId(Long userId) {
        //获取当前时间戳
        Long now = LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now());
        List<Map<String,Object>> contractList = customerDao.getCustContractByUserId(userId,now);
        return BaseResultUtil.success(contractList == null ? Collections.EMPTY_LIST:contractList);
    }

    @Override
    public ResultVo getCustomerCouponByTerm(CustomerCouponDto dto) {
        PageInfo<CustomerCouponVo> pageInfo = null;
        try{
            List<CustomerCouponVo> voList = couponDao.getCustomerCouponByTerm(dto);
            if(!CollectionUtils.isEmpty(voList)){
                for(CustomerCouponVo vo : voList){
                    if(vo == null){
                        continue;
                    }
                    vo.setFullAmount(vo.getFullAmount() == null ? BigDecimal.ZERO : vo.getFullAmount().divide(new BigDecimal(100)));
                    vo.setCutAmount(vo.getCutAmount() == null ? BigDecimal.ZERO : vo.getCutAmount().divide(new BigDecimal(100)));
                    if(CouponLifeTypeEnum.FOREVER.code != vo.getIsForever()){
                        //有效期
                        if(StringUtils.isNotBlank(vo.getStartPeriodDate())){
                            vo.setStartPeriodDate(LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(Long.valueOf(vo.getStartPeriodDate())),TimePatternConstant.COMPLEX_TIME_FORMAT));
                        }
                        if(StringUtils.isNotBlank(vo.getEndPeriodDate())){
                            vo.setEndPeriodDate(LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(Long.valueOf(vo.getEndPeriodDate())),TimePatternConstant.COMPLEX_TIME_FORMAT));
                        }
                    }
                    //永久 没有有效期
                    if(StringUtils.isNotBlank(vo.getReceiveTime())){
                        vo.setReceiveTime(LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(Long.valueOf(vo.getReceiveTime())),TimePatternConstant.COMPLEX_TIME_FORMAT));
                    }
                }
                PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
                pageInfo = new PageInfo<>(voList);
                return BaseResultUtil.getPageVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),pageInfo);
            }else{
                return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),Collections.emptyList());
            }
        }catch (Exception e){
            log.error("查看用户优惠券信息出现异常",e);
        }
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),Collections.emptyList());
    }

    @Override
    public ResultVo getCouponByUserId(Long userId) {
        Customer customer = customerDao.getCustomerByUserId(userId);
        List<CustomerCouponSendVo> sendVoList = null;
        if(customer != null){
            //根据客户id查看发放的优惠券
            List<CustomerCouponSendVo> couponVos = couponSendDao.getCustomerCoupon(customer.getId());
            Long now = LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now());
            if(!CollectionUtils.isEmpty(couponVos)){
                sendVoList = new ArrayList<>();
                for(CustomerCouponSendVo sendVo : couponVos){
                    if(sendVo == null){
                        continue;
                    }
                    if(sendVo.getEndPeriodDate() != null){
                        if(sendVo.getEndPeriodDate() >now){
                            sendVoList.add(sendVo);
                        }
                    }else{
                        sendVoList.add(sendVo);
                    }
                }
            }
        }
        return BaseResultUtil.success(sendVoList == null ? Collections.EMPTY_LIST:sendVoList);
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
        bcb.setUserId(customer.getUserId());
        bcb.setCardPhone(customer.getContactPhone());
        bcb.setUserType(UserTypeEnum.CUSTOMER.code);
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
                if(StringUtils.isNotBlank(dto.getContractLife())){
                    custCont.setContractLife(LocalDateTimeUtil.convertToLong(dto.getContractLife(),TimePatternConstant.SIMPLE_DATE_FORMAT));
                }
                if(StringUtils.isNotBlank(dto.getProjectEstabTime())){
                    custCont.setProjectEstabTime(LocalDateTimeUtil.convertToLong(dto.getProjectEstabTime(),TimePatternConstant.SIMPLE_DATE_FORMAT));
                }
                list.add(custCont);
            }
        }
        return list;
    }

    /**
     * 将C端客户保存到物流平台
     * @param customer
     * @return
     */
    private ResultData<Long> addCustomerToPlatform(Customer customer) {
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
        user.setPassword(YmlProperty.get("cjkj.salesman.password"));
        ResultData<AddUserResp> saveRd = sysUserService.save(user);
        if (!ReturnMsg.SUCCESS.getCode().equals(saveRd.getCode())) {
            return ResultData.failed("保存客户信息失败，原因：" + saveRd.getMsg());
        }
        return ResultData.ok(saveRd.getData().getUserId());
    }

    /**
     * 修改账号信息到物流平台：
     *  修改物流平台账号信息：如果修改账号则将要修改的账号不能存在否则修改失败
     * @param customer
     * @param newPhone
     * @return
     */
    private ResultData<Boolean> updateCustomerToPlatform(Customer customer, String newPhone) {
        String oldPhone = customer.getContactPhone();
        if (!oldPhone.equals(newPhone)) {
            //新旧账号不相同需要替换手机号
            //先查询韵车是否存在newPhone 相同账号，存在则不允许修改
            if (validPhoneExits(newPhone)) {
                return ResultData.failed("用户账号不允许修改，预修改账号：" + newPhone + " 已存在");
            }
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

    /**
     * 验证手机号在韵车所有用户中是否存在
     * @param phone
     * @return
     */
    private boolean validPhoneExits(String phone) {
        List<Admin> adminList = adminDao.selectList(new QueryWrapper<Admin>()
                .eq("phone", phone));
        if (!CollectionUtils.isEmpty(adminList)) {
            return true;
        }
        List<Driver> driverList = driverDao.selectList(new QueryWrapper<Driver>()
                .eq("phone", phone));
        if (!CollectionUtils.isEmpty(driverList)) {
            return true;
        }
        List<Customer> customerList = customerDao.selectList(new QueryWrapper<Customer>()
                .eq("contact_phone", phone));
        if (!CollectionUtils.isEmpty(customerList)) {
            return true;
        }
        return false;
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
    private boolean phoneExistsInCustomer(String phone) {
        List<Customer> existList = customerDao.selectList(new QueryWrapper<Customer>()
                .eq("contact_phone", phone));
        if (!CollectionUtils.isEmpty(existList)) {
            log.error("手机号已存在，请检查");
            return true;
        }
        return false;
    }
}
