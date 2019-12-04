package com.cjyc.common.model.vo.store;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

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

    @Excel(name = "管辖范围" ,orderNum = "1",width = 15)
    private Integer areaCount;

    @Excel(name = "所属大区" ,orderNum = "2",width = 15)
    private String regionName;

    @Excel(name = "省份" ,orderNum = "3",width = 15)
    private String province;

    @Excel(name = "城市" ,orderNum = "4",width = 15)
    private String city;

    @Excel(name = "区/县" ,orderNum = "5",width = 15)
    private String area;

    @Excel(name = "详细地址" ,orderNum = "6",width = 25)
    private String detailAddr;

    @Excel(name = "操作人" ,orderNum = "7",width = 15)
    private String operationName;

    private Long updateTime;
    @Excel(name = "更新时间" ,orderNum = "8",width = 15)
    private String updateTimeStr;

    @Excel(name = "备注" ,orderNum = "9",width = 15)
    private String remark;

    public String getUpdateTimeStr() {
        if (!Objects.isNull(updateTime)) {
            return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(updateTime),"yyyy/MM/dd");
        }
        return "";
    }
}
