package com.cjyc.salesman.api.exception;

import com.cjkj.common.exception.GlobalExceptionAdvice;
import com.cjkj.common.model.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * Created by youyou on 2019/6/15.
 */
@RestControllerAdvice
@Slf4j
public class AdminGlobalExceptionAdvice extends GlobalExceptionAdvice {

    /**
     * HttpMessageNotReadableException 需要读RequestBody时，没有传值时
     * @param
     * @return 400
     */
    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public ResultData handleException(HttpMessageNotReadableException e){
        log.error("参数异常", e);


        return ResultData.failed("400", "参数异常");
    }

    /**
     * RequestBody 对应 @RequestParam时异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    public ResultData handleException(MissingServletRequestParameterException e){
        log.error("参数异常", e);
        return ResultData.failed("400", "参数异常");
    }

    /**
     * sentinel异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = {UndeclaredThrowableException.class})
    public ResultData handleException(UndeclaredThrowableException e){
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw, true));
        String msg = sw.toString();
        if (msg != null){
            if(msg.contains("FlowException")){
                return ResultData.failed("系统访问：限流");
            }else if(msg.contains("DegradeException")){
                return ResultData.failed("系统访问：降级");
            }else if(msg.contains("SystemBlockException")){
               return ResultData.failed("系统访问：保护");
            }else {
                return ResultData.failed("500", "系统内部错误");
            }
        }else {
            return ResultData.failed("500", "系统内部错误");
        }
    }

}
