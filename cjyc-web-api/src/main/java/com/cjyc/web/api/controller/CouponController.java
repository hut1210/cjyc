package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.coupon.ConsumeCouponDto;
import com.cjyc.common.model.dto.web.coupon.CouponDto;
import com.cjyc.common.model.dto.web.coupon.SeleCouponDto;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.coupon.ConsumeCouponVo;
import com.cjyc.common.model.vo.web.coupon.CouponVo;
import com.cjyc.web.api.service.ICouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 *  @author: zj
 *  @Date: 2019/10/16 16:14
 *  @Description:优惠券管理
 */
@Api(tags = "优惠券管理")
@CrossOrigin
@RestController
@RequestMapping("/coupon")
public class CouponController {

    @Autowired
    private ICouponService couponService;

    @ApiOperation(value = "优惠券新增/修改")
    @PostMapping(value = "/saveOrModifyCoupon")
    public ResultVo saveOrModifyCoupon(@Validated @RequestBody CouponDto dto){
        return couponService.saveOrModifyCoupon(dto);
    }

    @ApiOperation(value = "审核/作废优惠券")
    @PostMapping(value = "/verifyCoupon")
    public ResultVo verifyCoupon(@RequestBody OperateDto dto) {
        return couponService.verifyCoupon(dto);
    }

    @ApiOperation(value = "根据条件筛选分页查询优惠券(优惠券管理中分页查询)")
    @PostMapping(value = "/getCouponByTerm")
    public ResultVo<PageVo<CouponVo>> getCouponByTerm(@RequestBody SeleCouponDto dto){
        return couponService.getCouponByTerm(dto);
    }

    @ApiOperation(value = "查询该优惠券消耗明细")
    @PostMapping(value = "/getConsumeDetail")
    public ResultVo<PageVo<ConsumeCouponVo>> getConsumeDetail(@RequestBody ConsumeCouponDto dto){
        return couponService.getConsumeDetail(dto);
    }

}