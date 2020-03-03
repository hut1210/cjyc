package com.cjyc.customer.api.controller;

import com.cjyc.common.model.dto.KeywordDto;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.city.CityVo;
import com.cjyc.common.system.service.ICsCityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@Api(tags = "城市")
@CrossOrigin
@RestController
@RequestMapping("/city")
public class CityController {

    @Resource
    private ICsCityService csCityService;

    @ApiOperation(value = "查看城市")
    @PostMapping(value = "/queryCity")
    public ResultVo<CityVo> queryCity(@RequestBody KeywordDto dto){
        return csCityService.queryCity(true,dto);
    }
}