package com.cjyc.foreign.api.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
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
public class OrderSubmitReqDto implements Serializable {
    private static final long serialVersionUID = 8848873980441061943L;
    @ApiModelProperty(value = "操作人id", hidden = true)
    private Long loginId;

    @NotNull(message = "订单来源不能为空")
    @ApiModelProperty(value = "订单来源默认传9：99车圈(C端客户)", required = true)
    private Integer clientId;

    @NotBlank(message = "客户电话不能为空")
    @Pattern(regexp = "^[1]\\d{10}$", message = "请输入正确的手机号")
    @ApiModelProperty(value = "客户电话默认传16000000001", required = true)
    private String customerPhone;

    @NotBlank(message = "出发城市不能为空")
    @ApiModelProperty(value = "出发城市区编号", required = true)
    private String startAreaCode;

    @NotBlank(message = "始发地详细地址不能为空")
    @ApiModelProperty(value = "始发地详细地址", required = true)
    private String startAddress;

    @NotBlank(message = "目的地城市不能为空")
    @ApiModelProperty(value = "目的地城市区编号", required = true)
    private String endAreaCode;

    @NotBlank(message = "目的地详细地址不能为空")
    @ApiModelProperty(value = "目的地详细地址", required = true)
    private String endAddress;

    @NotNull(message = "提车日期不能为空")
    @ApiModelProperty(value = "提车日期", required = true)
    private Long expectStartDate;

    @NotNull(message = "线路ID不能为空")
    @ApiModelProperty(value = "线路ID", required = true)
    private Long lineId;

    @NotNull(message = "提车方式不能为空")
    @ApiModelProperty(value = "提车方式:1 自送，2代驾上门，3拖车上门, 4.物流上门", required = true)
    private Integer pickType;

    @NotBlank(message = "提车联系人不能为空")
    @ApiModelProperty(value = "提车联系人", required = true)
    private String pickContactName;

    @NotBlank(message = "提车联系人电话不能为空")
    @Pattern(regexp = "^[1]\\d{10}$", message = "请输入正确的手机号")
    @ApiModelProperty(value = "提车联系人电话", required = true)
    private String pickContactPhone;

    @NotNull(message = "交付方式不能为空")
    @ApiModelProperty(value = "交付方式： 1 自提，2代驾上门，3拖车上门, 4.物流上门", required = true)
    private Integer backType;

    @NotBlank(message = "交付联系人不能为空")
    @ApiModelProperty(value = "交付联系人", required = true)
    private String backContactName;

    @NotBlank(message = "交付联系人电话不能为空")
    @Pattern(regexp = "^[1]\\d{10}$", message = "请输入正确的手机号")
    @ApiModelProperty(value = "交付联系人电话", required = true)
    private String backContactPhone;

    @NotNull(message = "支付方式不能为空")
    @ApiModelProperty(value = "支付方式：0到付(默认)", required = true)
    private Integer payType;

    @NotNull(message = "线路费用不能为空")
    @ApiModelProperty(value = "线路费用/元", required = true)
    private BigDecimal lineWlFreightFee;

    @ApiModelProperty(value = "订单总价/元",hidden = true)
    private BigDecimal totalFee;

    @NotEmpty(message = "车辆信息不能为空")
    @ApiModelProperty(value = "车辆列表", required = true)
    private List<@Valid OrderCarSubmitReqDto> orderCarList;
}
