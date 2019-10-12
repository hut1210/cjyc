package com.cjyc.common.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

/**
 * 返回前端数据结构
 *
 * @author JPG
 */
@Builder
@Getter
@ApiModel(value = "ResultVo对象", description = "通用返回报文结构")
public class ResultVo<T> implements Serializable {

    private static final long serialVersionUID = 2L;

    @ApiModelProperty(value = "[响应码]")
    private int code;

    @ApiModelProperty(value = "[响应信息]")
    private String msg;
    /**
     * 返回内容：自定义对象、集合、List&统计信息{@link ListVo} 、分页&统计{@link PageVo}
     */
    @ApiModelProperty(value = "[响应内容]")
    private T data;

}
