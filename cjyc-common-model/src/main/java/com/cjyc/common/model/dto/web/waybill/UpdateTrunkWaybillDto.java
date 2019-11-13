package com.cjyc.common.model.dto.web.waybill;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.List;
@Data
public class UpdateTrunkWaybillDto {
    @ApiModelProperty(value = "用户userId", required = true)
    private Long userId;
    @ApiModelProperty(value = "业务中心ID", required = true)
    private Long storeId;

    @ApiModelProperty(value = "运单ID（修改时传）", required = true)
    private String id;

    @ApiModelProperty(value = "指导线路", required = true)
    private String guideLine;

    @ApiModelProperty(value = "运单总运费", required = true)
    private BigDecimal freightFee;

    @ApiModelProperty(value = "承运商ID", required = true)
    private Long carrierId;

    @ApiModelProperty(value = "司机ID")
    private Long driverId;

    @ApiModelProperty(value = "运费是否固定（包板）0否，1是", required = true)
    private Boolean fixedFreightFee;

    @ApiModelProperty(value = "备注")
    private String remark;

    @NotEmpty
    @ApiModelProperty(value = "调度内容", required = true)
    private List<UpdateTrunkWaybillCarDto> list;
}
