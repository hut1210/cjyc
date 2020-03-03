package com.cjyc.common.model.vo.salesman.order;

import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SalesOrderCarVo implements Serializable {
    private static final long serialVersionUID = -8119244731311513499L;
    @ApiModelProperty(value = "车辆id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long orderCarId;
    @ApiModelProperty(value = "品牌")
    private String brand;
    @ApiModelProperty(value = "型号")
    private String model;
    @ApiModelProperty(value = "品牌logo")
    private String logoImg;
    @ApiModelProperty(value = "是否新车 0-否 1-是")
    private Integer isNew;
    @ApiModelProperty(value = "是否能动 0-否 1-是")
    private Integer isMove;
    @ApiModelProperty(value = "估值/万")
    private Integer valuation;
    @ApiModelProperty(value = "追加保额/万")
    private Integer addInsuranceAmount;
    @ApiModelProperty(value = "保费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal addInsuranceFee;
    @ApiModelProperty(value = "vin码")
    private String vin;
    @ApiModelProperty(value = "车牌号")
    private String plateNo;

    @ApiModelProperty(value = "提车费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal pickFee;
    @ApiModelProperty(value = "送车费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal backFee;
    @ApiModelProperty(value = "物流费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal trunkFee;
    @ApiModelProperty(value = "单车总运费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal totalFee;

    public String getBrand(){return StringUtils.isBlank(brand) ? "":brand;}
    public String getModel(){return StringUtils.isBlank(model) ? "":model;}
    public String getLogoImg(){return StringUtils.isBlank(logoImg) ? "":logoImg;}
    public Integer getIsNew(){return isNew == null ? 0:isNew;}
    public Integer getIsMove(){return isMove == null ? 0:isMove;}
    public Integer getValuation(){return valuation == null ? 0:valuation;}
    public Integer getAddInsuranceAmount(){return addInsuranceAmount == null ? 0:addInsuranceAmount;}
    public String getVin(){return StringUtils.isBlank(vin) ? "":vin;}
    public String getPlateNo(){return StringUtils.isBlank(plateNo) ? "":plateNo;}
    public BigDecimal getPickFee(){return pickFee == null ? BigDecimal.ZERO:pickFee;}
    public BigDecimal getBackFee(){return backFee == null ? BigDecimal.ZERO:backFee;}
    public BigDecimal getAddInsuranceFee(){return addInsuranceFee == null ? BigDecimal.ZERO:addInsuranceFee;}
    public BigDecimal getTrunkFee(){return trunkFee == null ? BigDecimal.ZERO:trunkFee;}
    public BigDecimal getTotalFee(){return totalFee == null ? BigDecimal.ZERO:totalFee;}
}