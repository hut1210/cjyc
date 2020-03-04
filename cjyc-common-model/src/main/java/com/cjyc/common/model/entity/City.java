package com.cjyc.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 城市表
 * </p>
 *
 * @author JPG
 * @since 2019-10-29
 */
@Data
@Accessors(chain = true)
@TableName("s_city")
@ApiModel(value="City对象", description="城市表")
public class City implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "编码")
    @TableId(value = "code", type = IdType.NONE)
    private String code;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "级别")
    private Integer level;

    @ApiModelProperty(value = "上级行政区编码")
    private String parentCode;

    @ApiModelProperty(value = "上级行政区名称")
    private String parentName;

    private String pinYin;

    private String pinInitial;

    private String pinAcronym;

    @ApiModelProperty(value = "经度")
    private String lng;

    @ApiModelProperty(value = "纬度")
    private String lat;

    @ApiModelProperty(value = "热门城市：0否，1是")
    private Boolean hot;

    @ApiModelProperty(value = "备注")
    private String remark;
}
