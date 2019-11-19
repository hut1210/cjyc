package com.cjyc.common.model.dto.driver;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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

    //@ApiModelProperty(value = "任务状态：0待承接，5待装车，55运输中，100已完成，113已取消，115已拒接")
    private Integer state;
}
