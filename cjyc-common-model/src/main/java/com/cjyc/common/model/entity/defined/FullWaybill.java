package com.cjyc.common.model.entity.defined;

import com.cjyc.common.model.entity.Waybill;
import com.cjyc.common.model.entity.WaybillCar;
import lombok.Data;

import java.util.List;

@Data
public class FullWaybill extends Waybill {
    private List<WaybillCar> waybillCarList;
}
