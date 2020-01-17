package com.cjyc.common.model.vo.web.customer;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
@Data
public class PartnerImportExcel implements Serializable {
    private static final long serialVersionUID = 2915085699940417236L;

    @Excel(name = "合伙人名称" ,orderNum = "0")
    private String name;

    @Excel(name = "联系人名称" ,orderNum = "1")
    private String contactMan;

    @Excel(name = "联系人电话" ,orderNum = "2")
    private String contactPhone;

    @Excel(name = "统一社会信用代码" ,orderNum = "3")
    private String socialCreditCode;

    @Excel(name = "实际详细地址" ,orderNum = "4")
    private String contactAddress;

    @Excel(name = "是否一般纳税人 0：否  1：是" ,orderNum = "5")
    private Integer isTaxpayer;

    @Excel(name = "是否可以开票 0：否 1：是" ,orderNum = "6")
    private Integer isInvoice;

    @Excel(name = "结算方式" ,orderNum = "7")
    private Integer settleType;

    @Excel(name = "账期/天" ,orderNum = "8")
    private Integer settlePeriod;

    @Excel(name = "卡类型" ,orderNum = "9")
    private Integer cardType;

    @Excel(name = "开户名称" ,orderNum = "10")
    private String cardName;

    @Excel(name = "银行卡号" ,orderNum = "11")
    private String cardNo;

    @Excel(name = "开户行名称" ,orderNum = "12")
    private String bankName;

    @Excel(name = "持卡人身份证号" ,orderNum = "13")
    private String idCard;

    @Excel(name = "银行开户许可证" ,orderNum = "14")
    private String bankLicence;

    @Excel(name = "备注/描述" ,orderNum = "15")
    private String description;

    @Excel(name = "企业执照正面" ,orderNum = "16")
    private String businessLicenseFrontImg;

    @Excel(name = "企业执照反面" ,orderNum = "17")
    private String businessLicenseBackImg;

    @Excel(name = "法人身份证复印件正面" ,orderNum = "18")
    private String legalIdcardFrontImg;

    @Excel(name = "法人身份证复印件反面" ,orderNum = "19")
    private String legalIdcardBackImg;

    @Excel(name = "联系人身份证正面" ,orderNum = "20")
    private String linkmanIdcardFrontImg;

    @Excel(name = "联系人身份证反面" ,orderNum = "21")
    private String linkmanIdcardBackImg;

    @Excel(name = "授权书正面" ,orderNum = "22")
    private String authorizationFrontImg;

    @Excel(name = "授权书反面" ,orderNum = "23")
    private String authorizationBackImg;
}