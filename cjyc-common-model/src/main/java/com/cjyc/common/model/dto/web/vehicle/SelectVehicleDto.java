package com.cjyc.common.model.dto.web.vehicle;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class SelectVehicleDto extends BasePageDto implements Serializable {

    @ApiModelProperty("车牌号")
    private String plateNo;

    @ApiModelProperty("司机姓名")
    private String realName;

    @ApiModelProperty("司机电话")
    private String phone;

    @ApiModelProperty(value = "状态：0待审核，2已审核，4已驳回，7已停用")
    private Integer state;

    @ApiModelProperty("所有权：0韵车自营，1个人所有，2第三方物流公司")
    private Integer ownershipType;
}