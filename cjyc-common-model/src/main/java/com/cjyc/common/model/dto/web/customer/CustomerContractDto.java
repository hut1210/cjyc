package com.cjyc.common.model.dto.web.customer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CustomerContractDto implements Serializable {

    private static final long serialVersionUID = -560902731674096318L;

    @NotBlank(message = "合同编号不能为空")
    @ApiModelProperty(value = "合同编号",required = true)
    private String contractNo;

    @NotNull(message = "合同性质不能为空")
    @ApiModelProperty(value = "合同性质 0：框式  1：制式",required = true)
    private Integer contactNature;

    @NotNull(message = "结算类型不能为空")
    @ApiModelProperty(value = "结算类型 0:时付 1：账期",required = true)
    private Integer settleType;

    @ApiModelProperty("账期/天")
    private Integer settlePeriod;

    @NotNull(message = "合同有效期不能为空")
    @ApiModelProperty(value = "合同有效期",required = true)
    private Long contractLife;

    @NotBlank(message = "项目名称不能为空")
    @ApiModelProperty(value = "项目名称",required = true)
    private String projectName;

    @NotNull(message = "项目级别不能为空")
    @ApiModelProperty(value = "项目级别 0：一级  1：二级",required = true)
    private Integer projectLevel;

    @NotNull(message = "主要产品不能为空")
    @ApiModelProperty(value = "主要产品 0：新车  1：二手车 2：新车+二手车",required = true)
    private Integer majorProduct;

    @NotNull(message = "项目性质不能为空")
    @ApiModelProperty(value = "项目性质 0：新开  1：存量",required = true)
    private Integer projectNature;

    @ApiModelProperty(value = "项目预计运量")
    private BigDecimal proTraVolume;

    @ApiModelProperty(value = "月度平均运量")
    private BigDecimal avgMthTraVolume;

    @ApiModelProperty(value = "业务覆盖范围")
    private String busiCover;

    @ApiModelProperty(value = "固定路线")
    private String fixedRoute;

    @NotBlank(message = "项目开发人员不能为空")
    @ApiModelProperty(value = "项目开发人员",required = true)
    private String projectDeper;

    @NotBlank(message = "项目负责人不能为空")
    @ApiModelProperty(value = "项目负责人",required = true)
    private String projectLeader;

    @NotBlank(message = "负责人电话不能为空")
    @ApiModelProperty(value = "负责人电话",required = true)
    private String leaderPhone;

    @NotNull(message = "项目状态不能为空")
    @ApiModelProperty(value = "项目状态 0：停止   1：正常",required = true)
    private Integer projectStatus;

    @ApiModelProperty(value = "项目团队成员")
    private String projectTeamPer;

    @ApiModelProperty(value = "立项日期")
    private Long projectEstabTime;

    @ApiModelProperty(value = "主要KPI")
    private String majorKpi;
}