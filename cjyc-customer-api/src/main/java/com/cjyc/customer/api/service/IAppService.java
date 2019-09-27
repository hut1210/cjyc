package com.cjyc.customer.api.service;

import java.util.Map;

/**
 * Created by leo on 2019/7/25.
 */
public interface IAppService {

    //发送短信验证码
    boolean sendMessage(String phone);

    //校验短信验证码
    boolean checkMsgCode(String phone, String msgCode);

    //登录
    Map login(String phone) throws Exception;

    void logout(String customerCode, String token);

}
