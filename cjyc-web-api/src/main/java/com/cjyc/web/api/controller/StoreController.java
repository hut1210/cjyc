package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.BaseWebDto;
import com.cjyc.common.model.dto.web.city.StoreDto;
import com.cjyc.common.model.dto.web.store.GetStoreDto;
import com.cjyc.common.model.dto.web.store.StoreAddDto;
import com.cjyc.common.model.dto.web.store.StoreQueryDto;
import com.cjyc.common.model.dto.web.store.StoreUpdateDto;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.entity.Store;
import com.cjyc.common.model.entity.defined.FullCity;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.store.StoreVo;
import com.cjyc.web.api.service.IStoreService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    /**
     * 根据二级城市编码查询业务中心
     * @author JPG
     */
    @ApiOperation(value = "根据二级城市编码查询业务中心")
    @PostMapping(value = "/get/{cityCode}")
    public ResultVo<List<Store>> getByCityCode(@PathVariable String cityCode) {
        List<Store> list = storeService.findStore(cityCode);
        return BaseResultUtil.success(list);
    }

    /**
     * 根据角色查询角色所属机构下属业务中心
     * @author JPG
     */
    @Deprecated
    @ApiOperation(value = "根据角色查询业务中心")
    @PostMapping(value = "/get/by/{roleId}/{loginId}")
    public ResultVo<List<Store>> getByRole(@PathVariable Long roleId,
                                           @PathVariable Long loginId) {
        BaseWebDto baseWebDto = new BaseWebDto();
        baseWebDto.setRoleId(roleId);
        baseWebDto.setLoginId(loginId);
        List<Store> list = storeService.listByWebLogin(baseWebDto);
        return BaseResultUtil.success(list);
    }

    /**
     * v2.0
     * @author JPG
     */
    @ApiOperation(value = "根据角色和登录ID查询业务中心")
    @PostMapping(value = "/list/by/role/login")
    public ResultVo<List<Store>> getByWebLogin(@RequestBody BaseWebDto reqDto) {
        List<Store> list = storeService.listByWebLogin(reqDto);
        return BaseResultUtil.success(list);
    }

    /**
     * @author JPG
     */
    @Deprecated
    @ApiOperation(value = "根据角色查询业务中心")
    @PostMapping(value = "/get/vo/by/{roleId}/{loginId}")
    public ResultVo<List<StoreVo>> getVoByRole(@PathVariable Long roleId,
                                               @PathVariable Long loginId) {
        BaseWebDto baseWebDto = new BaseWebDto();
        baseWebDto.setRoleId(roleId);
        baseWebDto.setLoginId(loginId);
        List<StoreVo> list = storeService.listVoByWebLogin(baseWebDto);
        return BaseResultUtil.success(list);
    }
    /**
     * v2.0
     * @author JPG
     */
    @ApiOperation(value = "根据角色和登录ID查询业务中心")
    @PostMapping(value = "/list/vo/by/role/login")
    public ResultVo<List<StoreVo>> getVoByWebLogin(@RequestBody BaseWebDto reqDto) {
        List<StoreVo> list = storeService.listVoByWebLogin(reqDto);
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

    /**
     * 功能描述: 分页查询业务中心列表
     * @author liuxingxiang
     * @date 2019/12/17
     * @param storeQueryDto
     * @return com.cjyc.common.model.vo.ResultVo<com.github.pagehelper.PageInfo<com.cjyc.common.model.vo.web.store.StoreVo>>
     */
    @ApiOperation(value = "分页查询业务中心列表", notes = "\t 请求接口为json格式")
    @PostMapping("/queryPage")
    public ResultVo<PageInfo<StoreVo>> queryPage(@RequestBody StoreQueryDto storeQueryDto) {
        return storeService.queryPage(storeQueryDto);
    }

    /**
     * 功能描述: 新增
     * @author liuxingxiang
     * @date 2019/12/17
     * @param storeAddDto
     * @return com.cjyc.common.model.vo.ResultVo
     */
    @ApiOperation(value = "新增", notes = "\t 请求接口为json格式")
    @PostMapping("/add")
    public ResultVo add(@RequestBody @Validated StoreAddDto storeAddDto) {
        return storeService.add(storeAddDto);
    }

    /**
     * 功能描述: 删除
     * @author liuxingxiang
     * @date 2019/12/17
     * @param id
     * @return com.cjyc.common.model.vo.ResultVo
     */
    @ApiOperation(value = "删除", notes = "\t 请求接口为/remove/id格式")
    @PostMapping("/remove/{id}")
    public ResultVo remove(@PathVariable Long id) {
        return storeService.remove(id);
    }

    /**
     * 功能描述: 查询详情
     * @author liuxingxiang
     * @date 2019/12/17
     * @param id
     * @return com.cjyc.common.model.vo.ResultVo<com.cjyc.common.model.entity.Store>
     */
    @ApiOperation(value = "查询详情", notes = "\t 请求接口为/get/id格式")
    @PostMapping("/getDetail/{id}")
    public ResultVo<Store> getDetail(@PathVariable Long id) {
        return BaseResultUtil.success(storeService.getById(id));
    }

    /**
     * 功能描述: 修改
     * @author liuxingxiang
     * @date 2019/12/17
     * @param storeUpdateDto
     * @return com.cjyc.common.model.vo.ResultVo
     */
    @ApiOperation(value = "修改", notes = "\t 请求接口为json格式")
    @PostMapping("/modify")
    public ResultVo modify(@RequestBody @Validated StoreUpdateDto storeUpdateDto) {
        return storeService.modify(storeUpdateDto);
    }

    /**
     * 功能描述: 导出Excel
     * @author liuxingxiang
     * @date 2019/12/17
     * @param request
     * @param response
     * @return void
     */
    @ApiOperation(value = "导出Excel", notes = "\t 请求接口为/store/exportExcel?currentPage=1&pageSize=6" +
            "&name=业务中心名称&provinceCode=省编码&cityCode=市编码&areaCode=区编码")
    @GetMapping("/exportExcel")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response){
        storeService.exportExcel(request,response);
    }

    /**
     * 功能描述: 业务中心下用户列表信息
     * @author zhangcangman
     * @date 2019/12/17
     * @param storeId
     * @return com.cjyc.common.model.vo.ResultVo<java.util.List<com.cjyc.common.model.entity.Admin>>
     */
    @ApiOperation(value = "业务中心下用户列表信息", notes = "查询用户关联此用户中心角色信息")
    //@GetMapping("/listAdminsByStoreId/{storeId}")
    public ResultVo<List<Admin>> listAdminsByStoreId(
            @ApiParam(name = "storeId", value = "业务中心标识", required = true)
            @PathVariable Long storeId){
        return storeService.listAdminsByStoreId(storeId);
    }

    /**
     * 功能描述: 根据业务中心ID查询覆盖区列表
     * @author liuxingxiang
     * @date 2019/12/17
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo<com.github.pagehelper.PageInfo<com.cjyc.common.model.entity.defined.FullCity>>
     */
    @ApiOperation(value = "根据业务中心ID查询覆盖区列表")
    @PostMapping("/getStoreCoveredAreaList")
    public ResultVo<PageInfo<FullCity>> getStoreCoveredAreaList(@RequestBody @Validated({StoreDto.GetStoreAreaList.class}) StoreDto dto) {
        return storeService.getStoreCoveredAreaList(dto);
    }

    /**
     * 功能描述: 查询未覆盖区列表
     * @author liuxingxiang
     * @date 2019/12/17
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo<com.github.pagehelper.PageInfo<com.cjyc.common.model.entity.defined.FullCity>>
     */
    @ApiOperation(value = "查询未覆盖区列表")
    @PostMapping("/getStoreNoCoveredAreaList")
    public ResultVo<PageInfo<FullCity>> getStoreNoCoveredAreaList(@RequestBody StoreDto dto) {
        return storeService.getStoreNoCoveredAreaList(dto);
    }

    /**
     * 功能描述: 新增前业务中心覆盖区域
     * @author liuxingxiang
     * @date 2019/12/17
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo
     */
    @ApiOperation(value = "新增前业务中心覆盖区域")
    @PostMapping("/addCoveredArea")
    public ResultVo addCoveredArea(@RequestBody @Validated({StoreDto.AddAndRemoveCoveredArea.class}) StoreDto dto) {
        return storeService.addCoveredArea(dto);
    }

    /**
     * 功能描述: 删除当前业务中心覆盖区域
     * @author liuxingxiang
     * @date 2019/12/17
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo
     */
    @ApiOperation(value = "删除当前业务中心覆盖区域")
    @PostMapping("/removeCoveredArea")
    public ResultVo removeCoveredArea(@RequestBody @Validated({StoreDto.AddAndRemoveCoveredArea.class}) StoreDto dto) {
        return storeService.removeCoveredArea(dto);
    }

    @ApiOperation(value = "导入Excel", notes = "\t 请求接口为/importStoreExcel/loginId(导入用户ID)格式")
    @PostMapping("/importStoreExcel/{loginId}")
    public ResultVo importStoreExcel(@RequestParam("file") MultipartFile file, @PathVariable Long loginId){
        boolean result = storeService.importStoreExcel(file, loginId);
        return result ? BaseResultUtil.success() : BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "获取全部业务中心")
    @PostMapping(value = "/findAllStore")
    public ResultVo<List<Store>> findAllStore() {
        return storeService.findAllStore();
    }
}
