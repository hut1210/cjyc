package com.cjyc.customer.api.controller;

import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.customer.api.service.ISystemFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 *  @author: zj
 *  @Date: 2019/10/14 9:36
 *  @Description:处理系统文件
 */
@Api(tags = "系统文件")
@CrossOrigin
@RestController
@RequestMapping("/systemFileController")
public class SystemFileController {

    @Resource
    private ISystemFileService iSystemFileService;

    @ApiOperation(value = "移动端用户查看banner图", notes = "移动端用户查看banner图", httpMethod = "POST")
    @PostMapping(value = "/getAllBannerPhoto")
    public ResultVo getAllBannerPhoto(){
       List<String> urlList = iSystemFileService.getAllBannerPhoto();
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),urlList);
    }
}