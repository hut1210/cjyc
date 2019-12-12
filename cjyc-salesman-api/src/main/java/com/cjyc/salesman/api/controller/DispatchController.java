package com.cjyc.salesman.api.controller;

import com.cjyc.common.model.dto.salesman.dispatch.DispatchListDto;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.salesman.dispatch.DispatchListVo;
import com.cjyc.salesman.api.service.IDispatchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @Description 调度模块接口控制层
 * @Author Liu Xing Xiang
 * @Date 2019/12/11 11:25
 **/
@Api(tags = "调度")
@RestController
@RequestMapping(value = "/dispatch", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class DispatchController {
    @Autowired
    private IDispatchService dispatchService;

    /**
     * 功能描述: 查询所有出发城市-目的地城市的车辆数量
     * @author liuxingxiang
     * @date 2019/12/9
     * @param loginId
     * @return com.cjyc.common.model.vo.ResultVo<com.cjyc.common.model.vo.PageVo<com.cjyc.common.model.vo.salesman.task.TaskWaybillVo>>
     */
    @ApiOperation(value = "查询所有出发城市-目的地城市的车辆数量")
    @PostMapping("/getCityCarCount/{loginId}")
    public ResultVo getCityCarCount(@PathVariable Long loginId) {
        return dispatchService.getCityCarCount(loginId);
    }

    @ApiOperation(value = "调度列表信息")
    @PostMapping("/list")
    public ResultVo<PageVo<DispatchListVo>> list(@RequestBody DispatchListDto dto) {
        return BaseResultUtil.success(dispatchService.getPageList(dto));
    }

}
