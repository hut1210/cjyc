package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.service.IDictionaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 *  @author: zj
 *  @Date: 2019/10/29 13:10
 *  @Description:系统开关设置
 */
@Api(tags = "系统开关设置")
@CrossOrigin
@RestController
@RequestMapping("/config")
public class ConfigController {

    @Resource
    private IDictionaryService dictionaryService;

    @ApiOperation(value = "查询系统配置")
    @PostMapping(value = "/queryConfig")
    public ResultVo queryConfig(){
        return dictionaryService.queryConfig();
    }

   @ApiOperation(value = "更新系统配置")
    @PostMapping(value = "/modifyConfig")
    public ResultVo modifyConfig(@RequestBody OperateDto dto){
        boolean result = dictionaryService.modifyConfig(dto);
        return result ? BaseResultUtil.success():BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
    }
}