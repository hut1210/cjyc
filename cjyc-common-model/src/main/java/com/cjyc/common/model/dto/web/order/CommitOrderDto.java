package com.cjyc.common.model.dto.web.order;

import com.cjyc.common.model.constant.ArgsConstant;
import com.cjyc.common.model.dto.BaseLogin2Dto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author JPG
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class CommitOrderDto extends BaseLogin2Dto {
    @ApiModelProperty(hidden = true)
    private Integer state;
    @ApiModelProperty(hidden = true)
    private String createUserName;
    @ApiModelProperty(hidden = true)
    private Long createUserId;

    @ApiModelProperty(value = "用户不存在，是否创建用户")
    private Boolean createCustomerFlag = false;

    @Valid
    @ApiModelProperty(value = "车辆列表")
    private List<CommitOrderCarDto> orderCarList;

    @ApiModelProperty(value = "订单ID(修改时传)")
    private Long orderId;
    @NotNull
    @ApiModelProperty(value = "1C端 2大客户 3-伙人", required = true)
    private Integer customerType;
    @ApiModelProperty(value = "客户id")
    private Long customerId;
    @ApiModelProperty(hidden = true)
    private Boolean releaseCarFlag;
    //@NotBlank(message = "客户电话不能为空")
    @Pattern(regexp = "(^$)|(^[1]\\d{10}$)", message = "手机号格式不正确")
    @ApiModelProperty(value = "客户电话", required = true)
    private String customerPhone;
    //@NotBlank(message = "客户姓名不能为空")
    @ApiModelProperty(value = "客户姓名", required = true)
    private String customerName;
    @NotBlank(message = "出发城市不能为空")
    @ApiModelProperty(value = "区编号", required = true)
    private String startAreaCode;
    @ApiModelProperty(value = "始发地详细地址")
    private String startAddress;
    @ApiModelProperty(value = "出发地业务中心ID:  0无业务中心，-5用户无主观操作")
    private Long startStoreId;
    @ApiModelProperty(value = "出发地业务中心名称")
    private String startStoreName;

    @NotBlank(message = "目的城市不能为空")
    @ApiModelProperty(value = "区编号", required = true)
    private String endAreaCode;
    @ApiModelProperty(value = "目的地详细地址")
    private String endAddress;
    @ApiModelProperty(value = "目的地业务中心ID:  0无业务中心，-5用户无主观操作")
    private Long endStoreId;
    @ApiModelProperty(value = "目的地业务中心名称")
    private String endStoreName;

    @ApiModelProperty(value = "订单所属业务中心ID")
    private Long inputStoreId;
    @ApiModelProperty(value = "订单所属业务中心名称")
    private String inputStoreName;
    @ApiModelProperty(value = "期望提车日期")
    private Long expectStartDate;
    @ApiModelProperty(value = "期望到达日期")
    private Long expectEndDate;
    @ApiModelProperty(value = "车辆总数")
    private Integer carNum;
    @NotNull(message = "线路ID不能为空")
    @ApiModelProperty(value = "线路ID", required = true)
    private Long lineId;
    @NotNull(message = "提车方式不能为空")
    @ApiModelProperty(value = "提车方式:1 自送，2代驾上门，3拖车上门, 4.物流上门, 9我去提车", required = true)
    private Integer pickType;
    @ApiModelProperty(value = "是否调度", hidden = true)
    private boolean isDispatch = false;

    @NotBlank(message = "发车联系人不能为空")
    @ApiModelProperty(value = "发车联系人", required = true)
    private String pickContactName;
    @NotBlank(message = "发车联系人电话不能为空")
    @Pattern(regexp = "(^[1]\\d{10}$)", message = "发车人手机号格式不正确")
    @ApiModelProperty(value = "发车联系人电话", required = true)
    private String pickContactPhone;
    @ApiModelProperty(value = "送车方式： 1 自提，2代驾上门，3拖车上门, 4.物流上门", required = true)
    private Integer backType;
    @NotBlank(message = "收车联系人电话不能为空")
    @ApiModelProperty(value = "收车联系人", required = true)
    private String backContactName;
    @NotBlank(message = "收车联系人电话不能为空")
    @Pattern(regexp = "(^[1]\\d{10}$)", message = "收车人手机号格式不正确")
    @ApiModelProperty(value = "收车联系人电话", required = true)
    private String backContactPhone;
    @ApiModelProperty(value = "是否开票：0否（默认根据设置），1是")
    private Integer invoiceFlag;
    @ApiModelProperty(value = "发票类型：0无， 1-普通(个人) ，2增值普票(企业) ，3增值专用发票")
    private Integer invoiceType;
    @ApiModelProperty(value = "合同ID")
    private Long customerContractId;
    @ApiModelProperty(value = "加急")
    private Integer hurryDays;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "支付方式 0-到付，1-预付，2账期", required = true)
    private Integer payType = 0;
    @ApiModelProperty(value = "优惠券id")
    private Long couponSendId;
    @Digits(integer = ArgsConstant.INT_MAX, fraction = ArgsConstant.FRACTION_MAX, message = "金额整数最多8位，小数最多2位")
    @DecimalMin(value = ArgsConstant.DECIMAL_ZERO, message = "金额不能小于0")
    @ApiModelProperty(value = "物流券抵消金额")
    private BigDecimal couponOffsetFee;
    @ApiModelProperty(value = "应收总价：收车后客户应支付平台的费用", required = true)
    @Digits(integer = ArgsConstant.INT_MAX, fraction = ArgsConstant.FRACTION_MAX, message = "金额整数最多8位，小数最多2位")
    @DecimalMin(value = ArgsConstant.DECIMAL_ZERO, message = "金额不能小于0")
    private BigDecimal totalFee;




}
