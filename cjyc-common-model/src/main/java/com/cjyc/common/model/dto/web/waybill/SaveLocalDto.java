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
public class SaveLocalDto {

    @ApiModelProperty(value = "用户Id")
    private Long loginId;

    @ApiModelProperty(value = "用户roleId")
    private Long roleId;


    @ApiModelProperty(value = "用户userName", required = true)
    private String loginName;

    @ApiModelProperty(value = "运单类型：1提车运单，2干线运单，3送车运单", required = true)
    private Integer type;


    @ApiModelProperty(value = "调度内容", required = true)
    private List<SaveLocalWaybillDto> list;
}
