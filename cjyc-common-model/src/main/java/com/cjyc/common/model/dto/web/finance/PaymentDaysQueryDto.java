package com.cjyc.common.model.dto.web.finance;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 账期应收账款列表查询条件
 *
 * @Author:RenPL
 * @Date:2020/3/19 15:33
 */
@Data
public class PaymentDaysQueryDto extends BasePageDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "车辆编号")
    private String no;

    @ApiModelProperty(value = "vin码")
    private String vin;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "交付日期开始时间")
    private Long completeStartTime;

    @ApiModelProperty(value = "交付日期结束时间")
    private Long completeEndTime;

    @ApiModelProperty(value = "始发地省编号")
    private String startProvinceCode;

    @ApiModelProperty(value = "始发地市编号")
    private String startCityCode;

    @ApiModelProperty(value = "始发地区编号")
    private String startAreaCode;

    @ApiModelProperty(value = "目的地省编号")
    private String endProvinceCode;

    @ApiModelProperty(value = "目的地市编号")
    private String endCityCode;

    @ApiModelProperty(value = "目的地区编号")
    private String endAreaCode;

    @ApiModelProperty(value = "客户账号")
    private String contactPhone;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "结算类型")
    private Integer payMode;

    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "车系")
    private String model;

    @ApiModelProperty(value = "大区")
    private String area;

    @ApiModelProperty(value = "业务中心")
    private String inputStoreName;

    @ApiModelProperty(value = "客户类型")
    private Integer type;

//    @ApiModelProperty(value = "收款状态")
//    private String state;
//
//    @ApiModelProperty(value = "收款开始时间")
//    private Long receiveStartTime;
//
//    @ApiModelProperty(value = "收款结束时间")
//    private Long receiveEndTime;
}
