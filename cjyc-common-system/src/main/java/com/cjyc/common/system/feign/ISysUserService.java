package com.cjyc.common.system.feign;

import com.cjkj.common.constant.ServiceNameConstants;
import com.cjkj.common.feign.fallback.UserServiceFallbackFactory;
import com.cjkj.common.model.ResultData;
import com.cjkj.usercenter.dto.common.*;
import com.cjkj.usercenter.dto.yc.SelectPageUsersByDeptReq;
import com.cjkj.usercenter.dto.yc.SelectUsersByRoleResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 用户Feign接口
 * @author JPG
 */
@FeignClient(name = ServiceNameConstants.USER_SERVICE, fallbackFactory = UserServiceFallbackFactory.class, decode404 = true)
public interface ISysUserService {


    /**
     * 保存用户
     * @author JPG
     * @since 2019/10/21 9:38
     * @param updateUserReq 参数
     */
    @PostMapping("/feign/uc/updateUser")
    ResultData update(@RequestBody UpdateUserReq updateUserReq);



    /**
     * 保存用户
     * @author JPG
     * @since 2019/10/21 9:38
     * @param addUserReq 参数
     */
    @PostMapping("/feign/uc/addUser")
    ResultData<AddUserResp> save(@RequestBody AddUserReq addUserReq);

    /**
     * 根据用户名查询用户
     * @author JPG
     * @since 2019/10/21 9:38
     * @param account 参数
     */
    @GetMapping("/feign/uc/getUser/{account}")
    ResultData<AddUserResp> getByAccount(@PathVariable(value = "account") String account);



    /**
     * 修改密码
     * @author JPG
     * @since 2019/10/21 9:38
     * @param updatePwdReq 参数
     */
    @PostMapping("/feign/uc/updatePwd")
    ResultData updatePwd(@RequestBody UpdatePwdReq updatePwdReq);

    /**
     * 获取当前机构下所有角色信息(子机构下不获取)
     * @param deptId
     * @return
     */
    @GetMapping("/feign/uc/getSingleLevelRoles/{deptId}")
    ResultData<List<SelectRoleResp>> getSingleLevelRolesByDeptId(@PathVariable("deptId")Long deptId);

    /**
     * 用户信息更新
     * @param user
     * @return
     */
    @PostMapping("/feign/uc/updateUser")
    ResultData updateUser(@RequestBody UpdateUserReq user);

    /**
     * 重置用户密码
     * @param userId
     * @param newPwd
     * @return
     */
    @PostMapping("/feign/uc/resetPwd/{userId}/{newPwd}")
    ResultData resetPwd(@PathVariable("userId")Long userId, @PathVariable("newPwd")String newPwd);

    /**
     * 根据机构id获取用户信息列表
     * @param deptId
     * @return
     */
    @GetMapping("/feign/yc/getUsersByDeptId/{deptId}")
    ResultData<List<SelectUsersByRoleResp>> getUsersByDeptId(@PathVariable(value = "deptId") Long deptId);

    /**
     * 根据机构及用户条件查询机构下所有角色的关联用户列表:分页
     * @param selectPageUsersByDeptReq
     * @return
     */
    @GetMapping("/feign/yc/getPageUsersByDept")
    ResultData getPageUsersByDept(@RequestBody SelectPageUsersByDeptReq req);
}
