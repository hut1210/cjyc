package com.cjyc.common.model.vo.web.carrier;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class TrailCarrierVo implements Serializable {
    private static final long serialVersionUID = -4994289044268667924L;

    @ApiModelProperty("承运商id")
    private Long carrierId;

    @ApiModelProperty("企业名称")
    private String businessName;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("承运商类型：1个人承运商，2企业承运商")
    private Integer type;

    @ApiModelProperty("车牌号")
    private String plateNo;

    @ApiModelProperty("身份证号")
    private String idCard;

    @ApiModelProperty("承运数")
    private Integer carryCarNum;

    @ApiModelProperty("非空车位")
    private Integer occupiedCarNum;

    @ApiModelProperty("运行状态：0空闲，1在途 2繁忙")
    private Integer runningState;
}