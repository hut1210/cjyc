package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.store.StoreAddDto;
import com.cjyc.common.model.dto.web.store.StoreQueryDto;
import com.cjyc.common.model.dto.web.store.StoreUpdateDto;
import com.cjyc.common.model.entity.Store;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.service.IStoreService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description 业务中心管理
 * @Author LiuXingXiang
 * @Date 2019/10/29 15:58
 **/
@Api(tags = "业务中心管理")
@RestController
@RequestMapping("/store")
public class StoreController {
    @Resource
    private IStoreService storeService;

    /**
     * 根据二级城市编码查询业务中心
     */
    @ApiOperation(value = "根据二级城市编码查询业务中心")
    @PostMapping(value = "/get/{cityCode}")
    public ResultVo<List<Store>> getByCityCode(@PathVariable String cityCode) {
        List<Store> list = storeService.getByCityCode(cityCode);
        return BaseResultUtil.success(list);
    }

    @ApiOperation(value = "分页查询", notes = "\t 请求接口为json格式")
    @PostMapping("/queryPage")
    public ResultVo<PageInfo<Store>> queryPage(@RequestBody StoreQueryDto storeQueryDto) {
        return storeService.queryPage(storeQueryDto);
    }

    @ApiOperation(value = "新增", notes = "\t 请求接口为json格式")
    @PostMapping("/add")
    public ResultVo add(@RequestBody @Validated StoreAddDto storeAddDto) {
        boolean result = storeService.add(storeAddDto);
        return result ? BaseResultUtil.success() : BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "删除", notes = "\t 请求接口为/remove/id格式")
    @PostMapping("/remove/{id}")
    public ResultVo remove(@PathVariable Long id) {
        boolean result = storeService.removeById(id);
        return result ? BaseResultUtil.success() : BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "查询详情", notes = "\t 请求接口为/getDetail/id格式")
    @PostMapping("/getDetail/{id}")
    public ResultVo<Store> getDetail(@PathVariable Long id) {
        return BaseResultUtil.success(storeService.getById(id));
    }

    @ApiOperation(value = "修改", notes = "\t 请求接口为json格式")
    @PostMapping("/modify")
    public ResultVo modify(@RequestBody @Validated StoreUpdateDto storeUpdateDto) {
        boolean result = storeService.modify(storeUpdateDto);
        return result ? BaseResultUtil.success() : BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "导出Excel", notes = "\t 请求接口为/store/exportExcel?currentPage=1&pageSize=6" +
            "&name=业务中心名称&provinceCode=省编码&cityCode=市编码&areaCode=区编码")
    @GetMapping("/exportExcel")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response){
        storeService.exportExcel(request,response);
    }

}
