package com.cjyc.foreign.api.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Description 班线请求参数
 * @Author Liu Xing Xiang
 * @Date 2020/3/10 10:20
 **/
@Data
public class LineReqDto implements Serializable {
    private static final long serialVersionUID = 4079246204301360452L;
    @ApiModelProperty(value = "起点城市编码",required = true)
    @NotBlank(message = "起点城市编码不能为空")
    private String fromCode;

    @ApiModelProperty(value = "终点城市编码",required = true)
    @NotBlank(message = "终点城市编码不能为空")
    private String toCode;
}
