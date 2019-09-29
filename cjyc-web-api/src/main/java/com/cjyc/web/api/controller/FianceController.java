package com.cjyc.web.api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.dto.OrderDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @auther litan
 * @description: com.cjyc.web.api.controller
 * @date:2019/9/28
 */
@RestController
@RequestMapping("/fiance")
@Api(tags = "index",description = "web端财务相关接口,包含收付款单等")
public class FianceController {

    /**
     * 下单测试--dto接收
     * */
    @ApiOperation(value = "下单测试接口", notes = "下单测试", httpMethod = "POST")
    @RequestMapping(value = "/orderTest", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultVo orderTest(@RequestBody OrderDto orderDto) {
        Map<String,Object> obj = new HashMap<>();
        Page<String> page = null;
        return BaseResultUtil.getPageVo(1,"sadf",page,obj);
    }
}
