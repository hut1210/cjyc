package com.cjyc.common.model.dto.web.postal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class PostalDto implements Serializable {
    private static final long serialVersionUID = -1266014589673193534L;

    @ApiModelProperty(value = "关键字")
    private String keyword;
}