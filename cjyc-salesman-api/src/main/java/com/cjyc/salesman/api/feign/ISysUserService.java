package com.cjyc.salesman.api.feign;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cjkj.common.constant.ServiceNameConstants;
import com.cjkj.common.feign.fallback.UserServiceFallbackFactory;
import com.cjkj.common.model.ResultData;
import com.cjyc.common.model.dto.sys.SysUserDto;
import com.cjyc.common.model.entity.sys.SysUserEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
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
     * 查询用户实体对象SysUser
     * @author JPG
     * @since 2019/10/10 12:52
     * @param userId 用户ID
     */
    @PostMapping("/sys/user/get/{userId}")
    ResultData<SysUserEntity> selectById(@PathVariable("userId") Long userId);

    /**
     * 查询用户实体对象SysUser
     * @author JPG
     * @since 2019/10/10 12:58
     * @param username 用户名
     */
    @PostMapping("/sys/user/getbyname/{username}")
    ResultData<SysUserEntity> selectByUsername(@PathVariable("username") String username);

    /**
     * 查询用户列表（分页）
     * @author JPG
     * @since 2019/10/10 13:00
     * @param sysUserDto 用户Dto
     */
    @PostMapping("/sys/user/list")
    ResultData<IPage<SysUserEntity>> list(@RequestBody SysUserDto sysUserDto);


    /**
     * 删除用户
     * @author JPG
     * @since 2019/10/10 12:54
     * @param sysUserEntity 用户实体
     */
    @PostMapping("/sys/user/delete")
    ResultData delete(@RequestBody SysUserEntity sysUserEntity);

    /**
     * 获取当前用户顶级机构
     * @author JPG
     * @since 2019/10/10 12:56
     * @param userId 用户ID
     */
    @PostMapping("/sys/user/getRoleTopDeptId/{userId}")
    ResultData<List<Long>> getRoleTopDeptId(@PathVariable("userId") Long userId);

    /**
     * 查询用户列表信息: 部门id必传，需要针对部门做数据过滤
     * @author JPG
     * @since 2019/10/10 13:05
     * @param sysUserDto 用户Dto
     */
    @PostMapping("/sys/user/listFilterDeptId")
    ResultData<IPage<SysUserEntity>> listFilterDeptId(@RequestBody SysUserDto sysUserDto);

    /**
     * 查询所有用户列表信息（不使用用户角色做限定): its调用
     * @author JPG
     * @since 2019/10/10 13:09
     * @param sysUserDto 用户Dto
     */
    @PostMapping("/sys/user/listForIts")
    ResultData<IPage<SysUserEntity>> listForIts(@RequestBody SysUserDto sysUserDto);

    /**
     * 添加用户
     * @author JPG
     * @since 2019/10/10 13:12
     * @param user
     */
    @PostMapping("/sys/user/save")
    ResultData save(@RequestBody SysUserEntity user);

    /**
     * 修改用户
     * @author JPG
     * @since 2019/10/10 13:13
     * @param user
     */
    @PostMapping("/sys/user/update")
    ResultData update(@RequestBody SysUserEntity user);


}
