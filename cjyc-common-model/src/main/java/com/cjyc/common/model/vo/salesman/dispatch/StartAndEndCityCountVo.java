package com.cjyc.common.model.vo.salesman.dispatch;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @Description 出发城市-目的城市车辆数量对象
 * @Author Liu Xing Xiang
 * @Date 2019/12/11 17:40
 **/
@Data
public class StartAndEndCityCountVo implements Serializable {
    private static final long serialVersionUID = 1223630726877035936L;
    @ApiModelProperty(value = "出发城市编码")
    private String startCityCode;

    @ApiModelProperty(value = "出发城市名称")
    private String startCity;

    @ApiModelProperty(value = "目的城市编码")
    private String endCityCode;

    @ApiModelProperty(value = "目的城市名称")
    private String endCity;

    @ApiModelProperty(value = "出发城市-目的城市车辆数量")
    private int carCount;

    public String getStartCityCode() {
        return StringUtils.isBlank(startCityCode) ? "" : startCityCode;
    }

    public String getStartCity() {
        return StringUtils.isBlank(startCity) ? "" : startCity;
    }

    public String getEndCityCode() {
        return StringUtils.isBlank(endCityCode) ? "" : endCityCode;
    }

    public String getEndCity() {
        return StringUtils.isBlank(endCity) ? "" : endCity;
    }
}
