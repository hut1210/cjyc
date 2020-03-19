package com.cjyc.foreign.api.push;

import com.alibaba.fastjson.JSONObject;
import com.cjyc.foreign.api.utils.HttpClientUtil;
import com.cjyc.foreign.api.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author zcm
 * @date 2020/3/19 8:29
 */
@Slf4j
public abstract class AbstractCarCirclePush implements IPushable{
    //返回接口key
    private static final String RESULT_KEY = "success";
    
    /**
    * @Description: 韵车接口请求及验证返回结果
    * @Param: [interUrl, data, charset, headers]
    * @return: boolean
    * @Author: zcm
    * @Date: 2020/3/19
    */
    protected boolean doPush(String interUrl, JSONObject jo, String clientId, String charset,
                                        Map<String, String> headers) {
        if (!jo.containsKey("sign") || StringUtils.isEmpty(jo.getString("sign"))) {
            StringBuilder sb = new StringBuilder("");
            sb.append("createTime" + jo.get("createTime"));
            sb.append("orderNo" + jo.get("orderNo"));
            sb.append("clientId" + clientId);
            String md5Str = MD5Util.getMD5ToBase64Str(sb.toString());
            if (StringUtils.isEmpty(md5Str)) {
                return false;
            }
            jo.put("sign", md5Str);
        }
        String result = HttpClientUtil.sendJsonPost(interUrl, jo.toJSONString(), null, null);
        if (StringUtils.isEmpty(result)) {
            log.error("请求接口失败，将在重发次数用尽之前重发");
            return false;
        }
        log.info("请求数据：{}", jo.toJSONString());
        JSONObject resultJo = JSONObject.parseObject(result);
        if (resultJo.containsKey(RESULT_KEY) && resultJo.getBoolean(RESULT_KEY)) {
            return true;
        }
        return false;
    }
}
