package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.inquiry.SelectInquiryDto;
import com.cjyc.common.model.dto.web.line.AddAndUpdateLineDto;
import com.cjyc.common.model.dto.web.line.SortNodeDto;
import com.cjyc.common.model.dto.web.line.SortNodeListDto;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BasePageUtil;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.line.LineVo;
import com.cjyc.web.api.service.ILineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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

    @ApiOperation(value = "根据条件查询班线")
    @PostMapping(value = "/getLineByTerm")
    public ResultVo<PageVo<LineVo>> getLineByTerm(@RequestBody SelectInquiryDto dto){
        BasePageUtil.initPage(dto.getCurrentPage(),dto.getPageSize());
        return lineService.getLineByTerm(dto);
    }

    @ApiOperation(value = "新增/更新班线")
    @PostMapping(value = "/addAndUpdateLine")
    public ResultVo addAndUpdateLine(@Validated({ AddAndUpdateLineDto.LineDto.class }) @RequestBody AddAndUpdateLineDto dto){
        return lineService.addAndUpdateLine(dto);
    }

    @ApiOperation(value = "删除班线")
    @PostMapping(value = "/deleteLineByIds")
    public ResultVo deleteLineByIds(@RequestBody List<Long> lines){
        if(lines == null || lines.size() == 0){
            return BaseResultUtil.getVo(ResultEnum.MOBILE_PARAM_ERROR.getCode(),ResultEnum.MOBILE_PARAM_ERROR.getMsg());
        }
        return lineService.deleteLineByIds(lines);
    }
}
