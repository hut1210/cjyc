package com.cjyc.salesman.api.exception;

import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * 通用异常处理
 * @author JPG
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * ParameterException需要读RequestBody时，必传参数未传时
     * @param e
     * @return
     */
    @ExceptionHandler({ParameterException.class})
    public ResultVo handleException(ParameterException e){
        log.error(e.getMessage(), e);
        return BaseResultUtil.getVo(ResultEnum.MOBILE_PARAM_ERROR.getCode(), e.getMessage());
    }


    /**
     * HttpMessageNotReadableException 需要读RequestBody时，没有传值时
     * @param
     * @return 400
     */
    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public ResultVo handleException(HttpMessageNotReadableException e){
        log.error("参数异常", e);
        return BaseResultUtil.paramError("参数异常");
    }

    /**
     * RequestBody 对应 @RequestParam时异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    public ResultVo handleException(MissingServletRequestParameterException e){
        log.error(e.getMessage(), e);
        return BaseResultUtil.paramError(e.getMessage());
    }

    /**
     * sentinel异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = {UndeclaredThrowableException.class})
    public ResultVo handleException(UndeclaredThrowableException e){
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw, true));
        String msg = sw.toString();
        if (msg != null){
            if(msg.contains("FlowException")){
                return BaseResultUtil.fail("系统访问：限流");
            }else if(msg.contains("DegradeException")){
                return BaseResultUtil.fail("系统访问：降级");
            }else if(msg.contains("SystemBlockException")){
                return BaseResultUtil.fail("系统访问：保护");
            }else {
                return BaseResultUtil.serverError( "系统内部错误");
            }
        }else {
            return BaseResultUtil.serverError("系统内部错误");
        }
    }

    /**
     * RequestBody 对应 @RequestParam时异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = {Exception.class})
    public ResultVo handleException(Exception e){
        log.error(e.getMessage(), e);
        return BaseResultUtil.getVo(ResultEnum.API_INVOKE_ERROR.getCode(), "未知错误");
    }


}
