package com.cjyc.customer.api.util;


import com.cjyc.common.system.config.MiaoxinProperty;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 秒信短信业务客户端SDK
 * @author JPG
 */
public class MiaoxinSmsUtil {

    /**你的短信子账号App_ID（非登陆账号）*/
    private static final String ACCOUNT = MiaoxinProperty.account;
    /**你的短信子账号的App_SECRET 密匙（非登陆密码）*/
    private static final String SECRET = MiaoxinProperty.secret;

    private static final String SERVER = MiaoxinProperty.server;


    /**
     * 发送全变量（自定义签名及内容）接口
     *
     * @param mobiles           接受短信用户的手机号码，多个手机用半角字符“,”分开
     * @param content           具体短信内容，以UTF-8方式传递
     * @return
     * @throws IOException
     */
    public static String send(String mobiles, String content) throws IOException {
        if(mobiles == null || mobiles.equals("")){
            return null;
        }
        String interfaceName = "/sms/send";
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("mobiles", mobiles);
        params.put("content", content);
        String result = request(interfaceName, ACCOUNT, SECRET, params);
        return result;
    }

    /**
     * 发送全变量（自定义签名及内容）接口
     *
     * @param mobiles           接受短信用户的手机号码，多个手机用半角字符“,”分开
     * @param content           具体短信内容，以UTF-8方式传递
     * @param ref               客户可以对提交的短信加入reference参数以便后续进行跟踪
     * @param ext               客户的自定义扩展号码（与运营人员确认是否具有扩展码）
     * @return
     * @throws IOException
     */
    public static String send(String mobiles, String content, String ref, String ext) throws IOException {
        String interfaceName = "/sms/send";
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("mobiles", mobiles);
        params.put("content", content);
        params.put("ref", ref);
        params.put("ext", ext);
        String result = request(interfaceName, ACCOUNT, SECRET, params);
        //TODO 返回的JSON中，result数组即发送结果
        return result;
    }


    /**
     * 发送固定签名内容接口
     *
     * @param signatureId        签名ID
     * @param mobiles            接受短信用户的手机号码，多个手机用半角字符“,”分开
     * @param content            具体短信内容，以UTF-8方式传递
     * @param ref                客户可以对提交的短信加入reference参数以便后续进行跟踪
     * @param ext                客户的自定义扩展号码（与运营人员确认是否具有扩展码）
     * @return
     * @throws IOException
     */
    public static String sendFixedSignature(String signatureId, String mobiles, String content, String ref, String ext) throws IOException {
        String interfaceName = "/sms/sendFixedSignature";
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("signatureId", signatureId);
        params.put("mobiles", mobiles);
        params.put("content", content);
        params.put("ref", ref);
        params.put("ext", ext);
        String result = request(interfaceName, ACCOUNT, SECRET, params);
        //TODO 返回的JSON中，result数组即发送结果
        return result;
    }

    /**
     * 发送模板变量
     *
     * @param mobiles            接受短信用户的手机号码，多个手机用半角字符“,”分开
     * @param templateId         模板ID
     * @param ref                客户可以对提交的短信加入reference参数以便后续进行跟踪
     * @param ext                客户的自定义扩展号码（与运营人员确认是否具有扩展码）
     * @param paramValues        对应的参数
     * @return
     * @throws IOException
     */
    public static String sendTemplateParamd(String mobiles, String templateId, String ref, String ext, String... paramValues) throws IOException {
        String interfaceName = "/sms/sendTemplateParamd";
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("mobiles", mobiles);
        params.put("templateId", templateId);
        params.put("ref", ref);
        params.put("ext", ext);
        for (int i = 0; i < paramValues.length; i++) {
            params.put("param" + (i + 1), paramValues[i]);
        }
        String result = request(interfaceName, ACCOUNT, SECRET, params);
        //TODO 返回的JSON中，result数组即发送结果
        return result;
    }

    /**
     * 短信发送报告查询
     *
     * @param orderIds           可以一次查询一个订单或者多个订单，多个订单号用半角字符“,”分开
     * @return
     * @throws IOException
     */
    public static String check(String orderIds) throws IOException {
        String interfaceName = "/sms/check";
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("orderIds", orderIds);
        String result = request(interfaceName, ACCOUNT, SECRET, params);
        //TODO 返回的JSON中，reports即查询结果
        return result;
    }










    /**
     * 账号余额查询
     *
     * @return
     * @throws IOException
     */
    public static String balance() throws IOException {
        String interfaceName = "/account/getBalance";
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        String result = request(interfaceName, ACCOUNT, SECRET, params);
        //TODO 返回的JSON中，RMP，即剩余
        return result;
    }

    /**
     *
     * @param interfaceName
     * @param account
     * @param secret
     * @param params
     * @return
     * @throws IOException
     */
    private static String request(String interfaceName, String account, String secret, Map<String, Object> params) throws IOException {
        params.put("account", account);
        String ts = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        params.put("ts", ts);
        String token = SHA1("account=" + account + "&ts=" + ts + "&secret=" + secret);
        params.put("token", token);
        String result = post(SERVER + interfaceName, params, "UTF-8");
        return result;
    }

    /**
     *
     * @param decript
     * @return
     */
    private static String SHA1(String decript) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * post方法
     * @param action
     * @param params
     * @param encoding
     * @return
     * @throws IOException
     */
    private static String post(String action, Map<String, Object> params, String encoding) throws IOException {
        if (params.isEmpty()) {
            post(action, "", 2000, 10000);
        }
        StringBuilder content = new StringBuilder();
        Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
        Map.Entry<String, Object> entry = null;
        while (it.hasNext()) {
            entry = it.next();
            Object value = entry.getValue();
            if (value instanceof String) {
                content.append("&").append(entry.getKey()).append("=").append(URLEncoder.encode((String) value, encoding));
            } else {
                content.append("&").append(entry.getKey()).append("=").append(value);
            }
        }
        content.deleteCharAt(0);
        return post(action, content.toString(), 2000, 10000);
    }

    /**
     * 发送请求数据
     *
     * @param action
     * @param content
     * @param timeout4Connect
     * @param timeout4Read
     * @return
     * @throws IOException
     */
    private static String post(String action, String content, int timeout4Connect, int timeout4Read) throws IOException {
        URL url = new URL(action);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(timeout4Connect);
        conn.setReadTimeout(timeout4Read);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        conn.setUseCaches(false);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.connect();
        OutputStream outputStream = conn.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        dataOutputStream.writeBytes(content);
        dataOutputStream.flush();
        dataOutputStream.close();
        outputStream.close();
        InputStream inputStream = conn.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuffer response = new StringBuffer();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            response.append(line);
        }

        bufferedReader.close();
        inputStreamReader.close();
        inputStream.close();
        conn.disconnect();

        return response.toString();
    }

}
