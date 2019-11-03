package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.BasePageDto;
import com.cjyc.common.model.dto.web.dictionary.DictionaryDto;
import com.cjyc.common.model.dto.web.dictionary.SelectDictionaryDto;
import com.cjyc.common.model.entity.Dictionary;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BasePageUtil;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.service.IDictionaryService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 *  @author: zj
 *  @Date: 2019/10/12 9:46
 *  @Description: 韵车2.0字典维护
 */
@Api(tags = "字典维护")
@CrossOrigin
@RestController
@RequestMapping("/dictionary")
public class DictionaryController {

    @Autowired
    private IDictionaryService dictionaryService;

    @ApiOperation(value = "根据id更新字典项")
    @PostMapping(value = "/modify")
    public ResultVo modify(@RequestBody DictionaryDto dictionaryDto){
        boolean result = dictionaryService.modify(dictionaryDto);
        return result ? BaseResultUtil.success():BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据条件查询字典项")
    @PostMapping(value = "/queryPage")
    public ResultVo<PageVo<Dictionary>> queryPage(@RequestBody SelectDictionaryDto dto){
        BasePageUtil.initPage(dto);
        return dictionaryService.queryPage(dto);
    }

    @ApiOperation(value = "根据估值获取保险费")
    @PostMapping(value = "/insurance/get/{valuation}")
    public ResultVo<Map<String, Object>> getInsurance(@ApiParam(value = "估值") @PathVariable int valuation){
        return dictionaryService.getInsurance(valuation);
    }


}