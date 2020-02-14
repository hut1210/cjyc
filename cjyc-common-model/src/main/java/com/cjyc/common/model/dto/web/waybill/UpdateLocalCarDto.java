package com.cjyc.common.model.dto.web.waybill;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class UpdateLocalCarDto{

    @NotNull(message = "车辆ID不能为空")
    @ApiModelProperty(value = "ID")
    private Long id;
    @NotNull(message = "运单ID不能为空")
    @ApiModelProperty(value = "运单ID")
    private Long waybillId;
    @NotNull(message = "运单编号不能为空")
    @ApiModelProperty(value = "运单编号")
    private String waybillNo;
    @NotNull(message = "订单车辆ID不能为空")
    @ApiModelProperty(value = "订单车辆ID")
    private Long orderCarId;
    @NotBlank(message = "车辆编号不能为空")
    @ApiModelProperty(value = "车辆编号")
    private String orderCarNo;
    @NotNull(message = "运费不能为空")
    @ApiModelProperty(value = "运费")
    private BigDecimal freightFee;
    @ApiModelProperty(value = "省")
    private String startProvince;
    @ApiModelProperty(value = "省编码")
    private String startProvinceCode;
    @ApiModelProperty(value = "市")
    private String startCity;
    @ApiModelProperty(value = "市编码")
    private String startCityCode;
    @ApiModelProperty(value = "区")
    private String startArea;
    @NotBlank(message = "区县编码不能为空")
    @ApiModelProperty(value = "区县编码")
    private String startAreaCode;
    @NotBlank(message = "始发地地址不能为空")
    @ApiModelProperty(value = "装车地址")
    private String startAddress;

    @ApiModelProperty(value = "出发地业务中心名称")
    private String startStoreName;

    @ApiModelProperty(value = "出发地业务中心ID")
    private Long startStoreId;

    @ApiModelProperty(value = "省")
    private String endProvince;
    @ApiModelProperty(value = "省编码")
    private String endProvinceCode;
    @ApiModelProperty(value = "市")
    private String endCity;
    @ApiModelProperty(value = "市编码")
    private String endCityCode;
    @ApiModelProperty(value = "区")
    private String endArea;
    @NotBlank(message = "目的地区县编码不能为空")
    @ApiModelProperty(value = "区县编码")
    private String endAreaCode;
    @NotBlank(message = "目的地地址不能为空")
    @ApiModelProperty(value = "卸车地址")
    private String endAddress;

    @ApiModelProperty(value = "目的地业务中心名称")
    private String endStoreName;

    @ApiModelProperty(value = "目的地业务中心ID")
    private Long endStoreId;

    @ApiModelProperty(value = "线路ID")
    private Long lineId;

    @NotNull(message = "预计提车日期不能为空")
    @ApiModelProperty(value = "预计提车日期")
    private Long expectStartTime;

    @ApiModelProperty(value = "预计到达时间")
    private Long expectEndTime;

    @NotBlank(message = "提车联系人不能为空")
    @ApiModelProperty(value = "提车联系人")
    private String loadLinkName;

    @ApiModelProperty(value = "提车联系人userid")
    private Long loadLinkUserId;
    @NotBlank(message = "提车联系人电话不能为空")
    @ApiModelProperty(value = "提车联系人电话")
    private String loadLinkPhone;
    @NotBlank(message = "收车人不能为空")
    @ApiModelProperty(value = "收车人名称")
    private String unloadLinkName;

    @ApiModelProperty(value = "收车联系人userId")
    private Long unloadLinkUserId;
    @NotBlank(message = "收车人电话不能为空")
    @ApiModelProperty(value = "收车人电话")
    private String unloadLinkPhone;

}
