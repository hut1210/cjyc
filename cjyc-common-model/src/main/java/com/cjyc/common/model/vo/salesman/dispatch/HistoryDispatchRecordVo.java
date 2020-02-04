package com.cjyc.common.model.vo.salesman.dispatch;

import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
import com.cjyc.common.model.serizlizer.DateLongSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description 历史调度记录
 * @Author Liu Xing Xiang
 * @Date 2019/12/13 17:07
 **/
@Data
public class HistoryDispatchRecordVo implements Serializable {
    private static final long serialVersionUID = 7800535148779633095L;
    @ApiModelProperty(value = "运单id")
    private String waybillId;

    @ApiModelProperty(value = "运单编号")
    private String waybillNo;

    @ApiModelProperty(value = "运单类型：1提车运单，2干线运单，3送车运单")
    private Integer waybillType;

    @ApiModelProperty(value = "指导线路")
    private String guideLine;

    @ApiModelProperty(value = "调度日期")
    @JsonSerialize(using = DateLongSerizlizer.class)
    private Long createTime;

    @ApiModelProperty(value = "车辆数量")
    private int carNum;

    @ApiModelProperty(value = "运单运费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal freightFee;

    @ApiModelProperty(value = "承运商联系人")
    private String linkMan;

    @ApiModelProperty(value = "承运商手机号")
    private String linkmanPhone;

    public String getGuideLine() {
        return guideLine == null ? "" : guideLine;
    }

    public String getLinkMan() {
        return linkMan == null ? "" : linkMan;
    }

    public String getLinkmanPhone() {
        return linkmanPhone == null ? "" : linkmanPhone;
    }
}
