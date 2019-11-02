package com.cjyc.customer.api.controller;

import com.cjyc.common.model.dto.customer.invoice.InvoiceOrderQueryDto;
import com.cjyc.common.model.util.BasePageUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.customer.api.service.ICustomerLineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 *  @author: zj
 *  @Date: 2019/11/1 17:18
 *  @Description:客户历史线路
 */
@Api(tags = "客户历史线路")
@CrossOrigin
@RestController
@RequestMapping("/customerLine")
public class CustomerLineController {

    @Resource
    private ICustomerLineService customerLineService;

    @ApiOperation(value = "查看用户历史线路")
    @PostMapping(value = "/getCustomerLine")
    public ResultVo queryPage(@RequestBody InvoiceOrderQueryDto dto){
        return customerLineService.getCustomerLine(dto);
    }
}