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
@ApiModel(value="PaymentErrorLog对象", description="打款错误日志")
public class PaymentErrorLog {

    private String remark;

    private Long createTime;
}
