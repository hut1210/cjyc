package com.cjyc.common.model.dto.web.carrier;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class DispatchCarrierDto extends BasePageDto implements Serializable {
    private static final long serialVersionUID = -7496908972850812115L;

    @ApiModelProperty("登陆id(loginId)")
    private Long loginId;

    @ApiModelProperty("企业名称")
    private String name;

    @ApiModelProperty("联系人")
    private String linkman;

    @ApiModelProperty("结算方式：0时付，1账期")
    private Integer settleType;

    @ApiModelProperty("联系电话")
    private String linkmanPhone;
}