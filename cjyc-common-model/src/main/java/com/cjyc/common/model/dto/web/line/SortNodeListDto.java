package com.cjyc.common.model.dto.web.line;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 入参
 * @author JPG
 */
@Data
@ApiModel
public class SortNodeListDto {
    @NotNull
    @ApiModelProperty(value = "出发地级城市名",required = true)
    private String lineId;
    @NotNull
    @ApiModelProperty(value = "目的地级城市名",required = true)
    private String recommendLine;
    @NotNull
    @ApiModelProperty(value = "目的地级城市名",required = true)
    private List<SortNodeDto> list;
}
