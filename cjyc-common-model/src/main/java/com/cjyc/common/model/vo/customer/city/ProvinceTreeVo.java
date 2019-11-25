package com.cjyc.common.model.vo.customer.city;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ProvinceTreeVo implements Serializable {
    private static final long serialVersionUID = -86760864313927082L;
    @ApiModelProperty("城市父编码")
    private String parentCode;

    @ApiModelProperty("城市编码")
    private String code;

    @ApiModelProperty("城市名称")
    private String name;

    @ApiModelProperty("城市树")
    private List<CityTreeVo> cityVos;

}