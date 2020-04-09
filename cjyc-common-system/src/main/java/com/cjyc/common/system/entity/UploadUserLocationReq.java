package com.cjyc.common.system.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description 用户位置信息上传请求类
 * @Author Liu Xing Xiang
 * @Date 2020/4/9 10:36
 **/
@Data
public class UploadUserLocationReq implements Serializable {
    private static final long serialVersionUID = 4591226258719798761L;
    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "终端定位时间")
    private String gpsTime;

    @ApiModelProperty(value = "发送时间")
    private String sendTime;

    @ApiModelProperty(value = "纬度")
    private Double lat;

    @ApiModelProperty(value = "经度")
    private Double lng;

    @ApiModelProperty(value = "坐标系统类型 bd09:BD-09(百度地图采用坐标系);" +
            "gcj02:GCJ-02(高德地图采用坐标系);wgs84:WGS-84(GPS设备常用的坐标系)")
    private String coordinateType;
}
