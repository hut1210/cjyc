package com.cjyc.common.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class ItemVo implements Serializable {
    private static final long serialVersionUID = -4211724368453233784L;
    @ApiModelProperty("H5链接地址")
    private String pictureUrl;
    @ApiModelProperty("地址")
    private String url;
}