package com.cjyc.common.model.vo.web.store;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
@Data
public class StoreImportExcel implements Serializable {
    private static final long serialVersionUID = 4067429126073211539L;

    @Excel(name = "业务中心名称" ,orderNum = "0")
    private String name;

    @Excel(name = "归属省" ,orderNum = "1")
    private String province;

    @Excel(name = "归属市" ,orderNum = "2")
    private String city;

    @Excel(name = "归属区县" ,orderNum = "3")
    private String area;

    @Excel(name = "详细地址" ,orderNum = "4")
    private String detailAddr;
}