package com.cjyc.common.model.vo.web.city;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

@Data
public class ProvinceCityTreeVo implements Serializable {

    @ApiModelProperty("省/直辖市编码")
    private String provinceCode;

    @ApiModelProperty("省/直辖市名称")
    private String provinceName;

    @ApiModelProperty("下属城市")
    List<HashMap<String,String>> cityVos;
}