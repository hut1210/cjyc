package com.cjyc.common.entity.atuo;

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
 * 字典配置表
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("s_dictionary")
public class Dictionary implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 条目
     */
    @TableField("item")
    private String item;

    /**
     * 键
     */
    @TableField("item_key")
    private String itemKey;

    /**
     * 值
     */
    @TableField("item_value")
    private String itemValue;

    /**
     * 单位
     */
    @TableField("item_unit")
    private String itemUnit;

    /**
     * 是否固定值：1不可修改，0可修改
     */
    @TableField("fixed_flag")
    private Integer fixedFlag;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 状态：0无效，1有效
     */
    @TableField("state")
    private Integer state;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;


}
