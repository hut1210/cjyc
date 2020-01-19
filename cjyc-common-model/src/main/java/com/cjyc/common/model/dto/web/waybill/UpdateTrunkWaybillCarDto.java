package com.cjyc.common.model.dto.web.waybill;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class UpdateTrunkWaybillCarDto {


    @NotNull(message = "id不能为空")
    @ApiModelProperty(value = "ID")
    private Long id;
    @NotNull(message = "waybillId不能为空")
    @ApiModelProperty(value = "运单ID")
    private Long waybillId;
    @NotNull(message = "waybillNo不能为空")
    @ApiModelProperty(value = "运单编号")
    private String waybillNo;
    @NotNull(message = "orderCarId不能为空")
    @ApiModelProperty(value = "订单车辆ID")
    private Long orderCarId;
    @NotBlank(message = "orderCarNo不能为空")
    @ApiModelProperty(value = "车辆编号")
    private String orderCarNo;
    @NotNull(message = "freightFee不能为空")
    @ApiModelProperty(value = "运费")
    private BigDecimal freightFee;
    @NotBlank(message = "startProvince不能为空")
    @ApiModelProperty(value = "省")
    private String startProvince;
    @NotBlank(message = "startProvinceCode不能为空")
    @ApiModelProperty(value = "省编码")
    private String startProvinceCode;
    @NotBlank(message = "startCity不能为空")
    @ApiModelProperty(value = "市")
    private String startCity;
    @NotBlank(message = "startCityCode不能为空")
    @ApiModelProperty(value = "市编码")
    private String startCityCode;
    @NotBlank(message = "startArea不能为空")
    @ApiModelProperty(value = "区")
    private String startArea;
    @NotBlank(message = "startAreaCode不能为空")
    @ApiModelProperty(value = "区县编码")
    private String startAreaCode;
    @NotBlank(message = "startAddress不能为空")
    @ApiModelProperty(value = "装车地址")
    private String startAddress;

    @ApiModelProperty(value = "出发地业务中心名称")
    private String startStoreName;

    @ApiModelProperty(value = "出发地业务中心ID")
    private Long startStoreId;

    @NotBlank(message = "endProvince不能为空")
    @ApiModelProperty(value = "省")
    private String endProvince;
    @NotBlank(message = "endProvinceCode不能为空")
    @ApiModelProperty(value = "省编码")
    private String endProvinceCode;
    @NotBlank(message = "endCity不能为空")
    @ApiModelProperty(value = "市")
    private String endCity;
    @NotBlank(message = "endCityCode不能为空")
    @ApiModelProperty(value = "市编码")
    private String endCityCode;
    @NotBlank(message = "endArea不能为空")
    @ApiModelProperty(value = "区")
    private String endArea;
    @NotBlank(message = "endAreaCode不能为空")
    @ApiModelProperty(value = "区县编码")
    private String endAreaCode;
    @NotBlank(message = "endAddress不能为空")
    @ApiModelProperty(value = "卸车地址")
    private String endAddress;

    @ApiModelProperty(value = "目的地业务中心名称")
    private String endStoreName;

    @ApiModelProperty(value = "目的地业务中心ID")
    private Long endStoreId;

    @ApiModelProperty(value = "线路ID")
    private Long lineId;

    @NotNull(message = "expectStartTime不能为空")
    @ApiModelProperty(value = "预计提车日期")
    private Long expectStartTime;

    @ApiModelProperty(value = "预计到达时间")
    private Long expectEndTime;

    @NotNull(message = "loadLinkName不能为空")
    @ApiModelProperty(value = "提车联系人")
    private String loadLinkName;

    @ApiModelProperty(value = "提车联系人userid")
    private Long loadLinkUserId;
    @NotNull(message = "loadLinkPhone不能为空")
    @ApiModelProperty(value = "提车联系人电话")
    private String loadLinkPhone;

    @ApiModelProperty(value = "收车人名称")
    private String unloadLinkName;

    @ApiModelProperty(value = "收车联系人userId")
    private Long unloadLinkUserId;

    @ApiModelProperty(value = "收车人电话")
    private String unloadLinkPhone;


}
