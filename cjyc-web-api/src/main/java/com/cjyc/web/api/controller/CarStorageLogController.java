package com.cjyc.web.api.controller;


import com.cjyc.common.model.dto.web.waybill.storeListDto;
import com.cjyc.common.model.entity.CarStorageLog;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.service.ICarStorageLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 出入库记录 前端控制器
 * </p>
 *
 * @author JPG
 * @since 2019-11-22
 */
@Api(tags = "出入库记录")
@RestController
@RequestMapping("/storage/log")
public class CarStorageLogController {

    @Resource
    private ICarStorageLogService carStorageLogService;


    @ApiOperation(value = "查询出入库记录", notes = " ")
    @PostMapping(value = "/list")
    public ResultVo<PageVo<CarStorageLog>> inList(@RequestBody storeListDto dto) {
        return carStorageLogService.inList(dto);
    }
}
