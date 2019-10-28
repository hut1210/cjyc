package com.cjyc.common.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * List&统计信息
 * {@link ResultVo data}数据结构
 * @author JPG
 */
@Builder
@Getter
@ApiModel(value = "ListVo对象", description = "通用返回Data报文List结构")
public class ListVo<T> implements Serializable {

    private static final long serialVersionUID = 2L;

    @ApiModelProperty(value = "<总记录数>")
    private long totalRecords;

    @ApiModelProperty(value = "<内容>")
    private List<T> list;

    @ApiModelProperty(value = "<统计信息>")
    private Map<String, Object> countInfo;

}
