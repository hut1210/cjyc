package com.cjyc.common.model.dto.web.waybill;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TrunkMainListWaybillDto extends BasePageDto {

    private Long loginId;
    private Long roleId;

    @ApiModelProperty(value = "运单单号", required = true)
    private String waybillNo;
    @ApiModelProperty(value = "指导线路")
    private String guideLine;
    @ApiModelProperty(value = "承运商名称")
    private String carrierName;
    @ApiModelProperty(value = "起始创建时间")
    private Long beginCreateTime;
    @ApiModelProperty(value = "截止创建时间")
    private Long endCreateTime;

}
