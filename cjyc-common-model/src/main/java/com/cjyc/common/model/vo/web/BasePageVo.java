package com.cjyc.common.model.vo.web;

import lombok.Data;

import java.io.Serializable;

@Data
public class BasePageVo implements Serializable {

    /**
     * 每页大小
     */
    private Integer pageNo = 1;

    /**
     * 当前页码
     */
    private Integer pageSize = 10;
}