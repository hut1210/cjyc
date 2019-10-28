package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.coupon.CouponSendDto;
import com.cjyc.common.model.dto.web.coupon.SeleCouponSendDto;
import com.cjyc.common.model.entity.CouponSend;
import com.cjyc.common.model.vo.web.coupon.CouponSendVo;
import com.github.pagehelper.PageInfo;

import java.math.BigDecimal;

/**
 *  @author: zj
 *  @Date: 2019/10/16 10:08
 *  @Description:优惠券发放
 */
public interface ICouponSendService {

    /**
     * 根据筛选条件查询发放优惠券
     * @param dto
     * @return
     */
   PageInfo<CouponSendVo> seleCouponSendByTerm(SeleCouponSendDto dto);

    /**
     * 根据ids分页查询发放优惠券
     * @param dto
     * @return
     */
   PageInfo<CouponSend> seleCouponSendByIds(CouponSendDto dto);

    BigDecimal getAmountById(Long couponSendId, BigDecimal realWlTotalFee);
}
