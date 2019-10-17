package com.cjyc.customer.api.controller;

import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BasePageUtil;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.couponSend.CouponSendVo;
import com.cjyc.customer.api.service.ICouponSendService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *  @author: zj
 *  @Date: 2019/10/17 9:09
 *  @Description:用户端优惠券
 */
@Api(tags = "用户端优惠券明细")
@CrossOrigin
@RestController
@RequestMapping("/couponSend")
public class CouponSendController {

    @Autowired
    private ICouponSendService iCouponSendService;

    @ApiOperation(value = "根据userId查看所属优惠券明细", notes = "根据userId查看所属优惠券明细", httpMethod = "POST")
    @PostMapping(value = "/getCouponSendByUserId/{userId}/{currentPage}/{pageSize}")
    public ResultVo<PageVo<CouponSendVo>> getCouponSendByUserId(@PathVariable @ApiParam(value = "用户userId",required = true) Long userId,
                                                  @PathVariable @ApiParam(value = "当前页",required = true) Integer currentPage,
                                                  @PathVariable @ApiParam(value = "每页大小",required = true) Integer pageSize){
        if(userId == null || currentPage == null || pageSize == null){
            return BaseResultUtil.getVo(ResultEnum.MOBILE_PARAM_ERROR.getCode(),ResultEnum.MOBILE_PARAM_ERROR.getMsg());
        }
        BasePageUtil.initPage(currentPage,pageSize);
        PageInfo<CouponSendVo> pageInfo = iCouponSendService.getCouponSendByUserId(userId,currentPage,pageSize);
        return BaseResultUtil.getPageVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),pageInfo);
    }
}