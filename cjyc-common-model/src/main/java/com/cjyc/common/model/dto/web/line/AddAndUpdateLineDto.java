package com.cjyc.common.model.dto.web.line;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class AddAndUpdateLineDto implements Serializable {

    public interface LineDto {
    }

    @ApiModelProperty("保存更新标志 1：保存 2：更新")
    @NotBlank(groups = {AddAndUpdateLineDto.LineDto.class},message = "保存更新标志不能为空")
    private Integer flag;

    @ApiModelProperty("登陆人的userId")
    @NotNull(groups = {AddAndUpdateLineDto.LineDto.class},message = "登陆人的userId不能为空")
    private Long userId;

    @ApiModelProperty("班线id")
    @NotNull(groups = {AddAndUpdateLineDto.LineDto.class},message = "班线id不能为空")
    private Long id;

    @ApiModelProperty("起始省编码")
    private String startProvinceCode;

    @ApiModelProperty("起始省")
    private String startProvince;

    @ApiModelProperty("起始城市编码")
    @NotBlank(groups = {AddAndUpdateLineDto.LineDto.class},message = "起始城市编码不能为空")
    private String startCityCode;

    @ApiModelProperty("起始城市")
    @NotBlank(groups = {AddAndUpdateLineDto.LineDto.class},message = "起始城市不能为空")
    private String startCity;

    @ApiModelProperty("目的省编码")
    private String endProvinceCode;

    @ApiModelProperty("目的省")
    private String endProvince;

    @ApiModelProperty("目的城市编码")
    @NotBlank(groups = {AddAndUpdateLineDto.LineDto.class},message = "目的城市编码不能为空")
    private String endCityCode;

    @ApiModelProperty("目的城市")
    @NotBlank(groups = {AddAndUpdateLineDto.LineDto.class},message = "目的城市不能为空")
    private String endCity;

    @ApiModelProperty("总里程")
    private BigDecimal kilometer;

    @ApiModelProperty("总耗时(天)")
    private BigDecimal days;

    @ApiModelProperty("上游运费(元)")
    private BigDecimal defaultWlFee;

    @ApiModelProperty("下游运费(元)")
    private BigDecimal defaultFreightFee;

    @ApiModelProperty("备注")
    private String remark;
}