package com.cjyc.customer.api.controller;

import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.customer.api.service.IInvoiceOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 *  @author: zj
 *  @Date: 2019/10/31 19:31
 *  @Description:用户端发票
 */
@Api(tags = "发票")
@CrossOrigin
@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    @Resource
    private IInvoiceOrderService invoiceOrderService;

    @ApiOperation(value = "分页查询用户未开发票的订单")
    @PostMapping(value = "/getUnbilledOrder")
    public ResultVo getUnbilledOrder(@PathVariable @ApiParam(value = "用户userId",required = true)Long userId){
        return invoiceOrderService.getUnbilledOrder(userId);
    }

}