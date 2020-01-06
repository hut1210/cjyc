package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.mineStore.ListMineSalesmanDto;
import com.cjyc.common.model.dto.web.mineStore.SetContactPersonDto;
import com.cjyc.common.model.dto.web.mineStore.StorageCarQueryDto;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.mineStore.MySalesmanVo;
import com.cjyc.common.model.vo.web.mineStore.StorageCarVo;
import com.cjyc.web.api.service.IMineStoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    @PostMapping("/listSalesman")
    public ResultVo<PageVo<MySalesmanVo>> listSalesman(@Valid @RequestBody ListMineSalesmanDto dto) {
        return mineStoreService.listSalesman(dto);
    }

    @ApiOperation(value = "设置联系人")
    @PostMapping("/setContractPerson")
    public ResultVo setContractPerson(@Valid @RequestBody SetContactPersonDto dto) {
        return mineStoreService.setContractPerson(dto);
    }

    /**
     * 功能描述: 分页查询在库车辆列表
     * @author liuxingxiang
     * @date 2019/12/25
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo
     */
    @ApiOperation(value = "分页查询在库车辆列表")
    @PostMapping("/getStorageCarPage")
    public ResultVo<PageVo<List<StorageCarVo>>> getStorageCarPage(@RequestBody @Validated StorageCarQueryDto dto) {
        return mineStoreService.getStorageCarPage(dto);
    }

    /************************************韵车集成改版 st***********************************/
    @ApiOperation(value = "查询我的业务中心-我的业务员列表")
    @PostMapping("/listSalesmanNew")
    public ResultVo<PageVo<MySalesmanVo>> listSalesmanNew(@Valid @RequestBody ListMineSalesmanDto dto) {
        return mineStoreService.listSalesmanNew(dto);
    }
    /************************************韵车集成改版 ed***********************************/
}
