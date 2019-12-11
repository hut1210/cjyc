package com.cjyc.common.model.vo.salesman.customer;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
@Data
public class SalesCustomerVo implements Serializable {
    private static final long serialVersionUID = 2919228330713611587L;

    @ApiModelProperty(value = "客户id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long customerId;

    @ApiModelProperty(value = "客户名称")
    private String contactMan;

    @ApiModelProperty(value = "手机号")
    private String contactPhone;

    public Long getCustomerId(){return customerId == null ? 0:customerId;}
    public String getContactMan(){return StringUtils.isBlank(contactMan) ? "":contactMan;}
    public String getContactPhone(){return StringUtils.isBlank(contactPhone) ? "":contactPhone;}
}