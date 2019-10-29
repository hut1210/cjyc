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

    @ApiModelProperty(value = "运单ID",required = true)
    private Long taskCarId;

    @ApiModelProperty(value = "图片地址，逗号分隔")
    private String loadPhotoImg;

}
