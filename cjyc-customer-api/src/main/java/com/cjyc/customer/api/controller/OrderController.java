package com.cjyc.customer.api.controller;

import com.cjyc.common.model.entity.City;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.service.service.ICityService;
import com.cjyc.customer.api.dto.OrderDto;
import com.cjyc.customer.api.service.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Created by leo on 2019/7/25.
 */
@RequestMapping("/order")
@Api(tags = "订单",description = "订单相关接口")
@RestController
public class OrderController {

    @Autowired
    IOrderService orderService;
    @Autowired
    ICityService cityService;

    /**
     * 客户端下单--dto接收
     * */
    @ApiOperation(value = "客户端下单接口", notes = "客户端下单", httpMethod = "POST")
    @RequestMapping(value = "/commit", method = RequestMethod.POST,  consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultVo commit(@RequestBody OrderDto orderDto) {
        boolean result = orderService.commitOrder(orderDto);

        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),orderDto)
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),"提交失败",orderDto);
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
    public ResultVo getCityList(String cityCode,String keyword) {
        List<City> cityList = cityService.getCityList(cityCode,keyword);
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),cityList);
    }


}
