package com.cjyc.common.model.exception;


import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.ResultEnum;
import com.cjyc.common.model.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {


    /**
     * 所有异常报错
     *
     * @param request
     * @param exception
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    public ResultVo allExceptionHandler(HttpServletRequest request,
                                        Exception exception) throws Exception {
        log.error("服务端异常 ：", exception);
        return BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),"服务器异常，请联系管理员！",exception.getMessage());
    }

}