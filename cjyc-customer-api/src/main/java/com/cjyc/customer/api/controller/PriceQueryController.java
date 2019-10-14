package com.cjyc.customer.api.controller;

import com.cjyc.common.model.dto.BasePageDto;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.customer.api.service.IPriceQueryService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 *  @author: zj
 *  @Date: 2019/10/12 16:36
 *  @Description:
 */
@Api(tags = "价格相关信息")
@CrossOrigin
@RestController
@RequestMapping("/priceQueryController")
public class PriceQueryController {

    @Autowired
    private IPriceQueryService iPriceQueryService;

    @ApiOperation(value = "分页查看移动端用户", notes = "分页查看移动端用户", httpMethod = "POST")
    @PostMapping(value = "/getLinePriceByCode/{fromCode}/{toCode}")
    public ResultVo getLinePriceByCode(@ApiParam(required = true) @PathVariable String fromCode,@ApiParam(required = true) @PathVariable String toCode){
        String price = iPriceQueryService.getLinePriceByCode(fromCode,toCode);
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),price);
    }
}