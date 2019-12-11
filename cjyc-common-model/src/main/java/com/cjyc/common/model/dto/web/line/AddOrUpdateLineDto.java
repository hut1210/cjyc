package com.cjyc.common.model.dto.web.line;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class AddOrUpdateLineDto implements Serializable {
    private static final long serialVersionUID = 5221537417581694290L;

    @ApiModelProperty(value = "登陆人的id(loginId)",required = true)
    @NotNull(message = "登陆人的id(loginId)不能为空")
    private Long loginId;

    @ApiModelProperty("班线id(lineId)")
    private Long lineId;

    @ApiModelProperty(value = "起始城市编码",required =true)
    @NotBlank(message = "起始城市编码不能为空")
    private String fromCode;

    @ApiModelProperty(value = "起始城市",required = true)
    @NotBlank(message = "起始城市不能为空")
    private String fromCity;

    @ApiModelProperty(value = "目的城市编码",required = true)
    @NotBlank(message = "目的城市编码不能为空")
    private String toCode;

    @ApiModelProperty(value = "目的城市",required = true)
    @NotBlank(message = "目的城市不能为空")
    private String toCity;

    @ApiModelProperty(value = "总里程",required = true)
    @NotNull(message = "总里程不能为空")
    @DecimalMin(value = "0.01")
    private BigDecimal kilometer;

    @ApiModelProperty(value = "总耗时(天)",required = true)
    @NotNull(message = "总耗时(天)不能为空")
    private BigDecimal days;

    @ApiModelProperty(value = "上游运费(元)/物流费",required = true)
    @NotNull(message = "上游运费(元)/物流费不能为空")
    private BigDecimal defaultWlFee;

    @ApiModelProperty(value = "下游运费(元)/运费",required = true)
    @NotNull(message = "下游运费(元)/运费不能为空")
    private BigDecimal defaultFreightFee;

    @ApiModelProperty("备注")
    private String remark;
}