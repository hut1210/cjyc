package com.cjyc.common.model.dto.web.finance;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author:Hut
 * @Date:2019/11/25 18:27
 */
@Data
public class WaitQueryDto extends BasePageDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "结算流水号")
    private  String serialNumber;

    @ApiModelProperty(value = "客户名称")
    private  String customerName;

    @ApiModelProperty(value = "纳税人识别号")
    private String taxPayerNumber;

    @ApiModelProperty(value = "客户账号")
    private  String phone;

    @ApiModelProperty(value = "申请结算开始时间")
    private  Long applyStartTime;

    @ApiModelProperty(value = "申请结算结束时间")
    private  Long applyEndTime;

    @ApiModelProperty(value = "申请人")
    private  String applicant;

    @ApiModelProperty(value = "发票类型")
    private  String type;

    @ApiModelProperty(value = "确认开票开始时间")
    private  Long invoiceStartTime;

    @ApiModelProperty(value = "确认开票结束时间")
    private  Long invoiceEndTime;

    @ApiModelProperty(value = "确认人")
    private  String confirmMan;

    @ApiModelProperty(value = "核销开始时间")
    private  Long writeOffStartTime;

    @ApiModelProperty(value = "核销结束时间")
    private  Long writeOffEndTime;

    @ApiModelProperty(value = "核销人")
    private  String writeOffmMan;

}
