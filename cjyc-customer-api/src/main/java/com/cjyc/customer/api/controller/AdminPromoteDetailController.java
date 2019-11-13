package com.cjyc.customer.api.controller;


import com.cjyc.common.model.dto.promote.AdminPromoteDetailAddDto;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.customer.api.service.IAdminPromoteDetailService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 业务员推广明细 前端控制器
 * </p>
 *
 * @author JPG
 * @since 2019-11-13
 */
@RestController
@RequestMapping("/adminPromoteDetail")
public class AdminPromoteDetailController {
    @Autowired
    private IAdminPromoteDetailService adminPromoteDetailService;

    @ApiOperation(value = "新增", notes = "\t 请求接口为json格式")
    @PostMapping("/add")
    public ResultVo add(@RequestBody @Validated AdminPromoteDetailAddDto dto){
        boolean result = adminPromoteDetailService.add(dto);
        return result ? BaseResultUtil.success() : BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
    }

}
