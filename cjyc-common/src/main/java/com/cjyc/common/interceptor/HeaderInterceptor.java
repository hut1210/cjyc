package com.cjyc.common.interceptor;

import com.cjkj.common.utils.JsonUtil;
import com.cjyc.common.base.ResultVo;
import com.cjyc.common.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author leo
 * @date 2019/7/25.
 */
@Component
public class HeaderInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //目前暂时不做本地token校验 暂注释掉
//        HeaderIgnoreNav annotation;
//
//        if(handler instanceof HandlerMethod) {
//            HandlerMethod handlerMethod = (HandlerMethod)handler;
//
//            if(handlerMethod.hasMethodAnnotation(HeaderIgnoreNav.class)){
//                annotation = handlerMethod.getMethodAnnotation(HeaderIgnoreNav.class);
//
//                //如果有@tokenIgnore注解，且为true，则不验证token
//                if(annotation.tokenIgnore()){
//                    return true;
//                }
//            }
//
//        }else{
//            return true;
//        }
//
//        //获取token凭证
//        String token = request.getHeader("token");
//        String newCustomerCode = request.getHeader("customerCode");
//        String newTimestamp = request.getHeader("timestamp");
//
//        if(StringUtils.isBlank(token)){
//            //返回移动端封装状态码
//            responseOutWithJson(response,
//                    ResultVo.response(ResultEnum.MOBILE_TOKEN_ILLEGAL.getCode(),
//                            ResultEnum.MOBILE_TOKEN_ILLEGAL.getMsg()));
//            return false;
//
//        }else{
//            String customerCode = (String)redisUtil.get(token);
//
//            if(StringUtils.isNotBlank(customerCode)){
//                String deToken = EncryptUtils.decodeUTF8(token);
//                String[] tokenArr = deToken.split(",");
//                String sign = tokenArr[0];
//                String tokenTime = tokenArr[1];
//                String newSign = EncryptUtils.MD5(newCustomerCode + tokenTime + "YCcustomersalt");
//
//                if(sign.equals(newSign)){
//                    long expires = System.currentTimeMillis() - Long.parseLong(newTimestamp);
//                    System.out.print("##############token时效："+expires/60000);
//                    return true;
//                }else{
//                    responseOutWithJson(response,
//                            ResultVo.response(ResultEnum.MOBILE_TOKEN_ILLEGAL.getCode(),
//                                    ResultEnum.MOBILE_TOKEN_ILLEGAL.getMsg()));
//                    return false;
//                }
//            }else{
//                responseOutWithJson(response,
//                        ResultVo.response(ResultEnum.MOBILE_TOKEN_TIMEOUT.getCode(),
//                                ResultEnum.MOBILE_TOKEN_TIMEOUT.getMsg()));
//                return false;
//            }
//
//        }
        return true;

    }


    /**
     * 以JSON格式输出
     * @param response
     * @param json
     */
    private void responseOutWithJson(HttpServletResponse response,ResultVo json) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.append(JsonUtil.toJson(json));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
