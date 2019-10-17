package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.BasePageDto;
import com.cjyc.common.model.dto.web.CustomerDto;
import com.cjyc.common.model.dto.web.coupon.CouponDto;
import com.cjyc.common.model.dto.web.coupon.SeleCouponDto;
import com.cjyc.common.model.entity.Coupon;
import com.cjyc.common.model.entity.Customer;
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
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  @author: zj
 *  @Date: 2019/10/16 16:14
 *  @Description:优惠券管理
 */
@Api(tags = "优惠券管理")
@CrossOrigin
@RestController
@RequestMapping("/couponController")
public class CouponController {

    @Autowired
    private ICouponService iCouponService;

    @ApiOperation(value = "优惠券新增", notes = "优惠券新增", httpMethod = "POST")
    @RequestMapping(value = "/saveCoupon", method = RequestMethod.POST)
    public ResultVo saveCoupon(@Validated({ CouponDto.SaveCouponDto.class }) @RequestBody CouponDto dto){
        boolean result = iCouponService.saveCoupon(dto);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "分页查看优惠券", notes = "分页查看优惠券", httpMethod = "POST")
    @PostMapping(value = "/getAllCoupon")
    public ResultVo<PageVo<CouponVo>> getAllCoupon(@RequestBody BasePageDto basePageDto){
        BasePageUtil.initPage(basePageDto.getCurrentPage(),basePageDto.getPageSize());
        PageInfo<CouponVo> pageInfo = iCouponService.getAllCoupon(basePageDto);
        return BaseResultUtil.getPageVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),pageInfo);
    }

    @ApiOperation(value = "根据ids作废优惠券", notes = "根据ids作废优惠券", httpMethod = "POST")
    @PostMapping(value = "/abolishCouponByIds")
    public ResultVo abolishCouponByIds(@RequestBody List<Long> ids){
        if(ids == null || ids.size() ==0){
            return BaseResultUtil.getVo(ResultEnum.MOBILE_PARAM_ERROR.getCode(),ResultEnum.MOBILE_PARAM_ERROR.getMsg());
        }
        boolean result = iCouponService.abolishCouponByIds(ids);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据id查看优惠券", notes = "根据id查看优惠券", httpMethod = "POST")
    @PostMapping(value = "/showCouponById/{id}")
    public ResultVo<Coupon> showCouponById(@PathVariable @ApiParam(value = "主键id",required = true) Long id){
        if(id == null){
            return BaseResultUtil.getVo(ResultEnum.MOBILE_PARAM_ERROR.getCode(),ResultEnum.MOBILE_PARAM_ERROR.getMsg());
        }
        Coupon coupon = iCouponService.showCouponById(id);
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),coupon);
    }

    @ApiOperation(value = "根据id审核优惠券", notes = "根据id审核优惠券", httpMethod = "POST")
    @PostMapping(value = "/verifyCouponById/{id}/{state}")
    public ResultVo verifyCouponById(@PathVariable @ApiParam(value = "主键id",required = true) Long id,
                                     @PathVariable @ApiParam(value = "审核状态 3：审核通过  5：审核不通过",required = true) String state){
        if(id == null || StringUtils.isBlank(state)){
            return BaseResultUtil.getVo(ResultEnum.MOBILE_PARAM_ERROR.getCode(),ResultEnum.MOBILE_PARAM_ERROR.getMsg());
        }
        boolean result = iCouponService.verifyCouponById(id,state);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "更新优惠券", notes = "更新优惠券", httpMethod = "POST")
    @PostMapping(value = "/updateCoupon")
    public ResultVo updateCoupon(@Validated({ CouponDto.UpaCouponDto.class }) @RequestBody CouponDto dto){
        boolean result = iCouponService.updateCoupon(dto);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据条件筛选分页查询优惠券", notes = "根据条件筛选分页查询优惠券", httpMethod = "POST")
    @PostMapping(value = "/getCouponByTerm")
    public ResultVo<PageVo<CouponVo>> getCouponByTerm(@RequestBody SeleCouponDto dto){
        BasePageUtil.initPage(dto.getCurrentPage(),dto.getPageSize());
        PageInfo<CouponVo> pageInfo = iCouponService.getCouponByTerm(dto);
        return BaseResultUtil.getPageVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),pageInfo);
    }
}