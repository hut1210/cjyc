package com.cjyc.customer.api.controller;

import com.cjyc.common.model.dto.customer.order.OrderDetailDto;
import com.cjyc.common.model.dto.customer.order.OrderQueryDto;
import com.cjyc.common.model.dto.web.order.CancelOrderDto;
import com.cjyc.common.model.dto.web.order.SaveOrderDto;
import com.cjyc.common.model.dto.web.task.ReceiptTaskDto;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.enums.UserTypeEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.order.OrderCenterDetailVo;
import com.cjyc.common.model.vo.customer.order.OrderCenterVo;
import com.cjyc.common.system.service.ICsAdminService;
import com.cjyc.common.system.service.ICsCustomerService;
import com.cjyc.common.system.service.ICsOrderService;
import com.cjyc.common.system.service.ICsTaskService;
import com.cjyc.customer.api.service.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;


/**
 * 订单
 * @author JPG
 */
@RequestMapping("/order")
@Api(tags = "订单管理")
@RestController
public class OrderController {
    @Autowired
    IOrderService orderService;
    @Resource
    private ICsCustomerService csCustomerService;
    @Resource
    private ICsAdminService csAdminService;
    @Resource
    private ICsOrderService csOrderService;
    @Resource
    private ICsTaskService csTaskService;
    /**
     * 保存,只保存无验证
     * @author JPG
     */
    @ApiOperation(value = "订单保存")
    @PostMapping(value = "/save")
    public ResultVo save(@RequestBody SaveOrderDto reqDto) {

        //验证用户存不存在
        Customer customer = csCustomerService.getById(reqDto.getLoginId(), true);
        if(customer == null){
            return BaseResultUtil.fail("用户不存在");
        }
        reqDto.setCreateUserId(customer.getUserId());
        reqDto.setCreateUserName(customer.getName());

        //发送推送信息
        return orderService.save(reqDto);
    }

    /**
     * 订单提交-客户
     * @author JPG
     */
    @ApiOperation(value = "订单提交-客户")
    @PostMapping(value = "/submit")
    public ResultVo submit(@Validated @RequestBody SaveOrderDto reqDto) {

        //验证用户存不存在
        Customer admin = csCustomerService.getById(reqDto.getLoginId(), true);
        if(admin == null){
            return BaseResultUtil.fail("用户不存在");
        }
        reqDto.setCreateUserId(admin.getUserId());
        reqDto.setCreateUserName(admin.getName());

        //发送推送信息
        return orderService.submit(reqDto);
    }
    /**
     * 订单提交
     * @author JPG
     */
 /*   @ApiOperation(value = "订单提交-业务员")
    @PostMapping(value = "/commit")
    public ResultVo commit(@Validated @RequestBody CommitOrderDto reqDto) {

        //验证用户存不存在
        Customer admin = csCustomerService.getById(reqDto.getLoginId(), true);
        if(admin == null){
            return BaseResultUtil.fail("用户不存在");
        }
        reqDto.setCreateUserId(admin.getUserId());
        reqDto.setCreateUserName(admin.getName());

        //发送推送信息
        return orderService.commit(reqDto);
    }*/

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
     * 功能描述: 查询每种状态下的订单数量
     * @author liuxingxiang
     * @date 2019/11/26
     * @param loginId
     * @return com.cjyc.common.model.vo.ResultVo<java.util.Map<java.lang.String,java.lang.Object>>
     */
    @ApiOperation(value = "查询每种状态下的订单数量", notes = "查询各种订单状态下的订单数量", httpMethod = "POST")
    @PostMapping(value = "/getOrderCount/{loginId}")
    public ResultVo<Map<String,Object>> getOrderCount(@PathVariable Long loginId){
        return orderService.getOrderCount(loginId);
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

    /**
     * 签收-客户
     * @author JPG
     */
    @ApiOperation(value = "签收")
    @PostMapping(value = "/car/receipt")
    public ResultVo receipt(@RequestBody ReceiptTaskDto reqDto) {
        Customer customer = csCustomerService.validate(reqDto.getLoginId());
        reqDto.setLoginName(customer.getName());
        reqDto.setLoginPhone(customer.getContactPhone());
        reqDto.setLoginType(UserTypeEnum.CUSTOMER);
        return csTaskService.receipt(reqDto);
    }

}
