package com.cjyc.common.model.dto.web.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @author JPG
 */
@Data
@ApiModel
public class CustomerfuzzyListDto {

    @ApiModelProperty(value = "类型：1个人，2企业（大客户）3-合伙人")
    private Integer type;

    @ApiModelProperty(value = "信息")
    private Integer info;
}
