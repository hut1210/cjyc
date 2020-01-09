package com.cjyc.common.model.vo.customer.invoice;

import com.cjyc.common.model.enums.InvoiceTypeEnum;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * @Description fa  发票信息VO
 * @Author LiuXingXiang
 * @Date 2019/11/12 17:32
 **/
@Data
public class CustomerInvoiceVo implements Serializable {
    private static final long serialVersionUID = -7395378935652933302L;
    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "客户ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long customerId;

    @ApiModelProperty(value = "发票类型 1-普通(个人) ，2-增值普票(企业) ，3-增值专用发票'")
    private Integer type;

    @ApiModelProperty(value = "发票抬头")
    private String title;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "纳税人识别号")
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

    @ApiModelProperty(value = "收票人")
    private String pickupPerson;

    @ApiModelProperty(value = "收票电话")
    private String pickupPhone;

    @ApiModelProperty(value = "收票地址")
    private String pickupAddress;

    public Long getId() {
        return id == null ? 0 : id;
    }
    public Long getCustomerId() {
        return customerId == null ? 0 : customerId;
    }
    public Integer getDefaultFlag() {
        return defaultFlag == null ? 0 : defaultFlag;
    }
    public String getTitle() {
        if (type == InvoiceTypeEnum.PERSONAL_INVOICE.code && StringUtils.isEmpty(title)) {
            title = name;
        }
        return title == null ? "" : title;
    }
    public String getName() {
        return name == null ? "" : name;
    }
    public String getTaxCode() {
        return taxCode == null ? "" : taxCode;
    }
    public String getInvoiceAddress() {
        return invoiceAddress == null ? "" : invoiceAddress;
    }
    public String getTel() {
        return tel == null ? "" : tel;
    }
    public String getBankName() {
        return bankName == null ? "" : bankName;
    }
    public String getBankAccount() {
        return bankAccount == null ? "" : bankAccount;
    }
    public String getPickupPerson() {
        return pickupPerson == null ? "" : pickupPerson;
    }
    public String getPickupPhone() {
        return pickupPhone == null ? "" : pickupPhone;
    }
    public String getPickupAddress() {
        return pickupAddress == null ? "" : pickupAddress;
    }
    public Integer getType() {
        return type == null ? -1 : type;
    }
}
