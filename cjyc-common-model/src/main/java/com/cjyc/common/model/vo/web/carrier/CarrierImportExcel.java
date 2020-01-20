package com.cjyc.common.model.vo.web.carrier;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
@Data
public class CarrierImportExcel implements Serializable {
    private static final long serialVersionUID = 5664221051779713772L;

    @Excel(name = "企业名称" ,orderNum = "0")
    private String name;

    @Excel(name = "是否能开发票" ,orderNum = "1")
    private Integer isInvoice;

    @Excel(name = "法人姓名" ,orderNum = "2")
    private String legalName;

    @Excel(name = "法人身份" ,orderNum = "3")
    private String legalIdCard;

    @Excel(name = "联系人姓名" ,orderNum = "4")
    private String linkman;

    @Excel(name = "联系人电话" ,orderNum = "5")
    private String linkmanPhone;

    @Excel(name = "结算类型（时付/账期）" ,orderNum = "6")
    private Integer settleType;

    @Excel(name = "对公/对私账户" ,orderNum = "7")
    private Integer cardType;

    @Excel(name = "开户名称" ,orderNum = "8")
    private String cardName;

    @Excel(name = "开户行" ,orderNum = "9")
    private String bankName;

    @Excel(name = "银行账号" ,orderNum = "10")
    private String cardNo;

    @Excel(name = "营业执照正面" ,orderNum = "11")
    private String busLicenseFrontImg;

    @Excel(name = "营业执照反面" ,orderNum = "12")
    private String busLicenseBackImg;

    @Excel(name = "道路运输许可证正面" ,orderNum = "13")
    private String transportLicenseFrontImg;

    @Excel(name = "道路运输许可证反面" ,orderNum = "14")
    private String transportLicenseBackImg;

    @Excel(name = "银行开户证明正面" ,orderNum = "15")
    private String bankOpenFrontImg;

    @Excel(name = "银行开户证明反面" ,orderNum = "16")
    private String bankOpenBackImg;
}