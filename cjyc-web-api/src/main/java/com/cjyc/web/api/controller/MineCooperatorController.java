package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.order.ListOrderDto;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.order.ListOrderVo;
import com.cjyc.web.api.service.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 我的合伙人
 * @author JPG
 */
@RestController
@Api(tags = "我的合伙人")
@RequestMapping(value = "/cooperator",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MineCooperatorController {

    @Resource
    private IOrderService orderService;

    /**
     * 查询订单列表
     * @author JPG
     */
    @ApiOperation(value = "查询订单列表")
    @PostMapping(value = "/list")
    public ResultVo<PageVo<ListOrderVo>> list(@RequestBody ListOrderDto reqDto) {
        return orderService.listForHhr(reqDto);
    }

}
