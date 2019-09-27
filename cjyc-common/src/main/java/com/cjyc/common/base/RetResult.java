package com.cjyc.common.base;

import java.io.Serializable;

/**
 * 通用返回数据模板类
 * 可以根据业务需要调用不同方法返回数据
 * Created by leo on 2019/7/23.
 */
public class RetResult<T> implements Serializable {

    //错误码
    private Integer code;

    //信息描述
    private String msg;

    //返回的数据内容
    private T data;

    public RetResult(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public RetResult(Integer code, String msg, T data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    //自定义返回值
    public static <T> RetResult<T> buildResponse(Integer code, String msg, T data) {
        return new RetResult<T>(code, msg, data);
    }

    //自定义返回值（不带返回数据）
    public static <T> RetResult<T> buildResponse(Integer code, String msg) {
        return new RetResult<T>(code, msg);
    }

    //成功（带返回数据）
    public static <T> RetResult<T> buildSuccessResponse(T data) {
        return new RetResult<T>(RetCodeEnum.SUCCESS.getCode(), RetCodeEnum.SUCCESS.getMsg(), data);
    }

    //成功（不带返回数据）
    public static <T> RetResult<T> buildSuccessResponse() {
        return new RetResult<T>(RetCodeEnum.SUCCESS.getCode(), RetCodeEnum.SUCCESS.getMsg());
    }

    //失败
    public static <T> RetResult<T> buildFailResponse() {
        return new RetResult<T>(RetCodeEnum.FAIL.getCode(), RetCodeEnum.FAIL.getMsg());
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
