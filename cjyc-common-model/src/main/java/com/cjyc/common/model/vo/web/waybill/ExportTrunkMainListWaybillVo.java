package com.cjyc.common.model.vo.web.waybill;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 运单管理-主干线导出vo
 */
@Data
public class ExportTrunkMainListWaybillVo {
    @Excel(name = "运单单号", orderNum = "0")
    private String waybillNo;
    @Excel(name = "运输路线", orderNum = "1")
    private String guideLine;
    @Excel(name = "运输车辆数", orderNum = "2")
    private Integer carNum;
    @Excel(name = "承运商", orderNum = "3")
    private String carrierName;
    @Excel(name = "司机", orderNum = "4")
    private String driverName;
    @Excel(name = "司机电话", orderNum = "5")
    private String driverPhone;
    @Excel(name = "车牌号", orderNum = "6")
    private String plateNo;
    @Excel(name = "动态车位", orderNum = "7")
    private String occupiedCarNum;
    @Excel(name = "备注信息", orderNum = "8")
    private String remark;
    @Excel(name = "创建时间", orderNum = "9")
    private String createTimeDesc;
    @Excel(name = "创建人", orderNum = "10")
    private String createUser;
}
