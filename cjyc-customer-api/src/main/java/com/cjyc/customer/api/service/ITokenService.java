package com.cjyc.customer.api.service;


/**
 * Created by leo on 2019/7/25.
 */
public interface ITokenService {
    /**
     * 创建token
     * @param customerCode
     * @return
     */
    String createToken(String customerCode) throws Exception;

    /**
     * 刷新用户
     * @param token
     */
    void refreshTokenTime(String token);

    /**
     * 删除token
     * @param token
     */
    void delToken(String token);

    /**
     * 获取token信息
     * @param token
     * @return
     */
    String getCodeByToken(String token);
}
