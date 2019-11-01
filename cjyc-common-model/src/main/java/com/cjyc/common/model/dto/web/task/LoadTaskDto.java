package com.cjyc.common.model.dto.web.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class LoadTaskDto {
    @ApiModelProperty(value = "用户userId",required = true)
    private Long userId;

    @ApiModelProperty(value = "用户userName")
    private String userName;

    @ApiModelProperty(value = "装车方式：1客户家提车，2业务中心提车，3司机交接提车",required = true)
    private Long loadType;

    @ApiModelProperty(value = "任务车辆ID",required = true)
    private Long taskCarId;

    @ApiModelProperty(value = "图片地址，逗号分隔")
    private String loadPhotoImg;

}
