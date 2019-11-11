package com.cjyc.common.model.dto.web.waybill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 入参
 * @author JPG
 */
@Data
@ApiModel
public class CancelWaybillDto {

    @ApiModelProperty(value = "用户ID",required = true)
    private Long userId;
    @ApiModelProperty(value = "运单号", required = true)
    private List<String> waybillNoList;
}
