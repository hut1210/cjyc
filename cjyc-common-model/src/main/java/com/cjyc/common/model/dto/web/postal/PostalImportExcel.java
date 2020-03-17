package com.cjyc.common.model.dto.web.postal;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
@Data
public class PostalImportExcel implements Serializable {
    private static final long serialVersionUID = 6522327142689279323L;

    @Excel(name = "省洲名称" ,orderNum = "0")
    private String provinceName;

    @Excel(name = "地区名称" ,orderNum = "1")
    private String areaName;

    @Excel(name = "邮政编码" ,orderNum = "2")
    private String postalCode;

    @Excel(name = "电话区号" ,orderNum = "3")
    private String areaCode;
}