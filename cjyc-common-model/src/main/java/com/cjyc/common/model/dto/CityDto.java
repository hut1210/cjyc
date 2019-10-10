package com.cjyc.common.model.dto;

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
 * 韵车城市信息表
 * </p>
 *
 * @author JPG
 * @since 2019-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CityDto", description="城市Dto")
public class CityDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "行政区编码（含大区、省、市、区）")
    @TableId(value = "code", type = IdType.INPUT)
    private String code;

    @ApiModelProperty(value = "上级城市编码")
    private String parentCode;

    @ApiModelProperty(value = "行政区名称")
    private String name;

    @ApiModelProperty(value = "行政区级别： 0大区， 1省， 2市， 3区县")
    private Boolean level;

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


    @ApiModelProperty(value = "更新时间")
    private int currentPage;
    @ApiModelProperty(value = "更新时间")
    private int pageSize;


}