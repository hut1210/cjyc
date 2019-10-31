package com.cjyc.web.api.feign;

import com.cjkj.common.constant.ServiceNameConstants;
import com.cjkj.common.feign.fallback.UserServiceFallbackFactory;
import com.cjkj.common.model.ResultData;
import com.cjkj.usercenter.dto.common.*;
import com.cjkj.usercenter.dto.yc.SelectUsersByRoleReq;
import com.cjkj.usercenter.dto.yc.SelectUsersByRoleResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * sys角色接口
 * @author JPG
 */
@FeignClient(value = ServiceNameConstants.USER_SERVICE, fallbackFactory = UserServiceFallbackFactory.class, decode404 = true)
public interface ISysRoleService {


    /**
     * 保存角色
     * @author JPG
     * @since 2019/10/21 9:43
     * @param addRoleReq 参数
     * @return ResultData<AddRoleResp>
     */
    @PostMapping("/feign/uc/addRole")
    ResultData<AddRoleResp> save(@RequestBody AddRoleReq addRoleReq);

    /**
     * 批量保存角色信息
     * @param batchRoleReq
     * @return
     */
    @PostMapping("/feign/uc/addBatchRole")
    ResultData saveBatch(@RequestBody AddBatchRoleReq batchRoleReq);

    /**
     * 保存角色
     * @author JPG
     * @since 2019/10/21 9:43
     * @param updateRoleReq 参数
     * @return ResultData
     */
    @PostMapping("/feign/uc/updateRole")
    ResultData update(@RequestBody UpdateRoleReq updateRoleReq);


    /**
     * 查询角色信息：根据部门id查询多级组织下的所有角色
     * @author JPG
     * @since 2019/10/21 9:46
     * @param deptId 组机机构ID
     * @return ResultData<List<SelectRoleResp>>
     */
    @PostMapping("feign/uc/getMultiLevelRoles/{deptId}")
    ResultData<List<SelectRoleResp>> getMultiLevelList(@PathVariable Integer deptId);

    /**
     * 查询角色信息：根据部门id查询多级组织下的所有角色
     * @author JPG
     * @since 2019/10/21 9:46
     * @param deptId 部门ID
     * @return ResultData<List<SelectRoleResp>>
     */
    @PostMapping("/feign/uc/getSingleLevelRoles/{deptId}")
    ResultData<List<SelectRoleResp>> getSingleLevelList(@PathVariable Integer deptId);


    /**
     * 查询角色信息：根据用户id
     * @author JPG
     * @since 2019/10/21 9:46
     * @param userId 用户ID
     * @return ResultData<List<SelectRoleResp>>
     */
    @PostMapping("/feign/uc/getRoles/{userId}")
    ResultData<List<SelectRoleResp>> getListByUserId(@PathVariable Integer userId);

    /**
     * 查询角色信息：根据用户id
     * @author JPG
     * @since 2019/10/21 9:46
     * @param roleId 角色ID
     * @return  ResultData<SelectRoleResp>
     */
    @PostMapping("/feign/uc/deleteRole/{roleId}")
    ResultData<SelectRoleResp> delete(@PathVariable Integer roleId);

    /**
     * 批量删除角色信息
     * @param req
     * @return
     */
    @PostMapping("/feign/uc/deleteBatchRole")
    ResultData deleteBatch(@RequestBody DeleteBatchRoleReq req);

    /**
     * 获取角色关联用户列表信息
     * @param req
     * @return
     */
    @PostMapping("/feign/yc/getUsersByRoleId")
    ResultData<List<SelectUsersByRoleResp>> getUsersByRoleId(@RequestBody SelectUsersByRoleReq req);
}
