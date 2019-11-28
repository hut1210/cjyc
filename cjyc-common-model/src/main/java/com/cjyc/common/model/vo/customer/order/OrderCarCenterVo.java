package com.cjyc.common.model.vo.customer.order;

import com.cjyc.common.model.util.BigDecimalSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderCarCenterVo implements Serializable {
    private static final long serialVersionUID = 3239344642824513783L;
    @ApiModelProperty(value = "车辆id")
    private Long id;

    @ApiModelProperty(value = "车辆估值/万")
    private Integer valuation;

    @ApiModelProperty(value = "追加保险费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal insuranceFee;

    @ApiModelProperty(value = "保费/万")
    private Integer insuredAmount;

    @ApiModelProperty("车辆logo图片链接")
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
    private Integer isMove;

    @ApiModelProperty(value = "是否新车 0-否 1-是")
    private Integer isNew;

    @ApiModelProperty(value = "车辆图片列表")
    private List<String> carImgList;

    public Integer getValuation() {
        return valuation == null ? 0 : valuation;
    }
    public BigDecimal getInsuranceFee() {
        return insuranceFee == null ? new BigDecimal(0) : insuranceFee;
    }
    public Integer getInsuredAmount() {
        return insuredAmount == null ? 0 : insuredAmount;
    }
    public Integer getIsMove() {
        return isMove == null ? 0 : isMove;
    }
    public Integer getIsNew() {
        return isNew == null ? 0 : isNew;
    }
    public String getLogoImg() {
        return logoImg == null ? "" : logoImg;
    }
    public String getBrand() {
        return brand == null ? "" : brand;
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
    public List<String> getCarImgList() {
        return carImgList == null ? new ArrayList<>(0) : carImgList;
    }
}