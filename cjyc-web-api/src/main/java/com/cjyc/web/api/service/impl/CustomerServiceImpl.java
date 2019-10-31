package com.cjyc.web.api.service.impl;

import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.web.customer.*;
import com.cjyc.common.model.entity.BankCardBind;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.entity.CustomerContract;
import com.cjyc.common.model.entity.CustomerPartner;
import com.cjyc.common.model.enums.*;
import com.cjyc.common.model.enums.coupon.CouponLifeTypeEnum;
import com.cjyc.common.model.enums.customer.CustomerStateEnum;
import com.cjyc.common.model.enums.customer.CustomerTypeEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.CustomerContractVo;
import com.cjyc.common.model.vo.web.CustomerVo;
import com.cjyc.common.model.vo.web.ListKeyCustomerVo;
import com.cjyc.common.model.vo.web.ShowKeyCustomerVo;
import com.cjyc.common.model.vo.web.customer.CustomerCouponVo;
import com.cjyc.web.api.exception.CommonException;
import com.cjyc.web.api.feign.ISysUserService;
import com.cjyc.web.api.service.ICustomerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
public class CustomerServiceImpl implements ICustomerService{

    @Resource
    private ICustomerDao customerDao;

    @Resource
    private ICustomerContractDao customerContractDao;
    @Resource
    private ISysUserService sysUserService;

    @Resource
    private ICustomerPartnerDao customerPartnerDao;

    @Resource
    private IBankCardBindDao bankCardBindDao;

    @Resource
    private ICouponDao couponDao;

