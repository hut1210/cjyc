package com.cjyc.foreign.api.dto.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description 订单车辆运输信息
 * @Author Liu Xing Xiang
 * @Date 2020/3/13 17:26
 **/
@Data
public class OrderCarTransportDetailResDto implements Serializable {
    private static final long serialVersionUID = 4354792664711892057L;
    @ApiModelProperty(value = "车辆编码")
    private String no;

    @ApiModelProperty(value = "vin码")
    private String vin;

    @ApiModelProperty("提车运输状态：1待调度，2待提车，3待交车，5已完成，21自送待调度，23自送待交车，25自送已交付，100物流上门")
    private Integer pickTransportState;

    @ApiModelProperty("干线运输状态：1待调度，2待提车，3待交车，5已完成, 100无干线")
    private Integer trunkTransportState;

    @ApiModelProperty("送车运输状态：1待调度，2待提车，3待交车，5已完成，21自提待调度，22自提待提车，23自提待交车，25自提已交付，100物流上门")
    private Integer backTransportState;

    public String getNo() {
        return no == null ? "" : no;
    }

    public String getVin() {
        return vin == null ? "" : vin;
    }

    public Integer getPickTransportState() {
        return pickTransportState == null ? -1 : pickTransportState;
    }

    public Integer getTrunkTransportState() {
        return trunkTransportState == null ? -1 : trunkTransportState;
    }

    public Integer getBackTransportState() {
        return backTransportState == null ? -1 : backTransportState;
    }
}
