package com.cjyc.foreign.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjyc.common.model.dto.web.order.CancelOrderDto;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.enums.UserTypeEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.system.service.ICsCustomerService;
import com.cjyc.common.system.service.ICsOrderService;
import com.cjyc.foreign.api.dto.req.CancelOrderReqDto;
import com.cjyc.foreign.api.dto.req.OrderDetailReqDto;
import com.cjyc.foreign.api.dto.req.OrderSaveReqDto;
import com.cjyc.foreign.api.dto.res.OrderDetailResDto;
import com.cjyc.foreign.api.service.IOrderService;
import com.cjyc.foreign.api.utils.LoginAccountUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 *
 */
@RestController
@Api(tags = {"订单"})
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private ICsCustomerService csCustomerService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private ICsOrderService csOrderService;

    /**
     * 功能描述: 保存订单
     * @author liuxingxiang
     * @date 2020/3/11
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo<java.lang.String>
     */
    @ApiOperation(value = "保存订单")
    @PostMapping("/saveOrder")
    public ResultVo<String> saveOrder(@RequestBody @Validated OrderSaveReqDto dto) {
        return orderService.saveOrder(dto);
    }

    /**
     * 功能描述: 查询订单详情
     * @author liuxingxiang
     * @date 2020/3/11
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo<com.cjyc.common.model.vo.customer.order.OrderCenterDetailVo>
     */
    @ApiOperation(value = "根据订单号查询订单详情")
    @PostMapping(value = "/getOrderDetailByOrderNo")
    public ResultVo<OrderDetailResDto> getDetail(@RequestBody @Validated OrderDetailReqDto dto){
        return orderService.getOrderDetailByOrderNo(dto);
    }

    @ApiOperation(value = "取消订单")
    @PostMapping("/cancelOrder")
    public ResultVo<String> cancelOrder(@Valid @RequestBody CancelOrderReqDto reqDto) {
        String account = LoginAccountUtil.getLoginAccount();
        if (StringUtils.isEmpty(account)) {
            return BaseResultUtil.fail("登录账号信息有误，请检查!");
        }
        //查询订单信息是否存在
        Order order = orderService.getOne(new QueryWrapper<Order>().lambda()
                .eq(Order::getNo, reqDto.getOrderNo()));
        if (null == order) {
            return BaseResultUtil.fail("订单编号有误，请检查!");
        }
        Customer customer = csCustomerService.getByPhone(account, true);
        if (null == customer) {
            return BaseResultUtil.fail("用户信息有误，请检查!");
        }
        //请求信息封装
        CancelOrderDto dto = new CancelOrderDto();
        dto.setOrderId(order.getId());
        dto.setLoginId(customer.getId());
        dto.setLoginPhone(customer.getContactPhone());
        dto.setLoginName(customer.getName());
        dto.setLoginType(UserTypeEnum.CUSTOMER);
        ResultVo cancelVo = csOrderService.cancel(dto);
        if (isResultSuccess(cancelVo)) {
            return BaseResultUtil.getVo(cancelVo.getCode(), cancelVo.getMsg(),
                    reqDto.getOrderNo());
        }else {
            return cancelVo;
        }
    }

    /**
     * 检查返回结果是否成功
     *
     * @param resultVo
     * @return
     */
    private boolean isResultSuccess(ResultVo resultVo) {
        if (null == resultVo) {
            return false;
        }
        return ResultEnum.SUCCESS.getCode().equals(resultVo.getCode());
    }
}
