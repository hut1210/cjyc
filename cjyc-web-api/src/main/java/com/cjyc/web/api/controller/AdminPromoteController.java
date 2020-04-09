package com.cjyc.web.api.controller;


import com.cjyc.common.model.dto.promote.AdminPromoteQueryDto;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.promote.AdminPromoteVo;
import com.cjyc.web.api.service.IAdminPromoteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 业务员推广表 前端控制器
 * </p>
 *
 * @since 2019-11-13
 */
@Api(tags = "分享管理")
@RestController
@RequestMapping("/adminPromote")
public class AdminPromoteController {
    @Autowired
    private IAdminPromoteService adminPromoteService;

    @ApiOperation(value = "查询分享分页列表", notes = "\t 请求接口为json格式")
    @PostMapping("/getPage")
    public ResultVo<PageVo<List<AdminPromoteVo>>> getPage(@RequestBody AdminPromoteQueryDto dto) {
        return adminPromoteService.getPage(dto);
    }

}
