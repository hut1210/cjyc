package com.cjyc.driver.api.controller;

import com.cjyc.common.model.dto.LocationInfoDto;
import com.cjyc.common.model.dto.LogisticsInformationDto;
import com.cjyc.common.model.vo.LogisticsInformationVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.system.service.ICsLogisticsInformationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @Description 位置信息
 * @Author Liu Xing Xiang
 * @Date 2020/4/9 8:26
 **/
@Api(tags = "位置信息")
@CrossOrigin
@RestController
@RequestMapping("/location")
public class LocationInfoController {
    @Resource
    private ICsLogisticsInformationService csLogisticsInformationService;

    /**
     * 功能描述: 定位信息上传
     * @author liuxingxiang
     * @date 2020/4/9
     * @param reqDto
     * @return com.cjyc.common.model.vo.ResultVo
     */
    @ApiOperation(value = "定位信息上传")
    @PostMapping(value = "/uploadUserLocation")
    public ResultVo uploadUserLocation(@RequestBody @Valid LocationInfoDto reqDto) {
        return csLogisticsInformationService.uploadUserLocation(reqDto);
    }

    /**
     * 功能描述: 查询物流信息
     * @author liuxingxiang
     * @date 2020/4/3
     * @param reqDto
     * @return com.cjyc.common.model.vo.ResultVo<com.cjyc.common.model.vo.LogisticsInformationVo>
     */
    @ApiOperation(value = "查询物流信息")
    @PostMapping(value = "/getLogisticsInfo")
    public ResultVo<LogisticsInformationVo> getLogisticsInformation(@RequestBody @Valid LogisticsInformationDto reqDto) {
        return csLogisticsInformationService.getLogisticsInformation(reqDto);
    }
}
