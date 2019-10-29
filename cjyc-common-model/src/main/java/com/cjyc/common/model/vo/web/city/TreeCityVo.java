package com.cjyc.common.model.vo.web.city;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

@Data
public class TreeCityVo<T> implements Serializable {

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("名称")
    private String level;

    @TableField(exist = false)
    @ApiModelProperty("下属城市")
    List<T> next;
}