package com.cjyc.common.model.dto.web.task;

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
public class OutStoreTaskDto {
    @ApiModelProperty(value = "登录用户ID", required = true)
    private Long loginId;
    @ApiModelProperty(hidden = true)
    private String loginName;
    @ApiModelProperty(hidden = true)
    private String loginPhone;
    @ApiModelProperty(hidden = true)
    private UserTypeEnum loginType;

    @ApiModelProperty(value = "任务ID", required = true)
    private Long taskId;

    @ApiModelProperty(value = "任务车辆ID", required = true)
    private List<Long> taskCarIdList;
}