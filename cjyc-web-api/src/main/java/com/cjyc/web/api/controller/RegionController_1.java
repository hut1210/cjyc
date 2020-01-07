package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.city.RegionAddDto;
import com.cjyc.common.model.dto.web.city.RegionQueryDto;
import com.cjyc.common.model.dto.web.city.RegionUpdateDto;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.city.RegionVo;
import com.cjyc.web.api.service.IRegionService_1;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 大区管理控制层
 * @Author LiuXingXiang
 * @Date 2019/11/7 11:44
 **/
@Slf4j
@Api(tags = "基础数据-大区管理")
@RestController
//@RequestMapping("/region")
public class RegionController_1 {
    @Autowired
    private IRegionService_1 regionService;

    /**
     * 功能描述: 分页查询大区列表
     * @author liuxingxiang
     * @date 2019/12/17
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo<com.cjyc.common.model.vo.PageVo<com.cjyc.common.model.vo.web.city.RegionVo>>
     */
    @ApiOperation(value = "分页查询大区列表")
    @PostMapping("/getRegionPage")
    public ResultVo<PageVo<RegionVo>> getRegionPage(@RequestBody RegionQueryDto dto){
        return regionService.getRegionPage(dto);
    }

    /**
     * 功能描述: 新增大区
     * @author liuxingxiang
     * @date 2019/12/17
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo
     */
    @ApiOperation(value = "新增大区")
    @PostMapping("/addRegion")
    public ResultVo addRegion(@RequestBody @Validated RegionAddDto dto){
        ResultVo resultVo = null;
        try {
            resultVo = regionService.addRegion(dto);
        } catch (Exception e) {
            log.error("新增大区异常",e);
            resultVo = BaseResultUtil.fail("新增大区失败");
        }
        return resultVo;
    }

    /**
     * 功能描述: 修改大区
     * @author liuxingxiang
     * @date 2019/12/17
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo
     */
    @ApiOperation(value = "修改大区")
    @PostMapping("/modifyRegion")
    public ResultVo modifyRegion(@RequestBody @Validated RegionUpdateDto dto){
        ResultVo resultVo = null;
        try {
            resultVo = regionService.modifyRegion(dto);
        } catch (Exception e) {
            log.error("修改大区异常",e);
            resultVo = BaseResultUtil.fail("修改大区失败");
        }
        return resultVo;
    }

    /**
     * 功能描述: 删除大区
     * @author liuxingxiang
     * @date 2019/12/17
     * @param regionCode
     * @return com.cjyc.common.model.vo.ResultVo
     */
    @ApiOperation(value = "删除大区")
    @PostMapping("/removeRegion/{regionCode}")
    public ResultVo removeRegion(@PathVariable String regionCode){
        ResultVo resultVo = null;
        try {
            resultVo = regionService.removeRegion(regionCode);
        } catch (Exception e) {
            log.error("删除大区异常",e);
            resultVo = BaseResultUtil.fail("删除大区失败");
        }
        return resultVo;
    }


}
