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
 * ping++交易流水表
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("f_trade_bill")
public class TradeBill implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 交易单号
     */
    @TableField("no")
    private String no;

    /**
     * ping++流水号
     */
    @TableField("ping_pay_no")
    private String pingPayNo;

    /**
     * ping++支付订单ID
     */
    @TableField("ping_pay_id")
    private String pingPayId;

    /**
     * 接口调用模式 : live 模式 或 test模式
     */
    @TableField("livemode")
    private String livemode;

    /**
     * 标题
     */
    @TableField("subject")
    private String subject;

    /**
     * 商品描述
     */
    @TableField("body")
    private String body;

    /**
     * 交易金额
     */
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 收款方ID
     */
    @TableField("receiver_id")
    private Long receiverId;

    /**
     * 收款人姓名
     */
    @TableField("receier_name")
    private String receierName;

    /**
     * 付款方ID
     */
    @TableField("payer_id")
    private Long payerId;

    /**
     * 付款人姓名
     */
    @TableField("payer_name")
    private String payerName;

    /**
     * 交易状态： 0未支付， 2支付成功，4支付失败
     */
    @TableField("state")
    private Integer state;

    /**
     * 支付渠道
     */
    @TableField("channel")
    private String channel;

    /**
     * 支付渠道名称
     */
    @TableField("channel_name")
    private String channelName;

    /**
     * 渠道费率
     */
    @TableField("channel_fee")
    private BigDecimal channelFee;

    /**
     * 类型：1物流费预付，2物流费全款到付，3物流费分车支付， 11运费支付、12居间服务费支付
     */
    @TableField("type")
    private Integer type;

    /**
     * 回调事件ID
     */
    @TableField("event_id")
    private String eventId;

    /**
     * 事件类型
     */
    @TableField("event_type")
    private String eventType;

    /**
     * 交易备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;

    /**
     * 交易时间
     */
    @TableField("trade_time")
    private Long tradeTime;


}
