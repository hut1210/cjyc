package com.cjyc.common.model.dto.web.driver;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CarrierDriverListDto extends BasePageDto {
    private Long carrierId;

    @ApiModelProperty("司机姓名")
    private String driverName;

    @ApiModelProperty("车牌号")
    private String vehiclePlateNo;

    @ApiModelProperty("手机号")
    private String driverPhone;

    @ApiModelProperty("身份证号")
    private String driverIdCard;



}
