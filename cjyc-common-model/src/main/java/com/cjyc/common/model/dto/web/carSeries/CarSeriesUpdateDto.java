package com.cjyc.common.model.dto.web.carSeries;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description 修改品牌车系类
 * @Author LiuXingXiang
 * @Date 2019/10/28 19:33
 **/
@Data
public class CarSeriesUpdateDto implements Serializable {
    private static final long serialVersionUID = 8972038509829772756L;
    @NotNull(message = "ID不能为空")
    @ApiModelProperty(value = "id",required = true)
    private Long id;

    @NotBlank(message = "品牌不能为空")
    @ApiModelProperty(value = "品牌",required = true)
    private String brand;

    @NotBlank(message = "型号不能为空")
    @ApiModelProperty(value = "型号",required = true)
    private String model;

    @NotNull(message = "修改人ID不能为空")
    @ApiModelProperty(value = "最后修改人ID",required = true)
    private Long updateUserId;
}
