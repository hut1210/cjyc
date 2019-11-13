package com.cjyc.common.model.dto.web.carrier;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class TrailCarrierDto extends BasePageDto implements Serializable {
    private static final long serialVersionUID = -8220451193541684094L;

    @ApiModelProperty("姓名")
    private String name;

    private String phone;
}