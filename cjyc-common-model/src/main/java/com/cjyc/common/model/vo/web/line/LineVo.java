package com.cjyc.common.model.vo.web.line;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class LineVo implements Serializable {

    @ApiModelProperty("班线id")
    private Long id;

    @ApiModelProperty("起始省编码")
    private String startProvinceCode;

    @ApiModelProperty("起始省")
    private String startProvince;

    @ApiModelProperty("起始城市编码")
    private String startCityCode;

    @ApiModelProperty("起始城市")
    private String startCity;

    @ApiModelProperty("目的省编码")
    private String endProvinceCode;

    @ApiModelProperty("目的省")
    private String endProvince;

    @ApiModelProperty("目的城市编码")
    private String endCityCode;

    @ApiModelProperty("目的城市")
    private String endCity;

    @ApiModelProperty("上游运费(元)")
    private BigDecimal defaultWlFee;

    @ApiModelProperty("下游运费(元)")
    private BigDecimal defaultFreightFee;

    @ApiModelProperty("总里程")
    private String kilometer;

    @ApiModelProperty("总耗时(天)")
    private int days;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建时间")
    private String createTime;

    @ApiModelProperty("创建人员")
    private String createUserName;
}