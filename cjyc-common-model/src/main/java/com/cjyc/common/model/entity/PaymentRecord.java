package com.cjyc.common.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Author: Hut
 * @Date: 2020/02/22 16:47
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("f_payment_record")
@ApiModel(value="PaymentRecord对象", description="打款日志")
public class PaymentRecord {

    private Long carrierId;

    private Long waybillId;

    @ApiModelProperty("打款类型 1给承运商 2给合伙人")
    private int type;

    private Long orderId;

    private Long createTime;
}
