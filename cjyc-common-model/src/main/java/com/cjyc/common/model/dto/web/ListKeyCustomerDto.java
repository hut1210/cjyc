package com.cjyc.common.model.dto.web;

import lombok.Data;

import java.io.Serializable;

@Data
public class ListKeyCustomerDto implements Serializable {

    /**
     * 大客户id
     */
    private String id;

    /**
     * 大客户客户全称
     */
    private String name;

    /**
     * 客户简称
     */
    private String alias;

    /**
     * 联系人
     */
    private String contactMan;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 客户性质
     */
    private String customerNature;

    /**
     * 主营业务描述
     */
    private String majorBusDes;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 创建人
     */
    private String createUserId;
}