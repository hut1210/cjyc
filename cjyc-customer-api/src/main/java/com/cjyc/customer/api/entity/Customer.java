package com.cjyc.customer.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 客户主表实体
 * Created by leo on 2019/7/23.
 */
@Data(staticConstructor = "getInstance")
@TableName(value = "yc_customer")//指定表名
public class Customer {

    //value与数据库主键列名一致，若实体类属性名与表主键列名一致可省略value
    @TableId(value = "id",type = IdType.AUTO)//指定自增策略
    private Integer id;
    @TableField(value = "customer_code")
    private String customerCode;
    private String token;

    @TableField(value = "open_id")
    private String openId;
    private String alias;
    private String name;
    @TableField(value = "first_letter")
    private String firstLetter;
    private String phone;
    private String pwd;

}
