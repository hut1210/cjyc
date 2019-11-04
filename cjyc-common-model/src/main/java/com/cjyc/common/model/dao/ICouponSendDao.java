package com.cjyc.common.model.dao;

import com.cjyc.common.model.dto.web.coupon.SeleCouponSendDto;
import com.cjyc.common.model.entity.CouponSend;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.vo.customer.coupon.CustomerCouponVo;
import com.cjyc.common.model.vo.web.coupon.CouponSendVo;
import com.cjyc.common.model.vo.web.coupon.CustomerCouponSendVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 优惠券主表 Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-10-16
 */
public interface ICouponSendDao extends BaseMapper<CouponSend> {

    /**
     * 获取领取/消耗张数
     * @param couponId
     * @return
     */
    Integer getReceiveNum(@Param("couponId") Long couponId,@Param("isUse") Integer isUse);

    /**
     * 根据条件查询发放优惠券
     * @param dto
     * @return
     */
    List<CouponSendVo> seleCouponSendByTerm(SeleCouponSendDto dto);

    /**
     * 根据用户编号查询所属自己的优惠券
     * @param id
     * @return
     */
    List<CustomerCouponVo> getCustomerCouponById(@Param("id") Long id);

    /**
     * 根据客户id获取优惠券
     * @param customerId
     * @return
     */
    List<CustomerCouponSendVo> getCustomerCoupon(@Param("customerId") Long customerId);
}
