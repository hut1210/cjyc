package com.cjyc.common.model.vo.web.order;

import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.entity.OrderCar;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class OrderVo extends Order {

    @ApiModelProperty("车辆列表")
    private List<OrderCar> OrderCarList;
}
