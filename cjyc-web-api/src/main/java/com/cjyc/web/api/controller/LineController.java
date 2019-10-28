package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.line.SortNodeListDto;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.service.ILineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 线路
 *
 * @author JPG
 */
@Api(tags = "线路")
@RestController
@RequestMapping(value = "/line",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class LineController {

    @Resource
    private ILineService lineService;
    /**
     * 查询推荐节点顺序
     * @author JPG
     * @since 2019/10/15 11:53
     */
    @ApiOperation("查询推荐节点顺序")
    @PostMapping("/recommend/node/sort/get")
    public ResultVo<String> sortNode(@Validated @RequestBody SortNodeListDto reqDto) {
        return lineService.sortNode(reqDto);
    }
}
