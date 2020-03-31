package com.cjyc.web.api.controller;

import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.ReturnMsg;
import com.cjkj.usercenter.dto.asc.MenuResp;
import com.cjkj.usercenter.dto.yc.SelectUsersByRoleResp;
import com.cjyc.common.model.dto.web.role.AddRoleDto;
import com.cjyc.common.model.dto.web.role.ModifyRoleMenusDto;
import com.cjyc.common.model.dto.web.role.SetRoleForAppDto;
import com.cjyc.common.model.entity.Role;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.role.MenuTreeVo;
import com.cjyc.common.model.vo.web.role.SelectUserByRoleVo;
import com.cjyc.common.system.feign.ISysRoleService;
import com.cjyc.web.api.service.IRoleService;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

@Api(tags = "基础数据-角色")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private IRoleService roleService;
    @Autowired
    private ISysRoleService sysRoleService;

//    @PostMapping("/add")
    @ApiOperation(value = "添加角色信息")
    public ResultVo add(@Valid @RequestBody AddRoleDto dto) {
        return roleService.addRole(dto);
    }

    @PostMapping("/delete/{id}")
    @ApiOperation(value = "删除角色信息")
    public ResultVo delete(@ApiParam(name = "id", value = "角色标识", required = true)
                           @PathVariable Long id) {
        return roleService.deleteRole(id);
    }

//    @GetMapping("/getUsersByRoleId/{roleId}")
    @ApiOperation(value = "根据角色id获取关联用户列表信息")
    public ResultVo<List<SelectUserByRoleVo>> getUsersByRoleId(
            @ApiParam(name = "roleId", value = "角色标识", required = true)
            @PathVariable Long roleId) {
        return roleService.getUsersByRoleId(roleId);
    }



    @GetMapping("/getMenuList")
    @ApiOperation(value = "获取韵车系统资源列表")
    public ResultVo<List<MenuTreeVo>> getMenuList() {
        ResultData<List<MenuResp>> rd = sysRoleService.getMenuList();
        if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
            return BaseResultUtil.fail(rd.getMsg());
        }
        return BaseResultUtil.success(Arrays.asList(convertMenuListToTree(rd.getData())));
    }


    /**
     * 查询角色对应机构下的角色列表
     * @author JPG
     * @since 2019/11/18 13:17
     * @param roleId
     */
    @GetMapping("/dept/role/list/{roleId}")
    @ApiOperation(value = "查询角色对应机构下的角色列表")
    public ResultVo<List<SelectUsersByRoleResp>> getDeptRoleListByUserRoleId(
            @ApiParam(name = "roleId", value = "角色标识", required = true)
            @PathVariable Long roleId) {
        return roleService.getDeptRoleListByUserRoleId(roleId);
    }

    /**
     * 查询所有角色信息列表
     * @return
     */
//    @GetMapping(value = {"/getAllList", "/getAllList/{roleName}"})
    @ApiOperation(value = "获取所有角色信息列表")
    public ResultVo<List<Role>> getAllList(
            @ApiParam(name = "roleName", value = "角色名称", required = false)
            @PathVariable(name = "roleName", required = false) String roleName) {
        return roleService.getAllList(roleName);
    }

    @ApiOperation(value = "根据角色标识获取用户类型信息")
    @GetMapping("/getUserTypeByRole/{roleId}")
    public ResultVo<Integer> getUserTypeByRole(
            @ApiParam(name = "roleId", value = "角色标识", required = true)
            @PathVariable("roleId")Long roleId) {
        return roleService.getUserTypeByRole(roleId);
    }

    @ApiOperation(value = "根据角色标识更新角色-菜单列表信息")
//    @PostMapping("/modifyRoleMenus")
    public ResultVo modifyRoleMenus(@Valid @RequestBody ModifyRoleMenusDto dto) {
        return roleService.modifyRoleMenus(dto);
    }

    @ApiOperation(value = "根据角色标识获取末级角色-菜单id列表信息")
//    @GetMapping("/getBtmMenuIdsByRoleId/{roleId}")
    public ResultVo<List<String>> getBtmMenuIdsByRoleId(
            @ApiParam(value = "roleId", name = "角色标识", required = true)
            @PathVariable("roleId")Long roleId) {
        return roleService.getBtmMenuIdsByRoleId(roleId);
    }


    /*********************************韵车集成改版 st*****************************/
//    @PostMapping("/addNew")
    @PostMapping("/add")
    @ApiOperation(value = "添加角色信息")
    public ResultVo addNew(@Valid @RequestBody AddRoleDto dto) {
        return roleService.addRoleNew(dto);
    }

