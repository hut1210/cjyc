package com.cjyc.common.model.vo.salesman.dispatch;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @Description 车辆调度记录
 * @Author Liu Xing Xiang
 * @Date 2019/12/13 14:30
 **/
@Data
public class DispatchRecordVo implements Serializable {
    private static final long serialVersionUID = -4832183796204894651L;
    @ApiModelProperty(value = "承运商ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long carrierId;

    @ApiModelProperty(value = "运单编号")
    private String waybillNo;

    @ApiModelProperty(value = "运单类型：1提车，2干线运输，3送车")
    private Integer type;

    @ApiModelProperty(value = "指导线路")
    private String guideLine;

    @ApiModelProperty(value = "提车联系人")
    private String loadLinkName;

    @ApiModelProperty(value = "提车联系人电话")
    private String loadLinkPhone;

    @ApiModelProperty(value = "提车地址")
    private String startAddress;

    @ApiModelProperty(value = "承运商联系人")
    private String carrierName;

    @ApiModelProperty(value = "承运商类型：1干线-个人承运商，2干线-企业承运商，" +
            "3同城-业务员，4同城-代驾，5同城-拖车，6客户自己")
    private Integer carrierType;

    @ApiModelProperty(value = "承运商手机号")
    private String linkmanPhone;

    @ApiModelProperty(value = "交车人名称")
    private String unloadLinkName;

    @ApiModelProperty(value = "交车人电话")
    private String unloadLinkPhone;

    @ApiModelProperty(value = "交车地址")
    private String endAddress;

    public String getGuideLine() {
        return StringUtils.isBlank(guideLine) ? "" : guideLine;
    }
}
