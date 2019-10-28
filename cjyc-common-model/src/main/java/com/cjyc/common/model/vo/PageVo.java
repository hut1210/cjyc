package com.cjyc.common.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 分页&统计
 * {@link ResultVo data}数据结构
 * @author JPG
 */
@Builder
@Getter
@ApiModel(value = "PageVo对象", description = "通用返回Data报文Page结构")
public class PageVo<T> implements Serializable {

    private static final long serialVersionUID = 2L;

    @ApiModelProperty(value = "<总记录数（分页）>")
    private long totalRecords;

    @ApiModelProperty(value = "<每页条数（分页）>")
    private int pageSize;

    @ApiModelProperty(value = "<当前页码（分页）>")
    private int currentPage;

    @ApiModelProperty(value = "<总页数（分页）>")
    private int totalPages;

    @ApiModelProperty(value = "<内容（分页）>")
    private List<T> list;

    @ApiModelProperty(value = "<非分页相关统计信息（分页）>")
    private Map<String, Object> countInfo;

}
