package com.yqzl.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * @Author:Hut
 * @Date:2019/11/29 14:24
 */
@Slf4j
public class SocketUtil {

    public static final String DEFAULT_CHARSET = "GBK";

    public static String doSocket(String msg) {
        String returnStr = "";
        try {
            // 超时设置 5s
            int soTimeOut = 5000;
            // 建立socket
            Socket socket = new Socket("127.0.0.1", 30010);
            socket.setSoTimeout(soTimeOut);
            BufferedOutputStream wr = null;
            String message = "<ap>" + msg + "</ap>";
            //先转换成 GBK 的 byte 数组
            byte[] messageByte = message.getBytes(DEFAULT_CHARSET);
            //7 位头
            String length = "0" + messageByte.length + "      ";
            length = length.substring(0, 7);
            message = length + message;
            log.info("请求银行数据：{}", message);
            messageByte = message.getBytes(DEFAULT_CHARSET);
            //1s，线程休眠时间
            int sleepTime = 1000;
            try {
                Thread.sleep(sleepTime);
            } catch (Exception e) {
                log.error("", e);
            }
            //开始发送消息
            wr = new BufferedOutputStream(socket.getOutputStream());
            wr.write(messageByte);
            wr.flush();
            //接收回应消息
            BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream(), DEFAULT_CHARSET));
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            //处理返回
            returnStr = new String(sb.toString().getBytes());
            wr.close();
            rd.close();
            socket.close();
            log.info("请求银行返回数据：{}", returnStr);
        } catch (Exception e) {
            log.error("转账请求银行发生异常", e);
        }
        return returnStr;
    }
}
