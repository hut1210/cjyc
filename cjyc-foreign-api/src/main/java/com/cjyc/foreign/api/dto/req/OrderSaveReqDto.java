package com.cjyc.foreign.api.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Description 下单参数类
 * @Author Liu Xing Xiang
 * @Date 2020/3/11 11:49
 **/
@Data
public class OrderSaveReqDto implements Serializable {
    private static final long serialVersionUID = 8848873980441061943L;
    @NotNull(message = "客户端类型不能为空")
    @ApiModelProperty(value = "1WEB管理后台, 2业务员APP, 4司机APP, 6用户端APP, 7用户端小程序", required = true)
    private int clientId;

    @NotNull(message = "操作人id不能为空")
    @ApiModelProperty(value = "操作人id", required = true)
    private Long loginId;

    @ApiModelProperty(value = "操作人类型")
    private Integer loginType;

    @ApiModelProperty(hidden = true)
    private String loginName;

    @ApiModelProperty(hidden = true)
    private String loginPhone;

    @ApiModelProperty(hidden = true)
    private Integer state;

    @ApiModelProperty(value = "物流券抵消金额")
    private BigDecimal couponOffsetFee;

    @ApiModelProperty(value = "车辆列表")
    private List<@Valid OrderCarSaveDto> orderCarList;

    @ApiModelProperty(value = "订单ID（修改时传）")
    private Long orderId;

    @NotNull
    @ApiModelProperty(value = "1C端 2大客户 3-伙人", required = true)
    private Integer customerType;

    @ApiModelProperty(value = "客户id")
    private Long customerId;

    @NotBlank(message = "客户电话不能为空")
    @Pattern(regexp = "^[1]\\d{10}$", message = "请输入正确的手机号")
    @ApiModelProperty(value = "客户电话", required = true)
    private String customerPhone;

    @NotBlank(message = "客户姓名不能为空")
    @ApiModelProperty(value = "客户姓名", required = true)
    private String customerName;

    @NotBlank(message = "出发城市不能为空")
    @ApiModelProperty(value = "区编号", required = true)
    private String startAreaCode;

    @ApiModelProperty(value = "始发地详细地址")
    private String startAddress;

    @ApiModelProperty(value = "出发地业务中心ID: 0无业务中心，-1有但不经过业务中心，-5用户无主观操作")
    private Long startStoreId;

    @ApiModelProperty(value = "出发地业务中心名称")
    private String startStoreName;

    @NotBlank(message = "出发城市不能为空")
    @ApiModelProperty(value = "区编号", required = true)
    private String endAreaCode;

    @ApiModelProperty(value = "目的地详细地址")
    private String endAddress;

    @ApiModelProperty(value = "目的地业务中心ID: 0无业务中心，-1有但不经过业务中心，-5用户无主观操作")
    private Long endStoreId;

    @ApiModelProperty(value = "目的地业务中心名称")
    private String endStoreName;

    @ApiModelProperty(value = "订单所属业务中心ID")
    private Long inputStoreId;

    @ApiModelProperty(value = "订单所属业务中心名称")
    private String inputStoreName;

    @ApiModelProperty(value = "预计出发时间（提车日期）")
    private Long expectStartDate;

    @ApiModelProperty(value = "预计到达时间")
    private Long expectEndDate;

    @ApiModelProperty(value = "车辆总数")
    private Integer carNum;

    @NotNull(message = "线路ID不能为空")
    @ApiModelProperty(value = "线路ID")
    private Long lineId;

    @ApiModelProperty(value = "线路费用")
    private BigDecimal lineWlFreightFee;

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

    @ApiModelProperty(value = "应收总价：收车后客户应支付平台的费用")
    private BigDecimal totalFee;
}
