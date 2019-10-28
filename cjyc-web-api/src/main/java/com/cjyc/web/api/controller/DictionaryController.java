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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  @author: zj
 *  @Date: 2019/10/12 9:46
 *  @Description: 韵车2.0字典维护
 */
@Api(tags = "韵车2.0字典维护")
@CrossOrigin
@RestController
@RequestMapping("/dictionary")
public class DictionaryController {

    @Autowired
    private IDictionaryService dictionaryService;

    @ApiOperation(value = "新增字典项")
    @PostMapping(value = "/saveDictionary")
    public ResultVo saveDictionary(@RequestBody DictionaryDto dictionaryDto){
        boolean result = dictionaryService.saveDictionary(dictionaryDto);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据主键id查看字典")
    @PostMapping(value = "/showDictionaryById/{id}")
    public ResultVo<Dictionary> showDictionaryById(@PathVariable Long id){
        Dictionary dic = dictionaryService.showDictionaryById(id);
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),dic);

    }

    @ApiOperation(value = "根据ids删除字典项目")
    @PostMapping(value = "/delDictionaryByIds")
    public ResultVo delDictionaryByIds(@RequestBody List<Long> ids){
        boolean result = dictionaryService.delDictionaryByIds(ids);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据id更新字典项")
    @PostMapping(value = "/updDictionary")
    public ResultVo updDictionary(@RequestBody DictionaryDto dictionaryDto){
        boolean result = dictionaryService.updDictionaryById(dictionaryDto);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "分页查询字典项")
    @PostMapping(value = "/getAllDictionary")
    public ResultVo<PageVo<Dictionary>> getAllDictionary(@RequestBody BasePageDto basePageDto){
        BasePageUtil.initPage(basePageDto.getCurrentPage(),basePageDto.getPageSize());
        PageInfo<Dictionary> pageInfo = dictionaryService.getAllDictionary(basePageDto);
        return BaseResultUtil.getPageVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),pageInfo);
    }

    @ApiOperation(value = "根据条件查询字典项")
    @PostMapping(value = "/findDictionary")
    public ResultVo<PageVo<Dictionary>> findDictionary(@RequestBody SelectDictionaryDto dto){
        PageInfo<Dictionary> pageInfo = dictionaryService.findDictionary(dto);
        return BaseResultUtil.getPageVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),pageInfo);
    }

}