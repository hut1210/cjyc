package com.cjyc.common.model.dto.web.order;

import com.cjyc.common.model.dto.BaseLoginDto;
import com.cjyc.common.model.dto.web.BaseWebDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AllotOrderDto extends BaseWebDto {
    @NotNull
    @ApiModelProperty(value = "被分配人ID", required = true)
    private Long toAdminId;
    @ApiModelProperty(value = "被分配人名称", hidden = true)
    private String toAdminName;
}
