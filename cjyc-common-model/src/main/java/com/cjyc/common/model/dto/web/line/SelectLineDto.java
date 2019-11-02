package com.cjyc.common.model.dto.web.line;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class SelectLineDto extends BasePageDto implements Serializable {

    @ApiModelProperty("登录人userId")
    private Long userId;

    @ApiModelProperty("起始城市编码")
    private String fromCityCode;

    @ApiModelProperty("目的地城市编码")
    private String toCityCode;

    @ApiModelProperty("线路编码")
    private String lineCode;

}