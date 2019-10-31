package com.cjyc.web.api.service.impl;

import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.dao.ICouponDao;
import com.cjyc.common.model.dao.ICouponSendDao;
import com.cjyc.common.model.dto.web.coupon.CouponSendDto;
import com.cjyc.common.model.dto.web.coupon.SeleCouponSendDto;
import com.cjyc.common.model.entity.Coupon;
import com.cjyc.common.model.entity.CouponSend;
import com.cjyc.common.model.enums.CommonStateEnum;
import com.cjyc.common.model.enums.coupon.CouponLifeTypeEnum;
import com.cjyc.common.model.enums.coupon.CouponTypeEnum;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.web.coupon.CouponSendVo;
import com.cjyc.web.api.service.ICouponSendService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 *  @author: zj
 *  @Date: 2019/10/16 10:08
 *  @Description:优惠券发放
 */
@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
public class CouponSendServiceImpl implements ICouponSendService {

    @Resource
    private ICouponSendDao iCouponSendDao;

    @Resource
    private ICouponDao couponDao;

    @Override
    public PageInfo<CouponSendVo> seleCouponSendByTerm(SeleCouponSendDto dto) {
        PageInfo<CouponSendVo> pageInfo = new PageInfo<>();
        try{
            List<CouponSendVo> couponSendVos = iCouponSendDao.seleCouponSendByTerm(dto);
            if(couponSendVos != null && couponSendVos.size() > 0){
                for(CouponSendVo vo : couponSendVos){
                    LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(Long.valueOf(vo.getUseTime())),TimePatternConstant.COMPLEX_TIME_FORMAT);
                }
                PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
                pageInfo = new PageInfo<>(couponSendVos);
            }
        }catch (Exception e){
            log.info("查询发放优惠券出现异常");
        }
        return pageInfo;
    }

    @Override
    public PageInfo<CouponSend> seleCouponSendByIds(CouponSendDto dto) {
        PageInfo<CouponSend> pageInfo = new PageInfo<>();
        try{
            List<CouponSend> couponSends = iCouponSendDao.selectBatchIds(dto.getIds());
            if(couponSends != null && couponSends.size() > 0){
                for(CouponSend vo : couponSends){
                    LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(Long.valueOf(vo.getUseTime())),TimePatternConstant.COMPLEX_TIME_FORMAT);
                }
                PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
                pageInfo = new PageInfo<>(couponSends);
            }
        }catch (Exception e){
            log.info("查询发放优惠券出现异常");
        }
        return pageInfo;
    }

    @Override
    public BigDecimal getAmountById(Long couponSendId, BigDecimal realWlTotalFee) {
        CouponSend couponSend = iCouponSendDao.selectById(couponSendId);
        if (couponSend == null || couponSend.getIsUse() == 1) {
            return BigDecimal.ZERO;
        }
        Coupon coupon = couponDao.selectById(couponSend.getCouponId());
        if (coupon == null) {
            return BigDecimal.ZERO;
        }
        if(coupon.getState() != CommonStateEnum.CHECKED.code){
            return BigDecimal.ZERO;
        }
        if (coupon.getIsForever() == CouponLifeTypeEnum.FOREVER.code
                && coupon.getStartPeriodDate() < System.currentTimeMillis()
                && coupon.getEndPeriodDate() > System.currentTimeMillis()) {
            return BigDecimal.ZERO;
        }

        if (coupon.getType() == CouponTypeEnum.FULL_CUT.code) {
            if(realWlTotalFee.compareTo(coupon.getFullAmount()) < 0){
                return BigDecimal.ZERO;
            }
            return coupon.getCutAmount();
        }
        if(coupon.getType() == CouponTypeEnum.DIRECT_CUT.code){
            return coupon.getCutAmount();
        }
        if(coupon.getType() == CouponTypeEnum.DISCOUNT_CUT.code){
            return realWlTotalFee.multiply(new BigDecimal(coupon.getDiscount().toString()));
        }
        return BigDecimal.ZERO;
    }
}