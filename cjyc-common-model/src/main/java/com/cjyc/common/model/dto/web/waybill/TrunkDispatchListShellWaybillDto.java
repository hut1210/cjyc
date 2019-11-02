package com.cjyc.common.model.dto.web.waybill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * @author JPG
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class TrunkDispatchListShellWaybillDto {

    @NotNull(message = "userId不能为空")
    @ApiModelProperty(value = "用户userId", required = true)
    private Long userId;
    @NotNull(message = "storeId不能为空")
    @ApiModelProperty(value = "业务中心ID", required = true)
    private Long storeId;

    @NotEmpty(message = "list不能为空")
    @ApiModelProperty(value = "调度内容", required = true)
    private List<TrunkDispatchListWaybillDto> list;
}
