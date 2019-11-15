package com.cjyc.common.model.vo.web.carrier;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class ExistCarrierVo implements Serializable {
    private static final long serialVersionUID = -9133746868504511553L;

    @ApiModelProperty("承运商类型 1个人承运商，2企业承运商")
    private Integer type;

    @ApiModelProperty("企业名称")
    private String name;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("身份证号")
    private String idCard;
}