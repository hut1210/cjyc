package com.cjyc.common.model.vo.store;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description 业务中心导出数据实体
 * @Author LiuXingXiang
 * @Date 2019/10/29 17:46
 **/
@Data
public class StoreExportExcel implements Serializable {
    private static final long serialVersionUID = 7476308427025587624L;
    @Excel(name = "业务中心名称" ,orderNum = "0",width = 15)
    private String name;

    @Excel(name = "归属大区" ,orderNum = "1",width = 15)
    private String region;

    @Excel(name = "省份" ,orderNum = "2",width = 15)
    private String province;

    @Excel(name = "城市" ,orderNum = "3",width = 15)
    private String city;

    @Excel(name = "区/县" ,orderNum = "4",width = 15)
    private String area;

    @Excel(name = "详细地址" ,orderNum = "5",width = 25)
    private String detailAddr;
}
