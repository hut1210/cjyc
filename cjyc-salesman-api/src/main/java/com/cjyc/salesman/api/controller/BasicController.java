package com.cjyc.salesman.api.controller;

import com.cjyc.common.model.dto.CommonDto;
import com.cjyc.common.model.dto.KeywordDto;
import com.cjyc.common.model.dto.customer.freightBill.FindStoreDto;
import com.cjyc.common.model.dto.salesman.customer.SalesCustomerDto;
import com.cjyc.common.model.vo.salesman.customer.SalesCustomerListVo;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.city.CityVo;
import com.cjyc.common.model.vo.customer.customerLine.CustomerLineVo;
import com.cjyc.common.model.vo.customer.customerLine.StoreListVo;
import com.cjyc.common.model.vo.web.carSeries.CarSeriesTree;
import com.cjyc.common.system.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "基础数据")
@RestController
@RequestMapping(value = "/basic",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class BasicController {

    @Resource
    private ICsCarSeriesService csCarSeriesService;
    @Resource
    private ICsCityService csCityService;
    @Resource
    private ICsStoreService csStoreService;
    @Resource
    private ICsCustomerLineService csCustomerLineService;
    @Resource
    private ICsCustomerService csCustomerService;

    @ApiOperation(value = "查看品牌车系")
    @PostMapping(value = "/findCarSeries")
    public ResultVo<List<CarSeriesTree>> queryCarSeries(@RequestBody KeywordDto dto){
        return csCarSeriesService.tree(false,dto.getKeyword());
    }

    @ApiOperation(value = "查看城市")
    @PostMapping(value = "/findCity")
    public ResultVo<CityVo> queryCity(@RequestBody KeywordDto dto)
    {
        return csCityService.queryCity(dto);
    }

    @ApiOperation(value = "获取业务中心")
    @PostMapping(value = "/findStore")
    public ResultVo<StoreListVo> findStore(@RequestBody FindStoreDto dto){
        return csStoreService.findStore(dto);
    }

    @ApiOperation(value = "查看用户历史线路")
    @PostMapping(value = "/findLine")
    public ResultVo<PageVo<CustomerLineVo>> queryLinePage(@RequestBody CommonDto dto){
        return csCustomerLineService.queryLinePage(dto);
    }

    @ApiOperation(value = "客户信息")
    @PostMapping(value = "/findCustomer")
    public ResultVo<SalesCustomerListVo> findCustomer(@RequestBody SalesCustomerDto dto){
        return csCustomerService.findCustomer(dto);
    }

}