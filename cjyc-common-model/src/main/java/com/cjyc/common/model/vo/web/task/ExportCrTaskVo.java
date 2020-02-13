package com.cjyc.common.model.vo.web.task;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ExportCrTaskVo implements Serializable {
    private static final long serialVersionUID = 5120393411261743643L;
    @Excel(name = "运单单号" ,orderNum = "0",width = 20)
    private String no;
    @Excel(name = "所属主运单号" ,orderNum = "1",width = 20)
    private String waybillNo;
    @Excel(name = "类型" ,orderNum = "2",width = 20)
    private String outterState;
    @Excel(name = "指导线路" ,orderNum = "3",width = 20)
    private String guideLine;
    @Excel(name = "运输车辆数" ,orderNum = "4",width = 20)
    private Integer waybillCarNum;
    @Excel(name = "司机名称" ,orderNum = "5",width = 20)
    private String driverName;
    @Excel(name = "司机电话" ,orderNum = "6",width = 20)
    private String driverPhone;
    @ApiModelProperty(value = "运力车牌号")
    @Excel(name = "运力车牌号" ,orderNum = "7",width = 20)
    private String vehiclePlateNo;
    @Excel(name = "非空车位" ,orderNum = "8",width = 20)
    private Integer occupiedCarNum;
    @Excel(name = "承运数" ,orderNum = "9",width = 20)
    private Integer carryCarNum;
    @Excel(name = "备注" ,orderNum = "10",width = 20)
    private String remark;
    @Excel(name = "指派时间" ,orderNum = "11",width = 20)
    private Long createTime;
    @Excel(name = "指派操作人" ,orderNum = "12",width = 20)
    private String createUser;

    public String getCreateTime(){
        if(createTime != null){
            return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(createTime), TimePatternConstant.COMPLEX_TIME_FORMAT);
        }
        return "";
    }
}