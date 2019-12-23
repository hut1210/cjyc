package com.cjyc.common.model.vo.web.waybill;

import com.cjyc.common.model.entity.Waybill;
import com.cjyc.common.model.util.BigDecimalSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class WaybillVo  extends Waybill {

    @ApiModelProperty("承运商")
    private String carrier;
    @ApiModelProperty("司机名称")
    private String driverName;
    @ApiModelProperty("司机电话")
    private String driverPhone;
    @ApiModelProperty("身份证号")
    private String idCard;
    @ApiModelProperty("车牌号")
    private String vehiclePlateNo;
    @ApiModelProperty("车辆列表")
    private List<WaybillCarVo> list;



}
