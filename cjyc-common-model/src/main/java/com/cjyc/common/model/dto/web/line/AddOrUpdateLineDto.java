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

    public interface AddLineDto {
    }

    public interface UpdateLineDto {
    }

    @ApiModelProperty("保存更新标志 1：保存 2：更新")
    @NotNull(groups = {AddOrUpdateLineDto.AddLineDto.class},message = "保存更新标志不能为空")
    @NotNull(groups = {AddOrUpdateLineDto.UpdateLineDto.class},message = "保存更新标志不能为空")
    private Integer flag;

    @ApiModelProperty("登陆人的userId")
    @NotNull(groups = {AddOrUpdateLineDto.AddLineDto.class},message = "登陆人的userId不能为空")
    @NotNull(groups = {AddOrUpdateLineDto.UpdateLineDto.class},message = "登陆人的userId不能为空")
    private Long userId;

    @ApiModelProperty("班线id")
    @NotNull(groups = {AddOrUpdateLineDto.UpdateLineDto.class},message = "班线id不能为空")
    private Long id;

    @ApiModelProperty("起始城市编码")
    @NotBlank(groups = {AddOrUpdateLineDto.AddLineDto.class},message = "起始城市编码不能为空")
    @NotBlank(groups = {AddOrUpdateLineDto.UpdateLineDto.class},message = "起始城市编码不能为空")
    private String fromCode;

    @ApiModelProperty("起始城市")
    @NotBlank(groups = {AddOrUpdateLineDto.AddLineDto.class},message = "起始城市不能为空")
    @NotBlank(groups = {AddOrUpdateLineDto.UpdateLineDto.class},message = "起始城市不能为空")
    private String fromCity;

    @ApiModelProperty("目的城市编码")
    @NotBlank(groups = {AddOrUpdateLineDto.AddLineDto.class},message = "目的城市编码不能为空")
    @NotBlank(groups = {AddOrUpdateLineDto.UpdateLineDto.class},message = "目的城市编码不能为空")
    private String toCode;

    @ApiModelProperty("目的城市")
    @NotBlank(groups = {AddOrUpdateLineDto.AddLineDto.class},message = "目的城市不能为空")
    @NotBlank(groups = {AddOrUpdateLineDto.UpdateLineDto.class},message = "目的城市不能为空")
    private String toCity;

    @ApiModelProperty("总里程")
    @NotNull(groups = {AddOrUpdateLineDto.AddLineDto.class},message = "总里程不能为空")
    @NotNull(groups = {AddOrUpdateLineDto.UpdateLineDto.class},message = "总里程不能为空")
    private BigDecimal kilometer;

    @ApiModelProperty("总耗时(天)")
    @NotNull(groups = {AddOrUpdateLineDto.AddLineDto.class},message = "总耗时(天)不能为空")
    @NotNull(groups = {AddOrUpdateLineDto.UpdateLineDto.class},message = "总耗时(天)不能为空")
    private BigDecimal days;

    @ApiModelProperty("上游运费(元)/物流费")
    @NotNull(groups = {AddOrUpdateLineDto.AddLineDto.class},message = "上游运费(元)/物流费不能为空")
    @NotNull(groups = {AddOrUpdateLineDto.UpdateLineDto.class},message = "上游运费(元)/物流费不能为空")
    private BigDecimal defaultWlFee;

    @ApiModelProperty("下游运费(元)/运费")
    @NotNull(groups = {AddOrUpdateLineDto.AddLineDto.class},message = "下游运费(元)/运费不能为空")
    @NotNull(groups = {AddOrUpdateLineDto.UpdateLineDto.class},message = "下游运费(元)/运费不能为空")
    private BigDecimal defaultFreightFee;

    @ApiModelProperty("备注")
    private String remark;
}