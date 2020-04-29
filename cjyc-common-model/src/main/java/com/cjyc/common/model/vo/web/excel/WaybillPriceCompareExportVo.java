package com.cjyc.common.model.vo.web.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WaybillPriceCompareExportVo {
    @Excel(name = "车辆编号")
    private String orderCarNo;
    @Excel(name = "订单编号")
    private String orderNo;
    @Excel(name = "品牌")
    private String brand;
    @Excel(name = "车型")
    private String model;
    @Excel(name = "车架号")
    private String vin;
    @Excel(name = "订单出发城市")
    private String orderStartCity;
    @Excel(name = "订单目的城市")
    private String orderEndCity;
    @Excel(name = "订单线路价卡")
    private BigDecimal orderLineFreightFee;
    @Excel(name = "订单创建时间")
    private Long orderCreateTime;
    @Excel(name = "运单号")
    private String waybillNo;
    @Excel(name = "运单承运人")
    private String waybillCarrierName;
    @Excel(name = "运单创建人")
    private String waybillCreateUser;
    @Excel(name = "运单创建时间")
    private Long waybillCreateTime;
    @Excel(name = "运单车辆出发城市")
    private String waybillStartCity;
    @Excel(name = "运单车辆目的城市")
    private String waybillEndCity;
    @Excel(name = "车辆提车是否物流上门")
    private String isPickFromHome;
    @Excel(name = "车辆送车是否物流上门")
    private String isBackToHome;
    @Excel(name = "车辆实际运费")
    private BigDecimal waybillRealFreightFee;
    @Excel(name = "车辆线路成本")
    private BigDecimal waybillLineFreightFee;
    @Excel(name = "差（实际-线路）")
    private BigDecimal waybillFreightFeeCha;


}
