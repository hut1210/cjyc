package com.cjyc.web.api.exception;



import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
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
        return BaseResultUtil.getVo(ResultEnum.API_INVOKE_ERROR.getCode(),
                ResultEnum.API_INVOKE_ERROR.getMsg(),
                 exception.getMessage());
    }

}