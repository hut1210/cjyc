package com.cjyc.web.api.controller;

import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.ReturnMsg;
import com.cjyc.common.model.dto.web.city.StoreDto;
import com.cjyc.common.model.dto.web.store.GetStoreDto;
import com.cjyc.common.model.dto.web.store.StoreAddDto;
import com.cjyc.common.model.dto.web.store.StoreQueryDto;
import com.cjyc.common.model.dto.web.store.StoreUpdateDto;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.entity.Store;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.store.StoreCoveredAreaVo;
import com.cjyc.common.model.vo.store.StoreVo;
import com.cjyc.common.system.feign.ISysDeptService;
import com.cjyc.web.api.service.IStoreService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api(tags = "基础数据-业务中心")
@RestController
@RequestMapping("/store")
public class StoreController {
    @Resource
    private IStoreService storeService;
    @Autowired
    private ISysDeptService sysDeptService;

    /**
     * 根据二级城市编码查询业务中心
     * @author JPG
     */
    @ApiOperation(value = "根据二级城市编码查询业务中心")
    @PostMapping(value = "/get/{cityCode}")
    public ResultVo<List<Store>> getByCityCode(@PathVariable String cityCode) {
        List<Store> list = storeService.getByCityCode(cityCode);
        return BaseResultUtil.success(list);
    }


    /**
     * 根据角色查询角色所属机构下属业务中心
     * @author JPG
     */
    @ApiOperation(value = "根据角色查询业务中心")
    @PostMapping(value = "/get/by/{roleId}")
    public ResultVo<List<Store>> getByRole(@PathVariable Long roleId) {
        List<Store> list = storeService.getListByRoleId(roleId);
        return BaseResultUtil.success(list);
    }

    /**
     * 根据角色查询角色所属机构下属业务中心
     * @author JPG
     */
    @ApiOperation(value = "根据角色查询业务中心")
    @PostMapping(value = "/get/vo/by/{roleId}")
    public ResultVo<List<StoreVo>> getVoByRole(@PathVariable Long roleId) {
        List<StoreVo> list = storeService.getVoListByRoleId(roleId);
        return BaseResultUtil.success(list);
    }

    /**
     * 根据角色查询角色所属机构下属业务中心
     * @author JPG
     */
    @ApiOperation(value = "根据角色查询业务中心")
    @PostMapping(value = "/get")
    public ResultVo<List<Store>> get(@RequestBody GetStoreDto reqDto) {
        List<Store> list = storeService.get(reqDto);
        return BaseResultUtil.success(list);
    }

    @ApiOperation(value = "分页查询", notes = "\t 请求接口为json格式")
    @PostMapping("/queryPage")
    public ResultVo<PageInfo<StoreVo>> queryPage(@RequestBody StoreQueryDto storeQueryDto) {
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
        Store store = storeService.getById(id);
        if (null == store) {
            return BaseResultUtil.fail("业务中心不存在，请检查");
        }
        ResultData deleteRd = sysDeptService.delete(store.getDeptId());
        if (!ReturnMsg.SUCCESS.getCode().equals(deleteRd.getCode())) {
            return BaseResultUtil.fail("业务中心删除失败，原因：" + deleteRd.getMsg());
        }
        boolean result = storeService.removeById(id);
        return result ? BaseResultUtil.success() : BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "查询详情", notes = "\t 请求接口为/get/id格式")
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

    @ApiOperation(value = "业务中心下用户列表信息", notes = "查询用户关联此用户中心角色信息")
    @GetMapping("/listAdminsByStoreId/{storeId}")
    public ResultVo<List<Admin>> listAdminsByStoreId(
            @ApiParam(name = "storeId", value = "业务中心标识", required = true)
            @PathVariable Long storeId){
        return storeService.listAdminsByStoreId(storeId);
    }

    @ApiOperation(value = "根据业务中心ID查询当前业务中心覆盖区和所有业务中心未覆盖区列表")
    @PostMapping("/getStoreAreaList")
    public ResultVo<StoreCoveredAreaVo> getStoreAreaList(@RequestBody @Validated({StoreDto.GetStoreAreaList.class}) StoreDto dto) {
        return storeService.getStoreAreaList(dto);
    }

    @ApiOperation(value = "新增前业务中心覆盖区域")
    @PostMapping("/addCoveredArea")
    public ResultVo addCoveredArea(@RequestBody @Validated({StoreDto.AddAndRemoveCoveredArea.class}) StoreDto dto) {
        return storeService.addCoveredArea(dto);
    }

    @ApiOperation(value = "删除当前业务中心覆盖区域")
    @PostMapping("/removeCoveredArea")
    public ResultVo removeCoveredArea(@RequestBody @Validated({StoreDto.AddAndRemoveCoveredArea.class}) StoreDto dto) {
        return storeService.removeCoveredArea(dto);
    }
}
