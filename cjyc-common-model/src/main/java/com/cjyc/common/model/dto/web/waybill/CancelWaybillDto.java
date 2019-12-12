package com.cjyc.common.model.dto.web.waybill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 入参
 * @author JPG
 */
@Data
@ApiModel
public class CancelWaybillDto {

    @NotNull(message = "loginId不能为空")
    @ApiModelProperty(value = "用户ID",required = true)
    private Long loginId;

    @ApiModelProperty(hidden = true)
    private String loginName;

    @NotEmpty(message = "waybillIdList不能为空")
    @ApiModelProperty(value = "运单ID列表", required = true)
    private List<Long> waybillIdList;
}
