package com.cjyc.customer.api.controller;

import com.cjyc.common.model.dto.customer.order.OrderDetailDto;
import com.cjyc.common.model.dto.customer.order.OrderQueryDto;
import com.cjyc.common.model.dto.customer.order.SimpleSaveOrderDto;
import com.cjyc.common.model.dto.web.order.CancelOrderDto;
import com.cjyc.common.model.dto.web.order.SaveOrderCarDto;
import com.cjyc.common.model.dto.web.order.SaveOrderDto;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.enums.order.OrderStateEnum;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.order.OrderCenterDetailVo;
import com.cjyc.common.model.vo.customer.order.OrderCenterVo;
import com.cjyc.common.system.service.ICsCustomerService;
import com.cjyc.common.system.service.ICsOrderService;
import com.cjyc.customer.api.service.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * 订单
 * @author JPG
 */
@RequestMapping("/order")
@Api(tags = "订单管理")
@RestController
public class OrderController {
    @Autowired
    private IOrderService orderService;
    @Resource
    private ICsCustomerService csCustomerService;
    @Resource
    private ICsOrderService csOrderService;
    /**
     * 保存,只保存无验证
     * @author JPG
     */
    @ApiOperation(value = "订单保存")
    @PostMapping(value = "/save")
    public ResultVo save(@RequestBody SaveOrderDto reqDto) {

        //验证用户存不存在
        Customer customer = csCustomerService.validate(reqDto.getLoginId());
        reqDto.setLoginName(customer.getName());
        reqDto.setCreateUserId(customer.getUserId());
        reqDto.setCreateUserName(customer.getName());
        reqDto.setState(OrderStateEnum.WAIT_SUBMIT.code);
        //干线费用
        List<SaveOrderCarDto> carList = reqDto.getOrderCarList();
        if(!CollectionUtils.isEmpty(carList) && reqDto.getLineWlFreightFee() != null){
            carList.forEach(dto -> dto.setTrunkFee(reqDto.getLineWlFreightFee()));
        }
        //发送推送信息
        return csOrderService.save(reqDto);
    }

    /**
     * 订单提交-客户
     * @author JPG
     */
    @ApiOperation(value = "订单提交-客户")
    @PostMapping(value = "/submit")
    public ResultVo submit(@Validated @RequestBody SaveOrderDto reqDto) {

        //验证用户存不存在
        Customer customer = csCustomerService.validate(reqDto.getLoginId());
        reqDto.setLoginName(customer.getName());
        reqDto.setCreateUserId(customer.getUserId());
        reqDto.setCreateUserName(customer.getName());
        reqDto.setState(OrderStateEnum.SUBMITTED.code);
        //干线费用
        List<SaveOrderCarDto> carList = reqDto.getOrderCarList();
        if(!CollectionUtils.isEmpty(carList) && reqDto.getLineWlFreightFee() != null){
            carList.forEach(dto -> dto.setTrunkFee(reqDto.getLineWlFreightFee()));
        }
        //发送推送信息
        return csOrderService.save(reqDto);
    }

    /**
     * 订单提交-客户
     * @author JPG
     */
    @ApiOperation(value = "订单简单提交-客户")
    @PostMapping(value = "/simple/submit")
    public ResultVo simpleSubmit(@Validated @RequestBody SimpleSaveOrderDto reqDto) {

        //验证用户存不存在
        Customer customer = csCustomerService.validate(reqDto.getLoginId());
        reqDto.setLoginName(customer.getName());

        //发送推送信息
        return orderService.simpleSubmit(reqDto);
    }

    /**
     * 功能描述: 分页查询订单列表
     * @author liuxingxiang
     * @date 2019/11/26
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo<com.cjyc.common.model.vo.PageVo<com.cjyc.common.model.vo.customer.order.OrderCenterVo>>
     */
    @ApiOperation(value = "分页查询订单列表", notes = "根据条件分页查询订单", httpMethod = "POST")
    @PostMapping(value = "/getPage")
    public ResultVo<PageVo<OrderCenterVo>> getPage(@RequestBody @Validated OrderQueryDto dto){
        return orderService.getPage(dto);
    }

    /**
     * 取消订单
     * @author JPG
     */
    @ApiOperation(value = "取消订单")
    @PostMapping(value = "/cancel")
    public ResultVo cancel(@RequestBody CancelOrderDto reqDto) {
        Customer customer = csCustomerService.validate(reqDto.getLoginId());
        reqDto.setLoginName(customer.getName());
        return csOrderService.cancel(reqDto);
    }

    /**
     * 功能描述: 查询订单详情
     * @author liuxingxiang
     * @date 2019/11/26
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo<com.cjyc.common.model.vo.customer.order.OrderCenterDetailVo>
     */
    @ApiOperation(value = "查询订单详情", notes = "根据条件查询订单明细：参数orderNo(订单号),loginId(客户ID)", httpMethod = "POST")
    @PostMapping(value = "/getDetail")
    public ResultVo<OrderCenterDetailVo> getDetail(@RequestBody @Validated OrderDetailDto dto){
        return orderService.getDetail(dto);
    }



}
