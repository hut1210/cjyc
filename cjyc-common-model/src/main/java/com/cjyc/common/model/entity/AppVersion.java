package com.cjyc.common.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author JPG
 * @since 2020-03-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("s_app_version")
@ApiModel(value="AppVersion对象", description="")
public class AppVersion implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "系统 0：Android  1：IOS")
    private Integer systemType;

    @ApiModelProperty(value = "app类型 0：用户端  1 : 司机端  2：业务员端")
    private Integer appType;

    @ApiModelProperty(value = "系统版本号")
    private String version;

    @ApiModelProperty(value = "0：不更新  1：更新  2：强制更新")
    private Integer isUpdate;

    @ApiModelProperty(value = "版本更新描述")
    private String message;

    @ApiModelProperty(value = "下载路径")
    private String url;

    @ApiModelProperty(value = "0：不可用 1：可用")
    private Integer isActive;

    @ApiModelProperty(value = "创建人id")
    private Long createUseId;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "更新人id")
    private Long updateUserId;

    @ApiModelProperty(value = "更新时间")
    private Long updateTime;


}
