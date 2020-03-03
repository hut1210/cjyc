package com.cjyc.common.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Author: Hut
 * @Date: 2020/02/12 13:44
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("w_order_refund")
@ApiModel(value="Refund对象", description="退款")
public class Refund {

    private String orderNo;

    private Long createTime;

    private int state;
}
