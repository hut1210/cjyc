package com.cjyc.common.model.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class BasePageDto implements Serializable {

    @ApiModelProperty(value = "当前页")
    private Integer currentPage;

    @ApiModelProperty(value = "每页条数")
    private Integer pageSize;

    public Integer getCurrentPage() {
        return currentPage == null || currentPage == 0 ? 1 : currentPage;
    }

    public Integer getPageSize() {
        return pageSize == null  || pageSize == 0 ? 20 : pageSize;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}