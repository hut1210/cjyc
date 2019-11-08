package com.cjyc.common.model.dto.web.vehicle;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SelectVehicleDto extends BasePageDto implements Serializable {

    private static final long serialVersionUID = 4398214452811967048L;
    @ApiModelProperty("车牌号")
    private String plateNo;

    @ApiModelProperty("司机姓名")
    private String realName;

    @ApiModelProperty("司机电话")
    private String phone;
}