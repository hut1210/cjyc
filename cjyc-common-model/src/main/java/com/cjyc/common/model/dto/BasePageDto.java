package com.cjyc.common.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

public class BasePageDto implements Serializable {

    @ApiModelProperty(value = "当前页",required = true)
    private Integer currentPage;

    @ApiModelProperty(value = "每页条数",required = true)
    private Integer pageSize;

    public Integer getCurrentPage() {
        return currentPage == null ? 1 : currentPage;
    }

    public Integer getPageSize() {
        return pageSize == null ? 20 : pageSize;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}