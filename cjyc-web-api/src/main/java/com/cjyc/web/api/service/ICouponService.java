package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.coupon.ConsumeCouponDto;
import com.cjyc.common.model.dto.web.coupon.CouponDto;
import com.cjyc.common.model.dto.web.coupon.SeleCouponDto;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.coupon.ConsumeCouponVo;
import com.cjyc.common.model.vo.web.coupon.CouponVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  @author: zj
 *  @Date: 2019/10/16 10:08
 *  @Description:优惠券
 */
public interface ICouponService {

    /**
     * 新增/修改优惠券
     * @param dto
     * @return
     */
    ResultVo saveOrModifyCoupon(CouponDto dto);

    /**
     * 审核/作废优惠券
     * @param dto
     * @return
     */
    ResultVo verifyCoupon(OperateDto dto);

    /**
     * 根据条件筛选优惠券
     * @param dto
     * @return
     */
    ResultVo<PageVo<CouponVo>> getCouponByTerm(SeleCouponDto dto);

    /**
     * 查看优惠券消耗明细
     * @param dto
     * @return
     */
    ResultVo<PageVo<ConsumeCouponVo>> getConsumeDetail(ConsumeCouponDto dto);

    /**
     * 优惠券管理导出至excel
     * @param request
     * @param response
     */
    void exportCouponExcel(HttpServletRequest request, HttpServletResponse response);

}
