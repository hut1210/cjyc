package com.cjyc.common.model.vo.web.order;

import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class OrderVo extends Order {

    /**其余参数继承order*/
    @ApiModelProperty("出发业务中心地址")
    private String startStoreAddress;
    @ApiModelProperty("目的业务中心地址")
    private String endStoreAddress;
    @ApiModelProperty("所属业务中心地址")
    private String inputStoreAddress;
    @ApiModelProperty("物流券")
    private String couponName;
    @ApiModelProperty("总物流费")

    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal wlTotalFee;
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal totalAgencyFee;

    @ApiModelProperty("车辆列表")
    private List<OrderCar> OrderCarList;
}
