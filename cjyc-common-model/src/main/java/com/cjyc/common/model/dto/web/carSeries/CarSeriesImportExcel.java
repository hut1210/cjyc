package com.cjyc.common.model.dto.web.carSeries;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Description 品牌车系导入实体
 * @Author LiuXingXiang
 * @Date 2019/10/29 11:22
 **/
@Data
public class CarSeriesImportExcel implements Serializable {
    private static final long serialVersionUID = 6411881639865298089L;
    @Excel(name = "品牌" ,orderNum = "0")
    @NotBlank(message = "品牌不能为空")
    private String brand;

    @Excel(name = "型号" ,orderNum = "1")
    @NotBlank(message = "型号不能为空")
    private String model;
}
