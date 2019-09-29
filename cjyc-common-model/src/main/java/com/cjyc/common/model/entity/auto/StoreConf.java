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
 * 业务中心配置表
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("s_store_conf")
public class StoreConf implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableField("id")
    private Long id;

    /**
     * 业务中心ID
     */
    @TableField("store_id")
    private Long storeId;

    /**
     * 配置条目
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
    private Integer itemValue;

    /**
     * 备注
     */
    @TableField("rewark")
    private String rewark;


}
