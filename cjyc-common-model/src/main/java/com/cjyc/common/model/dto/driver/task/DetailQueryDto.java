package com.cjyc.common.model.dto.driver.task;

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
    public interface GetHistoryDetail{}
    public interface GetNoHandleDetail{}

    @ApiModelProperty(value = "运单id")
    @NotNull(groups = {GetHistoryDetail.class,GetNoHandleDetail.class},message = "运单id不能为空")
    private Long waybillId;

    @ApiModelProperty(value = "任务单id")
    @NotNull(groups = {GetHistoryDetail.class},message = "任务单id不能为空")
    private Long taskId;

    @ApiModelProperty(value = "详情类型：0全部，1待提车，2待交车")
    @Pattern(groups = {GetHistoryDetail.class},regexp = "(0|1|2)",message = "详情类型只能是0,1,2中的一位数")
    private String detailType;

}
