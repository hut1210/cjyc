package com.yqzl.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * @Author:Hut
 * @Date:2019/11/29 14:24
 */
public class SocketUtil {

    private static Logger logger = LoggerFactory.getLogger(SocketUtil.class);

    public static final String DEFAULT_CHARSET = "GBK";

    public static String doSocket(String msg){
        String returnStr ="";
        try {
          int soTimeOut = 5000; //超时设置 5s
          Socket socket = new Socket("127.0.0.1", 30010);// 建立socket
          socket.setSoTimeout(soTimeOut);
          BufferedOutputStream wr = null;

          String message = "<ap>" + msg + "</ap>";
          byte[] messageByte = message.getBytes(DEFAULT_CHARSET);//先转换成 GBK 的 byte 数组
          String length = "0" + messageByte.length + "      ";//7 位头
          length = length.substring(0, 7);
          message = length + message;
          System.out.println(message);
          messageByte = message.getBytes(DEFAULT_CHARSET);
          int sleepTime = 1000; //1s，线程休眠时间
          try {
              Thread.sleep(sleepTime);
          } catch (Exception e) {
              logger.error("",e);
          }
          wr = new BufferedOutputStream(socket.getOutputStream());//开始发送消息
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
          System.out.println("======" + returnStr);
        }catch (Exception e){
          logger.error("",e);
        }
        return returnStr;
    }
}
