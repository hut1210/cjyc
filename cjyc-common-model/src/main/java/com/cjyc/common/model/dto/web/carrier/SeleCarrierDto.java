package com.cjyc.common.model.dto.web.carrier;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class SeleCarrierDto extends BasePageDto {

    @ApiModelProperty("企业名称")
    private String name;

    @ApiModelProperty("联系人")
    private String linkman;

    @ApiModelProperty("联系电话")
    private String linkmanPhone;

    @ApiModelProperty("银行卡号")
    private String cardNo;

    @ApiModelProperty("法人姓名")
    private String legalName;

    @ApiModelProperty("法人身份证号")
    private String legalIdCard;

    @ApiModelProperty("是否开发票 0：否  1：是")
    private Integer isInvoice;

    @ApiModelProperty("结算方式：0:时付，1:账期")
    private Integer settleType;

    @ApiModelProperty("状态：0待审核，2已审核，4取消，5冻结，7已驳回，9已停用（CommonStateEnum）")
    private Integer state;

    @ApiModelProperty("操作人")
    private String operateName;

}