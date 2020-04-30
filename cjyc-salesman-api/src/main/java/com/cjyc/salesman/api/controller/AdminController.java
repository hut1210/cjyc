package com.cjyc.salesman.api.controller;

import com.cjyc.common.model.dto.salesman.Admin.StoreAdminDto;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.system.service.ICsAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 查询业务中心业务员
     * @author JPG
     * @since 2020/4/27 16:41
     */
    @ApiOperation(value = "查询业务中心业务员")
    @PostMapping(value = "/list")
    public ResultVo<List<Admin>> listByStore(@RequestBody StoreAdminDto dto) {

        List<Admin> list = csAdminService.getListByStoreId(dto.getStoreId());
        //发送推送信息
        return BaseResultUtil.success(list);
    }
}
