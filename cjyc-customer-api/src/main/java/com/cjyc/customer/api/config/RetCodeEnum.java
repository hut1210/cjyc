package com.cjyc.customer.api.config;

/**
 * 响应状态码枚举类
 * Created by leo on 2019/7/23.
 */
public enum RetCodeEnum {
    /* 通用 */
    SUCCESS(0, "成功"),
    FAIL(9, "处理失败"),//通用业务失败状态， 可以自定义返回消息msg 如：用户名或密码错误、提交失败等

    /* 移动端错误：101-199 */
    MOBILE_TOKEN_ILLEGAL(100, "token校验失败"),
    MOBILE_TOKEN_TIMEOUT(101, "token超时"),
    MOBILE_HTTP_ILLEGAL(102, "非法请求"),
    MOBILE_HEAD_ERROR(103, "Header参数错误"),
    MOBILE_PARAM_ERROR(104, "接口请求参数错误"),
    MOBILE_SYSTEM_MAINTAIN(105, "系统维护中"),
    MOBILE_NO_ACCESS(106, "无访问权限"),
    MOBILE_VERRION_LOW(107, "版本太低，建议升级"),
    MOBILE_HTTP_OFTEN(108, "请求太过频繁"),

    /* 系统错误：201-299*/
    API_INVOKE_ERROR(201, "接口异常"),
    API_SYSTEM_BUSY(202, "系统繁忙，请稍后重试"),
    API_FORBID_VISIT(203, "该接口禁止访问"),
    API_ADDRESS_INVALID(204, "接口地址无效"),
    API_REQUEST_TIMEOUT(205, "接口请求超时");

    //状态码
    private Integer code;

    //消息内容
    private String msg;

    //构造方法
    RetCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
