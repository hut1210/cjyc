package com.cjyc.common.model.vo.web;

import lombok.Data;

import java.io.Serializable;

@Data
public class SelectKeyCustomerVo extends BasePageVo implements Serializable {

    /**
     * 大客户主键id(编号)
     */
    private Long id;

    /**
     * 大客户全称
     */
    private String name;

    /**
     * 大客户简称
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
     * 创建人
     */
    private String createUserId;

}