//    @GetMapping("/getUsersByRoleIdNew/{roleId}")
    @GetMapping("/getUsersByRoleId/{roleId}")
    @ApiOperation(value = "根据角色id获取关联用户列表信息")
    public ResultVo<List<SelectUserByRoleVo>> getUsersByRoleIdNew(
            @ApiParam(name = "roleId", value = "角色标识", required = true)
            @PathVariable Long roleId) {
        return roleService.getUsersByRoleIdNew(roleId);
    }

    /**
     * 查询所有角色信息列表
     * @return
     */
//    @GetMapping(value = {"/getAllListNew", "/getAllListNew/{roleName}"})
    @GetMapping(value = {"/getAllList", "/getAllList/{roleName}"})
    @ApiOperation(value = "获取所有角色信息列表")
    public ResultVo<List<Role>> getAllListNew(
            @ApiParam(name = "roleName", value = "角色名称", required = false)
            @PathVariable(name = "roleName", required = false) String roleName) {
        return roleService.getAllListNew(roleName);
    }

    @ApiOperation(value = "根据角色标识获取末级角色-菜单id列表信息_改版")
    @GetMapping("/getBtmMenuIdsByRoleId/{roleId}")
    public ResultVo<List<String>> getBtmMenuIdsByRoleIdNew(
            @ApiParam(value = "roleId", name = "角色标识", required = true)
            @PathVariable("roleId")Long roleId) {
        return roleService.getBtmMenuIdsByRoleIdNew(roleId);
    }

    @ApiOperation(value = "根据角色标识更新角色-菜单列表信息_改版")
    @PostMapping("/modifyRoleMenus")
    public ResultVo modifyRoleMenusNew(@Valid @RequestBody ModifyRoleMenusDto dto) {
        return roleService.modifyRoleMenusNew(dto);
    }

    @ApiOperation(value = "更新业务员APP相关角色信息")
    @PostMapping("/setRoleForApp")
    public ResultVo setRoleForApp(@Valid @RequestBody SetRoleForAppDto dto) {
        return roleService.setRoleForApp(dto);
    }
    /*********************************韵车集成改版 ed*****************************/

    /**
     * 资源列表转换为菜单树
     * @param list
     * @return
     */
    private MenuTreeVo convertMenuListToTree(List<MenuResp> list) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        MenuTreeVo root = new MenuTreeVo();
        //将所有数据按照parentId相同分级处理
        Map<Long, List<MenuResp>> treeMap = new HashMap<>();
        list.forEach(m -> {
            if (m.getParentId() == null || m.getParentId() <= 0L) {
                BeanUtils.copyProperties(m, root);
            }
            if (treeMap.containsKey(m.getParentId())) {
                treeMap.get(m.getParentId()).add(m);
            }else {
                treeMap.put(m.getParentId(), Lists.newArrayList(m));
            }
        });
        if (CollectionUtils.isEmpty(treeMap)) {
            return root;
        }
        root.setChildren(resolveTreeMap(root.getMenuId(), treeMap));
        return root;
    }

    /**
     * 根据父级id解析子集
     * @param parentId
     * @param treeMap
     * @return
     */
    private List<MenuTreeVo> resolveTreeMap(Long parentId, Map<Long, List<MenuResp>> treeMap) {
        List<MenuTreeVo> rsList = new ArrayList<>();
        List<MenuResp> respList = treeMap.get(parentId);
        if (CollectionUtils.isEmpty(respList)) {
            return null;
        }
        respList.stream().forEach(r -> {
            MenuTreeVo mvo = new MenuTreeVo();
            BeanUtils.copyProperties(r, mvo);
            mvo.setChildren(resolveTreeMap(r.getMenuId(), treeMap));
            rsList.add(mvo);
        });
        return rsList;
    }

    @ApiOperation(value = "角色管理导出Excel", notes = "\t 请求接口为/role/exportRoleListExcel?roleName=角色名称")
    @GetMapping("/exportRoleListExcel")
    public void exportRoleListExcel(HttpServletRequest request, HttpServletResponse response){
        roleService.exportRoleListExcel(request,response);
    }

    @ApiOperation(value = "删除角色以及该角色下所属人，以及韵车这边相关角色和人")
    @GetMapping("/deleteRoleAndUser/{roleId}")
    public ResultVo deleteRoleAndUser(@PathVariable Long roleId) {
        return roleService.deleteRoleAndUser(roleId);
    }
}
