package com.cjyc.common.model.dto.web.waybill;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdateLocalDto {

    @ApiModelProperty(value = "用户userId", required = true)
    private Long userId;

    @ApiModelProperty(value = "用户userName", required = true)
    private String userName;



    @ApiModelProperty("运单ID")
    private Long id;

    @ApiModelProperty(value = "运单类型：1提车运单，2自送运单，3干线运单，4送车运单，5自提运单", required = true)
    private Integer type;

    @ApiModelProperty(value = "承运商ID", required = true)
    private Long carrierId;

    @ApiModelProperty(value = "承运商类型：0承运商，1业务员, 2客户自己", required = true)
    private Integer carrierType;

    @ApiModelProperty(value = "司机ID")
    private Long driverId;

    @ApiModelProperty(value = "备注")
    private String remark;

    private UpdateLocalCarDto carDto;

}
