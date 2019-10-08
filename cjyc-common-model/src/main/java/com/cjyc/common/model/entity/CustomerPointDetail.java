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
 * 用户积分明细表
 * </p>
 *
 * @author JPG
 * @since 2019-10-08
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
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 来源：1订单完结
     */
    private Integer source;

    /**
     * 来源编号：订单编号
     */
    private String sourceNo;

    /**
     * 类型：0收入，1支出
     */
    private Integer type;

    /**
     * 积分数量
     */
    private Integer pointNum;

    /**
     * 说明
     */
    private String description;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Long createTime;


}
