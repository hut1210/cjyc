package com.cjyc.web.api.controller;

import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.service.service.ICustomerService;
import com.cjyc.web.api.dto.OrderDto;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * @auther litan
 * @description: com.cjyc.web.api.controller
 * @date:2019/9/28
 */
@RestController
@RequestMapping("/order")
@Api(tags = "order",description = "订单接口,包含下单、查询等")
public class OrderController {

    @Autowired
    private ICustomerService customerServiceCom;

    /**
     * 测试分页
     * */
    @ApiOperation(value = "分页测试接口", notes = "分页测试", httpMethod = "POST")
    @RequestMapping(value = "/testPageList", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "条数", required = true, dataType = "Integer", paramType = "query")
    })
    public ResultVo testPageList(Integer pageNum, Integer pageSize){

        PageInfo<Customer> customerPageInfo = customerServiceCom.pageList(pageNum,pageSize);
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),customerPageInfo);
    }

    /**
     * 下单测试--dto接收
     * */
    @ApiOperation(value = "下单测试接口", notes = "下单测试", httpMethod = "POST")
    @RequestMapping(value = "/orderTest", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultVo orderTest(@RequestBody OrderDto orderDto) {
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),orderDto);
    }

    /**
     * 下单测试2--Map接收
     * */
    @ApiOperation(value = "下单测试接口", notes = "下单测试", httpMethod = "POST")
    @RequestMapping(value = "/orderTest2", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultVo orderTest2(@RequestBody Map<String,Object> map) {
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),map);
    }

}
