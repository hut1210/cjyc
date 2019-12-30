package com.cjyc.common.model.dto.driver.task;

import com.cjyc.common.model.dto.driver.BaseDriverDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * @Description 未完成任务查询dto
 * @Author Liu Xing Xiang
 * @Date 2019/11/20 15:40
 **/
@Data
public class NoFinishTaskQueryDto extends BaseDriverDto {
    private static final long serialVersionUID = 2351753915517861300L;
    // 查询条件值 "任务状态：0待承接，5待装车，55运输中，100已完成，113已取消，115已拒接"
    @ApiModelProperty(value = "任务状态：2-提车任务，3-交车任务")
    @Pattern(regexp = "(2|3)",message = "任务状态只能是2,3中的一位数")
    private String taskState;
}
