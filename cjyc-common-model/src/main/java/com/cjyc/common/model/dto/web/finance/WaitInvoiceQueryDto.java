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
public class WaitInvoiceQueryDto  extends BasePageDto implements Serializable {

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
    private  Long startTime;

    @ApiModelProperty(value = "申请结算结束时间")
    private  Long endTime;

    @ApiModelProperty(value = "申请人")
    private  String applicant;

    @ApiModelProperty(value = "发票类型")
    private  String type;
}
