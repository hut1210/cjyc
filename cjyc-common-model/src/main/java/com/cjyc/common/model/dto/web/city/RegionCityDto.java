package com.cjyc.common.model.dto.web.city;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Description 大区覆盖城市dto
 * @Author LiuXingXiang
 * @Date 2019/11/11 10:08
 **/
@Data
public class RegionCityDto implements Serializable {
    private static final long serialVersionUID = -1870563291213581222L;
    @ApiModelProperty(value = "省编码")
    @NotBlank(message = "省编码不能为空")
    private String code;

    @ApiModelProperty(value = "省名称")
    @NotBlank(message = "省名称不能为空")
    private String name;
}
