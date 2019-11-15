package com.cjyc.customer.api.controller;

import com.cjyc.common.model.dto.customer.freightBill.LineDto;
import com.cjyc.common.model.dto.customer.freightBill.TransportDto;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.customer.api.service.ITransportService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *  @author: zj
 *  @Date: 2019/10/12 16:36
 *  @Description:
 */
@Api(tags = "运输班线")
@CrossOrigin
@RestController
@RequestMapping("/transport")
public class TransportController {

    @Autowired
    private ITransportService transportService;

    @ApiOperation(value = "查看运价查询")
    @PostMapping(value = "/linePriceByCode")
    public ResultVo linePriceByCode(@RequestBody TransportDto dto){
        return transportService.linePriceByCode(dto);
    }

}