package com.cjyc.common.model.dto.web.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author JPG
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class CommitOrderDto {

    @NotNull(message = "clientId不能为空")
    @ApiModelProperty(value = "1WEB管理后台, 2业务员APP, 4司机APP, 6用户端APP, 7用户端小程序", required = true)
    private int clientId;
    @NotNull(message = "loginId不能为空")
    @ApiModelProperty(value = "操作人id", required = true)
    private Long loginId;
    @ApiModelProperty(hidden = true)
    private String loginName;
    @ApiModelProperty(hidden = true)
    private String loginPhone;
    @ApiModelProperty(hidden = true)
    private Integer state;
    @ApiModelProperty(hidden = true)
    private String createUserName;
    @ApiModelProperty(hidden = true)
    private Long createUserId;

    @NotNull(message = "createCustomerFlag不能为空")
    @ApiModelProperty(value = "用户不存在，是否创建用户",required = true)
    private Boolean createCustomerFlag;

    @NotEmpty(message = "车辆信息不能为空")
    @ApiModelProperty(value = "车辆列表")
    private List<CommitOrderCarDto> orderCarList;

    @ApiModelProperty(value = "订单ID(修改时传)")
    private Long orderId;
    @ApiModelProperty(value = "1C端 2大客户 3-伙人", required = true)
    private int customerType;
    @ApiModelProperty(value = "客户id")
    private Long customerId;
    @NotBlank(message = "客户电话不能为空")
    @ApiModelProperty(value = "客户电话", required = true)
    private String customerPhone;
    @NotBlank(message = "客户姓名不能为空")
    @ApiModelProperty(value = "客户姓名", required = true)
    private String customerName;
    @NotBlank(message = "出发城市不能为空")
    @ApiModelProperty(value = "区编号", required = true)
    private String startAreaCode;
    @NotBlank(message = "出发不能为空地址")
    @ApiModelProperty(value = "始发地详细地址", required = true)
    private String startAddress;
    @NotNull(message = "startStoreId不能为空")
    @ApiModelProperty(value = "出发地业务中心ID:  0无业务中心，-1有但不经过业务中心，-5用户无主观操作")
    private Long startStoreId;
    @ApiModelProperty(value = "出发地业务中心名称")
    private String startStoreName;

    @NotBlank(message = "目的城市不能为空")
    @ApiModelProperty(value = "区编号", required = true)
    private String endAreaCode;
    @NotBlank(message = "目的地址不能为空")
    @ApiModelProperty(value = "目的地详细地址", required = true)
    private String endAddress;
    @NotNull(message = "endStoreId不能为空")
    @ApiModelProperty(value = "目的地业务中心ID:  0无业务中心，-1有但不经过业务中心，-5用户无主观操作")
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
    @ApiModelProperty(value = "提车方式:1 自送，2代驾上门，3拖车上门, 4.物流上门", required = true)
    private int pickType;
    @NotBlank(message = "发车联系人不能为空")
    @ApiModelProperty(value = "发车联系人", required = true)
    private String pickContactName;
    @NotBlank(message = "发车联系人电话不能为空")
    @ApiModelProperty(value = "发车联系人电话", required = true)
    private String pickContactPhone;
    @ApiModelProperty(value = "送车方式： 1 自提，2代驾上门，3拖车上门, 4.物流上门", required = true)
    private int backType;
    @NotBlank(message = "收车联系人电话不能为空")
    @ApiModelProperty(value = "收车联系人", required = true)
    private String backContactName;
    @NotBlank(message = "收车联系人电话不能为空")
    @ApiModelProperty(value = "收车联系人电话", required = true)
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
    @NotNull(message = "支付方式不能为空")
    @ApiModelProperty(value = "支付方式 0-到付，1-预付，2账期", required = true)
    private Integer payType;
    @ApiModelProperty(value = "优惠券id")
    private Long couponSendId;
    @ApiModelProperty(value = "物流券抵消金额")
    private BigDecimal couponOffsetFee;
    @ApiModelProperty(value = "应收总价：收车后客户应支付平台的费用", required = true)
    private BigDecimal totalFee;




}
