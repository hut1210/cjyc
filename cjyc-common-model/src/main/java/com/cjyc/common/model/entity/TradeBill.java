package com.cjyc.common.model.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * ping++交易流水表
 * </p>
 *
 * @author JPG
 * @since 2019-10-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("f_trade_bill")
@ApiModel(value="TradeBill对象", description="ping++交易流水表")
public class TradeBill implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "交易单号")
    private String no;

    @ApiModelProperty(value = "ping++支付订单ID")
    private String pingPayId;

    @ApiModelProperty(value = "接口调用模式 : live 模式 或 test模式")
    private String livemode;

    @ApiModelProperty(value = "标题")
    private String subject;

    @ApiModelProperty(value = "商品描述")
    private String body;

    @ApiModelProperty(value = "交易金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "收款方ID")
    private Long receiverId;

    @ApiModelProperty(value = "收款人姓名")
    private String receierName;

    @ApiModelProperty(value = "付款方ID")
    private Long payerId;

    @ApiModelProperty(value = "付款人姓名")
    private String payerName;

    @ApiModelProperty(value = "交易状态： 0未支付，1支付中， 2支付成功，3支付超时，4支付失败")
    private Integer state;

    @ApiModelProperty(value = "支付渠道")
    private String channel;

    @ApiModelProperty(value = "支付渠道名称")
    private String channelName;

    @ApiModelProperty(value = "渠道费率")
    private BigDecimal channelFee;

    @ApiModelProperty(value = "类型：1物流费预付，2物流费全款到付，3物流费分车支付， 11运费支付、12居间服务费支付")
    private Integer type;

    @ApiModelProperty(value = "回调事件ID")
    private String eventId;

    @ApiModelProperty(value = "事件类型")
    private String eventType;

    @ApiModelProperty(value = "来源单主单编号")
    private String sourceMainNo;

    @ApiModelProperty(value = "交易备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "交易时间")
    private Long tradeTime;

}
