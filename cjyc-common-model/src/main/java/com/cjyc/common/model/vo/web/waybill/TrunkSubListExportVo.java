package com.cjyc.common.model.vo.web.waybill;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cjyc.common.model.entity.Waybill;
import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 运单管理-干线 子运单导出信息
 */
@Data
public class TrunkSubListExportVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "运单编号")
    private String no;

    @ApiModelProperty(value = "运单类型：1提车运单，2干线运单，3送车运单")
    private Integer type;

    @ApiModelProperty(value = "调度类型：1自己处理，2人工调度")
    private Integer source;

    @ApiModelProperty(value = "指导线路")
    @Excel(name = "指导线路", orderNum = "2")
    private String guideLine;

    @ApiModelProperty(value = "推荐线路")
    private String recommendLine;

    @ApiModelProperty(value = "承运商ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long carrierId;

    @ApiModelProperty(value = "(carryType)承运类型：1干线-个人承运商，2干线-企业承运商，3同城-业务员，4同城-代驾，5同城-拖车，6客户自己")
    private Integer carrierType;

    @Excel(name = "承运商", orderNum = "4")
    private String carrierName;

    @ApiModelProperty(value = "车数量")
    private Integer carNum;

    @ApiModelProperty(value = "运单状态：0待分配承运商（竞抢），15待承运商承接任务，55运输中，100已完成，111超时关闭，113已取消，115已拒接")
    private Integer state;

    @ApiModelProperty(value = "运单总运费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal freightFee;

    @ApiModelProperty(value = "运费支付状态")
    private Integer freightPayState;

    @ApiModelProperty(value = "运费支付时间")
    private String freightPayTime;

    @ApiModelProperty(value = "运费支付流水单号")
    private String freightPayBillno;

    @ApiModelProperty(value = "运费是否固定（包板）0否，1是")
    private Boolean fixedFreightFee;

    @ApiModelProperty(value = "备注")
    @Excel(name = "备注信息", orderNum = "9")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "调度人")
    @Excel(name = "创建人", orderNum = "11")
    private String createUser;

    @ApiModelProperty(value = "调度人ID")
    private Long createUserId;

    @ApiModelProperty(value = "所属业务中心ID")
    private Long inputStoreId;

    @ApiModelProperty(value = "完成时间")
    private Long completeTime;
    @ApiModelProperty("外部状态")
    @Excel(name = "状态", orderNum = "1")
    private String outterState;

    @ApiModelProperty("任务编号")
    @Excel(name = "运单单号", orderNum = "0")
    private String wtNo;
    @ApiModelProperty("任务车辆数量")
    @Excel(name = "运输车辆数", orderNum = "3")
    private Integer taskCarNum;
    @ApiModelProperty("司机ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long driverId;
    @ApiModelProperty("司机名称")
    @Excel(name = "司机", orderNum = "5")
    private String driverName;
    @ApiModelProperty("手机号")
    @Excel(name = "司机电话", orderNum = "6")
    private String driverPhone;
    @ApiModelProperty("车牌号")
    @Excel(name = "车牌号", orderNum = "7")
    private String vehiclePlateNo;
    @ApiModelProperty("车位总数")
    private Integer carryCarNum;
    @ApiModelProperty("被占车位")
    private Integer occupiedCarNum;
    @Excel(name = "动态车位", orderNum = "8")
    private String dynamicCarryNum;
    @Excel(name = "创建时间", orderNum = "10")
    private String createTimeStr;

    public String getCreateTimeStr() {
        Long date = getCreateTime();
        if(null == date || date <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(date), "yyyy-MM-dd");
    }
    public String getDynamicCarryNum() {
        Integer oc = getOccupiedCarNum();
        Integer tc = getCarryCarNum();
        return (oc == null?"0": oc+"") + "/" + (tc == null?"0": tc+"");
    }
}
