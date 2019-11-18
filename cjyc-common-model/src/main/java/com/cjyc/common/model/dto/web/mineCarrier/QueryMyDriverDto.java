package com.cjyc.common.model.dto.web.mineCarrier;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class QueryMyDriverDto extends BasePageDto implements Serializable {

    @ApiModelProperty("登陆系统用户id(loginId)")
    @NotNull(message = "登陆系统用户loginId不能为空")
    private Long loginId;

    @ApiModelProperty("承运商id(carrierId)")
    private Long carrierId;

    @ApiModelProperty("司机姓名")
    private String realName;

    @ApiModelProperty("司机电话")
    private String phone;

    @ApiModelProperty("承运方式：2 : 代驾  3 : 干线   4：拖车")
    private Integer mode;

    @ApiModelProperty("运行状态：0空闲，1在途 2繁忙")
    private Integer runningState;

    @ApiModelProperty("司机状态 2：正常 5冻结")
    private Integer state;

    @ApiModelProperty("司机身份 0：普通司机 1：管理员")
    private Integer identity;

    @ApiModelProperty("车牌号")
    private String plateNo;
}