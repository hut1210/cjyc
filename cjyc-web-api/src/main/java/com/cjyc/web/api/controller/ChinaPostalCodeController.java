package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.postal.PostalDto;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.CityTreeVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.postal.ProvinceVo;
import com.cjyc.web.api.service.IChinaPostalCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "基础数据-中国邮政区号")
@CrossOrigin
@RestController
@RequestMapping("/postal")
public class ChinaPostalCodeController {

    @Resource
    private IChinaPostalCodeService chinaPostalCodeService;

    @ApiOperation(value = "邮政区号导入Excel", notes = "\t 请求接口为/importPostalCodeExcel/loginId(登录用户ID)格式")
    @PostMapping("/importPostalCodeExcel/{loginId}")
    public ResultVo importPostalCodeExcel(@RequestParam("file") MultipartFile file, @PathVariable Long loginId){
        boolean result = chinaPostalCodeService.importPostalCodeExcel(file,loginId);
        return result ? BaseResultUtil.success() : BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据关键字模糊搜索省/地区")
    @PostMapping(value = "/findChinaPostal")
    public ResultVo<List<ProvinceVo>> findChinaPostal(@RequestBody PostalDto dto) {
        return chinaPostalCodeService.findChinaPostal(dto);
    }
}