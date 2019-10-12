package com.cjyc.customer.api.controller;

/**
 * @auther litan
 * @description: com.cjyc.customer.api.controller
 * @date:2019/10/9
 */

import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.service.service.ICarSeriesService;
import com.cjyc.common.service.service.ICityService;
import com.cjyc.common.service.service.ICustomerContactService;
import com.cjyc.common.service.service.ICustomerService;
import com.cjyc.customer.api.dto.OrderDto;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author leo
 * @date 2019/7/27.
 */
@RestController
@RequestMapping("/index")
@Api(tags = "index",description = "基础信息相关的接口")
public class IndexController {

    @Autowired
    ICityService cityService;
    @Autowired
    ICarSeriesService carSeriesService;
    @Autowired
    private ICustomerContactService customerContactService;
    @Autowired
    private ICustomerService customerService;


    /**
     * 获取联系人接口（分页）
     * */
    @ApiOperation(value = "客户端获取联系人接口", notes = "客户端获取联系人接口", httpMethod = "POST")
    @RequestMapping(value = "/getContacts", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customerId", value = "客户id",  required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", defaultValue = "20", dataType = "Integer", paramType = "query")
    })
    public ResultVo<PageInfo<CustomerContact>> getContacts(Long customerId, Integer page, Integer pageSize) {
        PageInfo<CustomerContact> pageInfo = customerContactService.getContactPage(customerId,page,pageSize);
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),pageInfo);
    }

    /**
     * 添加联系人接口
     * */
    @ApiOperation(value = "添加联系人接口", notes = "添加联系人接口", httpMethod = "POST")
    @RequestMapping(value = "/addContact", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultVo addContact(@RequestBody CustomerContact customerContact) {
        boolean result = customerContactService.addCustomerContact(customerContact);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    /**
     * 获取车系列表
     * */
    @ApiOperation(value = "获取下单车辆列表", notes = "获取下单车辆列表", httpMethod = "POST")
    @RequestMapping(value = "/getCarSeriesList", method = RequestMethod.POST,  consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "检索关键词",  dataType = "String", paramType = "query")
    })
    public ResultVo<List<CarSeries>> getCarSeriesList(String keyword) {
        List<CarSeries> carSeriesList = carSeriesService.getList(keyword);
        return BaseResultUtil.success(carSeriesList);
    }

    /**
     * 获取城市列表
     * */
    @ApiOperation(value = "获取城市列表", notes = "获取城市列表", httpMethod = "POST")
    @RequestMapping(value = "/getCityList", method = RequestMethod.POST,  consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cityCode", value = "城市code,空表示全国和大区", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "检索关键词",  dataType = "String", paramType = "query")
    })
    public ResultVo<List<City>> getCityList(String cityCode, String keyword) {
        List<City> cityList = cityService.getCityList(cityCode,keyword);
        return BaseResultUtil.success(cityList);
    }

    /**
     * 获取历史线路接口（分页）
     * */
    @ApiOperation(value = "客户端历史线路接口", notes = "客户端历史线路接口", httpMethod = "POST")
    @RequestMapping(value = "/getLineHistory", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customerId", value = "客户id",  required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", defaultValue = "20", dataType = "Integer", paramType = "query")
    })
    public ResultVo<PageInfo<CustomerLine>> getLineHistory(Long customerId, Integer page, Integer pageSize) {
        PageInfo<CustomerLine> pageInfo = customerService.getLineHistoryPage(customerId,page,pageSize);
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),pageInfo);
    }
}
