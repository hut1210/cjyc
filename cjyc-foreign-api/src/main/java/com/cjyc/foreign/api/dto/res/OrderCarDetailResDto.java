package com.cjyc.foreign.api.dto.res;

import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 订单车辆详情返回类
 * @Author Liu Xing Xiang
 * @Date 2020/3/11 17:21
 **/
@Data
public class OrderCarDetailResDto implements Serializable {
    private static final long serialVersionUID = 1950307814239204252L;
    @ApiModelProperty(value = "车辆id")
    private Long id;

    @ApiModelProperty("车辆编码")
    private String no;

    @ApiModelProperty(value = "车辆估值/万")
    private Integer valuation;

    @ApiModelProperty(value = "保险费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal addInsuranceFee;

    @ApiModelProperty(value = "保费/万")
    private Integer addInsuranceAmount;

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
    public Integer getIsMove() {
        return isMove == null ? 0 : isMove;
    }
    public Integer getIsNew() {
        return isNew == null ? 0 : isNew;
    }
    public String getLogoImg() {
        return StringUtils.isBlank(logoImg) ? "" : logoImg;
    }
    public String getBrand() {
        return StringUtils.isBlank(brand) ? "" : brand;
    }
    public String getModel() {
        return StringUtils.isBlank(model) ? "" : model;
    }
    public String getPlateNo() {
        return StringUtils.isBlank(plateNo) ? "" : plateNo;
    }
    public String getVin() {
        return StringUtils.isBlank(vin) ? "" : vin;
    }
    public List<String> getCarImgList() {
        return CollectionUtils.isEmpty(carImgList) ? new ArrayList<>(0) : carImgList;
    }
    public BigDecimal getAddInsuranceFee() {
        return addInsuranceFee == null ? new BigDecimal(0) : addInsuranceFee;
    }
    public Integer getAddInsuranceAmount() {
        return addInsuranceAmount == null ? 0 : addInsuranceAmount;
    }
}
