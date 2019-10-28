package com.cjyc.customer.api.controller;

import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.OrderCarCenterVo;
import com.cjyc.customer.api.service.IOrderCarService;
import com.cjyc.customer.api.service.ISystemFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 *  @author: zj
 *  @Date: 2019/10/14 9:36
 *  @Description:订单明细（车辆信息）
 */
@Api(tags = "订单明细（车辆信息）")
@CrossOrigin
@RestController
@RequestMapping("/orderCarController")
public class OrderCarController {

    @Resource
    private IOrderCarService iOrderCarService;

    @ApiOperation(value = "通过车辆id查看车辆信息", notes = "通过车辆id查看车辆信息", httpMethod = "POST")
    @PostMapping(value = "/getOrderCarInfo/{orderCarId}")
    public ResultVo<OrderCarCenterVo> getOrderCarInfoById(@ApiParam(value="车辆id",required = true) @PathVariable Long orderCarId){
        OrderCarCenterVo vo = iOrderCarService.getOrderCarInfoById(orderCarId);
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),vo);
    }
}