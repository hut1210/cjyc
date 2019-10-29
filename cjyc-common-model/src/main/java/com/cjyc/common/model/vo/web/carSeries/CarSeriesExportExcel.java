package com.cjyc.common.model.vo.web.carSeries;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description 品牌车系导出实体
 * @Author LiuXingXiang
 * @Date 2019/10/29 11:19
 **/
@Data
public class CarSeriesExportExcel implements Serializable {
    private static final long serialVersionUID = 5168864974104366624L;
    @Excel(name = "品牌" ,orderNum = "0",width = 20)
    private String brand;

    @Excel(name = "型号" ,orderNum = "1",width = 25)
    private String model;
}
