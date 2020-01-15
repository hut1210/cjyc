package com.cjyc.common.model.dto.web.carrier;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class TrailCarrierDto extends BasePageDto {
    private static final long serialVersionUID = -8220451193541684094L;
    @ApiModelProperty("登陆id(loginId)")
    private Long loginId;

    @ApiModelProperty("承运方式: 代驾:2,5,6,9 拖车:4,6,7,9")
    private List<Integer> mode;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("身份证号")
    private String idCard;

    @ApiModelProperty("车牌号")
    private String plateNo;

    @ApiModelProperty("承运商类型：1个人承运商，2企业承运商")
    private Integer type;

    @ApiModelProperty(value = "订单起始/目的城市code")
    private String cityCode;

}