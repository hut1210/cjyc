package com.cjyc.driver.api.controller;

import com.cjyc.common.model.dto.web.user.DriverListDto;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.user.DriverListVo;
import com.cjyc.driver.api.service.IMineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户
 * @author JPG
 */
@Api(tags = "我的")
@RestController
@RequestMapping(value = "/mine",
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MineController {

    @Resource
    private IMineService mineService;

    @ApiOperation(value = "司机的银行卡信息")
    @PostMapping(value = "/findBinkCardInfo/{loginId}")
    public ResultVo findBinkCardInfo(@PathVariable @ApiParam(value = "司机id",required = true) Long loginId) {
        return mineService.findBinkCardInfo(loginId);
    }
}