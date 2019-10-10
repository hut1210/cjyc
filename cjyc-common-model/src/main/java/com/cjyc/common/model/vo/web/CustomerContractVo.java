package com.cjyc.common.model.vo.web;

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

    /**
     * 合同主键id
     */
    private Long id;

    /**
     * 合同编号
     */
    @NotBlank(groups = {SaveCustomerContractVo.class},message = "合同编号不能为空")
    @NotBlank(groups = {UpdateCustomerContractVo.class},message = "合同编号不能为空")
    private String contractNo;

    /**
     * 合同性质  0：框式  1：制式
     */
    @NotNull(groups = {SaveCustomerContractVo.class},message = "合同性质不能为空")
    @NotNull(groups = {UpdateCustomerContractVo.class},message = "合同性质不能为空")
    private Integer contactNature;

    /**
     * 账期 0：时付  1: 30天   2: 60天  3：90天  4：120天  5：150天
     */
    @NotNull(groups = {SaveCustomerContractVo.class},message = "账期不能为空")
    @NotNull(groups = {UpdateCustomerContractVo.class},message = "账期不能为空")
    private Integer settlePeriod;

    /**
     * 合同有效期
     */
    @NotBlank(groups = {SaveCustomerContractVo.class},message = "合同有效期不能为空")
    @NotBlank(groups = {UpdateCustomerContractVo.class},message = "合同有效期不能为空")
    private String contractLife;

    /**
     * 项目名称
     */
    @NotBlank(groups = {SaveCustomerContractVo.class},message = "项目名称不能为空")
    @NotBlank(groups = {UpdateCustomerContractVo.class},message = "项目名称不能为空")
    private String projectName;

    /**
     * 项目级别  0：一级  1：二级
     */
    @NotNull(groups = {SaveCustomerContractVo.class},message = "项目级别不能为空")
    @NotNull(groups = {UpdateCustomerContractVo.class},message = "项目级别不能为空")
    private Integer projectLevel;

    /**
     * 主要产品 0：新车  1：二手车 2：新车+二手车
     */
    @NotNull(groups = {SaveCustomerContractVo.class},message = "主要产品不能为空")
    @NotNull(groups = {UpdateCustomerContractVo.class},message = "主要产品不能为空")
    private Integer majorProduct;

    /**
     * 项目性质 0：新开  1：存量
     */
    @NotNull(groups = {SaveCustomerContractVo.class},message = "项目性质不能为空")
    @NotNull(groups = {UpdateCustomerContractVo.class},message = "项目性质不能为空")
    private Integer projectNature;

    /**
     * 项目签署日期
     */
    @NotBlank(groups = {SaveCustomerContractVo.class},message = "项目签署日期不能为空")
    @NotBlank(groups = {UpdateCustomerContractVo.class},message = "项目签署日期不能为空")
    private String dateOfProSign;

    /**
     * 一次性合同 0：否   1：是
     */
    @NotNull(groups = {SaveCustomerContractVo.class},message = "一次性合同不能为空")
    @NotNull(groups = {UpdateCustomerContractVo.class},message = "一次性合同不能为空")
    private Integer oneOffContract;

    /**
     * 项目预计运量
     */
    private Long proTraVolume;

    /**
     * 月度平均运量
     */
    private Long avgMthTraVolume;

    /**
     * 业务覆盖范围
     */
    private String busiCover;

    /**
     * 固定路线
     */
    private String fixedRoute;

    /**
     * 项目开发人员
     */
    @NotBlank(groups = {SaveCustomerContractVo.class},message = "项目开发人员不能为空")
    @NotBlank(groups = {UpdateCustomerContractVo.class},message = "项目开发人员不能为空")
    private String projectDeper;

    /**
     * 项目负责人
     */
    @NotBlank(groups = {SaveCustomerContractVo.class},message = "项目负责人不能为空")
    @NotBlank(groups = {UpdateCustomerContractVo.class},message = "项目负责人不能为空")
    private String projectLeader;

    /**
     * 负责人电话
     */
    @NotBlank(groups = {SaveCustomerContractVo.class},message = "负责人电话不能为空")
    @NotBlank(groups = {UpdateCustomerContractVo.class},message = "负责人电话不能为空")
    private String leaderPhone;

    /**
     * 项目状态 0：停止   1：正常
     */
    @NotNull(groups = {SaveCustomerContractVo.class},message = "项目状态不能为空")
    @NotNull(groups = {UpdateCustomerContractVo.class},message = "项目状态不能为空")
    private Integer projectStatus;

    /**
     * 项目团队成员
     */
    private String projectTeamPer;

    /**
     * 立项日期
     */
    private String projectEstabTime;

    /**
     * 主要KPI
     */
    private String majorKpi;
}