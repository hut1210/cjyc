package com.cjyc.common.model.dto.web.order;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.Date;

/**
 * C端客户订单导入实体
 */
@Data
public class ImportCustomerOrderDto {
    @Excel(name = "C端客户订单_订单序号")
    @NotNull(message = "订单序号不能为空")
    @Max(999999999)
    @Min(1)
    private Integer orderNo;
    @Excel(name = "客户名称")
    @NotEmpty(message = "客户名称不能为空")
    private String customerName;
    @Excel(name = "客户电话(账号)")
    @NotEmpty(message = "客户电话(账号)不能为空")
    @Pattern(regexp = "[1]\\d{10}", message = "客户电话(账号)需要为11位数字")
    private String customerPhone;
    @Excel(name = "支付方式")
    @NotEmpty(message = "支付方式不能为空")
    private String payType;
    @Excel(name = "始发城市(省)")
    @NotEmpty(message = "始发城市(省)不能为空")
    private String startProvince;
    @Excel(name = "始发城市(市)")
    @NotEmpty(message = "始发城市(市)不能为空")
    private String startCity;
    @Excel(name = "始发城市(区/县)")
    @NotEmpty(message = "始发城市(区/县)不能为空")
    private String startArea;
    @Excel(name = "目的城市(省)")
    @NotEmpty(message = "目的城市(省)不能为空")
    private String endProvince;
    @Excel(name = "目的城市(市)")
    @NotEmpty(message = "目的城市(市)不能为空")
    private String endCity;
    @Excel(name = "目的城市(区/县)")
    @NotEmpty(message = "目的城市(区/县)不能为空")
    private String endArea;
    @Excel(name = "提车方式")
    @NotEmpty(message = "提车方式不能为空")
    @Pattern(regexp = "(自送|代驾提车|拖车提车|物流提车)",
            message = "提车方式只有：自送、代驾提车、拖车提车、物流提车")
    private String pickType;
    @Excel(name = "提车日期", importFormat = "yyyy/MM/dd", format = "yyyy/MM/dd")
    @NotNull(message = "提车日期不能为空")
    private Date pickDate;
    @Excel(name = "提车联系人")
    @NotEmpty(message = "提车联系人不能为空")
    private String pickPerson;
    @Excel(name = "提车电话")
    @NotEmpty(message = "提车电话不能为空")
    @Pattern(regexp = "[1]\\d{10}", message = "提车电话需要为11位数字")
    private String pickPhone;
    @Excel(name = "提车地址")
    @NotEmpty(message = "提车地址不能为空")
    private String pickAddr;
    @Excel(name = "交付方式")
    @NotEmpty(message = "交付方式不能为空")
    @Pattern(regexp = "(自提|代驾送车|拖车送车|物流送车)",
            message = "交付方式只有：自提、代驾送车、拖车送车、物流送车")
    private String deliveryType;
    @Excel(name = "预计送达日期", importFormat = "yyyy/MM/dd", format = "yyyy/MM/dd")
    @NotNull(message = "预计送达日期不能为空")
    private Date sendDate;
    @Excel(name = "交付联系人")
    @NotEmpty(message = "交付联系人不能为空")
    private String deliveryPerson;
    @Excel(name = "交付电话")
    @NotEmpty(message = "交付电话不能为空")
    @Pattern(regexp = "[1]\\d{10}", message = "交付电话需要为11位数字")
    private String deliveryPhone;
    @Excel(name = "交付地址")
    @NotEmpty(message = "交付地址不能为空")
    private String deliveryAddr;
    @Excel(name = "订单备注")
    private String remark;
    private String startProvinceCode;
    private String startCityCode;
    private String startAreaCode;
    private String endProvinceCode;
    private String endCityCode;
    private String endAreaCode;
    private Long customerId;
    private Integer customerType;
}
