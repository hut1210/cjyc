package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.coupon.CouponSendDto;
import com.cjyc.common.model.dto.web.coupon.SeleCouponSendDto;
import com.cjyc.common.model.entity.CouponSend;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.coupon.CouponSendVo;
import com.cjyc.web.api.service.ICouponSendService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "发放优惠券")
@CrossOrigin
@RestController
@RequestMapping("/couponSend")
public class CouponSendController {

    @Autowired
    private ICouponSendService couponSendService;

    @ApiOperation(value = "根据条件筛选优惠券")
    @PostMapping(value = "/seleCouponSendByTerm")
    public ResultVo<PageVo<CouponSendVo>> seleCouponSendByTerm(@RequestBody SeleCouponSendDto dto){
        PageInfo<CouponSendVo> pageInfo = couponSendService.seleCouponSendByTerm(dto);
        return BaseResultUtil.getPageVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),pageInfo);
    }

    @ApiOperation(value = "根据ids分页查询优惠券")
    @PostMapping(value = "/seleCouponSendByIds")
    public ResultVo<PageVo<CouponSend>> seleCouponSendByIds(@RequestBody CouponSendDto dto){
        PageInfo<CouponSend> pageInfo = couponSendService.seleCouponSendByIds(dto);
        return BaseResultUtil.getPageVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),pageInfo);
    }

    
}