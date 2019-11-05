package com.cjyc.common.model.vo.web.customer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CustomerContractVo implements Serializable {

    @ApiModelProperty(value = "合同主键id")
    private Long id;

    @ApiModelProperty(value = "合同编号")
    private String contractNo;

    @ApiModelProperty(value = "合同性质  0：框式  1：制式")
    private Integer contactNature;

    @ApiModelProperty(value = "结算类型 0:账期 1：时付")
    private Integer settleType;

    @ApiModelProperty("账期/天")
    private Integer settlePeriod;

    @ApiModelProperty(value = "合同有效期")
    private String contractLife;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "项目级别  0：一级  1：二级")
    private Integer projectLevel;

    @ApiModelProperty(value = "主要产品 0：新车  1：二手车 2：新车+二手车")
    private Integer majorProduct;

    @ApiModelProperty(value = "项目性质 0：新开  1：存量")
    private Integer projectNature;

    @ApiModelProperty(value = "项目签署日期")
    private String dateOfProSign;

    @ApiModelProperty(value = "一次性合同 0：否   1：是")
    private Integer oneOffContract;

    @ApiModelProperty(value = "项目预计运量")
    private BigDecimal proTraVolume;

    @ApiModelProperty(value = "月度平均运量")
    private BigDecimal avgMthTraVolume;

    @ApiModelProperty(value = "业务覆盖范围")
    private String busiCover;

    @ApiModelProperty(value = "固定路线")
    private String fixedRoute;

    @ApiModelProperty(value = "项目开发人员")
    private String projectDeper;

    @ApiModelProperty(value = "项目负责人")
    private String projectLeader;

    @ApiModelProperty(value = "负责人电话")
    private String leaderPhone;

    @ApiModelProperty(value = "项目状态 0：停止   1：正常")
    private Integer projectStatus;

    @ApiModelProperty(value = "项目团队成员")
    private String projectTeamPer;

    @ApiModelProperty(value = "立项日期")
    private String projectEstabTime;

    @ApiModelProperty(value = "主要KPI")
    private String majorKpi;
}