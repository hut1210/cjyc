package com.cjyc.common.model.dto.web.line;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class SelectLineDto extends BasePageDto {

    private static final long serialVersionUID = -8401768906217301840L;
    @ApiModelProperty("登录人id(loginId)")
    private Long loginId;

    @ApiModelProperty("起始城市编码")
    private String fromCityCode;

    @ApiModelProperty("目的地城市编码")
    private String toCityCode;

    @ApiModelProperty("线路编码")
    private String lineCode;

}