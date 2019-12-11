package com.cjyc.common.model.vo.salesman.dispatch;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description 车辆调度出发城市即车辆数量返回对象
 * @Author Liu Xing Xiang
 * @Date 2019/12/11 16:49
 **/
@Data
public class StartCityVo implements Serializable {
    private static final long serialVersionUID = -7164029178679297083L;
    @ApiModelProperty(value = "订单ID")
    private Long orderId;

    @ApiModelProperty(value = "订单车辆ID")
    private Long orderCarId;

    @ApiModelProperty(value = "出发城市编码")
    private String startCityCode;

    @ApiModelProperty(value = "出发城市名称")
    private String startCity;

    @ApiModelProperty(value = "目的城市编码")
    private String endCityCode;

    @ApiModelProperty(value = "出发城市名称")
    private String endCity;
}
