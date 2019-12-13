package com.cjyc.common.model.dto.web.waybill;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UpdateLocalDto {

    @NotNull(message = "loginId不能为空")
    @ApiModelProperty(value = "用户Id", required = true)
    private Long loginId;
    @ApiModelProperty(hidden = true)
    private String loginName;

    @NotNull(message = "id不能为空")
    @ApiModelProperty(value = "运单ID", required = true)
    private Long id;
    @NotNull(message = "type不能为空")
    @ApiModelProperty(value = "运单类型：1提车运单，2干线运单，3送车运单", required = true)
    private Integer type;
    @NotNull(message = "carrierId不能为空")
    @ApiModelProperty(value = "承运商ID", required = true)
    private Long carrierId;
    @NotNull(message = "carrierType不能为空")
    @ApiModelProperty(value = "承运商类型：1干线-个人承运商，2干线-企业承运商，3同城-业务员，4同城-代驾，5同城-拖车，6客户自己", required = true)
    private Integer carrierType;
    @NotBlank(message = "carrierName不能为空")
    @ApiModelProperty(value = "承运商名称", required = true)
    private String carrierName;

    @ApiModelProperty(value = "司机ID")
    private Long driverId;
    @ApiModelProperty(value = "备注")
    private String remark;

    @NotEmpty(message = "carDto不能为空")
    @ApiModelProperty(value = "车辆信息", required = true)
    private UpdateLocalCarDto carDto;

}
