package com.cjyc.foreign.api.utils;

import com.cjkj.common.constant.SecurityConstants;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 获取用户登录账号信息
 */
public class LoginAccountUtil {
    private LoginAccountUtil() {

    }
    /**
     * 从登录信息中获取登录账号
     * @return
     */
    public static String getLoginAccount() {
        String account =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                        .getRequest().getHeader(SecurityConstants.USER_HEADER);
        if (!StringUtils.isEmpty(account)) {
            return account;
        }
        return null;
    }
}
