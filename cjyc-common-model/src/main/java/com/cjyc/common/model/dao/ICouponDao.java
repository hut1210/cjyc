package com.cjyc.common.model.dao;

import com.cjyc.common.model.dto.web.coupon.ConsumeCouponDto;
import com.cjyc.common.model.dto.web.coupon.SeleCouponDto;
import com.cjyc.common.model.dto.web.customer.CustomerCouponDto;
import com.cjyc.common.model.entity.Coupon;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.vo.web.coupon.ConsumeCouponVo;
import com.cjyc.common.model.vo.web.coupon.CouponVo;
import com.cjyc.common.model.vo.web.customer.CustomerCouponVo;

import java.util.List;

/**
 * <p>
 * 优惠券发放和使用明细表 Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-10-16
 */
public interface ICouponDao extends BaseMapper<Coupon> {

    /**
     * 根据条件筛选优惠券
     * @param dto
     * @return
     */
    List<CouponVo> getCouponByTerm(SeleCouponDto dto);

    /**
     * 查询优惠券消耗明细
     * @param dto
     * @return
     */
    List<ConsumeCouponVo> getConsumeDetail(ConsumeCouponDto dto);

    /**
     * 查询客户优惠券
     * @param dto
     * @return
     */
    List<CustomerCouponVo> getCustomerCouponByTerm(CustomerCouponDto dto);
}
