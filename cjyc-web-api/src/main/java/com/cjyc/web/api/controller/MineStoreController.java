package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.mineStore.ListMineSalesmanDto;
import com.cjyc.common.model.dto.web.mineStore.SetContactPersonDto;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.mineStore.MySalesmanVo;
import com.cjyc.web.api.service.IMineStoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 我的业务中心
 */

@Api(tags = {"我的业务中心"})
@RestController
@RequestMapping("/mineStore")
@Slf4j
public class MineStoreController {
    @Autowired
    private IMineStoreService mineStoreService;

    @ApiOperation(value = "查询我的业务中心-我的业务员列表")
    @RequestMapping("/listSalesman")
    public ResultVo<PageVo<MySalesmanVo>> listSalesman(@Valid @RequestBody ListMineSalesmanDto dto) {
        return mineStoreService.listSalesman(dto);
    }

    @ApiOperation(value = "设置联系人")
    @RequestMapping("/setContractPerson")
    public ResultVo setContractPerson(@Valid @RequestBody SetContactPersonDto dto) {
        return mineStoreService.setContractPerson(dto);
    }
}
