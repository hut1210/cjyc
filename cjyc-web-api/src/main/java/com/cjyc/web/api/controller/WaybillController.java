package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.waybill.WaybillPickDispatchDto;
import com.cjyc.common.model.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 运单
 * @author JPG
 */
@Api(tags = "运单")
@RestController
@RequestMapping(value = "/waybill",
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class WaybillController {



    /**
     * 提车调度
     * @author JPG
     * @since 2019/10/15 11:53
     */
    @ApiOperation("提车调度")
    @PostMapping("/pick/dispatch")
    public ResultVo pickDispatch(@RequestBody List<WaybillPickDispatchDto> waybillList){



        return null;

    }



}
