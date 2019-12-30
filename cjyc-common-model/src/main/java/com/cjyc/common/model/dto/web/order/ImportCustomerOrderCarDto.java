package com.cjyc.common.model.dto.web.order;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * C端客户订单车辆导入实体
 */
@Data
public class ImportCustomerOrderCarDto {
    @Excel(name = "车辆信息_订单序号")
    @NotNull(message = "订单序号不能为空")
    @Max(999999999)
    @Min(1)
    private Integer orderNo;
    @Excel(name = "VIN")
    @Pattern(regexp = "[A-Z0-9]{17}", message = "vin码必须为17位且只能是大写字母或数字")
    private String vinCode;
    @Excel(name = "品牌")
    @NotEmpty(message = "品牌不能为空")
    private String brand;
    @Excel(name = "车系")
    @NotEmpty(message = "车系不能为空")
    private String model;
    @Excel(name = "是否能动")
    @Pattern(regexp = "(是|否)", message = "是否能动只能输入是或否")
    @NotEmpty(message = "是否能动不能为空")
    private String isMove;
    @Excel(name = "车牌号")
    private String carNum;
    @Excel(name = "是否新车")
    @Pattern(regexp = "(是|否)", message = "只能输入是或否")
    @NotEmpty(message = "是否新车不能为空")
    private String isNew;
    @Excel(name = "提车费(元)")
    @NotNull(message = "提车费(元)不能为空")
    private BigDecimal pickFee;
    @Excel(name = "物流费(元)")
    @NotNull(message = "物流费(元)不能为空")
    private BigDecimal wlFee;
    @Excel(name = "送车费(元)")
    @NotNull(message = "送车费(元)不能为空")
    private BigDecimal sendFee;
    @Excel(name = "车值(万元)")
    private BigDecimal vehicleValue;
    @Excel(name = "保费(元)")
    private BigDecimal inFee;
}
