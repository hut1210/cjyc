package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.BasePageDto;
import com.cjyc.common.model.dto.web.coupon.CouponDto;
import com.cjyc.common.model.dto.web.coupon.SeleCouponDto;
import com.cjyc.common.model.entity.Coupon;
import com.cjyc.common.model.vo.web.coupon.CouponVo;
import com.github.pagehelper.PageInfo;

import java.math.BigDecimal;
import java.util.List;

/**
 *  @author: zj
 *  @Date: 2019/10/16 10:08
 *  @Description:优惠券
 */
public interface ICouponService {

    /**
     * 新增优惠券
     * @param dto
     * @return
     */
    boolean saveCoupon(CouponDto dto);

    /**
     * 分页查看所有优惠券
     * @param dto
     * @return
     */
    PageInfo<CouponVo> getAllCoupon(BasePageDto dto);

    /**
     * 根据ids作废优惠券
     * @param ids
     * @return
     */
    boolean abolishCouponByIds(List<Long> ids);

    /**
     * 根据优惠券主键id查询优惠券
     * @param id
     * @return
     */
    Coupon showCouponById(Long id);

    /**
     * 根据主键id审核优惠券
     * @param id
     * @param state
     * @return
     */
    boolean verifyCouponById(Long id,String state);

    /**
     * 根据更新优惠券
     * @param dto
     * @return
     */
    boolean updateCoupon(CouponDto dto);

    /**
     * 根据条件筛选优惠券
     * @param dto
     * @return
     */
    PageInfo<CouponVo> getCouponByTerm(SeleCouponDto dto);

}
