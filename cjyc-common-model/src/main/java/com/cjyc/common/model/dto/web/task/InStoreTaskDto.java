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
public class InStoreTaskDto {

    @NotNull(message = "用户ID不能为空")
    @ApiModelProperty(value = "用户Id",required = true)
    private Long loginId;

    @ApiModelProperty(value = "用户名称")
    private String loginName;

    @NotNull(message = "入库业务中心ID不能为空")
    @ApiModelProperty(value = "入库业务中心ID")
    private Long storeId;

    @NotNull(message = "任务车辆ID不能为空")
    @ApiModelProperty(value = "任务车辆ID",required = true)
    private List<Long> taskCarIdList;
}
