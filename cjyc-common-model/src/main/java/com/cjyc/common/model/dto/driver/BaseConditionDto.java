package com.cjyc.common.model.dto.driver;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * @Description 基础查询条件
 * @Author Liu Xing Xiang
 * @Date 2019/11/19 15:10
 **/
@Data
public class BaseConditionDto extends BaseDriverDto{
    private static final long serialVersionUID = -6926657129770237023L;
    @ApiModelProperty(value = "运单编号")
    private String waybillNo;

    @ApiModelProperty(value = "运单类型：1提车运单，2干线运单，3送车运单")
    private Integer type;

    @ApiModelProperty(value = "提车日期开始")
    private Long expectStartDateS;

    @ApiModelProperty(value = "提车日期结束")
    private Long expectStartDateE;

    @ApiModelProperty(value = "交车日期开始")
    private Long completeTimeS;

    @ApiModelProperty(value = "交车日期结束")
    private Long completeTimeE;

    @ApiModelProperty(value = "起始地")
    private String lineStart;

    @ApiModelProperty(value = "目的地")
    private String lineEnd;

    // 查询条件值 "任务状态：0待承接，5待装车，55运输中，100已完成，113已取消，115已拒接"
    @ApiModelProperty(value = "任务状态：1-分配任务，2-提车任务，3-交车任务，4-已交付")
    @Pattern(regexp = "[1|2|3|4]",message = "任务状态只能是1,2,3,4中的一位数")
    private String taskState;
}
