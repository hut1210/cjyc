package com.cjyc.common.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
@Data
public class AppVersionVo implements Serializable {
    private static final long serialVersionUID = -4036861611123975748L;

    @ApiModelProperty(value = "id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "系统 0：Android  1：IOS")
    private Integer systemType;

    @ApiModelProperty(value = "app类型 0：用户端  1 : 司机端  2：业务员端")
    private Integer appType;

    @ApiModelProperty(value = "app版本号")
    private String version;

    @ApiModelProperty(value = "0：不更新  1：更新  2：强制更新")
    private Integer isUpdate;

    @ApiModelProperty(value = "app更新描述")
    private String message;

    @ApiModelProperty(value = "下载路径")
    private String url;

    public Integer getSystemType() {
        return systemType == null ? -1 : systemType;
    }
    public Integer getAppType() {
        return appType == null ? -1 : appType;
    }
    public Integer getIsUpdate() {
        return isUpdate == null ? -1 : isUpdate;
    }
    public String getVersion() {return StringUtils.isBlank(version) ? "":version;}
    public String getMessage() {return StringUtils.isBlank(message) ? "":message;}
    public String getUrl() {return StringUtils.isBlank(url) ? "":url;}
}