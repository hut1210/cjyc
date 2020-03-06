package com.cjyc.customer.api.controller;

import com.cjyc.common.model.dto.AppItemDto;
import com.cjyc.common.model.dto.AppVersionDto;
import com.cjyc.common.model.dto.sys.SysPictureDto;
import com.cjyc.common.model.vo.AppItemVo;
import com.cjyc.common.model.vo.AppVersionVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.system.service.ICsAppService;
import com.cjyc.common.system.service.ICsAppVersionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author leo
 * @date 2019/7/27.
 */
@RestController
@RequestMapping("/app")
@Api(tags = "app",description = "轮播图/版本更新")
public class AppController {

    @Autowired
    private ICsAppService csAppService;
    @Resource
    private ICsAppVersionService appVersionService;

    @ApiOperation(value = "查询首页轮播图", notes = "用户端 systemPicture传system_picture_customer" +
            "；司机端systemPicture传system_picture_driver； 业务员端systemPicture传system_picture_sale ", httpMethod = "POST")
    @PostMapping(value = "/getSysPicture")
    public ResultVo<AppItemVo> getSysPicture(@Validated @RequestBody AppItemDto dto){
        return csAppService.getSysPicture(dto);
    }

    @ApiOperation(value = "修改首页轮播图")
    @PostMapping(value = "/updateSysPicture")
    public ResultVo updateSysPicture(@RequestBody @Validated SysPictureDto sysPictureDto){
        return csAppService.updateSysPicture(sysPictureDto);
    }

    @ApiOperation(value = "用户端检查app版本是否更新")
    @PostMapping(value = "/updateAppVersion")
    public ResultVo<AppVersionVo> updateAppVersion(@Validated @RequestBody AppVersionDto dto){
        return appVersionService.updateAppVersion(dto);
    }
}
