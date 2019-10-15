package com.cjyc.common.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 业务范围
 * @author JPG
 */
@Data
@ApiModel
public class BusinessScopeDto {
    @ApiModelProperty(value = "业务范围类型：0业务中心，1全国")
    private Integer type;
    @ApiModelProperty(value = "业务范围，业务中心ID")
    private List<Long> storeIds;
}
