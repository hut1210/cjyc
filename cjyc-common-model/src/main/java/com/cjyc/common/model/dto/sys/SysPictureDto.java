package com.cjyc.common.model.dto.sys;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Description 更新轮播图参数
 * @Author Liu Xing Xiang
 * @Date 2019/11/21 15:01
 **/
@Data
public class SysPictureDto implements Serializable {
    private static final long serialVersionUID = -7607232764399337763L;
    @ApiModelProperty(value = "键")
    @NotBlank(message = "key不能为空")
    private String itemKey;

    @ApiModelProperty(value = "值")
    @NotBlank(message = "值不能为空")
    private String itemValue;
}
