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
 * 城市信息表
 * </p>
 *
 * @author JPG
 * @since 2019-10-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("s_city")
@ApiModel(value="City对象", description="城市信息表")
public class City implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "地区编码（含大区、省、市、区）")
    @TableId(value = "code", type = IdType.AUTO)
    private String code;

    @ApiModelProperty(value = "上级城市编码")
    private String parentCode;

    @ApiModelProperty(value = "城市名称")
    private String name;

    @ApiModelProperty(value = "行政区级别： 0大区， 1省， 2市， 3区县")
    private Integer level;

    @ApiModelProperty(value = "经度")
    private String lng;

    @ApiModelProperty(value = "纬度")
    private String lat;

    @ApiModelProperty(value = "热门城市： 0否， 1是")
    private Boolean hot;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "更新时间")
    private Long updateTime;


}
