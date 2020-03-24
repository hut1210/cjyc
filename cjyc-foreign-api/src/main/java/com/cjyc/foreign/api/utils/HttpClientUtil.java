package com.cjyc.foreign.api.utils;

import com.cjkj.log.monitor.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * HttpClient调用工具封装
 */
@Slf4j
public class HttpClientUtil {
    private static final HttpClient httpClient;
    public static final String DEFAULT_CHARSET = "UTF-8";

    static {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(60000).setSocketTimeout(15000).build();
        httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    }

    /**
     * 发送Post文本内容请求
     * @param reqUrl
     * @param data
     * @param charset
     * @param headers
     * @return
     */
    public static String sendJsonPost(String reqUrl, String data, String charset,
                                              Map<String, String> headers) {
        HttpPost httpPost = new HttpPost(reqUrl);
        HttpResponse httpResponse = null;
        String result = "";
        if (StringUtils.isEmpty(charset)) {
            charset = DEFAULT_CHARSET;
        }
        try{
            StringEntity stringEntity = new StringEntity(data, charset);
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            if (!CollectionUtils.isEmpty(headers)) {
                headers.forEach((key, value) -> {
                    httpPost.setHeader(key, value);
                });
            }
            httpResponse = httpClient.execute(httpPost);
            result = EntityUtils.toString(httpResponse.getEntity(), charset);
        }catch (Exception e){
            log.error("请求通信[" + reqUrl + "]时偶遇异常,异常信息如下", e);
        }
        return result;
    }
}
