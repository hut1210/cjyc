package com.cjyc.common.model.dto.web.waybill;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class storeListDto extends BasePageDto {

    @ApiModelProperty(value = "业务中心ID")
    private Long storeId;

    @ApiModelProperty(value = "记录类型：1入库，2出库")
    private Integer type;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "车辆编号")
    private String orderCarNo;

    @ApiModelProperty(value = "车架号")
    private String vin;

    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "型号")
    private String model;

    @ApiModelProperty(value = "运费")
    private BigDecimal freight;

    @ApiModelProperty(value = "承运方式:1干线-个人承运商，2干线-企业承运商，3同城-业务员，4同城-代驾，5同城-拖车，6客户自己")
    private Integer carryType;

    @ApiModelProperty(value = "承运商ID")
    private Long carrierId;

    @ApiModelProperty(value = "承运商")
    private String carrier;

    @ApiModelProperty(value = "司机")
    private String driver;

    @ApiModelProperty(value = "司机ID")
    private Long driverId;

    @ApiModelProperty(value = "司机手机号")
    private String drvierPhone;

    @ApiModelProperty(value = "司机车牌号")
    private String vehiclePlateNo;

    @ApiModelProperty(value = "创建时间")
    private Long startCreateTime;
    @ApiModelProperty(value = "创建时间")
    private Long endCreateTime;


    @ApiModelProperty(value = "创建人ID")
    private Long createUserId;

    @ApiModelProperty(value = "创建人")
    private String createUser;

}
