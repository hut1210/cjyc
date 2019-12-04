package com.cjyc.common.model.vo.web.line;

import com.cjyc.common.model.util.BigDecimalSerizlizer;
import com.cjyc.common.model.util.DataLongSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class LineVo implements Serializable {

    private static final long serialVersionUID = -5565506994979750717L;
    @ApiModelProperty("班线id")
    private Long id;

    @ApiModelProperty("起始省编码")
    private String fromProvinceCode;

    @ApiModelProperty("起始省")
    private String fromProvince;

    @ApiModelProperty("起始城市编码")
    private String fromCityCode;

    @ApiModelProperty("起始城市")
    private String fromCity;

    @ApiModelProperty("目的省编码")
    private String toProvinceCode;

    @ApiModelProperty("目的省")
    private String toProvince;

    @ApiModelProperty("目的城市编码")
    private String toCityCode;

    @ApiModelProperty("目的城市")
    private String toCity;

    @ApiModelProperty("上游运费(元)")
    private BigDecimal defaultWlFee;

    @ApiModelProperty("下游运费(元)")
    private BigDecimal defaultFreightFee;

    @ApiModelProperty("总里程")
    private BigDecimal kilometer;

    @ApiModelProperty("总耗时(天)")
    private Integer days;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建时间")
    @JsonSerialize(using = DataLongSerizlizer.class)
    private Long createTime;

    @ApiModelProperty("创建人员")
    private String createName;

    @ApiModelProperty("修改人")
    private String updateName;

    @ApiModelProperty("修改时间")
    @JsonSerialize(using = DataLongSerizlizer.class)
    private Long updateTime;
}