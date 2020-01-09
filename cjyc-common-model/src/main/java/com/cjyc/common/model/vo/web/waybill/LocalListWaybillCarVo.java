package com.cjyc.common.model.vo.web.waybill;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cjyc.common.model.entity.WaybillCar;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LocalListWaybillCarVo extends WaybillCar {

    @ApiModelProperty(value = "状态")
    private String outterState;
    @ApiModelProperty(value = "提送车业务中心")
    private String localStoreName;
    @ApiModelProperty(value = "承运商ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long carrierId;
    @ApiModelProperty(value = "(carryType)承运类型：1干线-个人承运商，2干线-企业承运商，3同城-业务员，4同城-代驾，5同城-拖车，6客户自己")
    private Integer carrierType;
    @Excel(name = "承运商", orderNum = "12")
    @ApiModelProperty(value = "承运商名称")
    private String carrierName;
    @ApiModelProperty(value = "调度人")
    private String createUser;
    @ApiModelProperty(value = "调度人ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createUserId;
    @ApiModelProperty(value = "运单类型：1提车运单，2干线运单，3送车运单")
    private Integer type;
    @ApiModelProperty(value = "订单编号")
    private String orderNo;
    @ApiModelProperty(value = "品牌")
    private String brand;
    @ApiModelProperty(value = "型号")
    private String model;
    @ApiModelProperty(value = "车牌号")
    private String plateNo;
    @ApiModelProperty(value = "vin码")
    private String vin;
    @ApiModelProperty(value = "是否能动 0-否 1-是")
    private Integer isMove;
    @ApiModelProperty(value = "是否新车 0-否 1-是")
    private Integer isNew;

    @Excel(name = "司机", orderNum = "13")
    @ApiModelProperty(value = "司机名称")
    private String driverName;
    @Excel(name = "电话", orderNum = "14")
    @ApiModelProperty(value = "司机电话")
    private String driverPhone;
    @ApiModelProperty(value = "司机ID(loginId)")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long driverId;
    @Excel(name = "车牌号", orderNum = "15")
    @ApiModelProperty(value = "运力车牌号")
    private String vehiclePlateNo;
}
