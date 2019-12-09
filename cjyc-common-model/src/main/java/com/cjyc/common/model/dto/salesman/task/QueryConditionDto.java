package com.cjyc.common.model.dto.salesman.task;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @Description 查询条件对象
 * @Author Liu Xing Xiang
 * @Date 2019/12/9 10:44
 **/
@Data
public class QueryConditionDto extends BasePageDto {
    @ApiModelProperty(value = "用户登录id")
    @NotNull(message = "用户登录id不能为空")
    private Long loginId;

    @ApiModelProperty(value = "任务状态：0全部,1待提车,2待交车")
    @Pattern(regexp = "[0|1|2]",message = "任务状态只能是0,1,2中的一位数")
    private String taskState;

    @ApiModelProperty(value = "接单日期")
    private Long receiveOrderTime;

    @ApiModelProperty(value = "运单号")
    private String waybillNo;

    @ApiModelProperty(value = "出发地")
    private String startAddress;

    @ApiModelProperty(value = "目的地")
    private String endAddress;
}
