package com.cjyc.customer.api.feign;

import com.cjkj.common.constant.ServiceNameConstants;
import com.cjkj.common.feign.fallback.UserServiceFallbackFactory;
import com.cjkj.common.model.ResultData;
import com.cjkj.usercenter.dto.common.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    @PostMapping("/feign/uc/getUser/{account}")
    ResultData<AddUserResp> getByAccount(@PathVariable String account);



    /**
     * 修改密码
     * @author JPG
     * @since 2019/10/21 9:38
     * @param updatePwdReq 参数
     */
    @PostMapping("/feign/uc/updatePwd")
    ResultData updatePwd(@RequestBody UpdatePwdReq updatePwdReq);
}
