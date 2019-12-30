package com.cjyc.common.model.dto.customer.invoice;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

/**
 * @Description 发票新增新增实体
 * @Author LiuXingXiang
 * @Date 2019/11/2 14:05
 **/
@Data
public class CustomerInvoiceAddDto implements Serializable {
    private static final long serialVersionUID = -1287100314798602403L;
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "客户ID",required = true)
    @NotNull(message = "客户ID不能为空")
    private Long loginId;

    @ApiModelProperty(value = "发票类型 1-普通(个人) ，2-增值普票(企业) ，3-增值专用发票'",required = true)
    @NotNull(message = "发票类型不能为空")
    @Pattern(regexp = "(1|2|3)",message = "发票类型只能是1,2,3中的一位数")
    private String type;

    @ApiModelProperty(value = "发票抬头")
    private String title;

    @ApiModelProperty(value = "姓名",required = true)
    @NotBlank(message = "客户名称不能为空")
    private String name;

    @ApiModelProperty(value = "纳税人识别号",required = true)
    @NotBlank(message = "纳税人识别号不能为空")
    private String taxCode;

    @ApiModelProperty(value = "地址")
    private String invoiceAddress;

    @ApiModelProperty(value = "电话")
    private String tel;

    @ApiModelProperty(value = "开户银行名称")
    private String bankName;

    @ApiModelProperty(value = "开户行账号")
    private String bankAccount;

    @ApiModelProperty(value = "默认标识")
    private Integer defaultFlag;

    @ApiModelProperty(value = "收票人",required = true)
    @NotBlank(message = "收票人不能为空")
    private String pickupPerson;

    @ApiModelProperty(value = "收票电话",required = true)
    @NotBlank(message = "收票电话不能为空")
    @Pattern(regexp = "1[3|4|5|7|8][0-9]\\d{8}",message = "电话号码格式不对")
    private String pickupPhone;

    @ApiModelProperty(value = "收票地址",required = true)
    @NotBlank(message = "收票地址不能为空")
    private String pickupAddress;

    @ApiModelProperty(value = "订单信息列表",required = true)
    @NotEmpty(message= "订单信息列表不能为空")
    private List<@Valid OrderAmountDto> orderAmountList;
}
