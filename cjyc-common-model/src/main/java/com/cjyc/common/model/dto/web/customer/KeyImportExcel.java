package com.cjyc.common.model.dto.web.customer;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class KeyImportExcel implements Serializable {
    private static final long serialVersionUID = -7803062204570834476L;

    @Excel(name = "客户全称" ,orderNum = "0")
    private String name;

    @Excel(name = "统一社会编码" ,orderNum = "1")
    private String socialCreditCode;

    @Excel(name = "联系人/业务对接人" ,orderNum = "2")
    private String contactMan;

    @Excel(name = "联系电话" ,orderNum = "3")
    private String contactPhone;

    @Excel(name = "客户地址" ,orderNum = "4")
    private String contactAddress;

    @Excel(name = "客户性质  0：电商 1：租赁 2：金融公司 3：经销商 4：其他" ,orderNum = "5")
    private Integer customerNature;

    @Excel(name = "合同编号" ,orderNum = "6")
    private String contractNo;

    @Excel(name = "合同性质 0：框式  1：制式" ,orderNum = "7")
    private Integer contactNature;

    @Excel(name = "结算类型 0:时付 1：账期" ,orderNum = "8")
    private Integer settleType;

    @Excel(name = "账期/天" ,orderNum = "9")
    private Integer settlePeriod;

    @Excel(name = "合同有效期" ,orderNum = "10")
    private Long contractLife;

    @Excel(name = "项目名称" ,orderNum = "11")
    private String projectName;

    @Excel(name = "项目级别" ,orderNum = "12")
    private Integer projectLevel;

    @Excel(name = "主要产品" ,orderNum = "13")
    private Integer majorProduct;

    @Excel(name = "项目性质" ,orderNum = "14")
    private Integer projectNature;

    @Excel(name = "项目预计运量" ,orderNum = "15")
    private BigDecimal proTraVolume;

    @Excel(name = "月度平均运量" ,orderNum = "16")
    private BigDecimal avgMthTraVolume;

    @Excel(name = "业务覆盖范围" ,orderNum = "17")
    private String busiCover;

    @Excel(name = "固定路线" ,orderNum = "18")
    private String fixedRoute;

    @Excel(name = "项目开发人员" ,orderNum = "19")
    private String projectDeper;

    @Excel(name = "项目负责人" ,orderNum = "20")
    private String projectLeader;

    @Excel(name = "负责人电话" ,orderNum = "21")
    private String leaderPhone;

    @Excel(name = "项目状态 0：停止 1：正常" ,orderNum = "22")
    private Integer projectStatus;

    @Excel(name = "项目团队成员" ,orderNum = "23")
    private String projectTeamPer;

    @Excel(name = "立项日期" ,orderNum = "24")
    private Long projectEstabTime;

    @Excel(name = "主要KPI" ,orderNum = "25")
    private String majorKpi;
}