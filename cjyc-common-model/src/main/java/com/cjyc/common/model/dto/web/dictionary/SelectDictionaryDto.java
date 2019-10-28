package com.cjyc.common.model.dto.web.dictionary;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class SelectDictionaryDto extends BasePageDto implements Serializable {

    @ApiModelProperty("字典项名称")
    private String name;
}