package com.cjyc.common.model.dto.driver.task;

import com.cjyc.common.model.enums.UserTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class PickLoadDto {

    @ApiModelProperty(value = "登录用户ID",required = true)
    private Long loginId;
    @ApiModelProperty(hidden = true)
    private String loginName;
    @ApiModelProperty(hidden = true)
    private String loginPhone;
    @ApiModelProperty(hidden = true)
    private UserTypeEnum loginType;
    @NotNull(message = "运单ID")
    @ApiModelProperty(value = "运单车辆ID")
    private Long taskCarId;
    @ApiModelProperty(value = "品牌")
    private String brand;
    @ApiModelProperty(value = "型号")
    private String model;
    @ApiModelProperty(value = "车牌号")
    private String plateNo;
    @NotBlank(message = "车架号不能为空")
    @ApiModelProperty(value = "vin码",required = true)
    private String vin;
    @NotEmpty(message = "图片不能为空")
    @ApiModelProperty(value = "提车图片",required = true)
    private List<String> loadPhotoImgs;
}
