package com.cjyc.common.model.dto.driver.task;

import com.cjyc.common.model.enums.UserTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class ReplenishInfoDto {
    private Long loginId;
    @ApiModelProperty(value = "用户名(不用传)")
    private String loginName;
    @ApiModelProperty(value = "用户类型(不用传)")
    private UserTypeEnum loginType;
    @ApiModelProperty(value = "运单车辆ID")
    private Long waybillCarId;
    @ApiModelProperty(value = "品牌")
    private String brand;
    @ApiModelProperty(value = "型号")
    private String model;
    @ApiModelProperty(value = "车牌号")
    private String plateNo;
    @ApiModelProperty(value = "vin码",required = true)
    private String vin;
    @NotEmpty(message = "图片不能为空")
    @ApiModelProperty(value = "提车图片",required = true)
    private List<String> loadPhotoImgs;
}
