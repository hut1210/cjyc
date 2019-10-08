package com.cjyc.common.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2019-10-08
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
    private String carCode;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 型号
     */
    private String model;

    /**
     * 分类： 1微型车，2小型车，3中型车，4大型车， 5其他车
     */
    private Boolean type;

    /**
     * 拼音首字母
     */
    private String pinInitial;

    /**
     * 拼音首字母缩写
     */
    private String pinAcronym;

    private String logoImg;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 创建人
     */
    private String createUserId;

    /**
     * 最后修改时间
     */
    private String updateTime;

    /**
     * 最后修改人
     */
    private String updateUserId;


}
