package com.cjyc.customer.api.controller;

import com.cjyc.common.model.dto.customer.freightBill.FindStoreDto;
import com.cjyc.common.model.dto.customer.freightBill.TransportDto;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.customerLine.StoreListVo;
import com.cjyc.common.system.service.ICsStoreService;
import com.cjyc.customer.api.service.ITransportService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 *  @author: zj
 *  @Date: 2019/10/12 16:36
 *  @Description:
 */
@Api(tags = "班线/业务中心")
@CrossOrigin
@RestController
@RequestMapping("/transport")
public class TransportController {

    @Autowired
    private ITransportService transportService;
    @Resource
    private ICsStoreService csStoreService;

    @ApiOperation(value = "获取运价")
    @PostMapping(value = "/linePriceByCode")
    public ResultVo<Map<String,Object>> linePriceByCode(@Validated @RequestBody TransportDto dto){
        return transportService.linePriceByCode(dto);
    }

    @ApiOperation(value = "获取业务中心")
    @PostMapping(value = "/findStore")
    public ResultVo<StoreListVo> findStore(@Validated @RequestBody FindStoreDto dto){
        return csStoreService.findStore(dto);
    }

}