package com.cjyc.common.model.dto.web.config;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class ConfigDto extends BasePageDto implements Serializable {

    public interface QueryConfigDto {
    }

    public interface UpdateConfigDto {
    }

    @ApiModelProperty("登陆用户userId")
    @NotNull(groups = {ConfigDto.UpdateConfigDto.class},message = "登陆用户userId不能为空")
    private Long userId;

    @ApiModelProperty("系统配置id")
    @NotNull(groups = {ConfigDto.UpdateConfigDto.class},message = "系统配置id不能为空")
    private Long id;

    @ApiModelProperty("标志 0：查询 2：更新")
    @NotNull(groups = {ConfigDto.QueryConfigDto.class},message = "标志不能为空")
    @NotNull(groups = {ConfigDto.UpdateConfigDto.class},message = "标志不能为空")
    private Integer flag;

    @ApiModelProperty("开关状态 0：关 1：开")
    @NotNull(groups = {ConfigDto.UpdateConfigDto.class},message = "开关状态不能为空")
    private Integer state;
}