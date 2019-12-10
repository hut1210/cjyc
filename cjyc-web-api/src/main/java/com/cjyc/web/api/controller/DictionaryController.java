package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.dictionary.DictionaryDto;
import com.cjyc.common.model.dto.web.dictionary.SelectDictionaryDto;
import com.cjyc.common.model.entity.Dictionary;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.service.IDictionaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 *  @author: zj
 *  @Date: 2019/10/12 9:46
 *  @Description: 韵车2.0字典维护
 */
@Api(tags = "基础数据-字典")
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
        return dictionaryService.queryPage(dto);
    }
}