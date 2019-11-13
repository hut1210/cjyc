package com.cjyc.common.model.vo.web.order;

import com.cjyc.common.model.entity.OrderCar;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel
public class OrderCarWaitDispatchVo extends OrderCar {


    @ApiModelProperty(value = "始发业务中心地址")
    private String startStoreFullAddress;

    @ApiModelProperty(value = "目的业务中心地址")
    private String endStoreFullAddress;

    @ApiModelProperty(value = "目的业务中心地址")
    private String nowCityName;




    @ApiModelProperty(value = "订单所属业务中心ID")
    private Long inputStoreId;

    @ApiModelProperty(value = "订单所属业务中心名称")
    private String inputStoreName;

    @ApiModelProperty(value = "省")
    private String startProvince;

    @ApiModelProperty(value = "省编号")
    private String startProvinceCode;

    @ApiModelProperty(value = "市")
    private String startCity;

    @ApiModelProperty(value = "市编号")
    private String startCityCode;

    @ApiModelProperty(value = "区")
    private String startArea;

    @ApiModelProperty(value = "区编号")
    private String startAreaCode;

    @ApiModelProperty(value = "出发地详细地址")
    private String startAddress;

    @ApiModelProperty(value = "出发地经度")
    private String startLng;

    @ApiModelProperty(value = "出发地纬度")
    private String startLat;

    @ApiModelProperty(value = "出发地业务中心ID: -1不经过业务中心")
    private Long startStoreId;

    @ApiModelProperty(value = "出发地业务中心名称")
    private String startStoreName;
    @ApiModelProperty(value = "出发地业务所属中心名称")
    private Long startBelongStoreId;

    @ApiModelProperty(value = "省")
    private String endProvince;

    @ApiModelProperty(value = "省编号")
    private String endProvinceCode;

    @ApiModelProperty(value = "市")
    private String endCity;

    @ApiModelProperty(value = "市编号")
    private String endCityCode;

    @ApiModelProperty(value = "区")
    private String endArea;

    @ApiModelProperty(value = "区编号")
    private String endAreaCode;

    @ApiModelProperty(value = "目的地详细地址")
    private String endAddress;

    @ApiModelProperty(value = "目的地经度")
    private String endLng;

    @ApiModelProperty(value = "目的地纬度")
    private String endLat;

    @ApiModelProperty(value = "目的地业务中心ID: -1不经过业务中心")
    private Long endStoreId;

    @ApiModelProperty(value = "目的地业务中心名称")
    private String endStoreName;

    private Long endBelongStoreId;

    @ApiModelProperty(value = "预计出发时间（提车日期）")
    private Long expectStartDate;

    @ApiModelProperty(value = "预计到达时间")
    private Long expectEndDate;

    @ApiModelProperty(value = "车辆总数")
    private Integer carNum;

    @ApiModelProperty(value = "线路ID")
    private Long lineId;

    @ApiModelProperty(value = "提车方式:1 自送，2代驾上门，3拖车上门，4物流上门")
    private Integer pickType;

    @ApiModelProperty(value = "发车人")
    private String pickContactName;

    @ApiModelProperty(value = "发车人联系方式")
    private String pickContactPhone;

    @ApiModelProperty(value = "送车方式： 1 自提，2代驾上门，3拖车上门，4物流上门")
    private Integer backType;

    @ApiModelProperty(value = "收车人")
    private String backContactName;

    @ApiModelProperty(value = "收车人联系方式")
    private String backContactPhone;



}
