package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.postal.PostalDto;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.postal.AreaVo;
import com.cjyc.common.model.vo.web.postal.ProvinceVo;
import com.cjyc.web.api.service.IPostalCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "基础数据-中国邮政区号")
@CrossOrigin
@RestController
@RequestMapping("/postal")
public class PostalCodeController {

    @Resource
    private IPostalCodeService postalCodeService;

    @ApiOperation(value = "邮政区号导入Excel", notes = "\t 请求接口为/importPostalCodeExcel/loginId(登录用户ID)格式")
    @PostMapping("/importPostalCodeExcel/{loginId}")
    @Deprecated
    public ResultVo importPostalCodeExcel(@RequestParam("file") MultipartFile file, @PathVariable Long loginId) {
        boolean result = postalCodeService.importPostalCodeExcel(file, loginId);
        return result ? BaseResultUtil.success() : BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据关键字模糊搜索省/地区")
    @PostMapping(value = "/findChinaPostal")
    public ResultVo<List<ProvinceVo>> findChinaPostal(@RequestBody PostalDto dto) {
        return postalCodeService.findChinaPostal(true, dto);
    }

    @ApiOperation(value = "查询所有的省")
    @PostMapping(value = "/findAllProvince")
    public ResultVo findAllProvince() {
        return postalCodeService.findAllProvince(true);
    }

    @ApiOperation(value = "根据省/直辖市名称查询下属区县")
    @PostMapping(value = "/findSubArea/{provinceName}")
    public ResultVo<List<AreaVo>> findSubArea(@PathVariable String provinceName) {
        return postalCodeService.findSubArea(true, provinceName);
    }
}