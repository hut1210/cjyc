package com.cjyc.common.model.entity.defined;

import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.entity.OrderCar;
import lombok.Data;

import java.util.List;

@Data
public class FullOrder extends Order {

    private List<OrderCar> list;
}
