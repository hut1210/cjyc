package com.cjyc.common.model.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2019-10-08
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
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 交易单号
     */
    private String no;

    /**
     * ping++流水号
     */
    private String pingPayNo;

    /**
     * ping++支付订单ID
     */
    private String pingPayId;

    /**
     * 接口调用模式 : live 模式 或 test模式
     */
    private String livemode;

    /**
     * 标题
     */
    private String subject;

    /**
     * 商品描述
     */
    private String body;

    /**
     * 交易金额
     */
    private BigDecimal amount;

    /**
     * 收款方ID
     */
    private Long receiverId;

    /**
     * 收款人姓名
     */
    private String receierName;

    /**
     * 付款方ID
     */
    private Long payerId;

    /**
     * 付款人姓名
     */
    private String payerName;

    /**
     * 交易状态： 0未支付， 2支付成功，4支付失败
     */
    private Integer state;

    /**
     * 支付渠道
     */
    private String channel;

    /**
     * 支付渠道名称
     */
    private String channelName;

    /**
     * 渠道费率
     */
    private BigDecimal channelFee;

    /**
     * 类型：1物流费预付，2物流费全款到付，3物流费分车支付， 11运费支付、12居间服务费支付
     */
    private Integer type;

    /**
     * 回调事件ID
     */
    private String eventId;

    /**
     * 事件类型
     */
    private String eventType;

    /**
     * 交易备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 交易时间
     */
    private Long tradeTime;


}
