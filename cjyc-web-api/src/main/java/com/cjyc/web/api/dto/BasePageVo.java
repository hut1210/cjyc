package com.cjyc.web.api.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class BasePageVo implements Serializable {

    /**
     * 每页大小
     */
    private Integer pageNo;

    /**
     * 当前页码
     */
    private Integer pageSize;
}