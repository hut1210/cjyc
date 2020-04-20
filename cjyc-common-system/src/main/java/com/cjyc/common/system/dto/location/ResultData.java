package com.cjyc.common.system.dto.location;

import com.alibaba.fastjson.annotation.JSONType;
import com.cjkj.common.config.I18nMessage;
import com.cjkj.common.model.ReturnMsg;

import java.io.Serializable;

/**
 * @Description 位置服务返回数据类
 * @Author Liu Xing Xiang
 * @Date 2020/4/20 9:15
 **/
@JSONType(
        orders = {"code", "message", "data"}
)
public class ResultData<T> implements Serializable {
    private static final long serialVersionUID = 2101115750867811043L;

    private String code;
    private String message;
    private T data;

    public static <T> ResultData<T> ok(String message) {
        return (ResultData<T>) succeedWith((Object)null, ReturnMsg.SUCCESS.getCode(), message);
    }

    public static <T> ResultData<T> ok(T model, String message) {
        return succeedWith(model, ReturnMsg.SUCCESS.getCode(), message);
    }

    public static <T> ResultData<T> ok(T model) {
        return succeedWith(model, ReturnMsg.SUCCESS.getCode(), I18nMessage.getMessage("common_msg_op_success"));
    }

    public static <T> ResultData<T> succeedWith(T datas, String code, String message) {
        return new ResultData(code, message, datas);
    }

    public static <T> ResultData<T> failed(String code, String message) {
        return (ResultData<T>) failedWith((Object)null, code, message);
    }

    public static <T> ResultData<T> failed(String message) {
        return (ResultData<T>) failedWith((Object)null, ReturnMsg.ERROR.getCode(), message);
    }

    public static <T> ResultData<T> failed(T model, String message) {
        return failedWith(model, ReturnMsg.ERROR.getCode(), message);
    }

    public static <T> ResultData<T> failedWith(T data, String code, String message) {
        return new ResultData(code, message, data);
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public T getData() {
        return this.data;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String msg) {
        this.message = msg;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ResultData)) {
            return false;
        } else {
            ResultData<?> other = (ResultData)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label47: {
                    Object this$code = this.getCode();
                    Object other$code = other.getCode();
                    if (this$code == null) {
                        if (other$code == null) {
                            break label47;
                        }
                    } else if (this$code.equals(other$code)) {
                        break label47;
                    }

                    return false;
                }

                Object this$message = this.getMessage();
                Object other$message = other.getMessage();
                if (this$message == null) {
                    if (other$message != null) {
                        return false;
                    }
                } else if (!this$message.equals(other$message)) {
                    return false;
                }

                Object this$data = this.getData();
                Object other$data = other.getData();
                if (this$data == null) {
                    if (other$data != null) {
                        return false;
                    }
                } else if (!this$data.equals(other$data)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof ResultData;
    }

    public int hashCode() {
        int result = 1;
        Object $code = this.getCode();
        result = result * 59 + ($code == null ? 43 : $code.hashCode());
        Object $message = this.getMessage();
        result = result * 59 + ($message == null ? 43 : $message.hashCode());
        Object $data = this.getData();
        result = result * 59 + ($data == null ? 43 : $data.hashCode());
        return result;
    }

    public String toString() {
        return "ResultData(code=" + this.getCode() + ", message=" + this.getMessage() + ", data=" + this.getData() + ")";
    }

    public ResultData() {
    }

    public ResultData(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
