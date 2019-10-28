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
 * 财务付款单表
 * </p>
 *
 * @author JPG
 * @since 2019-10-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("f_finance_payment")
@ApiModel(value="FinancePayment对象", description="财务付款单表")
public class FinancePayment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "收款用户ID")
    private Long userId;

    @ApiModelProperty(value = "收款用户名称")
    private String userName;

    @ApiModelProperty(value = "类型：1运费，2中介费，3推广费")
    private Integer source;

    @ApiModelProperty(value = "应付账款")
    private BigDecimal amount;

    @ApiModelProperty(value = "实付账款")
    private BigDecimal realAmount;

    @ApiModelProperty(value = "收款卡号")
    private String bankCardNo;

    @ApiModelProperty(value = "收款银行名称")
    private String bankName;

    @ApiModelProperty(value = "结算方式：0时付，1账期")
    private Integer payType;

    @ApiModelProperty(value = "支付状态： 0待结算， 1结算中，2已结算")
    private Integer payState;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "结算编号")
    private String tradeBillNo;

    @ApiModelProperty(value = "结算时间")
    private Long payTime;


}
