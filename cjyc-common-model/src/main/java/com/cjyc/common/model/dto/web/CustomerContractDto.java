package com.cjyc.common.model.dto.web;

import lombok.Data;

import java.io.Serializable;

@Data
public class CustomerContractDto implements Serializable {

    /**
     * 合同主键id
     */
    private Long id;

    /**
     * 合同编号
     */
    private String contractNo;

    /**
     * 合同性质  0：框式  1：制式
     */
    private Integer contactNature;

    /**
     * 账期 0：时付  1: 30天   2: 60天  3：90天  4：120天  5：150天
     */
    private Integer settlePeriod;

    /**
     * 合同有效期
     */
    private String contractLife;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目级别  0：一级  1：二级
     */
    private Integer projectLevel;

    /**
     * 主要产品 0：新车  1：二手车 2：新车+二手车
     */
    private Integer majorProduct;

    /**
     * 项目性质 0：新开  1：存量
     */
    private Integer projectNature;

    /**
     * 项目签署日期
     */
    private String dateOfProSign;

    /**
     * 一次性合同 0：否   1：是
     */
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
    private String projectDeper;

    /**
     * 项目负责人
     */
    private String projectLeader;

    /**
     * 负责人电话
     */
    private String leaderPhone;

    /**
     * 项目状态 0：停止   1：正常
     */
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