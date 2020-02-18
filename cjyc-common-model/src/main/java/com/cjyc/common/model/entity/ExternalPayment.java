package com.cjyc.common.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Author: Hut
 * @Date: 2020/02/11 12:58
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("f_external_payment")
@ApiModel(value="ExternalPayment对象", description="打款详细日志")
public class ExternalPayment {

    private Long carrierId;

    private Long waybillId;

    @ApiModelProperty("打款类型 1给承运商 2给合伙人")
    private int type;

    private Long orderId;

    private Integer state;

    private Long payTime;
}
