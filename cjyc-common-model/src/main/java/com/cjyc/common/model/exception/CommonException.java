package com.cjyc.common.model.exception;

/**
 * @ClassName CommonException
 * @Description 自定义异常类
 * @Author zj
 * @Date 2019/5/8 15:53
 * @Version v1.0
 **/
public class CommonException extends RuntimeException{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String code;

    public CommonException() {
        super();
    }

    public CommonException(String msg, String code, Throwable cause) {
        super(msg, cause);
        this.code = code;
    }

    public CommonException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public CommonException(String msg, String code) {
        super(msg);
        this.code = code;
    }

    public CommonException(String msg) {
        super(msg);
    }

    public String getCode() {
        return code;
    }

}