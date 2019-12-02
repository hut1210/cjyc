package com.cjyc.common.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AppItemVo implements Serializable {
    private static final long serialVersionUID = 8409257959635441689L;

    @ApiModelProperty("app系统轮播图")
    private List<String> appSystemPicture;
}