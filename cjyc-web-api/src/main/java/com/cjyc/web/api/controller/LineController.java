package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.line.AddOrUpdateLineDto;
import com.cjyc.common.model.dto.web.line.ListLineDto;
import com.cjyc.common.model.dto.web.line.SelectLineDto;
import com.cjyc.common.model.dto.web.line.SortNodeListDto;
import com.cjyc.common.model.entity.Line;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.line.LineVo;
import com.cjyc.web.api.service.ILineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    /**
     * 起始城市目的城市查询线路
     * @author JPG
     * @since 2019/10/15 11:53
     */
    @ApiOperation("起始城市目的城市查询线路")
    @PostMapping("/two/city/list")
    public ResultVo<List<Line>> listByTwoCity(@Validated @RequestBody ListLineDto reqDto) {
        return lineService.listByTwoCity(reqDto);
    }

    @ApiOperation(value = "根据条件查询班线")
    @PostMapping(value = "/findPageLine")
    public ResultVo<PageVo<LineVo>> findPageLine(@RequestBody SelectLineDto dto){
        return lineService.findPageLine(dto);
    }

    @ApiOperation(value = "新增/更新班线")
    @PostMapping(value = "/addOrUpdateLine")
    public ResultVo addOrUpdateLine(@Validated @RequestBody AddOrUpdateLineDto dto){
        return lineService.addOrUpdateLine(dto);
    }

    @ApiOperation(value = "删除班线")
    @PostMapping(value = "/removeLineById/{id}")
    public ResultVo removeLineById(@PathVariable Long id){
        boolean result = lineService.removeById(id);
        return result ? BaseResultUtil.success() : BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据班线查询物流费")
    @PostMapping(value = "/getDefaultWlFeeByCode/{fromCode}/{toCode}")
    public ResultVo getDefaultWlFeeByCode(@PathVariable @ApiParam(value = "起始地code",required = true) String fromCode,
                                    @PathVariable @ApiParam(value = "目的地code",required = true) String toCode){
        return lineService.getDefaultWlFeeByCode(fromCode,toCode);
    }

    @ApiOperation(value = "导出Excel", notes = "\t 请求接口为/line/exportExcel?currentPage=1&pageSize=10&fromCode=&toCode=&code=")
    @GetMapping("/exportExcel")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response){
        lineService.exportExcel(request,response);
    }

    @ApiOperation(value = "导入Excel", notes = "\t 请求接口为/importExcel/userId(导入用户ID)格式")
    @PostMapping("/importExcel/{userId}")
    public ResultVo importExcel(@RequestParam("file") MultipartFile file, @PathVariable Long userId){
        boolean result = lineService.importExcel(file, userId);
        return result ? BaseResultUtil.success() : BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
    }

}
