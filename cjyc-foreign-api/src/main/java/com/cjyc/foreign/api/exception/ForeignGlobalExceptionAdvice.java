package com.cjyc.foreign.api.exception;

import com.cjkj.common.exception.GlobalExceptionAdvice;
import com.cjkj.common.model.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ForeignGlobalExceptionAdvice extends GlobalExceptionAdvice {
    /**
     * HttpMessageNotReadableException 需要读RequestBody时，没有传值时
     * @param e
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
}
