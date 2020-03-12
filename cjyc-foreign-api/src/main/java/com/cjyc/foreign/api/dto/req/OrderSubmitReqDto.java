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
public class OrderSubmitReqDto implements Serializable {
    private static final long serialVersionUID = 8848873980441061943L;
    @NotNull(message = "订单来源不能为空")
    @ApiModelProperty(value = "订单来源：1WEB管理后台, 2业务员APP," +
            " 4司机APP, 6用户端APP, 7用户端小程序,9-99车圈(C端客户)", required = true)
    private Integer clientId;

    @NotNull(message = "登录人id不能为空")
    @ApiModelProperty(value = "登录人id", required = true)
    private Long loginId;

    @ApiModelProperty(value = "车辆列表")
    private List<@Valid OrderCarSubmitReqDto> orderCarList;

    @NotBlank(message = "客户电话不能为空")
    @Pattern(regexp = "^[1]\\d{10}$", message = "请输入正确的手机号")
    @ApiModelProperty(value = "客户电话", required = true)
    private String customerPhone;

    @NotBlank(message = "出发城市不能为空")
    @ApiModelProperty(value = "区编号", required = true)
    private String startAreaCode;

    @ApiModelProperty(value = "始发地详细地址")
    private String startAddress;

    @NotBlank(message = "目的地城市不能为空")
    @ApiModelProperty(value = "区编号", required = true)
    private String endAreaCode;

    @ApiModelProperty(value = "目的地详细地址")
    private String endAddress;

    @ApiModelProperty(value = "提车日期")
    private Long expectStartDate;

    @ApiModelProperty(value = "预计到达时间")
    private Long expectEndDate;

    @NotNull(message = "线路ID不能为空")
    @ApiModelProperty(value = "线路ID")
    private Long lineId;

    @ApiModelProperty(value = "线路费用")
    private BigDecimal lineWlFreightFee;

    @ApiModelProperty(value = "提车方式:1 自送，2代驾上门，3拖车上门, 4.物流上门")
    private int pickType;

    @ApiModelProperty(value = "提车联系人")
    private String pickContactName;

    @ApiModelProperty(value = "提车联系人电话")
    private String pickContactPhone;

    @ApiModelProperty(value = "交付方式： 1 自提，2代驾上门，3拖车上门, 4.物流上门")
    private int backType;

    @ApiModelProperty(value = "交付联系人")
    private String backContactName;

    @ApiModelProperty(value = "交付联系人电话")
    private String backContactPhone;

    @ApiModelProperty(value = "支付方式 0-到付，1-预付，2账期")
    private Integer payType;
}
