package com.cjyc.common.model.dto.web.carSeries;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description 分页查询条件类
 * @Author LiuXingXiang
 * @Date 2019/10/28 19:53
 **/
@Data
public class CarSeriesQueryDto extends BasePageDto {
    private static final long serialVersionUID = -3387389364987750300L;
    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "型号")
    private String model;
}
