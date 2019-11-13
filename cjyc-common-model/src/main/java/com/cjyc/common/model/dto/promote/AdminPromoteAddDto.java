package com.cjyc.common.model.dto.promote;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description 分享新增参数类
 * @Author Liu Xing Xiang
 * @Date 2019/11/13 11:09
 **/
@Data
public class AdminPromoteAddDto implements Serializable {
    @ApiModelProperty(value = "分享渠道")
    @NotBlank(message = "分享渠道不能为空")
    private String channel;

    @ApiModelProperty(value = "分享人账号")
    @NotNull(message = "分享人账号不能为空")
    private Long userId;

    @ApiModelProperty(value = "分享人名称")
    @NotBlank(message = "分享人名称不能为空")
    private String userName;
}
