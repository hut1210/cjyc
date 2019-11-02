package com.cjyc.common.model.vo.customer.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class OrderCarCenterVo implements Serializable {
    private static final long serialVersionUID = 3239344642824513783L;
    @ApiModelProperty(value = "车辆id")
    private Long id;

    @ApiModelProperty(value = "估值/万")
    private String valuation;

    @ApiModelProperty(value = "车辆应收保险费")
    private String insuranceFee;

    @ApiModelProperty(value = "保额/万")
    private String insuredAmount;

    @ApiModelProperty("车辆图片链接")
    private String logoImg;

    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "型号")
    private String model;

    @ApiModelProperty(value = "车牌号")
    private String plateNo;

    @ApiModelProperty(value = "vin码")
    private String vin;

    @ApiModelProperty(value = "是否能动 0-否 1-是")
    private String isMove;

    public String getLogoImg() {
        return logoImg == null ? "" : logoImg;
    }

    public String getModel() {
        return model == null ? "" : model;
    }

    public String getPlateNo() {
        return plateNo == null ? "" : plateNo;
    }

    public String getVin() {
        return vin == null ? "" : vin;
    }
}