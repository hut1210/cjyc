package com.cjyc.foreign.api.dto.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description 城市返回数据
 * @Author Liu Xing Xiang
 * @Date 2020/3/10 14:44
 **/
@Data
public class CityResDto implements Serializable {
    private static final long serialVersionUID = -7613459823447018775L;
    @ApiModelProperty(value = "城市编码")
    private String cityCode;

    @ApiModelProperty(value = "城市名称")
    private String cityName;
}
