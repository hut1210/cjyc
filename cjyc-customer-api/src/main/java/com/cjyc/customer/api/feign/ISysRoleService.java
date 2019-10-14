package com.cjyc.customer.api.feign;

import com.cjkj.common.constant.ServiceNameConstants;
import com.cjkj.common.feign.fallback.UserServiceFallbackFactory;
import com.cjyc.common.model.dto.sys.SysRoleDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * sys角色接口
 * @author JPG
 */
@FeignClient(value = ServiceNameConstants.USER_SERVICE, fallbackFactory = UserServiceFallbackFactory.class, decode404 = true)
public interface ISysRoleService {

    /**
     * 根据用户ID查询
     * TODO 替换正确的RequestMapping和参数
     * @author JPG
     * @since 2019/10/14 12:27
     * @param userId
     */
    @GetMapping("/sys/role/select/{sysId}")
    List<SysRoleDto> getListByUserId(@PathVariable(value = "sysId") Long userId);
}
