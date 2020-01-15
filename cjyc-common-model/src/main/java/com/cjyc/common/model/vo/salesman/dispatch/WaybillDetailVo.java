package com.cjyc.common.model.vo.salesman.dispatch;

import com.cjyc.common.model.enums.waybill.WaybillStateEnum;
import com.cjyc.common.model.util.BigDecimalSerizlizer;
import com.cjyc.common.model.util.DateLongSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Description 运单明细VO
 * @Author Liu Xing Xiang
 * @Date 2019/12/16 16:09
 **/
@Data
public class WaybillDetailVo implements Serializable {
    private static final long serialVersionUID = 1170810595535938015L;
    @ApiModelProperty(value = "承运商ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long carrierId;

    @ApiModelProperty(value = "司机ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long driverId;

    @ApiModelProperty(value = "承运商联系人")
    private String carrierName;

    @ApiModelProperty(value = "承运商手机号")
    private String linkmanPhone;

    @ApiModelProperty(value = "运单编号")
    private String no;

    @ApiModelProperty(value = "调度日期")
    @JsonSerialize(using = DateLongSerizlizer.class)
    private Long createTime;

    @ApiModelProperty(value = "运单类型")
    private Integer type;

    @ApiModelProperty(value = "(carryType)承运类型：1干线-个人承运商，2干线-企业承运商，3同城-业务员，4同城-代驾，5同城-拖车，6客户自己")
    private Integer carrierType;

    @ApiModelProperty(value = "运单状态：0待承接，20待运输, 55运输中，100已完成，113已取消")
    private Integer state;

    @ApiModelProperty(value = "运单状态描述")
    private String stateDes;

    @ApiModelProperty(value = "指导线路")
    private String guideLine;

    @ApiModelProperty(value = "运单运费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal freightFee;

    @ApiModelProperty(value = "车辆信息列表")
    private List<WaybillCarDetailVo> carDetailVoList;

    public String getStateDes() {
        if (state == WaybillStateEnum.WAIT_ALLOT.code) {
            stateDes = "待承接";
        } else if (state == WaybillStateEnum.ALLOT_CONFIRM.code) {
            stateDes = "待运输";
        } else if (state == WaybillStateEnum.TRANSPORTING.code) {
            stateDes = "运输中";
        } else if (state == WaybillStateEnum.FINISHED.code) {
            stateDes = "已完成";
        } else if (state == WaybillStateEnum.F_CANCEL.code) {
            stateDes = "已取消";
        }
        return stateDes;
    }

    public String getGuideLine() {
        return guideLine == null ? "" : guideLine;
    }
}
