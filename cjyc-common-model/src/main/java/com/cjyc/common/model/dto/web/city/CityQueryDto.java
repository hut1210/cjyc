package com.cjyc.common.model.dto.web.city;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description 城市管理列表查询
 * @Author LiuXingXiang
 * @Date 2019/11/5 16:40
 **/
@Data
public class CityQueryDto extends BasePageDto {
    private static final long serialVersionUID = 7162047061553328314L;
    @ApiModelProperty("大区编码")
    private String regionCode;

    @ApiModelProperty(value = "省编码")
    private String provinceCode;

    @ApiModelProperty(value = "市编码")
    private String cityCode;

    @ApiModelProperty(value = "区编码")
    private String areaCode;
}
