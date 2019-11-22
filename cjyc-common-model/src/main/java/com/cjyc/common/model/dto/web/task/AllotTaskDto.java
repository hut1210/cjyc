package com.cjyc.common.model.dto.web.task;

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
public class AllotTaskDto {

    @ApiModelProperty(value = "用户Id",required = true)
    private Long loginId;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "运单ID",required = true)
    private Long waybillId;

    @ApiModelProperty(value = "司机ID",required = true)
    private Long driverId;

    @ApiModelProperty(value = "运单车辆ID",required = true)
    private List<Long> waybillCarIdList;

}
