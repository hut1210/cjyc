package com.cjyc.common.model.dto.web.order;

import com.cjyc.common.model.constant.ArgsConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Data
public class BatchImportOrderDto {


    @ApiModelProperty(value = "操作人(不用传)")
    private String loginName;
    @ApiModelProperty(value = "区编号")
    private String startAreaCode;
    @ApiModelProperty(value = "始发地详细地址")
    private String startAddress;
    @ApiModelProperty(value = "出发地业务中心ID: -1不经过业务中心")
    private Long startStoreId;
    @ApiModelProperty(value = "出发地业务中心名称")
    private String startStoreName;

    @ApiModelProperty(value = "区编号")
    private String endAreaCode;
    @ApiModelProperty(value = "目的地详细地址")
    private String endAddress;
    @ApiModelProperty(value = "目的地业务中心ID: -1不经过业务中心")
    private Long endStoreId;
    @ApiModelProperty(value = "目的地业务中心名称")
    private String endStoreName;


    @ApiModelProperty(value = "期望提车日期")
    private String expectStartDate;
    @ApiModelProperty(value = "期望到达日期")
    private String expectEndDate;
    @ApiModelProperty(value = "车辆总数")
    private Integer carNum;
    @ApiModelProperty(value = "线路ID")
    private Long lineId;
    @ApiModelProperty(value = "提车方式:1 自送，2代驾上门，3拖车上门, 4.物流上门")
    private int pickType;
    @ApiModelProperty(value = "发车联系人")
    private String pickContactName;
    @ApiModelProperty(value = "发车联系人电话")
    private String pickContactPhone;
    @ApiModelProperty(value = "送车方式： 1 自提，2代驾上门，3拖车上门, 4.物流上门")
    private int backType;
    @ApiModelProperty(value = "收车联系人")
    private String backContactName;
    @ApiModelProperty(value = "收车联系人电话")
    private String backContactPhone;
    @ApiModelProperty(value = "是否开票：0否（默认根据设置），1是")
    private int invoiceFlag;
    @ApiModelProperty(value = "发票类型：0无， 1-普通(个人) ，2增值普票(企业) ，3增值专用发票")
    private int invoiceType;
    @ApiModelProperty(value = "合同ID")
    private Long customerContractId;
    @ApiModelProperty(value = "加急")
    private Integer hurryDays;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "创建人：客户/业务员")
    private String createUserName;
    @ApiModelProperty(value = "创建人类型：0客户，1业务员")
    private Long createUserId;
    @ApiModelProperty(value = "支付方式 0-到付，1-预付，2账期")
    private Integer payType;
    @ApiModelProperty(value = "优惠券id")
    private Long couponSendId;
    @ApiModelProperty(value = "物流券抵消金额")
    @Digits(integer = ArgsConstant.INT_MAX, fraction = ArgsConstant.FRACTION_MAX, message = "金额整数最多8位，小数最多2位")
    @DecimalMin(value = ArgsConstant.DECIMAL_ZERO, message = "金额不能小于0")
    private BigDecimal couponOffsetFee;
    @ApiModelProperty(value = "应收总价：收车后客户应支付平台的费用")
    @Digits(integer = ArgsConstant.INT_MAX, fraction = ArgsConstant.FRACTION_MAX, message = "金额整数最多8位，小数最多2位")
    @DecimalMin(value = ArgsConstant.DECIMAL_ZERO, message = "金额不能小于0")
    private BigDecimal totalFee;

    @ApiModelProperty(value = "状态（不需要传）")
    private Integer state;
}
