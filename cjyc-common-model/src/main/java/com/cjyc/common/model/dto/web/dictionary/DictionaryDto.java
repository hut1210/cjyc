package com.cjyc.common.model.dto.web.dictionary;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class DictionaryDto implements Serializable {

    private static final long serialVersionUID = -6805423342689391790L;
    @ApiModelProperty(value = "字典项id",required = true)
    @NotNull(message = "字典项id不能为空")
    private Long id;

    @ApiModelProperty(value = "字典项名称",required = true)
    @NotBlank(message = "字典项名称不能为空")
    private String name;

    @ApiModelProperty(value = "字典值",required = true)
    @NotBlank(message = "字典值不能为空")
    private String itemValue;

    @ApiModelProperty(value = "字典单位",required = true)
    @NotBlank(message = "字典单位不能为空")
    private String itemUnit;
}