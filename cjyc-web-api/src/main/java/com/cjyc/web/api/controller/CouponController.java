package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.coupon.ConsumeCouponDto;
import com.cjyc.common.model.dto.web.coupon.CouponDto;
import com.cjyc.common.model.dto.web.coupon.SeleCouponDto;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BasePageUtil;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.coupon.CouponVo;
import com.cjyc.web.api.service.ICouponService;
import com.github.pagehelper.PageInfo;
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

    @ApiOperation(value = "优惠券新增")
    @PostMapping(value = "/saveCoupon")
    public ResultVo saveCoupon(@Validated({ CouponDto.SaveCouponDto.class }) @RequestBody CouponDto dto){
        return couponService.saveCoupon(dto);
    }

    @ApiOperation(value = "更新优惠券")
    @PostMapping(value = "/updateCoupon")
    public ResultVo updateCoupon(@Validated({ CouponDto.UpaCouponDto.class }) @RequestBody CouponDto dto){
        boolean result = couponService.updateCoupon(dto);
        return result ? BaseResultUtil.success() : BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "审核/作废优惠券")
    @PostMapping(value = "/operateCoupon")
    public ResultVo operateCoupon(@RequestBody OperateDto dto) {
        boolean result = couponService.operateCoupon(dto);
        return result ? BaseResultUtil.success() : BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据条件筛选分页查询优惠券")
    @PostMapping(value = "/getCouponByTerm")
    public ResultVo getCouponByTerm(@RequestBody SeleCouponDto dto){
        BasePageUtil.initPage(dto.getCurrentPage(),dto.getPageSize());
        return couponService.getCouponByTerm(dto);
    }

    @ApiOperation(value = "查询该优惠券消耗明细")
    @PostMapping(value = "/getConsumeDetail")
    public ResultVo getConsumeDetail(@RequestBody ConsumeCouponDto dto){
        BasePageUtil.initPage(dto.getCurrentPage(),dto.getPageSize());
        return couponService.getConsumeDetail(dto);
    }

}