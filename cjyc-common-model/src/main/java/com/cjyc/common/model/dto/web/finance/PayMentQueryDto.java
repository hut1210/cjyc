package com.cjyc.common.model.dto.web.finance;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Hut
 * @Date: 2019/12/11 10:02
 */
@Data
public class PayMentQueryDto extends BasePageDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "运单单号")
    private String waybillNo;

    @ApiModelProperty(value = "交付日期")
    private Long completeStartTime;

    @ApiModelProperty(value = "交付日期")
    private Long completeEndTime;

    @ApiModelProperty(value = "发票号")
    private String invoiceNo;

    @ApiModelProperty(value = "支付流水号")
    private String pingPayNo;

    @ApiModelProperty(value = "承运商")
    private String carrierName;

    @ApiModelProperty(value = "司机")
    private String driverName;

    @ApiModelProperty(value = "司机电话")
    private String driverPhone;

    @ApiModelProperty(value = "车牌号")
    private String vehiclePlateNo;

    @ApiModelProperty(value = "运单类型")
    private Integer waybillType;

    @ApiModelProperty(value = "付款状态 0未付款 2已付款")
    private Integer state;

    @ApiModelProperty(value = "付款开始时间")
    private Long payStartTime;

    @ApiModelProperty(value = "付款结束时间")
    private Long payEndTime;

    @ApiModelProperty(value = "付款操作人")
    private String operator;

    @ApiModelProperty(value = "付款开始时间")
    private Long payStartTime;

    @ApiModelProperty(value = "付款结束时间")
    private Long payEndTime;

}


