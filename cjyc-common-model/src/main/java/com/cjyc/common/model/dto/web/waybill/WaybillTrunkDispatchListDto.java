package com.cjyc.common.model.dto.web.waybill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class WaybillTrunkDispatchListDto {

    @ApiModelProperty(value = "指导线路", required = true)
    private String guideLine;

    @ApiModelProperty(value = "运单总运费", required = true)
    private BigDecimal freightFee;

    @ApiModelProperty(value = "运费是否固定（包板）0否，1是", required = true)
    private Boolean fixedFreightFee;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "调度内容", required = true)
    private List<WaybillTrunkDispatchDto> list;
}
