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
public class TaskQueryDto extends BaseDriverDto{
    private static final long serialVersionUID = -6926657129770237023L;
    @ApiModelProperty(value = "运单编号")
    private String waybillNo;

    @ApiModelProperty(value = "运单类型：1提车运单，2干线运单，3送车运单")
    @Pattern(regexp = "[1|2|3]",message = "运单类型只能是1,2,3中的一位数")
    private String type;

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
}
