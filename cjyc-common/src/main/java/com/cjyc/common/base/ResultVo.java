package com.cjyc.common.base;

import java.io.Serializable;

/**
 * 通用返回数据模板类
 * 可以根据业务需要调用不同方法返回数据
 * Created by leo on 2019/7/23.
 */
public class ResultVo<T> implements Serializable {

    private static final long serialVersionUID = 8547137674611521490L;

    //错误码
    private Integer code;

    //信息描述
    private String msg;

    //返回的数据内容
    private T data ;

    public ResultVo(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public ResultVo(Integer code, String msg, T data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    //自定义返回值
    public static <T> ResultVo<T> response(Integer code, String msg, T data) {
        return new ResultVo<T>(code, msg, data);
    }

    //自定义返回值（不带返回数据）
    public static <T> ResultVo<T> response(Integer code, String msg) {
        return new ResultVo<T>(code, msg);
    }

    //成功（带返回数据）
    public static <T> ResultVo<T> success(T data) {
        return new ResultVo<T>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), data);
    }

    //成功（不带返回数据）
    public static <T> ResultVo<T> success() {
        return new ResultVo<T>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg());
    }

    //失败
    public static <T> ResultVo<T> fail() {
        return new ResultVo<T>(ResultEnum.FAIL.getCode(), ResultEnum.FAIL.getMsg());
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
