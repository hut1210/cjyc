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
 * 
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("zdata_city")
public class ZdataCity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编码
     */
    @TableId(value = "code", type = IdType.ID_WORKER)
    private String code;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 上级行政区编码
     */
    @TableField("parent_code")
    private String parentCode;

    /**
     * 上级行政区名称
     */
    @TableField("parent_name")
    private String parentName;

    /**
     * 上上级行政区编码
     */
    @TableField("grandpa_code")
    private String grandpaCode;

    /**
     * 上上级行政区名称
     */
    @TableField("grandpa_name")
    private String grandpaName;

    /**
     * 级别
     */
    @TableField("level")
    private Boolean level;


}
