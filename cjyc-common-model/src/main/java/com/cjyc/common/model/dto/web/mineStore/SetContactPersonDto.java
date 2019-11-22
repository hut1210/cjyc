package com.cjyc.common.model.dto.web.mineStore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel
@Validated
public class SetContactPersonDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message = "业务中心标识不能为空")
    @ApiModelProperty(value = "业务中心标识", required = true)
    private Long storeId;
    @NotNull(message = "联系人标识不能为空")
    @ApiModelProperty(value = "联系人标识", required = true)
    private Long contactAdminId;
}
