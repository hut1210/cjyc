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

    @ApiModelProperty(value = "运单类型：1提车运单，2干线运单，3送车运单", required = true)
    private Integer type;

    @ApiModelProperty(value = "承运商ID", required = true)
    private Long carrierId;

    @ApiModelProperty(value = "承运商类型：1干线-个人承运商，2干线-企业承运商，3同城-业务员，4同城-代驾，5同城-拖车，6客户自己", required = true)
    private Integer carrierType;
    @ApiModelProperty(value = "承运商名称", required = true)
    private String carrierName;


    @ApiModelProperty(value = "司机ID")
    private Long driverId;

    @ApiModelProperty(value = "备注")
    private String remark;

    private UpdateLocalCarDto carDto;

}
