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

    /**其余参数继承order*/
    @ApiModelProperty("出发业务中心地址")
    private String startStoreAddress;
    @ApiModelProperty("目的业务中心地址")
    private String endStoreAddress;
    @ApiModelProperty("所属业务中心地址")
    private String inputStoreAddress;
    @ApiModelProperty("所属业务中心地址")
    private String couponName;

    @ApiModelProperty("车辆列表")
    private List<OrderCar> OrderCarList;
}
