package com.cjyc.common.model.dto.web.order;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

/**
 * 合伙人订单导入车辆信息
 */
@Data
public class ImportPatCustomerOrderCarDto {
    @Excel(name = "车辆信息_订单序号")
    @NotNull(message = "订单序号不能为空")
    @Max(999999999)
    @Min(1)
    private Integer orderNo;
    @Excel(name = "VIN")
    @Pattern(regexp = "[A-Z0-9]{17}", message = "VIN码只能为17位大写字母及数字组合")
    private String vinCode;
    @Excel(name = "品牌")
    private String brand;
    @Excel(name = "车系")
    private String model;
    @Excel(name = "是否能动")
    @Pattern(regexp = "(是|否)", message = "是否能动只能输入是或否")
    private String isMove;
    @Excel(name = "车牌号")
    private String carNum;
    @Excel(name = "是否新车")
    @Pattern(regexp = "(是|否)", message = "是否新车只能输入是或否")
    private String isNew;
    @Excel(name = "提车费(元)")
    private BigDecimal pickFee;
    @Excel(name = "物流费(元)")
    @NotNull(message = "物流费(元)不能为空")
    private BigDecimal wlFee;
    @Excel(name = "送车费(元)")
    private BigDecimal sendFee;
    @Excel(name = "车值(万元)")
    private BigDecimal vehicleValue;
}
