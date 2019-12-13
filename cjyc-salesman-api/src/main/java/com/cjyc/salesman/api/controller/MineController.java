package com.cjyc.salesman.api.controller;

import com.cjyc.common.model.dto.salesman.BaseSalesDto;
import com.cjyc.common.model.dto.salesman.mine.AchieveDto;
import com.cjyc.common.model.dto.salesman.mine.StockCarDto;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.salesman.mine.QRCodeVo;
import com.cjyc.common.model.vo.salesman.mine.StockCarDetailVo;
import com.cjyc.common.model.vo.salesman.mine.StockCarVo;
import com.cjyc.salesman.api.service.IMineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "业务员APP我的")
@RestController
@RequestMapping(value = "/mine",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MineController {

    @Resource
    private IMineService mineService;

    @ApiOperation(value = "库存")
    @PostMapping(value = "/findStockCar")
    public ResultVo<PageVo<StockCarVo>> findStockCar(@Validated @RequestBody StockCarDto dto) {
        return mineService.findStockCar(dto);
    }

    @ApiOperation(value = "库存车辆详情")
    @PostMapping(value = "/findStockCarDetail")
    public ResultVo<StockCarDetailVo> findStockCarDetail(@Validated @RequestBody BaseSalesDto dto) {
        return mineService.findStockCarDetail(dto);
    }

    @ApiOperation(value = "业绩统计")
    @PostMapping(value = "/achieveCount")
    public ResultVo achieveCount(@Validated @RequestBody AchieveDto dto) {
        return mineService.achieveCount(dto);
    }

    @ApiOperation(value = "二维码")
    @PostMapping(value = "/findQrCode")
    public ResultVo<QRCodeVo> findQrCode(@Validated @RequestBody BaseSalesDto dto) {
        return mineService.findQrCode(dto);
    }
}