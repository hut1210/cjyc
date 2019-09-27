package com.cjyc.customer.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Created by leo on 2019/7/26.
 */
@Data(staticConstructor = "getInstance")
@TableName(value = "sys_operation_log")//指定表名
public class OperationLog {

    @TableId(value = "id",type = IdType.INPUT)
    private String logId;
    private String ip;
    private String account;
    private String token;
    @TableField(value = "method_type")
    private String methodType;
    @TableField(value = "method_name")
    private String methodName;
    private String url;
    private String uri;
    private String clazz;
    private String time;
    private String param;
    @TableField(value = "cost_time")
    private Long costTime;

    public OperationLog(){
        super();
    }

    public OperationLog(String logId,String token,String account,String ip,String methodType,
                        String url,String uri,String clazz,String methodName,String time,String param){
        super();
        this.logId = logId;
        this.token = token;
        this.account = account;
        this.ip = ip;
        this.methodType = methodType;
        this.url = url;
        this.uri = uri;
        this.clazz = clazz;
        this.methodName = methodName;
        this.time = time;
        this.param = param;

    }

}
