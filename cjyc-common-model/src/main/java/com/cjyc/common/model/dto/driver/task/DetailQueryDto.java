package com.cjyc.common.model.dto.driver.task;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

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
    public interface GetSalesmanWaybillDetail{}

    @ApiModelProperty(value = "运单id")
    @NotNull(groups = {GetHistoryDetail.class,GetNoHandleDetail.class,GetSalesmanWaybillDetail.class},message = "运单id不能为空")
    private Long waybillId;

    @ApiModelProperty(value = "任务单id")
    @NotNull(groups = {GetHistoryDetail.class,GetSalesmanWaybillDetail.class},message = "任务单id不能为空")
    private Long taskId;

    @ApiModelProperty(value = "司机端详情类型：0全部，1待提车，2待交车，3已交付")
    @NotBlank(groups = {GetHistoryDetail.class},message = "详情类型不能为空")
    @Pattern(regexp = "(0|1|2|3)",message = "详情类型只能是0,1,2,3中的一位数")
    private String detailType;

    @ApiModelProperty(value = "司业务员端详情状态：1待提车，2待交车，3已交付，4待入库，5已入库，6待出库，7已出库")
    @NotBlank(groups = {GetSalesmanWaybillDetail.class},message = "详情类型不能为空")
    @Pattern(regexp = "(1|2|3|4|5|6|7)",message = "详情状态只能是1,2,3,4,5,6,7中的一位数")
    private String detailState;

    @ApiModelProperty(value = "登录id")
    @NotNull(groups = {GetSalesmanWaybillDetail.class},message = "登录id不能为空")
    private Long loginId;

    private Set<Long> storeIds;

    private Long waybillCarId;
}
