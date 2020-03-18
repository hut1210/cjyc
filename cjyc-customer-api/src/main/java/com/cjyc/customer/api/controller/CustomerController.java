package com.cjyc.customer.api.controller;

import com.cjyc.common.model.dto.customer.AppCustomerDto;
import com.cjyc.common.model.dto.customer.UpdateCustomerDto;
import com.cjyc.common.model.dto.web.customer.CustomerfuzzyListDto;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.customerInfo.CustomerCardInfoVo;
import com.cjyc.common.model.vo.customer.customerInfo.AppCustomerInfoVo;
import com.cjyc.common.model.vo.web.customer.ShowPartnerVo;
import com.cjyc.customer.api.service.ICustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 客户
 * @author JPG
 */
@Api(tags = "客户")
@Slf4j
@RestController
@RequestMapping(value = "/cust",
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CustomerController {

    @Resource
    private ICustomerService customerService;

    /**
     * 模糊查询用户
     * @author JPG
     * @since 2019/10/22 15:39
     */
    @ApiOperation(value = "修改个人信息")
    @PostMapping("/fuzzy/list/")
    public ResultVo fuzzyList(@RequestBody CustomerfuzzyListDto reqDto) {

        return customerService.fuzzyList(reqDto);
    }


    @ApiOperation(value = "修改个人信息")
    @PostMapping("/update")
    public ResultVo update(@RequestBody UpdateCustomerDto updateCustomerDto) {
        return null;
    }

    @ApiOperation(value = "获取个人最新信息")
    //@PostMapping("/findNewCustomerInfo")
    public ResultVo<AppCustomerInfoVo> findNewCustomerInfo(@Validated @RequestBody AppCustomerDto dto){
        return customerService.findNewCustomerInfo(dto);
    }

    @ApiOperation(value = "根据客户id查看申请合伙人信息")
    @PostMapping(value = "/findPartnerInfo")
    public ResultVo<ShowPartnerVo> findPartnerInfo(@Validated @RequestBody AppCustomerDto dto){
        return customerService.showPartner(dto);
    }



    /************************************韵车集成改版 st***********************************/

    @ApiOperation(value = "获取个人最新信息_改版")
    //@PostMapping("/findNewCustomerInfoNew")
    @PostMapping("/findNewCustomerInfo")
    public ResultVo<AppCustomerInfoVo> findNewCustomerInfoNew(@Validated @RequestBody AppCustomerDto dto){
        return customerService.findNewCustomerInfoNew(dto);
    }

    @ApiOperation(value = "获取合伙人银行信息")
    @PostMapping("/findPartnerBank")
    public ResultVo<CustomerCardInfoVo> findPartnerBank(@Validated @RequestBody AppCustomerDto dto){
        return customerService.findPartnerBank(dto);
    }

}
