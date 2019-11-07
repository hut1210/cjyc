package com.cjyc.common.model.vo.web.order;

import com.cjyc.common.model.entity.OrderCar;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author JPG
 */
@Data
public class TransportInfoOrderCarVo extends OrderCar {

    @ApiModelProperty("提车运输状态")
    private Integer pickTransportState;
    @ApiModelProperty("干线运输状态")
    private Integer trunkTransportState;
    @ApiModelProperty("送车运输状态")
    private Integer backTransportState;

    @ApiModelProperty("当前归属者")
    private Integer nowDriver;
    @ApiModelProperty("当前所在地")
    private Integer nowCity;

}
