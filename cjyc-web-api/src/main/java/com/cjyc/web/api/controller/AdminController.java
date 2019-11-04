package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.order.CommitOrderDto;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 业务员
 * @author JPG
 */
@RestController
@Api(tags = "业务员")
@RequestMapping(value = "/admin",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AdminController {

    private IAdminService adminService;

    /**
     * 查询业务中心业务员
     * @author JPG
     */
    @ApiOperation(value = "查询业务中心业务员")
    @PostMapping(value = "/list/{storeId}")
    public ResultVo listByStore(@PathVariable Long storeId) {

        List<Admin> list = adminService.listByStore(storeId);
        //发送推送信息
        return BaseResultUtil.success(list);
    }
}
