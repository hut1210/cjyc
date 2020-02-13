package com.cjyc.common.model.vo.web.waybill;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import lombok.Data;

import java.io.Serializable;
@Data
public class ExportCrWaybillVo implements Serializable {
    private static final long serialVersionUID = -4343698587809875129L;
    @Excel(name = "运单编号" ,orderNum = "0",width = 20)
    private String waybillNo;
    @Excel(name = "指导线路" ,orderNum = "1",width = 20)
    private String guideLine;
    @Excel(name = "车数量" ,orderNum = "2",width = 15)
    private Integer carNum;
    @Excel(name = "已分配数量" ,orderNum = "3",width = 15)
    private String hasAllottedNum;
    @Excel(name = "状态" ,orderNum = "4",width = 15)
    private String outterState;
    @Excel(name = "承运商名称" ,orderNum = "5",width = 20)
    private String carrierName;
    @Excel(name = "备注" ,orderNum = "6",width = 15)
    private String remark;
    @Excel(name = "创建时间" ,orderNum = "7",width = 15)
    private Long createTime;
    @Excel(name = "调度人" ,orderNum = "8",width = 20)
    private String createUser;

    public String getCreateTime(){
        if(createTime != null){
            return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(createTime), TimePatternConstant.COMPLEX_TIME_FORMAT);
        }
        return "";
    }

}