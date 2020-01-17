package com.cjyc.common.model.vo.web.customer;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
@Data
public class CustomerImportExcel implements Serializable {
    private static final long serialVersionUID = 4148171515993615835L;

    @Excel(name = "联系人姓名" ,orderNum = "0")
    private String contactMan;

    @Excel(name = "联系人手机号" ,orderNum = "1")
    private String contactPhone;

    @Excel(name = "身份证号" ,orderNum = "2")
    private String idCard;

    @Excel(name = "身份证正面" ,orderNum = "3")
    private String idCardFrontImg;

    @Excel(name = "身份证反面" ,orderNum = "4")
    private String idCardBackImg;
}