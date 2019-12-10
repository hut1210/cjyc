package com.cjyc.web.api.controller;

import com.cjkj.common.constant.SecurityConstants;
import com.cjkj.common.model.ResultData;
import com.cjkj.usercenter.dto.common.AddUserResp;
import com.cjkj.usercenter.dto.common.UserResp;
import com.cjkj.usercenter.dto.yc.SelectPageUsersByDeptReq;
import com.cjyc.common.model.dto.web.salesman.MySalesmanQueryDto;
import com.cjyc.common.model.dto.web.salesman.AdminPageDto;
import com.cjyc.common.model.dto.web.salesman.TypeSalesmanDto;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.admin.AdminPageVo;
import com.cjyc.common.model.vo.web.admin.AdminVo;
import com.cjyc.common.model.vo.web.admin.CacheData;
import com.cjyc.common.system.feign.ISysUserService;
import com.cjyc.common.system.service.ICsAdminService;
import com.cjyc.web.api.service.IAdminService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

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
    @Resource
    private IAdminService adminService;

    /**
     * 校验用户并获取缓存数据
     *
     * @author JPG
     */
    @ApiOperation(value = "校验用户并获取缓存数据")
    @PostMapping(value = "/validate/{roleId}")
    public ResultVo<CacheData> validateUser(@RequestHeader(SecurityConstants.USER_HEADER) String account,
                                            @PathVariable Long roleId) {
        ResultData<UserResp> resultData = sysUserService.getByAccount(account);
        if (resultData == null || resultData.getData() == null || resultData.getData().getUserId() == null) {
            return BaseResultUtil.fail(resultData == null ? "用户不存在" : resultData.getMsg());
        }
        CacheData cacheData = adminService.getCacheData(resultData.getData().getUserId(), roleId);
        //发送推送信息
        return BaseResultUtil.success(cacheData);
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
    /**
     * 根据userName查询业务员
     * @author JPG
     */
    @ApiOperation(value = "根据手机号查询业务员")
    @PostMapping(value = "/get/{phone}")
    public ResultVo<Admin> get(@PathVariable String phone) {
        AdminVo admin = csAdminService.getByPhone(phone, true);
        //发送推送信息
        return BaseResultUtil.success(admin);
    }

    @ApiOperation(value = "分页查询指定业务中心下的业务员")
    @PostMapping(value = "/page")
    public ResultVo<PageVo<AdminPageVo>> listByRoleId(@RequestBody AdminPageDto reqDto) {
        PageVo<AdminPageVo> page = adminService.page(reqDto);
        return BaseResultUtil.success(page);
    }
    @Deprecated
    @ApiOperation(value = "分页查询指定业务中心下的业务员")
    @PostMapping(value = "/listPage")
    public ResultVo listPage(@RequestBody MySalesmanQueryDto dto) {
        SelectPageUsersByDeptReq req = new SelectPageUsersByDeptReq();
        BeanUtils.copyProperties(dto,req);
        req.setPageNum(dto.getCurrentPage());
        ResultData resultData = sysUserService.getPageUsersByDept(req);
        return BaseResultUtil.success(resultData.getData());
    }


    @ApiOperation(value = "提送车业务员")
    @PostMapping(value = "/deliverySalesman")
    public ResultVo deliverySalesman(@Validated @RequestBody TypeSalesmanDto dto){
        return adminService.deliverySalesman(dto);
    }
}
