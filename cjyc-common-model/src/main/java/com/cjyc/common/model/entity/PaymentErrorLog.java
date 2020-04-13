package com.cjyc.common.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Author: Hut
 * @Date: 2020/02/22 17:27
 */
@Data
@Accessors(chain = true)
@TableName("f_payment_error_log")
@ApiModel(value = "PaymentErrorLog对象", description = "打款错误日志")
public class PaymentErrorLog {

    /**
     * 错误信息
     */
    private String remark;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 运单编号
     */
    private String waybillNo;

    /**
     * 床创建时间
     */
    private Long createTime;
}
