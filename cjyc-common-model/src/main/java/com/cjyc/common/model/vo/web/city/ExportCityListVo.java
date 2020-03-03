package com.cjyc.common.model.vo.web.city;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class ExportCityListVo implements Serializable {
    private static final long serialVersionUID = 2527745968331063845L;

    @Excel(name = "省" ,orderNum = "0",width = 15)
    private String province;
    @Excel(name = "市" ,orderNum = "1",width = 15)
    private String city;
    @Excel(name = "区/县" ,orderNum = "2",width = 15)
    private String area;
    @Excel(name = "大区" ,orderNum = "3",width = 15)
    private String region;
}