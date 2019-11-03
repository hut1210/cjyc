package com.cjyc.customer.api.controller;

import com.cjyc.common.model.dto.KeywordDto;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.customer.api.service.ICityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "城市")
@CrossOrigin
@RestController
@RequestMapping("/city")
public class CityController {

    @Resource
    private ICityService cityService;

    @ApiOperation(value = "查看城市")
    @PostMapping(value = "/queryCity")
    public ResultVo queryCity(@RequestBody KeywordDto dto)
    {
        return cityService.queryCity(dto);
    }
}