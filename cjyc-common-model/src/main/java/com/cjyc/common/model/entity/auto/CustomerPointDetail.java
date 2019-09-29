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
 * 用户积分明细表
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("c_customer_point_detail")
public class CustomerPointDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 来源：1订单完结
     */
    @TableField("source")
    private Integer source;

    /**
     * 来源编号：订单编号
     */
    @TableField("source_no")
    private String sourceNo;

    /**
     * 类型：0收入，1支出
     */
    @TableField("type")
    private Integer type;

    /**
     * 积分数量
     */
    @TableField("point_num")
    private Integer pointNum;

    /**
     * 说明
     */
    @TableField("description")
    private String description;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;


}
