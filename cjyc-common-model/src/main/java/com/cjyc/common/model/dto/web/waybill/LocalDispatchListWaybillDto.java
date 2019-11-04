package com.cjyc.common.model.dto.web.waybill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class LocalDispatchListWaybillDto {

    @ApiModelProperty(value = "用户userId", required = true)
    private Long userId;

    @ApiModelProperty(value = "用户userName", required = true)
    private String userName;

    @ApiModelProperty(value = "运单类型：1提车运单，2自送运单，3干线运单，4送车运单，5自提运单", required = true)
    private Integer type;

    @ApiModelProperty(value = "调度内容", required = true)
    private List<LocalDispatchWaybillDto> list;
}
