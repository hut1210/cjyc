package com.cjyc.common.model.dto.web.mimeCarrier;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class QueryMyCarDto extends BasePageDto implements Serializable {

    private static final long serialVersionUID = 8614314182597568522L;

    @ApiModelProperty("登陆系统用户id(loginId)")
    @NotNull(message = "登陆系统用户loginId不能为空")
    private Long loginId;

    @ApiModelProperty("承运商id(carrierId)")
    private Long carrierId;

    @ApiModelProperty("车牌号")
    private String plateNo;

    @ApiModelProperty("司机姓名")
    private String realName;

    @ApiModelProperty("司机手机号")
    private String phone;

    @ApiModelProperty("承运方式：2 : 代驾  3 : 干线   4：拖车")
    private Integer mode;
}