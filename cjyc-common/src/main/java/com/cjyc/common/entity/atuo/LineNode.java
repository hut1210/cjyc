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
 * 线路节点顺序表
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("s_line_node")
public class LineNode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableField("id")
    private Long id;

    /**
     * 线路ID
     */
    @TableField("line_id")
    private Long lineId;

    /**
     * 节点，逗号分隔
     */
    @TableField("nodes")
    private String nodes;

    /**
     * 默认标识
     */
    @TableField("default_flag")
    private Integer defaultFlag;

    /**
     * 状态
     */
    @TableField("state")
    private Integer state;


}
