package com.cjyc.common.model.dto.web.finance;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: RenPL
 * @Date: 2020/3/21
 * 应收账款数据来源：订单车辆信息
 */
@Data
public class ReceiveOrderCarDto implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "车辆id")
    private Long orderCarId;

    @ApiModelProperty(value = "车辆编号")
    @Excel(name = "车辆编号", orderNum = "0")
    private String no;

    @ApiModelProperty(value = "VIN码")
    @Excel(name = "VIN码", orderNum = "1")
    private String vin;

    @ApiModelProperty(value = "品牌")
    @Excel(name = "品牌", orderNum = "2")
    private String brand;

    @ApiModelProperty(value = "车系")
    @Excel(name = "车系", orderNum = "3")
    private String model;

    @ApiModelProperty(value = "结算类型")
    @Excel(name = "结算类型", orderNum = "4")
    private String payModeName;

    @ApiModelProperty(value = "剩余账期(天)")
    @Excel(name = "剩余账期(天)", orderNum = "5")
    private Long remainderSettlePeriod;

    @ApiModelProperty(value = "应收运费")
    @Excel(name = "应收运费", orderNum = "6", type = 10)
    private BigDecimal freightReceivable;

    @ApiModelProperty(value = "订单编号")
    @Excel(name = "订单编号", orderNum = "7")
    private String orderNo;

    @ApiModelProperty(value = "订单所属大区")
    @Excel(name = "订单所属大区", orderNum = "8")
    private String largeArea;

    @ApiModelProperty(value = "订单所属业务中心")
    @Excel(name = "订单所属业务中心", orderNum = "9")
    private String inputStoreName;

    @ApiModelProperty(value = "始发地")
    @Excel(name = "始发地", orderNum = "10")
    private String startAddress;

    @ApiModelProperty(value = "目的地")
    @Excel(name = "目的地", orderNum = "11")
    private String endAddress;

    @ApiModelProperty(value = "交付时间")
    private Long deliveryDate;

    @Excel(name = "交付时间", orderNum = "12")
    private String deliveryDateStr;

    public String getDeliveryDateStr() {
        Long deliveryDateDate = getDeliveryDate();
        if (null == deliveryDateDate || deliveryDateDate <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(deliveryDateDate), "yyyy-MM-dd");
    }

    @ApiModelProperty(value = "客户类型")
    private Integer type;

    @Excel(name = "客户类型", orderNum = "13")
    private String customTypeStr;

    public String getCustomTypeStr() {
        Integer type = getType();
        if (type != null && type == 1) {
            return "C端客户";
        } else if (type != null && type == 2) {
            return "企业";
        } else if (type != null && type == 3) {
            return "合伙人";
        } else {
            return "";
        }
    }

    @ApiModelProperty(value = "客户名称")
    @Excel(name = "客户名称", orderNum = "14")
    private String customerName;

    @ApiModelProperty(value = "客户账号")
    @Excel(name = "客户账号", orderNum = "15")
    private String contactPhone;

    @ApiModelProperty(value = "实付运费", hidden = true)
    private BigDecimal freightPay;

    @ApiModelProperty(value = "订单交付时间", hidden = true)
    private Long orderDeliveryDate;

    @ApiModelProperty(value = "客户Id", hidden = true)
    private Long customerId;

    @ApiModelProperty(value = "合同Id", hidden = true)
    private Long customerContractId;

    @ApiModelProperty(value = "账期(天)", hidden = true)
    private Integer settlePeriod;

}
