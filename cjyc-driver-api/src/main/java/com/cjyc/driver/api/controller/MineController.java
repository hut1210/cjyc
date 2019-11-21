package com.cjyc.driver.api.controller;

import com.cjyc.common.model.dto.driver.BaseDriverDto;
import com.cjyc.common.model.dto.driver.BaseDto;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.driver.mine.BinkCardVo;
import com.cjyc.common.model.vo.driver.mine.DriverInfoVo;
import com.cjyc.driver.api.service.IMineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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
    @PostMapping(value = "/findBinkCard")
    public ResultVo<List<BinkCardVo>> findBinkCard(@RequestBody BaseDto dto) {
        return mineService.findBinkCard(dto);
    }

    @ApiOperation(value = "司机管理信息")
    @PostMapping(value = "/findDriver")
    public ResultVo<PageVo<DriverInfoVo>> findDriver(@RequestBody BaseDriverDto dto) {
        return mineService.findDriver(dto);
    }





}