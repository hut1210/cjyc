package com.cjyc.salesman.api.controller;

import com.cjyc.common.model.dto.salesman.BaseSalesDto;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.salesman.store.StoreVo;
import com.cjyc.common.system.service.ICsStoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "业务员-业务中心")
@Accessors(chain = true)
@RestController
@RequestMapping("/store")
public class StoreController {
    @Autowired
    private ICsStoreService csStoreService;

    @ApiOperation(value = "根据loginId查询业务中心列表")
    @PostMapping("/list")
    public ResultVo<List<StoreVo>> getStoreListByLoginId(@Valid @RequestBody BaseSalesDto dto) {
        return csStoreService.listByAdminId(dto.getLoginId());
    }
}
