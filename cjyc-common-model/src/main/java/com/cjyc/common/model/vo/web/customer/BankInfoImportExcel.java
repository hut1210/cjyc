package com.cjyc.common.model.vo.web.customer;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
@Data
public class BankInfoImportExcel implements Serializable {
    private static final long serialVersionUID = -6068353174684429900L;

    @Excel(name = "银行编码" ,orderNum = "0")
    private String bankCode;

    @Excel(name = "银行名称" ,orderNum = "1")
    private String bankName;
}