package com.cjyc.common.model.entity;

import java.math.BigDecimal;
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
 * 运单表(业务员调度单)
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("w_waybill")
public class Waybill implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 运单编号
     */
    @TableField("waybill_no")
    private String waybillNo;

    /**
     * 运单类型：1提车运单，2送车运单，8干线运单
     */
    @TableField("type")
    private Integer type;

    /**
     * 调度类型：1自己处理，2人工调度
     */
    @TableField("dispatch_type")
    private Integer dispatchType;

    /**
     * 指导线路
     */
    @TableField("guide_line")
    private String guideLine;

    /**
     * 推荐线路
     */
    @TableField("recommend_line")
    private String recommendLine;

    /**
     * 承运商ID
     */
    @TableField("carrier_id")
    private Long carrierId;

    /**
     * 车数量
     */
    @TableField("car_num")
    private Integer carNum;

    /**
     * 运单状态：
0待分配承运商（竞抢），
15待承运商承接任务，
30运输中，
100已完成，
102已撤回，
103已拒接，
111超时关闭
     */
    @TableField("state")
    private Integer state;

    /**
     * 运单总运费
     */
    @TableField("freight_fee")
    private BigDecimal freightFee;

    /**
     * 运费支付状态
     */
    @TableField("freight_pay_state")
    private Integer freightPayState;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;

    /**
     * 运费支付时间
     */
    @TableField("freight_pay_time")
    private String freightPayTime;

    /**
     * 运费支付流水单号
     */
    @TableField("freight_pay_billno")
    private String freightPayBillno;

    /**
     * 调度人
     */
    @TableField("create_user")
    private String createUser;

    /**
     * 调度人ID
     */
    @TableField("create_user_id")
    private Long createUserId;


}
