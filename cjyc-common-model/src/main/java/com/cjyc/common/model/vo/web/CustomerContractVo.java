package com.cjyc.common.model.vo.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class CustomerContractVo implements Serializable {

    public interface SaveCustomerContractVo {
    }

    public interface UpdateCustomerContractVo {
    }

    @ApiModelProperty(value = "合同主键id")
    private Long id;

    @NotBlank(groups = {SaveCustomerContractVo.class},message = "合同编号不能为空")
    @NotBlank(groups = {UpdateCustomerContractVo.class},message = "合同编号不能为空")
    @ApiModelProperty(value = "合同编号",required = true)
    private String contractNo;

    @NotNull(groups = {SaveCustomerContractVo.class},message = "合同性质不能为空")
    @NotNull(groups = {UpdateCustomerContractVo.class},message = "合同性质不能为空")
    @ApiModelProperty(value = "合同性质 0：框式  1：制式",required = true)
    private Integer contactNature;

    @NotNull(groups = {SaveCustomerContractVo.class},message = "账期不能为空")
    @NotNull(groups = {UpdateCustomerContractVo.class},message = "账期不能为空")
    @ApiModelProperty(value = "账期 0：时付  1: 30天   2: 60天  3：90天  4：120天  5：150天",required = true)
    private Integer settlePeriod;

    @NotBlank(groups = {SaveCustomerContractVo.class},message = "合同有效期不能为空")
    @NotBlank(groups = {UpdateCustomerContractVo.class},message = "合同有效期不能为空")
    @ApiModelProperty(value = "合同有效期",required = true)
    private String contractLife;

    @NotBlank(groups = {SaveCustomerContractVo.class},message = "项目名称不能为空")
    @NotBlank(groups = {UpdateCustomerContractVo.class},message = "项目名称不能为空")
    @ApiModelProperty(value = "项目名称",required = true)
    private String projectName;

    @NotNull(groups = {SaveCustomerContractVo.class},message = "项目级别不能为空")
    @NotNull(groups = {UpdateCustomerContractVo.class},message = "项目级别不能为空")
    @ApiModelProperty(value = "项目级别 0：一级  1：二级",required = true)
    private Integer projectLevel;

    @NotNull(groups = {SaveCustomerContractVo.class},message = "主要产品不能为空")
    @NotNull(groups = {UpdateCustomerContractVo.class},message = "主要产品不能为空")
    @ApiModelProperty(value = "主要产品 0：新车  1：二手车 2：新车+二手车",required = true)
    private Integer majorProduct;

    @NotNull(groups = {SaveCustomerContractVo.class},message = "项目性质不能为空")
    @NotNull(groups = {UpdateCustomerContractVo.class},message = "项目性质不能为空")
    @ApiModelProperty(value = "项目性质 0：新开  1：存量",required = true)
    private Integer projectNature;

    @NotBlank(groups = {SaveCustomerContractVo.class},message = "项目签署日期不能为空")
    @NotBlank(groups = {UpdateCustomerContractVo.class},message = "项目签署日期不能为空")
    @ApiModelProperty(value = "项目签署日期",required = true)
    private String dateOfProSign;

    @NotNull(groups = {SaveCustomerContractVo.class},message = "一次性合同不能为空")
    @NotNull(groups = {UpdateCustomerContractVo.class},message = "一次性合同不能为空")
    @ApiModelProperty(value = "一次性合同 0：否   1：是",required = true)
    private Integer oneOffContract;

    @ApiModelProperty(value = "项目预计运量")
    private Long proTraVolume;

    @ApiModelProperty(value = "月度平均运量")
    private Long avgMthTraVolume;

    @ApiModelProperty(value = "业务覆盖范围")
    private String busiCover;

    @ApiModelProperty(value = "固定路线")
    private String fixedRoute;

    @NotBlank(groups = {SaveCustomerContractVo.class},message = "项目开发人员不能为空")
    @NotBlank(groups = {UpdateCustomerContractVo.class},message = "项目开发人员不能为空")
    @ApiModelProperty(value = "项目开发人员",required = true)
    private String projectDeper;

    @NotBlank(groups = {SaveCustomerContractVo.class},message = "项目负责人不能为空")
    @NotBlank(groups = {UpdateCustomerContractVo.class},message = "项目负责人不能为空")
    @ApiModelProperty(value = "项目负责人",required = true)
    private String projectLeader;

    @NotBlank(groups = {SaveCustomerContractVo.class},message = "负责人电话不能为空")
    @NotBlank(groups = {UpdateCustomerContractVo.class},message = "负责人电话不能为空")
    @ApiModelProperty(value = "负责人电话",required = true)
    private String leaderPhone;

    @NotNull(groups = {SaveCustomerContractVo.class},message = "项目状态不能为空")
    @NotNull(groups = {UpdateCustomerContractVo.class},message = "项目状态不能为空")
    @ApiModelProperty(value = "项目状态 0：停止   1：正常",required = true)
    private Integer projectStatus;

    @ApiModelProperty(value = "项目团队成员")
    private String projectTeamPer;

    @ApiModelProperty(value = "立项日期")
    private String projectEstabTime;

    @ApiModelProperty(value = "主要KPI")
    private String majorKpi;
}