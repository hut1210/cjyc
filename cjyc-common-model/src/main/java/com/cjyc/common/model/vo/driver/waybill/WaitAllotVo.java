package com.cjyc.common.model.vo.driver.waybill;

import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

@Data
public class WaitAllotVo {


    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "运单编号")
    private String no;

    @ApiModelProperty(value = "运单类型：1提车运单，2干线运单，3送车运单")
    private Integer type;

    @ApiModelProperty(value = "指导线路")
    private String guideLine;

    @ApiModelProperty(value = "承运商ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long carrierId;

    @ApiModelProperty(value = "(carryType)承运类型：1干线-个人承运商，2干线-企业承运商，3同城-业务员，4同城-代驾，5同城-拖车，6客户自己")
    private Integer carrierType;

    @ApiModelProperty(value = "承运商名称")
    private String carrierName;

    @ApiModelProperty(value = "车数量")
    private Integer carNum;

    @ApiModelProperty(value = "车数量")
    private Integer allottedCarNum;

    @ApiModelProperty(value = "运单总运费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal freightFee;

    @ApiModelProperty(value = "运费是否固定（包板）0否，1是")
    private Boolean fixedFreightFee;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "调度人")
    private String createUser;

    @ApiModelProperty(value = "调度人ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createUserId;
    public String getGuideLine() {
        return StringUtils.isBlank(guideLine) ? "" : guideLine;
    }
}
