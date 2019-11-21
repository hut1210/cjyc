package com.cjyc.customer.api.controller;

import com.cjyc.common.model.dto.sys.SysPictureDto;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.customer.api.service.IAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * @author leo
 * @date 2019/7/27.
 */
@RestController
@RequestMapping("/app")
@Api(tags = "app",description = "查询首页轮播图")
public class AppController {

    @Autowired
    private IAppService appService;

    @ApiOperation(value = "查询首页轮播图", notes = "用户端 item传system_picture_customer" +
            "；司机端item传system_picture_driver； 业务员端item传system_picture_sale ", httpMethod = "POST")
    @PostMapping(value = "/getSysPicture/{item}")
    public ResultVo<List<String>> getSysPicture(@PathVariable String item){
        return appService.getSysPicture(item);
    }

    @ApiOperation(value = "修改首页轮播图")
    @PostMapping(value = "/updateSysPicture")
    public ResultVo updateSysPicture(@RequestBody @Validated SysPictureDto sysPictureDto){
        return appService.updateSysPicture(sysPictureDto);
    }
}
