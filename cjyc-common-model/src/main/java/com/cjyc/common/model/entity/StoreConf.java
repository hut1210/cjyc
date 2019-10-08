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
 * 业务中心配置表
 * </p>
 *
 * @author JPG
 * @since 2019-10-08
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
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 业务中心ID
     */
    private Long storeId;

    /**
     * 配置条目
     */
    private String item;

    /**
     * 键
     */
    private String itemKey;

    /**
     * 值
     */
    private Integer itemValue;

    /**
     * 备注
     */
    private String rewark;


}
