package com.cjyc.common.model.vo.customer.city;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ProvinceTreeVo implements Serializable {
    private static final long serialVersionUID = -86760864313927082L;
    @ApiModelProperty("省编码")
    private String provinceCode;

    @ApiModelProperty("省名称")
    private String provinceName;

    @ApiModelProperty("城市树")
    private List<CityTreeVo> cityVos;

}