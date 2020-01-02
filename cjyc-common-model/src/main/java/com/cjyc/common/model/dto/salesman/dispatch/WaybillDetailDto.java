package com.cjyc.common.model.dto.salesman.dispatch;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description 历史调度记录明细查询参数
 * @Author Liu Xing Xiang
 * @Date 2020/1/2 13:05
 **/
@Data
public class WaybillDetailDto implements Serializable {
    private static final long serialVersionUID = 3633919203723353335L;
    @ApiModelProperty(value = "运单ID")
    @NotNull(message = "运单ID不能为空")
    private Long waybillId;
}
