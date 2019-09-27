package com.cjyc.common.until;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.cjyc.common.until.EncryptUtils.SHA1;

/**
 * 秒信短信服务工具类
 * @author leo
 * */
public class MiaoxinSmsUtil {

    //短信服务请求地址
    private static final String server =  "http://www.51miaoxin.com";
    //账号
    private static final String account = "cjwlhy";
    //密码
    private static final String secret = "ba9jet8miibxepu4gb5q";

    private static Logger log=LoggerFactory.getLogger(MiaoxinSmsUtil.class);


    /**
     * 2.3短信发送接口
     * @param mobiles 接受短信用户的手机号码，多个手机用半角字符“,”分开
     * @param sendText 具体短信内容，以UTF-8方式传递
     * @return
     * @throws IOException
     */
    public static String send(String mobiles,String sendText) throws IOException {
    	String result  = null;
    	String content = "【韵车物流】"+sendText;
    	String interfaceName = "/sms/send";
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        
        if (StringUtils.isBlank(mobiles)) {
        	 return result;
		}
        
        params.put("mobiles", mobiles);
        params.put("content", content);
//        params.put("ref", ref); //ref 客户可以对提交的短信加入reference参数以便后续进行跟踪
//        params.put("ext", ext); //ext 客户的自定义扩展号码（与运营人员确认是否具有扩展码）
         result = request(interfaceName, account, secret, params);
        //TODO 返回的JSON中，result数组即发送结果
        return result;
    }

    /**
     * 2.4短信发送报告查询
     * @param orderIds 可以一次查询一个订单或者多个订单，多个订单号用半角字符“,”分开
     * @return
     * @throws IOException
     */
    public static String check(String orderIds) throws IOException {
        String interfaceName = "/sms/check";
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("orderIds", orderIds);
        String result = request(interfaceName, account, secret, params);
        //TODO 返回的JSON中，reports即查询结果
        return result;
    }

    /**
     * 2.7账号余额查询
     * @return
     * @throws IOException
     */
    public static String balance() throws IOException {
        String interfaceName = "/account/getBalance";
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        String result = request(interfaceName, account, secret, params);
        //TODO 返回的JSON中，RMP，即剩余
        return result;
    }

    private static String request(String interfaceName, String account, String secret, Map<String, Object> params) throws IOException {
        params.put("account", account);
        String ts = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        params.put("ts", ts);
        String token = SHA1("account=" + account + "&ts=" + ts + "&secret=" + secret);
        params.put("token", token);
        log.debug("URL :" + server + interfaceName);
        log.debug("par :" + params);
        String result = post(server + interfaceName, params, "UTF-8");
        return result;
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
