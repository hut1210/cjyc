package com.cjyc.customer.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.dao.ICouponSendDao;
import com.cjyc.common.model.dao.ICustomerDao;
import com.cjyc.common.model.dto.CommonDto;
import com.cjyc.common.model.dto.customer.invoice.InvoiceApplyQueryDto;
import com.cjyc.common.model.entity.CouponSend;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.enums.UseStateEnum;
import com.cjyc.common.model.enums.coupon.CouponLifeTypeEnum;
import com.cjyc.common.model.util.BasePageUtil;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.coupon.CustomerCouponVo;
import com.cjyc.customer.api.exception.CommonException;
import com.cjyc.customer.api.service.ICustomerCouponService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class CustomerCouponServiceImpl extends ServiceImpl<ICouponSendDao, CouponSend> implements ICustomerCouponService {

    @Resource
    private ICouponSendDao couponSendDao;

    @Resource
    private ICustomerDao customerDao;

    @Override
    public ResultVo customerCoupon(CommonDto dto) {
        PageInfo<CustomerCouponVo> pageInfo = null;
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        Customer customer = customerDao.selectById(dto.getLoginId());
        if(customer != null){
            List<CustomerCouponVo> sendVos = couponSendDao.getCustomerCouponById(customer.getId());
            Long now = LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now());
            if(!CollectionUtils.isEmpty(sendVos)){
                for(CustomerCouponVo vo : sendVos){
                    if(CouponLifeTypeEnum.FOREVER.code != vo.getIsForever()){
                        if(StringUtils.isNotBlank(vo.getEndPeriodDate())){
                            Long endPeriodDate = Long.valueOf(vo.getEndPeriodDate());
                            vo.setEndPeriodDate(LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(endPeriodDate),TimePatternConstant.SIMPLE_DATE_FORMAT));
                            if((UseStateEnum.NO_USE.code == vo.getIsUse()) && endPeriodDate > now){
                                vo.setIsExpire(UseStateEnum.YES_EXPIRE.code);
                            }else{
                                vo.setIsExpire(UseStateEnum.NO_EXPIRE.code);
                            }
                        }
                    }
                }
                pageInfo = new PageInfo<>(sendVos);
            }
        }
        return BaseResultUtil.success(pageInfo == null ? new PageInfo<>(Collections.EMPTY_LIST):pageInfo);
    }
}