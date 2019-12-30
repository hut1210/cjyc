package com.cjyc.common.model.vo.salesman.mine;

import com.cjyc.common.model.util.DateLongSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
@Data
public class StockCarVo implements Serializable {
    private static final long serialVersionUID = -5471226303328556637L;
    @ApiModelProperty(value = "订单车辆起始城市")
    private String startCity;

    @ApiModelProperty(value = "订单车辆目的城市")
    private String endCity;

    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "型号")
    private String model;

    @ApiModelProperty(value = "车系图片logo")
    private String logoImg;

    @ApiModelProperty(value = "提车日期")
    @JsonSerialize(using = DateLongSerizlizer.class)
    private Long expectStartDate;

    public String getStartCity(){ return StringUtils.isBlank(startCity) ? "":startCity;}
    public String getEndCity() {return StringUtils.isBlank(endCity) ? "":endCity;}
    public Long getExpectStartDate() {return expectStartDate == null ? 0:expectStartDate;}
}