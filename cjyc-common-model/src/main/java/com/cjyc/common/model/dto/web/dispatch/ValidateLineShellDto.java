package com.cjyc.common.model.dto.web.dispatch;

import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
public class ValidateLineShellDto {
    @Valid
    private List<ValidateLineDto> list;
}
