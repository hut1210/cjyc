package com.cjyc.web.api.controller;

import com.cjkj.common.constant.SecurityConstants;
import com.cjkj.common.model.ResultData;
import com.cjkj.usercenter.dto.common.AddUserResp;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.admin.CacheAdminVo;
import com.cjyc.common.system.service.ICsAdminService;
import com.cjyc.common.system.feign.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 业务员
 *
 * @author JPG
 */
@RestController
@Api(tags = "业务员")
@RequestMapping(value = "/admin",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AdminController {

    @Resource
    private ICsAdminService csAdminService;
    @Resource
    private ISysUserService sysUserService;

    /**
     * 校验用户并获取缓存数据
     *
     * @author JPG
     */
    @ApiOperation(value = "校验用户并获取缓存数据")
    @PostMapping(value = "/validate/{roleId}")
    public ResultVo<CacheAdminVo> validateUser(@RequestHeader(SecurityConstants.USER_HEADER) String account,
                                 @PathVariable Long roleId) {
        ResultData<AddUserResp> resultData = sysUserService.getByAccount(account);
        if (resultData == null || resultData.getData() == null || resultData.getData().getUserId() == null) {
            return BaseResultUtil.fail("用户不存在");
        }
        CacheAdminVo cacheAdminVo = csAdminService.getCacheData(resultData.getData().getUserId(), roleId);
        //发送推送信息
        return BaseResultUtil.success(cacheAdminVo);
    }

    /**
     * 查询业务中心业务员
     *
     * @author JPG
     */
    @ApiOperation(value = "查询业务中心业务员")
    @PostMapping(value = "/list/{storeId}")
    public ResultVo<List<Admin>> listByStore(@PathVariable Long storeId) {

        List<Admin> list = csAdminService.getListByStoreId(storeId);
        //发送推送信息
        return BaseResultUtil.success(list);
    }

    @ApiOperation(value = "分页查询指定业务中心下的业务员")
    @PostMapping(value = "/listPage")
    public ResultVo listPage(@RequestBody Long storeId) {


        //发送推送信息
        return null;
    }
}
