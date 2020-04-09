package com.cjyc.common.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description APP位置信息参数
 * @Author Liu Xing Xiang
 * @Date 2020/4/9 8:33
 **/
@Data
public class LocationInfoDto implements Serializable {
    private static final long serialVersionUID = 1994023761991340031L;
    @ApiModelProperty(value = "用户登录id")
    @NotNull(message = "用户登录id不能为空")
    private Long loginId;

    @ApiModelProperty(value = "终端定位时间")
    @NotNull(message = "终端定位时间不能为空")
    private Long gpsTime;

    @ApiModelProperty(value = "发送时间")
    @NotNull(message = "发送时间不能为空")
    private Long sendTime;

    @ApiModelProperty(value = "纬度")
    @NotNull(message = "纬度不能为空")
    private Double lat;

    @ApiModelProperty(value = "经度")
    @NotNull(message = "经度不能为空")
    private Double lng;

    @ApiModelProperty(value = "坐标系统类型 bd09:BD-09(百度地图采用坐标系);" +
            "gcj02:GCJ-02(高德地图采用坐标系);wgs84:WGS-84(GPS设备常用的坐标系)")
    @NotBlank(message = "坐标系统类型不能为空")
    private String coordinateType;

}
