package com.cjyc.common.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 车系管理
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("s_car_series")
public class CarSeries implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 车辆ID
     */
    @TableField("car_code")
    private String carCode;

    /**
     * 品牌
     */
    @TableField("brand")
    private String brand;

    /**
     * 型号
     */
    @TableField("model")
    private String model;

    /**
     * 分类： 1微型车，2小型车，3中型车，4大型车， 5其他车
     */
    @TableField("type")
    private Boolean type;

    /**
     * 拼音首字母
     */
    @TableField("pin_initial")
    private String pinInitial;

    /**
     * 拼音首字母缩写
     */
    @TableField("pin_acronym")
    private String pinAcronym;

    @TableField("logo_img")
    private String logoImg;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private String createTime;

    /**
     * 创建人
     */
    @TableField("create_user_id")
    private String createUserId;

    /**
     * 最后修改时间
     */
    @TableField("update_time")
    private String updateTime;

    /**
     * 最后修改人
     */
    @TableField("update_user_id")
    private String updateUserId;


}
