package com.cjyc.customer.api.controller;

import com.cjyc.common.model.dto.customer.freightBill.TransportPriceDto;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.customer.api.service.ITransportPriceService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *  @author: zj
 *  @Date: 2019/10/12 16:36
 *  @Description:
 */
@Api(tags = "价格相关信息")
@CrossOrigin
@RestController
@RequestMapping("/priceQuery")
public class TransportPriceController {

    @Autowired
    private ITransportPriceService iPriceQueryService;

    @ApiOperation(value = "查看运价查询")
    @PostMapping(value = "/getLinePriceByCode")
    public ResultVo getLinePriceByCode(@RequestBody TransportPriceDto dto){
        return iPriceQueryService.getLinePriceByCode(dto);
    }
}