package com.cjyc.common.model.dto.driver;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @Description 明细查询条件
 * @Author Liu Xing Xiang
 * @Date 2019/11/20 10:24
 **/
@Data
public class DetailQueryDto implements Serializable {
    private static final long serialVersionUID = -764356177374166763L;
    @ApiModelProperty("司机id")
    @NotNull(message = "司机id不能为空")
    private Long loginId;

    @ApiModelProperty(value = "运单id")
    @NotNull(message = "运单id不能为空")
    private Long waybillId;

    // 查询条件值 "任务状态：0待承接，5待装车，55运输中，100已完成，113已取消，115已拒接"
    @ApiModelProperty(value = "任务状态：1-分配任务，2-提车任务，3-交车任务，4-已交付")
    @Pattern(regexp = "[1|2|3|4]",message = "任务状态只能是1,2,3,4中的一位数")
    private String taskState;
}
