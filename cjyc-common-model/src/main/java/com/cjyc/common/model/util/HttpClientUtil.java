package com.cjyc.common.model.util;

import org.apache.http.Consts;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * http请求工具
 * 依赖jar包httpclient(org.apache.httpcomponents)
 * @author JPG
 */
public class HttpClientUtil {


    /**
     * Get-map请求(url参数)
     * @author JPG
     * @date 2019/7/24 13:37
     * @param url 请求地址
     * @return jsonString
     */
    public static String doGet(String url) {
        return doGet(url, null);
    }

    /**
     * Get-map请求(url参数)
     * @author JPG
     * @date 2019/7/24 13:36
     * @param url 请求地址
     * @param urlParam url参数
     * @return jsonString
     */
    public static String doGet(String url, Map<String, String> urlParam) {

        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;

        String resultString = "";
        try {
            HttpGet httpGet;
            // 创建uri
            if(urlParam != null){
                URIBuilder builder = new URIBuilder(url);
                if (urlParam != null) {
                    for (String key : urlParam.keySet()) {
                        builder.addParameter(key, urlParam.get(key));
                    }
                }
                URI uri = builder.build();
                // 创建http GET请求
                httpGet = new HttpGet(uri);
            }else{
                httpGet = new HttpGet(url);
            }
            // 执行请求
            response = httpClient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                resultString = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }

    /**
     * Post-application/x-www-form-urlencoded请求(form参数)
     * @author JPG
     * @date 2019/7/24 13:26
     * @param url 请求地址
     * @param formParam Post-form格式参数
     * @return jsonString
     */
    public static String doPost(String url, Map<String, String> formParam) {
        return doPost(url, null, null, formParam, null);
    }

    /**
     * Post-application/x-www-form-urlencoded请求(无参)
     * @author JPG
     * @date 2019/7/24 13:26
     * @param url 请求地址
     * @return jsonString
     */
    public static String doPost(String url) {
        return doPost(url, null, null, null, null);
    }

    /**
     * Post-application/x-www-form-urlencoded请求(header参数、form参数)
     * @author JPG
     * @date 2019/7/24 13:26
     * @param url 请求地址
     * @param headerParam 请求头参数
     * @param formParam Post-form格式参数
     * @return jsonString
     */
    public static String doPost(String url, Map<String, String> headerParam, Map<String, String> formParam){
        return doPost(url, headerParam, null, formParam, null);
    }

    /**
     * Post-application/x-www-form-urlencoded请求(header参数、url参数、form参数)
     * @author JPG
     * @date 2019/7/24 13:26
     * @param url 请求地址
     * @param headerParam 请求头参数
     * @param urlParam url参数
     * @param formParam Post-form格式参数
     * @return jsonString
     */
    public static String doPost(String url, Map<String, String> headerParam, Map<String, String> urlParam, Map<String, String> formParam){
        return doPost(url, headerParam, urlParam, formParam, null);
    }

    /**
     * Post-application/json请求(json参数)
     * @author JPG
     * @date 2019/7/24 13:26
     * @param url 请求地址
     * @param jsonParam json格式参数
     * @return jsonString
     */
    public static String doPostJson(String url, String jsonParam){
        return doPost(url, null, null, null, jsonParam);
    }

    /**
     * Post-application/json请求(header参数、json参数)
     * @author JPG
     * @date 2019/7/24 13:26
     * @param url 请求地址
     * @param headerParam 请求头参数
     * @param jsonParam json格式参数
     * @return jsonString
     */
    public static String doPostJson(String url, Map<String, String> headerParam,  String jsonParam){
        return doPost(url, headerParam, null, null, jsonParam);
    }
    /**
     * Post-application/json请求(header参数、url参数、json参数)
     * @author JPG
     * @date 2019/7/24 13:26
     * @param url 请求地址
     * @param headerParam 请求头参数
     * @param urlParam url参数
     * @param jsonParam json格式参数
     * @return jsonString
     */
    public static String doPostJson(String url, Map<String, String> headerParam, Map<String, String> urlParam, String jsonParam){
        return doPost(url, headerParam, urlParam, null, jsonParam);
    }

    /**
     * BasePost请求
     * @author JPG
     * @date 2019/7/24 13:26
     * @param url 请求地址
     * @param headerParam 请求头参数
     * @param urlParam url参数
     * @param formParam Post-form格式参数
     * @param jsonParam json格式参数
     * @return jsonString
     */
    private static String doPost(String url, Map<String, String> headerParam, Map<String, String> urlParam, Map<String, String> formParam, String jsonParam) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {

            // 创建Http Post请求
            HttpPost httpPost;
            // 添加url参数
            if (urlParam != null) {
                URIBuilder builder = new URIBuilder(url);
                for (String key : urlParam.keySet()) {
                    builder.addParameter(key, urlParam.get(key));
                }
                URI uri = builder.build();
                httpPost = new HttpPost(uri);
            }else{
                httpPost = new HttpPost(url);
            }
            //添加header参数
            if(headerParam != null){
                for (String key : headerParam.keySet()) {
                    httpPost.addHeader(key, headerParam.get(key));
                }
            }
            //添加form参数
            if (formParam != null) {

                List<NameValuePair> paramList = new ArrayList<>();
                for (String key : formParam.keySet()) {
                    paramList.add(new BasicNameValuePair(key, formParam.get(key)));
                }
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
                httpPost.setEntity(entity);
            }
            //添加json参数
            if(jsonParam != null){
                StringEntity entity = new StringEntity(jsonParam, ContentType.APPLICATION_JSON);
                httpPost.setEntity(entity);
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() ==  HttpStatus.SC_OK) {
                resultString = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return resultString;
    }


}
