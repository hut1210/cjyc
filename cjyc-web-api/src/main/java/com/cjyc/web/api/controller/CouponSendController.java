package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.coupon.CouponSendDto;
import com.cjyc.common.model.dto.web.coupon.SeleCouponSendDto;
import com.cjyc.common.model.entity.CouponSend;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BasePageUtil;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.coupon.CouponSendVo;
import com.cjyc.common.model.vo.web.coupon.CouponVo;
import com.cjyc.web.api.service.ICouponSendService;
import com.cjyc.web.api.service.ICouponService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "发放优惠券")
@CrossOrigin
@RestController
@RequestMapping("/couponSend")
public class CouponSendController {

    @Autowired
    private ICouponSendService iCouponSendService;

    @ApiOperation(value = "根据条件筛选优惠券", notes = "根据条件筛选优惠券", httpMethod = "POST")
    @PostMapping(value = "/seleCouponSendByTerm")
    public ResultVo<PageVo<CouponSendVo>> seleCouponSendByTerm(@RequestBody SeleCouponSendDto dto){
        BasePageUtil.initPage(dto.getCurrentPage(),dto.getPageSize());
        PageInfo<CouponSendVo> pageInfo = iCouponSendService.seleCouponSendByTerm(dto);
        return BaseResultUtil.getPageVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),pageInfo);
    }

    @ApiOperation(value = "根据ids分页查询优惠券", notes = "根据ids分页查询优惠券", httpMethod = "POST")
    @PostMapping(value = "/seleCouponSendByIds")
    public ResultVo<PageVo<CouponSend>> seleCouponSendByIds(@RequestBody CouponSendDto dto){
        BasePageUtil.initPage(dto.getCurrentPage(),dto.getPageSize());
        PageInfo<CouponSend> pageInfo = iCouponSendService.seleCouponSendByIds(dto);
        return BaseResultUtil.getPageVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),pageInfo);
    }

    
}