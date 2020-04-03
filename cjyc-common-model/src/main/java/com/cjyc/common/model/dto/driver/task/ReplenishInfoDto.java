package com.cjyc.common.model.dto.driver.task;

import com.cjyc.common.model.enums.UserTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
public class ReplenishInfoDto {
    @NotNull(message = "登录人不能为空")
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
    @ApiModelProperty(value = "1：提车拍照，2入库拍照")
    private Integer type = 1;
    @Pattern(regexp = "(^$)|(^\\S{1,20}$)", message = "车牌号格式不正确，请检查")
    @ApiModelProperty(value = "车牌号")
    private String plateNo;
    @Pattern(regexp = "(^$)|(^[0-9a-zA-Z]{17}$)", message = "vin位数不足17位或大于17位")
    @ApiModelProperty(value = "vin码")
    private String vin;
    @ApiModelProperty(value = "提车图片",required = true)
    private List<String> loadPhotoImgs;
    @ApiModelProperty(value = "交车图片",required = true)
    private List<String> unloadPhotoImgs;
}
