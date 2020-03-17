package com.cjyc.common.model.dto.web.publicPayBank;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
@Data
public class PayBankImportExcel implements Serializable {
    private static final long serialVersionUID = -8523896325045984675L;
    @Excel(name = "银行代码" ,orderNum = "0")
    private String bankCode;

    @Excel(name = "支付行号" ,orderNum = "1")
    private String payBankNo;

    @Excel(name = "支行名称" ,orderNum = "2")
    private String subBankName;

}