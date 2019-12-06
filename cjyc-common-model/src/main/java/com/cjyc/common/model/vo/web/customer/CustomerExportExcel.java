package com.cjyc.common.model.vo.web.customer;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CustomerExportExcel extends BaseExportExcel {
    private static final long serialVersionUID = 5673262429663798668L;
    @Excel(name = "身份证号" ,orderNum = "9",width = 25)
    private String idCard;

    @Excel(name = "身份证人像" ,orderNum = "10",width = 60)
    private String idCardFrontImg;

    @Excel(name = "身份证反面（国徽）" ,orderNum = "11",width = 60)
    private String idCardBackImg;

}