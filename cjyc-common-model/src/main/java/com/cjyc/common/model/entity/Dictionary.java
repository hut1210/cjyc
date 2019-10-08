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
 * 字典配置表
 * </p>
 *
 * @author JPG
 * @since 2019-10-08
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
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 条目
     */
    private String item;

    /**
     * 键
     */
    private String itemKey;

    /**
     * 值
     */
    private String itemValue;

    /**
     * 单位
     */
    private String itemUnit;

    /**
     * 是否固定值：1不可修改，0可修改
     */
    private Integer fixedFlag;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态：0无效，1有效
     */
    private Integer state;

    /**
     * 创建时间
     */
    private Long createTime;


}
