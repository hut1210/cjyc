package com.cjyc.common.model.vo.salesman.dispatch;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 出发城市即车辆数量
 * @Author Liu Xing Xiang
 * @Date 2019/12/11 17:32
 **/
@Data
public class CityCarCountVo implements Serializable {
    private static final long serialVersionUID = -8833944574690055742L;
    @ApiModelProperty(value = "出发编码")
    private String startCityCode;

    @ApiModelProperty(value = "出发城市名称")
    private String startCity;

    @ApiModelProperty(value = "出发城市车辆数量")
    private int carCount;

    @ApiModelProperty(value = "出发城市-目的城市车辆数量列表")
    private List<StartAndEndCityCountVo> startAndEndCityCountList;
}
