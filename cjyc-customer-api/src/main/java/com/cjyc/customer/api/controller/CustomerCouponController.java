package com.cjyc.customer.api.controller;

import com.cjyc.common.model.dto.customer.invoice.InvoiceApplyQueryDto;
import com.cjyc.common.model.util.BasePageUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.coupon.CustomerCouponVo;
import com.cjyc.customer.api.service.ICustomerCouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *  @author: zj
 *  @Date: 2019/10/17 9:09
 *  @Description:用户端优惠券
 */
@Api(tags = "优惠券")
@CrossOrigin
@RestController
@RequestMapping("/coupon")
public class CustomerCouponController {

    @Autowired
    private ICustomerCouponService couponSendService;

    @ApiOperation(value = "客户优惠券")
    @PostMapping(value = "/customerCoupon")
    public ResultVo<PageVo<CustomerCouponVo>> customerCoupon(@RequestBody InvoiceApplyQueryDto dto){
        BasePageUtil.initPage(dto);
        return couponSendService.customerCoupon(dto);
    }
}