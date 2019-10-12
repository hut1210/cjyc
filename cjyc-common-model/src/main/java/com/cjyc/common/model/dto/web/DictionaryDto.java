package com.cjyc.common.model.dto.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class DictionaryDto implements Serializable {

    public interface SaveDictionaryDto {
    }

    public interface UpdDictionaryDto {
    }

    @ApiModelProperty("字典项id")
    @NotNull(groups = {DictionaryDto.UpdDictionaryDto.class},message = "字典项id不能为空")
    private Integer id;

    @ApiModelProperty("字典项名称")
    @NotBlank(groups = {DictionaryDto.SaveDictionaryDto.class},message = "字典项名称不能为空")
    @NotBlank(groups = {DictionaryDto.UpdDictionaryDto.class},message = "字典项名称不能为空")
    private String name;

    @ApiModelProperty("字典项/条目")
    @NotBlank(groups = {DictionaryDto.SaveDictionaryDto.class},message = "字典项/条目不能为空")
    @NotBlank(groups = {DictionaryDto.UpdDictionaryDto.class},message = "字典项/条目不能为空")
    private String item;

    @ApiModelProperty("字典键")
    @NotBlank(groups = {DictionaryDto.SaveDictionaryDto.class},message = "字典键不能为空")
    @NotBlank(groups = {DictionaryDto.UpdDictionaryDto.class},message = "字典键不能为空")
    private String itemKey;

    @ApiModelProperty("字典值")
    @NotBlank(groups = {DictionaryDto.SaveDictionaryDto.class},message = "字典值不能为空")
    @NotBlank(groups = {DictionaryDto.UpdDictionaryDto.class},message = "字典值不能为空")
    private String itemValue;

    @ApiModelProperty("字典单位")
    @NotBlank(groups = {DictionaryDto.SaveDictionaryDto.class},message = "字典单位不能为空")
    @NotBlank(groups = {DictionaryDto.UpdDictionaryDto.class},message = "字典单位不能为空")
    private String itemUnit;

    @ApiModelProperty("备注")
    private String remark;
}