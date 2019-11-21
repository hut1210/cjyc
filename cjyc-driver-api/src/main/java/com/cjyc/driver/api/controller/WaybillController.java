package com.cjyc.driver.api.controller;

import com.cjyc.common.model.dto.driver.waybill.WaitAllotDto;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.driver.waybill.WaitAllotVo;
import com.cjyc.driver.api.service.IWaybillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api(tags = "运单")
@RequestMapping(value = "/waybill",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class WaybillController {

    @Resource
    private IWaybillService waybillService;


/*    @ApiOperation(value = "分页查询待分配运单列表")
    @PostMapping("/wait/allot/page")
    public ResultVo<PageVo<WaitAllotVo>> getWaitAllot(@RequestBody WaitAllotDto dto) {
        return waybillService.getWaitAllotPage(dto);
    }*/
}
