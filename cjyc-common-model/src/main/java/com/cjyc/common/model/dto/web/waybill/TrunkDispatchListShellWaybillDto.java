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
public class TrunkDispatchListShellWaybillDto {

    @ApiModelProperty(value = "用户userId", required = true)
    private Long UserId;

    @ApiModelProperty(value = "调度内容", required = true)
    private List<TrunkDispatchListWaybillDto> list;
}
