package com.cjyc.common.model.dto.web.dispatch;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
public class ValidateLineShellDto {

    @ApiModelProperty(value = "是否检验线路")
    private boolean validateLine = false;
    @Valid
    private List<ValidateLineDto> list;
}
