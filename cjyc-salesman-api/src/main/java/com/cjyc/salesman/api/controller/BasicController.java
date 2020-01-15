package com.cjyc.salesman.api.controller;

import com.cjyc.common.model.dto.CommonDto;
import com.cjyc.common.model.dto.KeywordDto;
import com.cjyc.common.model.dto.AdminDto;
import com.cjyc.common.model.dto.customer.freightBill.FindStoreDto;
import com.cjyc.common.model.dto.customer.freightBill.TransportDto;
import com.cjyc.common.model.dto.salesman.customer.SalesCustomerDto;
import com.cjyc.common.model.dto.salesman.mine.AppCustomerIdDto;
import com.cjyc.common.model.vo.salesman.customer.SalesCustomerListVo;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.city.CityVo;
import com.cjyc.common.model.vo.customer.customerLine.CustomerLineVo;
import com.cjyc.common.model.vo.customer.customerLine.StoreListVo;
import com.cjyc.common.model.vo.salesman.mine.AppContractVo;
import com.cjyc.common.model.vo.web.carSeries.CarSeriesTree;
import com.cjyc.common.system.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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
    @Resource
    private ICsLineService csLineService;

    @ApiOperation(value = "查看品牌车系")
    @PostMapping(value = "/findCarSeries")
    public ResultVo<List<CarSeriesTree>> queryCarSeries(@RequestBody KeywordDto dto){
        return csCarSeriesService.tree(true,dto.getKeyword());
    }

    @ApiOperation(value = "城市三级列表")
    @PostMapping(value = "/findCity")
    public ResultVo<CityVo> queryCity(@RequestBody KeywordDto dto)
    {
        return csCityService.queryCity(dto);
    }

    @ApiOperation(value = "查询省/城市区树形结构")
    @PostMapping(value = "/findThreeCityByAdmin")
    public ResultVo<CityVo> findThreeCityByAdmin(@RequestBody AdminDto dto) {
        return csCityService.findThreeCityByAdminNew(dto);
    }

    @ApiOperation(value = "获取业务中心")
    @PostMapping(value = "/findStore")
    public ResultVo<StoreListVo> findStore(@RequestBody FindStoreDto dto){
        return csStoreService.findStore(dto);
    }

    @ApiOperation(value = "根据手机号或姓名客户信息")
    @PostMapping(value = "/findCustomer")
    public ResultVo<SalesCustomerListVo> findCustomer(@RequestBody SalesCustomerDto dto){
        return csCustomerService.findCustomer(dto);
    }

    @ApiOperation(value = "查看用户历史线路")
    @PostMapping(value = "/findLine")
    public ResultVo<PageVo<CustomerLineVo>> queryLinePage(@Validated @RequestBody CommonDto dto){
        return csCustomerLineService.queryLinePage(dto);
    }

    @ApiOperation(value = "模糊查询大客户信息")
    @PostMapping(value = "/findKeyCustomer")
    public ResultVo<SalesCustomerListVo> findKeyCustomer(@RequestBody SalesCustomerDto dto){
        return csCustomerService.findKeyCustomer(dto);
    }

    @ApiOperation(value = "模糊查询大客户合同")
    @PostMapping(value = "/findCustomerContract")
    public ResultVo<AppContractVo> findCustomerContract(@RequestBody AppCustomerIdDto dto){
        return csCustomerService.findCustomerContract(dto);
    }

    @ApiOperation(value = "获取运价")
    @PostMapping(value = "/linePriceByCode")
    public ResultVo<Map<String,Object>> linePriceByCode(@Validated @RequestBody TransportDto dto){
        return csLineService.linePriceByCode(dto);
    }

}