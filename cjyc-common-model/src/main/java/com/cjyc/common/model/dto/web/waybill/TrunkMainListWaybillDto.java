package com.cjyc.common.model.dto.web.waybill;

import com.cjyc.common.model.dto.web.PageWebDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TrunkMainListWaybillDto extends PageWebDto {
    @ApiModelProperty(value = "运单单号", required = true)
    private String waybillNo;

    @ApiModelProperty(value = "状态")
    private String state;
    @ApiModelProperty(value = "指导线路")
    private String guideLine;
    @ApiModelProperty(value = "承运商名称")
    private String carrierName;
    @ApiModelProperty(value = "起始创建时间")
    private Long beginCreateTime;
    @ApiModelProperty(value = "截止创建时间")
    private Long endCreateTime;

}
