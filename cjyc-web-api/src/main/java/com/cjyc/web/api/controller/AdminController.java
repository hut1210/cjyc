package com.cjyc.web.api.controller;

import com.cjkj.common.model.ResultData;
import com.cjkj.usercenter.dto.common.AddUserResp;
import com.cjyc.common.model.dto.web.order.CommitOrderDto;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.system.service.ICsAdminService;
import com.cjyc.web.api.feign.ISysUserService;
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
    private ICsAdminService csAdminService;
    private ISysUserService sysUserService;


    /**
     * 校验用户并获取缓存数据
     * @author JPG
     */
    @ApiOperation(value = "校验用户并获取缓存数据")
    @PostMapping(value = "/validate")
    public ResultVo validateUser(@RequestHeader("userName") String account) {
        ResultData<AddUserResp> resultData = sysUserService.getByAccount(account);
        if(resultData == null || resultData.getData() == null || resultData.getData().getUserId() == null){
            return BaseResultUtil.fail("用户不存在");
        }
        Admin admin = csAdminService.getByUserId(resultData.getData().getUserId(), true);
        //发送推送信息
        return BaseResultUtil.success(admin);
    }

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
