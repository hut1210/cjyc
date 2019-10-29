package com.cjyc.web.api.controller;

import com.cjyc.common.model.entity.Store;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.service.IStoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 业务中心
 * @author JPG
 */
@RestController
@Api(tags = "业务中心")
@RequestMapping(value = "/store",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class StoreController {

    @Resource
    private IStoreService storeService;

    /**
     * 根据cityCode查询所属业务中心
     */
    @ApiOperation(value = "根据userId查询所属业务中心")
    @PostMapping(value = "/get/{cityCode}")
    public ResultVo<List<Store>> getByUserId(@ApiParam(value = "地级市编码") @PathVariable String cityCode) {
        List<Store> list = storeService.getByCityCode(cityCode);
        return BaseResultUtil.success(list);
    }


}
