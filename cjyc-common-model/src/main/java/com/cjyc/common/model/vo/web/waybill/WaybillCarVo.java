package com.cjyc.common.model.vo.web.waybill;

import com.cjyc.common.model.entity.WaybillCar;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WaybillCarVo extends WaybillCar {

    @ApiModelProperty(value = "状态")
    private String outterState;

    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "型号")
    private String model;

    @ApiModelProperty(value = "车牌号")
    private String plateNo;

    @ApiModelProperty(value = "vin码")
    private String vin;

    @ApiModelProperty(value = "")
    private String lineFreightFee;

    @ApiModelProperty(value = "距离")
    private String km;


}
