package com.cjyc.common.model.vo.web.customer;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class KeyExportExcel extends BaseExportExcel {
    private static final long serialVersionUID = 336846592944382829L;
    @Excel(name = "全称" ,orderNum = "9",width = 15)
    private String name;

    @Excel(name = "客户类型  0：电商 1：租赁 2：金融公司 3：经销商 4：其他" ,orderNum = "10",width = 60)
    private Integer customerNature;

    @Excel(name = "状态：0待审核，1未登录，2已审核，3审核拒绝， 7已冻结" ,orderNum = "11",width = 60)
    private Integer state;

}