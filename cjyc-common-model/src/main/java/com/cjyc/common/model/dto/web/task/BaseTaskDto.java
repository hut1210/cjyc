package com.cjyc.common.model.dto.web.task;

import com.cjyc.common.model.dto.BaseLoginDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BaseTaskDto extends BaseLoginDto {

    @NotNull(message = "taskId不能为空")
    @ApiModelProperty(value = "任务ID")
    private Long taskId;

    @NotEmpty(message = "taskCarIdList不能为空")
    @ApiModelProperty(value = "任务车辆ID",required = true)
    private List<Long> taskCarIdList;
}
