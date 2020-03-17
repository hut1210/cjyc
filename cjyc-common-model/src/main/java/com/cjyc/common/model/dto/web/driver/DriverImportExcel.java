package com.cjyc.common.model.dto.web.driver;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
@Data
public class DriverImportExcel implements Serializable {
    private static final long serialVersionUID = -7385897386550627365L;
    @Excel(name = "司机姓名" ,orderNum = "0")
    private String realName;

    @Excel(name = "司机手机号" ,orderNum = "1")
    private String phone;

    @Excel(name = "承运方式：2 : 代驾  3 : 干线  4：拖车" ,orderNum = "2")
    private Integer mode;

    @Excel(name = "司机身份证号" ,orderNum = "3")
    private String idCard;

    @Excel(name = "身份证正面" ,orderNum = "4")
    private String idCardFrontImg;

    @Excel(name = "身份证反面" ,orderNum = "5")
    private String idCardBackImg;

    @Excel(name = "驾驶证正面" ,orderNum = "6")
    private String driverLicenceFrontImg;

    @Excel(name = "驾驶证反面" ,orderNum = "7")
    private String driverLicenceBackImg;

    @Excel(name = "行驶证正面" ,orderNum = "8")
    private String travelLicenceFrontImg;

    @Excel(name = "行驶证反面" ,orderNum = "9")
    private String travelLicenceBackImg;

    @Excel(name = "营运证正面" ,orderNum = "10")
    private String taxiLicenceFrontImg;

    @Excel(name = "营运证反面" ,orderNum = "11")
    private String taxiLicenceBackImg;

    @Excel(name = "从业证正面" ,orderNum = "12")
    private String qualifiCertFrontImg;

    @Excel(name = "从业证反面" ,orderNum = "13")
    private String qualifiCertBackImg;
}