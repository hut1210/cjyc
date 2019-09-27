package com.cjyc.customer.api.service.impl;

import com.cjyc.customer.api.entity.Customer;
import com.cjyc.customer.api.redis.RedisUtil;
import com.cjyc.customer.api.service.ITokenService;
import com.cjyc.customer.api.until.Constants;
import com.cjyc.customer.api.until.EncryptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * Created by leo on 2019/7/25.
 */
@Service
public class TokenServiceImpl implements ITokenService{

    @Autowired
    private RedisUtil redisUtil;

    @Value("${token.expires}")
    private int expires;

    @Value("${salt.customer_salt}")
    private String customerSalt;

    /**
     * 创建token
     * @param customerCode
     * @return
     */
    @Override
    public String createToken(String customerCode) throws Exception{
        long now = System.currentTimeMillis();
        String sign = EncryptUtils.MD5(customerCode + String.valueOf(now) + customerSalt);
        String token = EncryptUtils.encodeUTF8(sign+","+String.valueOf(now));
        redisUtil.set(token,customerCode,expires);
        return token;
    }

    /**
     * 刷新token时间
     * @param token
     */
    @Override
    public void refreshTokenTime(String token) {
        if(redisUtil.hasKey(token)){
            redisUtil.fresh(token,expires);
        }
    }

    /**
     * 删除token
     * @param token
     */
    @Override
    public void delToken(String token) {
        redisUtil.del(token);
    }

    /**
     * 获取token信息
     * @param token
     * @return
     */
    @Override
    public String getCodeByToken(String token) {
        if(redisUtil.hasKey(token)){
            return (String)redisUtil.get(token);
        }
        return null;
    }
}
