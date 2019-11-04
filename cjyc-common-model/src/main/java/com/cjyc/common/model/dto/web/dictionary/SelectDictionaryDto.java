package com.cjyc.common.model.dto.web.dictionary;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SelectDictionaryDto extends BasePageDto implements Serializable {

    @ApiModelProperty("字典项名称")
    private String name;
}