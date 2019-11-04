package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.coupon.ConsumeCouponDto;
import com.cjyc.common.model.dto.web.coupon.CouponDto;
import com.cjyc.common.model.dto.web.coupon.SeleCouponDto;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.coupon.CouponVo;
import com.github.pagehelper.PageInfo;

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
    ResultVo saveCoupon(CouponDto dto);

    /**
     * 根据更新优惠券
     * @param dto
     * @return
     */
    boolean updateCoupon(CouponDto dto);

    /**
     * 审核/作废优惠券
     * @param dto
     * @return
     */
    boolean operateCoupon(OperateDto dto);

    /**
     * 根据条件筛选优惠券
     * @param dto
     * @return
     */
    ResultVo getCouponByTerm(SeleCouponDto dto);

    /**
     * 查看优惠券消耗明细
     * @param dto
     * @return
     */
    ResultVo getConsumeDetail(ConsumeCouponDto dto);

}
