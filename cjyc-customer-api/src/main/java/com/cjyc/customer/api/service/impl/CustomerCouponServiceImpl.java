package com.cjyc.customer.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.dao.ICouponSendDao;
import com.cjyc.common.model.dao.ICustomerDao;
import com.cjyc.common.model.dto.CommonDto;
import com.cjyc.common.model.entity.CouponSend;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.enums.UseStateEnum;
import com.cjyc.common.model.enums.coupon.CouponLifeTypeEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.coupon.CustomerCouponVo;
import com.cjyc.customer.api.service.ICustomerCouponService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class CustomerCouponServiceImpl extends ServiceImpl<ICouponSendDao, CouponSend> implements ICustomerCouponService {

    @Resource
    private ICustomerDao customerDao;
    @Resource
    private ICouponSendDao couponSendDao;

    @Override
    public ResultVo customerCoupon(CommonDto dto) {
        Customer customer = customerDao.selectOne(new QueryWrapper<Customer>().lambda()
                .eq(Customer::getId, dto.getLoginId()));
        if(customer == null){
            return BaseResultUtil.fail("该客户不存在,请检查");
        }
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<CustomerCouponVo> sendVos = couponSendDao.getCustomerCouponById(dto.getLoginId());
        Long now = LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now());
        if(!CollectionUtils.isEmpty(sendVos)){
            for(CustomerCouponVo vo : sendVos){
                vo.setFullAmount(vo.getFullAmount() == null ? BigDecimal.ZERO:vo.getFullAmount().divide(new BigDecimal(100)));
                vo.setCutAmount(vo.getCutAmount() == null ? BigDecimal.ZERO:vo.getCutAmount().divide(new BigDecimal(100)));
                vo.setDiscount(vo.getDiscount() == null ? BigDecimal.ZERO:vo.getDiscount());
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
        }
        PageInfo<CustomerCouponVo> pageInfo = new PageInfo<>(sendVos);
        return BaseResultUtil.success(pageInfo);
    }
}