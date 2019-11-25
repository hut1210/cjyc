package com.cjyc.common.model.dto.web.task;

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

    @ApiModelProperty(value = "登录用户名(不用传)")
    private String loginName;

    @ApiModelProperty(value = "任务ID", required = true)
    private String taskId;

    @ApiModelProperty(value = "任务车辆ID", required = true)
    private List<Long> taskCarIdList;
}