    @Override
    public boolean saveCustomer(CustomerDto customerDto) {
        try{
            Customer customer = new Customer();
            customer.setName(customerDto.getName());
            customer.setAlias(customerDto.getName());
            customer.setContactMan(customerDto.getName());
            customer.setContactPhone(customerDto.getPhone());
            customer.setIdCard(customerDto.getIdCard());
            customer.setIdCardFrontImg(customerDto.getIdCardFrontImg());
            customer.setIdCardBackImg(customerDto.getIdCardBackImg());
            customer.setType(CustomerTypeEnum.INDIVIDUAL.code);
            customer.setState(CustomerStateEnum.CHECKED.code);
            customer.setCreateTime(LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now()));
            customer.setCreateUserId(customerDto.getUserId());
            //注册时间

            return customerDao.insert(customer)> 0 ? true : false;
        }catch (Exception e){
            log.error("新增用户出现异常",e);
            throw new CommonException(e.getMessage());
        }
    }

    @Override
    public boolean updateCustomer(CustomerDto customerDto) {
        try{
            Customer customer = customerDao.selectById(customerDto.getId());
            if(null != customer){
                customer.setName(customerDto.getName());
                customer.setContactPhone(customerDto.getPhone());
                customer.setIdCard(customerDto.getIdCard());
                customer.setIdCardFrontImg(customerDto.getIdCardFrontImg());
                customer.setIdCardBackImg(customerDto.getIdCardBackImg());
                return customerDao.updateById(customer) > 0 ? true : false;
            }
        }catch (Exception e){
            log.error("更新用户出现异常",e);
            throw new CommonException(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delCustomerByIds(List<Long> ids) {
        try{
            if(ids != null && ids.size() > 0){
                return customerDao.deleteBatchIds(ids) > 0 ? true:false;
            }
        }catch (Exception e){
            log.error("删除用户出现异常",e);
            throw new CommonException(e.getMessage());
        }
        return false;
    }

    @Override
    public PageInfo<CustomerVo> findCustomerByTerm(SelectCustomerDto customerDto) {
        PageInfo<CustomerVo> pageInfo = null;
        try{
            List<CustomerVo> customerVos = customerDao.findCustomer(customerDto);
            if(customerVos != null && customerVos.size() > 0){
                for(CustomerVo vo : customerVos){
                    if(StringUtils.isNotBlank(vo.getRegisterTime())){
                        Long registerTime = Long.parseLong(vo.getRegisterTime());
                        vo.setRegisterTime(LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(registerTime),TimePatternConstant.COMPLEX_TIME_FORMAT));
                    }
                }
                PageHelper.startPage(customerDto.getCurrentPage(), customerDto.getPageSize());
                pageInfo = new PageInfo<>(customerVos);
            }
        }catch (Exception e){
            log.error("根据条件查询用户出现异常",e);
        }
        return pageInfo;
    }

    @Override
    public boolean saveKeyCustAndContract(KeyCustomerDto keyCustomerDto) {
        try{
            //新增大客户
            Customer customer = new Customer();
            customer.setName(keyCustomerDto.getName());
            customer.setContactMan(keyCustomerDto.getContactMan());
            customer.setContactPhone(keyCustomerDto.getContactPhone());
            customer.setContactAddress(keyCustomerDto.getContactAddress());
            customer.setCustomerNature(keyCustomerDto.getCustomerNature());
            customer.setType(CustomerTypeEnum.ENTERPRISE.code);
            customer.setSocialCreditCode(keyCustomerDto.getSocialCreditCode());
            customer.setState(CustomerStateEnum.WAIT_LOGIN.code);
            customer.setCreateTime(LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now()));
            customer.setCreateUserId(keyCustomerDto.getUserId());
            //
            int num = customerDao.insert(customer);
            if(num > 0){
                //合同集合
                int no = 0;
                List<CustomerContractDto> customerConList = keyCustomerDto.getCustContraVos();
                if(customerConList != null && customerConList.size() > 0){
                    for(CustomerContractDto vo : customerConList){
                        int i = saveCustomerContract(customer.getId() , vo);
                        if(i > 0){
                            no ++;
                        }
                    }
                    if(no == customerConList.size()){
                        return true;
                    }
                }
            }
        }catch (Exception e){
            log.error("新增大客户&合同出现异常",e);
            throw new CommonException("新增大客户&合同出现异常");
        }
        return false;
    }

    @Override
    public boolean delKeyCustomerByIds(List<Long> ids) {
        int num ;
        int no = 0;
        try{
            if( null != ids && ids.size() > 0){
                num = customerDao.deleteBatchIds(ids);
                if(num > 0){
                    //循环删除大客户合同
                    for(Long custid : ids){
                        int i = customerContractDao.deleteContractByCustomerId(custid);
                        if(i > 0){
                            no ++;
                        }
                    }
                    if(no == ids.size()){
                        return true;
                    }
                }
            }
        }catch (Exception e){
            log.error("删除大客户出现异常",e);
            throw new CommonException(e.getMessage());
        }
        return false;
    }

    @Override
    public ShowKeyCustomerVo showKeyCustomerById(Long id) {
        ShowKeyCustomerVo sKeyCustomerDto = new ShowKeyCustomerVo();
        try{
            if(id != null){
                //根据主键id查询大客户
                Customer customer = customerDao.selectById(id);
                if(customer == null){
                    return sKeyCustomerDto;
                }
                sKeyCustomerDto.setId(customer.getId());
                sKeyCustomerDto.setUserId(customer.getUserId());
                sKeyCustomerDto.setName(customer.getName());
                sKeyCustomerDto.setSocialCreditCode(customer.getSocialCreditCode());
                sKeyCustomerDto.setContactMan(customer.getContactMan());
                sKeyCustomerDto.setContactPhone(customer.getContactPhone());
                sKeyCustomerDto.setContactAddress(customer.getContactAddress());

                //根据customer_user_id查询大客户的合同
                List<CustomerContractVo> contractVos = customerContractDao.getCustContractByCustId(customer.getId());
                if(contractVos != null && contractVos.size() > 0){
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
            }
        }catch (Exception e){
            log.error("查看大客户出现异常",e);
            throw new CommonException(e.getMessage());
        }
        return sKeyCustomerDto;
    }

    @Override
    public boolean updateKeyCustomer(KeyCustomerDto keyCustomerDto) {
        try{
            Customer customer = customerDao.selectById(keyCustomerDto.getId());
            if(null != customer){
                customer.setName(keyCustomerDto.getName());
                customer.setContactMan(keyCustomerDto.getContactMan());
                customer.setContactPhone(keyCustomerDto.getContactPhone());
                customer.setContactAddress(keyCustomerDto.getContactAddress());
                customer.setCustomerNature(keyCustomerDto.getCustomerNature());
                int num = customerDao.updateById(customer);
                if(num > 0){
                    int no = 0;
                    List<CustomerContractDto> contractVos = keyCustomerDto.getCustContraVos();
                    if(null != contractVos && contractVos.size() > 0){
                        for(CustomerContractDto vo : contractVos){
                            int i = updateCustomerContractById(vo);
                            if(i > 0){
                                no ++;
                            }
                        }
                        if(no == contractVos.size()){
                            return true;
                        }
                    }
                }
            }
        }catch (Exception e){
            log.error("更新大客户&合同出现异常",e);
            throw new CommonException(e.getMessage());
        }
        return false;
    }

    @Override
    public PageInfo<ListKeyCustomerVo> findKeyCustomer(SelectKeyCustomerDto keyCustomerDto) {
        PageInfo<ListKeyCustomerVo> pageInfo = null;
        try{
            List<ListKeyCustomerVo> keyCustomerList = customerDao.findKeyCustomter(keyCustomerDto);
            if(keyCustomerList != null && keyCustomerList.size() > 0){
                PageHelper.startPage(keyCustomerDto.getCurrentPage(), keyCustomerDto.getPageSize());
                pageInfo = new PageInfo<>(keyCustomerList);
            }
        }catch (Exception e){
            log.error("根据条件查询大客户出现异常",e);
            throw new CommonException(e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public int save(Customer customer) {
        //添加架构组数据
/*        AddUserReq addUserReq = new AddUserReq();
        addUserReq.setAccount(customer.getContactPhone());
        addUserReq.setPassword(YmlProperty.get("cjkj.web.password"));
        addUserReq.setDeptId(Long.valueOf(YmlProperty.get("cjkj.dept_customer_id")));
        addUserReq.setMobile(customer.getContactPhone());
        addUserReq.setName(customer.getName());
        ResultData<AddUserResp> resultData = sysUserService.save(addUserReq);

        if(resultData == null || resultData.getData() == null || resultData.getData().getUserId() == null){
            throw new ServerException("添加用户失败");
        }*/
        //customer.setUserId(resultData.getData().getUserId());
        return customerDao.insert(customer);
    }

    @Override
    public Customer selectById(Long customerId) {
        return customerDao.selectById(customerId);
    }

    @Override
    public ResultVo addOrUpdatePartner(PartnerDto dto) {
        int n;
        int m = 0;
        int j = 0;
        Customer customer = null;
        Long createTime = LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now());
        try{

            if(dto.getFlag() == FlagEnum.ADD.code){
                //新增c_customer
                customer = new Customer();
                encapCustomer(customer,dto);
                customer.setType(CustomerTypeEnum.COOPERATOR.code);
                customer.setState(CommonStateEnum.WAIT_CHECK.code);
                customer.setCreateTime(createTime);
                customer.setCreateUserId(dto.getUserId());
                n = customerDao.insert(customer);
                if(n > 0){
                    //新增合伙人附加信息c_customer_partner
                    CustomerPartner cp = new CustomerPartner();
                    cp.setCustomerId(customer.getId());
                    encapPartner(cp,dto);
                    m = customerPartnerDao.insert(cp);
                }
                if(m > 0){
                    //新增绑定银行卡信息s_bank_card_bind
                    BankCardBind bcb = new BankCardBind();
                    bcb.setUserId(customer.getUserId());
                    encapBankCarBind(bcb,dto);
                    bcb.setState(UseStateEnum.USABLE.code);
                    bcb.setCreateTime(createTime);
                    j = bankCardBindDao.insert(bcb);
                }
            }else if(dto.getFlag() == FlagEnum.UPDTATE.code){
                //更新
                customer = customerDao.selectById(dto.getId());
                encapCustomer(customer,dto);
                n = customerDao.updateById(customer);
                if(n > 0){
                    //更新合伙人附带信息
                  CustomerPartner cp = customerPartnerDao.getPartnerByUserId(customer.getUserId());
                  encapPartner(cp,dto);
                  m = customerPartnerDao.updateById(cp);
                }
                if(m > 0){
                    //更新绑定银行卡信息
                    BankCardBind bcb = bankCardBindDao.getBankCardBindByUserId(dto.getUserId());
                    encapBankCarBind(bcb,dto);
                    j = bankCardBindDao.updateById(bcb);
                }
            }
            if(j > 0){
                return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg());
            }
        }catch (Exception e){
            log.error("新增/更新合伙人出现异常",e);
            throw new CommonException("新增/更新合伙人出现异常");
        }
        return BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @Override
    public ResultVo verifyOrDeletePartner(Long id, Integer flag) {
        int n = 0;
        try{
            if(id == null || flag == null){
                return BaseResultUtil.getVo(ResultEnum.MOBILE_PARAM_ERROR.getCode(),ResultEnum.MOBILE_PARAM_ERROR.getMsg());
            }
            Customer customer = customerDao.selectById(id);
            //审核
            if(FlagEnum.AUDIT_PASS.code == flag){
                if(customer != null){
                    customer.setState(CommonStateEnum.CHECKED.code);
                    n = customerDao.updateById(customer);
                }
            }else if(FlagEnum.DELETE.code == flag){
                if(customer != null){
                    //删除
                    n =customerDao.deleteById(id);
                }
            }
            if(n > 0){
                return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg());
            }
        }catch (Exception e){
            log.error("审核/删除合伙人出现异常",e);
            throw new CommonException("审核/删除合伙人出现异常");
        }
        return BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @Override
    public Customer selectByPhone(String customerPhone) {
        return customerDao.findByPhone(customerPhone);
    }

    @Override
    public int updateById(Customer customer) {
        return customerDao.updateById(customer);
    }

    @Override
    public ResultVo getAllCustomerByKey(String keyword) {
        List<Map<String,String>> customerList = null;
        try{
            customerList = customerDao.getAllCustomerByKey(keyword);
            if(!CollectionUtils.isEmpty(customerList)){
                return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),customerList);
            }else{
                return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(), Collections.emptyList());
            }
        }catch (Exception e){
            log.error("根据用户名/手机号模糊查询用户信息出现异常",e);
            throw new CommonException("根据用户名/手机号模糊查询用户信息出现异常");
        }
    }

    @Override
    public ResultVo getCustContractByName(String name) {
        //获取当前时间戳
        Long now = LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now());
        try{
            List<Map<String,Object>> contractMap = customerDao.getCustContractByName(name,now);
            if(!CollectionUtils.isEmpty(contractMap)){
                return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),contractMap);
            }else{
                return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),Collections.emptyList());
            }
        }catch (Exception e){
            log.error("根据大客户名称获取有效期合同信息出现异常",e);
            return BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg(),Collections.emptyList());
        }
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

    /**
     * 封装合伙人
     * @param customer
     * @param dto
     * @return
     */
    private Customer encapCustomer(Customer customer,PartnerDto dto){
        customer.setName(dto.getName());
        customer.setContactMan(dto.getContactMan());
        customer.setContactPhone(dto.getContactPhone());
        customer.setContactAddress(dto.getContactAddress());
        return customer;
    }

    /**
     * 封装合伙人信息
     * @param cp
     * @param dto
     * @return
     */
    private CustomerPartner encapPartner(CustomerPartner cp,PartnerDto dto){
        cp.setIsTaxpayer(dto.getIsTaxpayer());
        cp.setIsInvoice(dto.getIsInvoice());
        cp.setSettleType(dto.getSettleType());
        cp.setSettlePeriod(dto.getSettlePeriod());
        cp.setBusinessLicenseFrontImg(dto.getBusinessLicenseFrontImg());
        cp.setBusinessLicenseBackImg(dto.getBusinessLicenseBackImg());
        cp.setLegalIdcardFrontImg(dto.getLegalIdCardFrontImg());
        cp.setLegalIdcardBackImg(dto.getLegalIdCardBackImg());
        cp.setLinkmanIdcardFrontImg(dto.getLinkmanIdCardFrontImg());
        cp.setLinkmanIdcardBackImg(dto.getLinkmanIdCardBackImg());
        cp.setAuthorizationFrontImg(dto.getAuthorizationFrontImg());
        cp.setAuthorizationBackImg(dto.getAuthorizationBackImg());
        return cp;
    }

    /**
     * 封装合伙人信息
     * @param bcb
     * @param dto
     * @return
     */
    private BankCardBind encapBankCarBind(BankCardBind bcb,PartnerDto dto){
        if(dto.getCardType() == CardTypeEnum.PUBLIC.code){
            //对公
            bcb.setBankLicence(dto.getBankLicence());
        }else if(dto.getCardType() == CardTypeEnum.PRIVATE.code){
            //对私
            bcb.setIdCard(dto.getIdCard());
        }
        bcb.setCardType(dto.getCardType());
        bcb.setCardNo(dto.getCardNo());
        bcb.setCardName(dto.getCardName());
        bcb.setBankName(dto.getBankName());
        bcb.setCardPhone(dto.getContactPhone());
        bcb.setDescription(dto.getDescription());
        return bcb;
    }
    /**
     * 新增大客户合同
     * @param id  大客户id
     * @param dto  合同
     */
    private int saveCustomerContract(Long id , CustomerContractDto dto){
        try{
            CustomerContract custCont = new CustomerContract();
            custCont.setCustomerId(id);
            custCont.setContractNo(dto.getContractNo());
            custCont.setContactNature(dto.getContactNature());
            custCont.setContractLife(LocalDateTimeUtil.convertToLong(dto.getContractLife(),TimePatternConstant.SIMPLE_DATE_FORMAT));
            custCont.setProjectName(dto.getProjectName());
            custCont.setProjectLevel(dto.getProjectLevel());
            custCont.setMajorProduct(dto.getMajorProduct());
            custCont.setProjectNature(dto.getProjectNature());
            custCont.setProTraVolume(dto.getProTraVolume());
            custCont.setAvgMthTraVolume(dto.getAvgMthTraVolume());
            custCont.setBusiCover(dto.getBusiCover());
            custCont.setFixedRoute(dto.getFixedRoute());
            custCont.setProjectDeper(dto.getProjectDeper());
            custCont.setProjectLeader(dto.getProjectLeader());
            custCont.setLeaderPhone(dto.getLeaderPhone());
            custCont.setProjectStatus(dto.getProjectStatus());
            custCont.setProjectTeamPer(dto.getProjectTeamPer());
            custCont.setProjectEstabTime(LocalDateTimeUtil.convertToLong(dto.getProjectEstabTime(),TimePatternConstant.SIMPLE_DATE_FORMAT));
            custCont.setMajorKpi(dto.getMajorKpi());
            return customerContractDao.insert(custCont);
        }catch (Exception e){
            log.error("新增合同出现异常",e);
            throw new CommonException("新增合同出现异常");
        }
    }

    private int updateCustomerContractById(CustomerContractDto dto){
        try{
            CustomerContract contract = customerContractDao.selectById(dto.getId());
            if(null != contract){
                contract.setContractNo(dto.getContractNo());
                contract.setContactNature(dto.getContactNature());
                contract.setSettleType(dto.getSettleType());
                contract.setSettlePeriod(dto.getSettlePeriod());
                contract.setContractLife(LocalDateTimeUtil.convertToLong(dto.getProjectEstabTime(), TimePatternConstant.SIMPLE_DATE_FORMAT));
                contract.setProjectName(dto.getProjectName());
                contract.setProjectLevel(dto.getProjectLevel());
                contract.setMajorProduct(dto.getMajorProduct());
                contract.setProjectNature(dto.getProjectNature());
                contract.setProTraVolume(dto.getProTraVolume());
                contract.setAvgMthTraVolume(dto.getAvgMthTraVolume());
                contract.setBusiCover(dto.getBusiCover());
                contract.setFixedRoute(dto.getFixedRoute());
                contract.setProjectDeper(dto.getProjectDeper());
                contract.setProjectLeader(dto.getProjectLeader());
                contract.setLeaderPhone(dto.getLeaderPhone());
                contract.setProjectStatus(dto.getProjectStatus());
                contract.setProjectTeamPer(dto.getProjectTeamPer());
                contract.setProjectEstabTime(LocalDateTimeUtil.convertToLong(dto.getProjectEstabTime(), TimePatternConstant.SIMPLE_DATE_FORMAT));
                contract.setMajorKpi(dto.getMajorKpi());
                return customerContractDao.updateById(contract);
            }
        }catch (Exception e){
            log.error("更新合同出现异常",e);
            throw new CommonException("更新合同出现异常");
        }
        return 0;
    }

}
