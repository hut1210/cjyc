package com.cjyc.common.model.dto.web.line;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class AddOrUpdateLineDto implements Serializable {
    private static final long serialVersionUID = 5221537417581694290L;

    @ApiModelProperty("登陆人的id(loginId)")
    @NotNull(message = "登陆人的id(loginId)不能为空")
    private Long loginId;

    @ApiModelProperty("班线id(lineId)")
    private Long lineId;

    @ApiModelProperty("起始城市编码")
    @NotBlank(message = "起始城市编码不能为空")
    private String fromCode;

    @ApiModelProperty("起始城市")
    @NotBlank(message = "起始城市不能为空")
    private String fromCity;

    @ApiModelProperty("目的城市编码")
    @NotBlank(message = "目的城市编码不能为空")
    private String toCode;

    @ApiModelProperty("目的城市")
    @NotBlank(message = "目的城市不能为空")
    private String toCity;

    @ApiModelProperty("总里程")
    @NotNull(message = "总里程不能为空")
    private BigDecimal kilometer;

    @ApiModelProperty("总耗时(天)")
    @NotNull(message = "总耗时(天)不能为空")
    private BigDecimal days;

    @ApiModelProperty("上游运费(元)/物流费")
    @NotNull(message = "上游运费(元)/物流费不能为空")
    private BigDecimal defaultWlFee;

    @ApiModelProperty("下游运费(元)/运费")
    @NotNull(message = "下游运费(元)/运费不能为空")
    private BigDecimal defaultFreightFee;

    @ApiModelProperty("备注")
    private String remark;
}