package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.inquiry.HandleInquiryDto;
import com.cjyc.common.model.dto.web.inquiry.SelectInquiryDto;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.inquiry.InquiryVo;
import com.cjyc.web.api.service.IInquiryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 *  @author: zj
 *  @Date: 2019/10/25 9:14
 *  @Description:客服中心询价管理
 */
@Api(tags = "客服中心询价管理")
@CrossOrigin
@RestController
@RequestMapping("/inquiry")
public class InquiryController  {

    @Resource
    private IInquiryService inquiryService;

    @ApiOperation(value = "根据条件分页查询询价条目")
    @PostMapping(value = "/findInquiry")
    public ResultVo<PageVo<InquiryVo>> findInquiry(@RequestBody SelectInquiryDto dto){
        return inquiryService.findInquiry(dto);
    }

    @ApiOperation(value = "处理工单")
    @PostMapping(value = "/handleInquiry")
    public ResultVo handleInquiry(@RequestBody HandleInquiryDto dto){
        boolean result= inquiryService.handleInquiry(dto);
        return result ? BaseResultUtil.success():BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
    }

}