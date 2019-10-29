package com.cjyc.common.model.dto.web.carSeries;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description 品牌车系新增类
 * @Author LiuXingXiang
 * @Date 2019/10/28 16:18
 **/
@Data
public class CarSeriesAddDto implements Serializable {
    private static final long serialVersionUID = -4448813455764326600L;
    @NotBlank(message = "品牌不能为空")
    @ApiModelProperty(value = "品牌",required = true)
    private String brand;

    @NotBlank(message = "型号不能为空")
    @ApiModelProperty(value = "型号;添加多个时，用逗号(,)隔开",required = true)
    private String model;

    @NotNull(message = "创建人ID不能为空")
    @ApiModelProperty(value = "创建人ID",required = true)
    private Long createUserId;
}
