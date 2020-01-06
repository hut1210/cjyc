package com.cjyc.common.model.dto.web.finance;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Hut
 * @Date: 2020/01/06 11:18
 **/
@Data
public class WaitTicketCollectDto extends BasePageDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "结算流水号")
    private  String serialNumber;

    @ApiModelProperty(value = "申请结算开始时间")
    private  Long applyStartTime;

    @ApiModelProperty(value = "申请结算结束时间")
    private  Long applyEndTime;

    @ApiModelProperty(value = "申请人")
    private  String applicant;
}
