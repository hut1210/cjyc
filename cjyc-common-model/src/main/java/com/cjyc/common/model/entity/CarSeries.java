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
 * 车系管理
 * </p>
 *
 * @author JPG
 * @since 2019-10-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("s_car_series")
@ApiModel(value="CarSeries对象", description="车系管理")
public class CarSeries implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "编号")
    private String carCode;

    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "型号")
    private String model;

    @ApiModelProperty(value = "分类： 1微型车，2小型车，3中型车，4大型车， 5其他车")
    private Boolean type;

    @ApiModelProperty(value = "拼音首字母")
    private String pinInitial;

    @ApiModelProperty(value = "拼音首字母缩写")
    private String pinAcronym;

    private String logoImg;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createUserId;

    @ApiModelProperty(value = "最后修改时间")
    private String updateTime;

    @ApiModelProperty(value = "最后修改人")
    private String updateUserId;


}
