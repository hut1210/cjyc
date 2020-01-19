package com.cjyc.common.model.dto.web.waybill;

import com.cjyc.common.model.dto.web.BaseWebDto;
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
public class CancelWaybillDto extends BaseWebDto {

    @NotEmpty(message = "waybillIdList不能为空")
    @ApiModelProperty(value = "运单ID列表", required = true)
    private List<Long> waybillIdList;
}
