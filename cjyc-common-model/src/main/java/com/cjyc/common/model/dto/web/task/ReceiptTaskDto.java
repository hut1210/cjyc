package com.cjyc.common.model.dto.web.task;

import com.cjyc.common.model.enums.ClientEnum;
import com.cjyc.common.model.enums.UserTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class ReceiptTaskDto {
    @NotNull(message = "loginId不能为空")
    @ApiModelProperty(value = "用户Id",required = true)
    private Long loginId;

    @ApiModelProperty(value = "用户名称（不用传）")
    private String loginName;

    @ApiModelProperty(value = "用户手机号（不用传）")
    private String loginPhone;

    @ApiModelProperty(value = "用户类型：1业务员，2司机，3客户（不用传）")
    private UserTypeEnum loginType;

    @ApiModelProperty(value = "客户端类型：1WEB管理后台, 2业务员APP, 3业务员小程序, 4司机APP, 5司机小程序, 6用户端APP, 7用户端小程序（不用传）")
    private ClientEnum clientEnum;

    @NotNull(message = "taskId不能为空")
    @ApiModelProperty(value = "任务ID")
    private String taskId;

    @NotNull(message = "captcha不能为空")
    @ApiModelProperty(value = "验证码")
    private String captcha;

    @ApiModelProperty(value = "任务车辆ID",required = true)
    private List<Long> taskCarIdList;
}
