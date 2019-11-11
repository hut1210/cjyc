package com.cjyc.customer.api.controller;

import com.cjyc.common.model.constant.FieldConstant;
import com.cjyc.common.model.entity.Dictionary;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.customer.api.service.IAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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

    @ApiOperation(value = "查询首页轮播图", notes = "无参数", httpMethod = "POST")
    @PostMapping(value = "/getSysPicture")
    public ResultVo<List<Dictionary>> getSysPicture(){
        return appService.getSysPicture(FieldConstant.SYSTEM_PICTURE);
    }
}
