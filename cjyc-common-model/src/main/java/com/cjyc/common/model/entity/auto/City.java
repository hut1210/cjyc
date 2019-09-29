package com.cjyc.common.model.entity.auto;

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
 * 韵车城市信息表
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("s_city")
public class City implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 行政区编码（含大区、省、市、区）
     */
    @TableId(value = "code", type = IdType.ID_WORKER)
    private String code;

    /**
     * 上级城市编码
     */
    @TableField("parent_code")
    private String parentCode;

    /**
     * 行政区名称
     */
    @TableField("name")
    private String name;

    /**
     * 行政区级别： 0大区， 1省， 2市， 3区县
     */
    @TableField("level")
    private Boolean level;

    /**
     * 经度
     */
    @TableField("lng")
    private String lng;

    /**
     * 纬度
     */
    @TableField("lat")
    private String lat;

    /**
     * 热门城市： 0否， 1是
     */
    @TableField("hot")
    private Boolean hot;

    @TableField("create_time")
    private Long createTime;

    @TableField("update_time")
    private Long updateTime;


}
