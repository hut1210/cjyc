package com.cjyc.common.model.dto.driver.task;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReplenishInfoDto {
    private Long loginId;

    private String loginName;

    private Long waybillCarId;
    @ApiModelProperty(value = "品牌",required = true)
    private String brand;
    @ApiModelProperty(value = "型号",required = true)
    private String model;
    @ApiModelProperty(value = "车牌号")
    private String plateNo;
    @ApiModelProperty(value = "vin码")
    private String vin;
    @ApiModelProperty(value = "提车图片")
    private String loadPhotoImg;
}
