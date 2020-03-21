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
    private String no;

    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "型号")
    private String model;

    @ApiModelProperty(value = "vin码")
    private String vin;

    @ApiModelProperty(value = "结算类型")
    private String payModeName;

    @ApiModelProperty(value = "应收运费")
    private BigDecimal freightReceivable;

    @ApiModelProperty(value = "实付运费")
    private BigDecimal freightPay;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "订单所属大区")
    private String largeArea;

    @ApiModelProperty(value = "订单所属业务中心")
    private String inputStoreName;

    @ApiModelProperty(value = "始发地")
    private String startAddress;

    @ApiModelProperty(value = "目的地")
    private String endAddress;

    @ApiModelProperty(value = "交付时间")
    private Long deliveryDate;

    @ApiModelProperty(value = "交付时间")
    private String deliveryDateStr;

    public String getDeliveryDateStr() {
        Long date = getDeliveryDate();
        if (null == date || date <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(date), "yyyy-MM-dd");
    }

    @ApiModelProperty(value = "客户Id")
    private Long customerId;

    @ApiModelProperty(value = "客户类型")
    private Integer type;

    @Excel(name = "客户类型", orderNum = "13")
    private String customTypeName;

    public String getCustomTypeName() {
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
    private String customerName;

    @ApiModelProperty(value = "合同Id")
    private Long customerContractId;
}
