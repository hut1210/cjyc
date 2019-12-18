package com.cjyc.common.model.vo.web.finance;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Hut
 * @Date: 2019/12/17 16:09
 */
@Data
public class CashCarDetailVo implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "车辆编号")
    private String no;
    @ApiModelProperty(value = "vin码")
    private String vin;
    @ApiModelProperty(value = "订单编号")
    private String orderNo;
}
