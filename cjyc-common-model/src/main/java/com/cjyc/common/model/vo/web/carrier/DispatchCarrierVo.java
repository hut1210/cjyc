package com.cjyc.common.model.vo.web.carrier;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
@Data
public class DispatchCarrierVo implements Serializable {
    private static final long serialVersionUID = 7468704802787165228L;

    @ApiModelProperty("承运商id(carrierId)")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long carrierId;

    @ApiModelProperty("企业名称")
    private String name;

    @ApiModelProperty("联系人")
    private String linkman;

    @ApiModelProperty("手机号")
    private String linkmanPhone;

    @ApiModelProperty("法人姓名")
    private String legalName;

    @ApiModelProperty("法人身份证号")
    private String legalIdCard;

    @ApiModelProperty("结算方式：0时付，1账期")
    private Integer settleType;

    @ApiModelProperty("账期时间")
    private Integer settlePeriod;

    @ApiModelProperty("承运方式：2 : 代驾  3 : 干线   4：拖车   5：代驾+干线  6：代驾+拖车  7：干线+拖车  9：代驾+干线+拖车")
    private Integer mode;

    @ApiModelProperty("是否开发票 0：否  1：是")
    private Integer isInvoice;

    public String getName(){return StringUtils.isBlank(name) ? "":name;}
    public String getLinkman(){return StringUtils.isBlank(linkman) ? "":linkman;}
    public String getLinkmanPhone(){return StringUtils.isBlank(linkmanPhone) ? "":linkmanPhone;}
    public Integer getSettlePeriod(){return settlePeriod == null ? 0:settlePeriod;}
    public Integer getIsInvoice(){return isInvoice == null ? 0:isInvoice;}

}