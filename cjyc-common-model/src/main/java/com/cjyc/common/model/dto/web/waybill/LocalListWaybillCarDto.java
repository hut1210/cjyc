package com.cjyc.common.model.dto.web.waybill;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LocalListWaybillCarDto extends BasePageDto {
    @ApiModelProperty(value = "业务中心ID")
    private Long storeId;


    @ApiModelProperty(value = "品牌")
    private String brand;
    @ApiModelProperty(value = "型号")
    private String model;
    @ApiModelProperty(value = "车牌号")
    private String plateNo;
    @ApiModelProperty(value = "vin码")
    private String vin;


    @ApiModelProperty(value = "运单类型：1提车运单，2干线运单，3送车运单")
    private Integer type;
    @ApiModelProperty(value = "承运商类型：0承运商，1业务员，2客户自己")
    private Integer carrierType;


    @ApiModelProperty(value = "运单编号")
    private String waybillNo;
    @ApiModelProperty(value = "车辆编号")
    private String orderCarNo;
    @ApiModelProperty(value = "省编码")
    private String startProvinceCode;
    @ApiModelProperty(value = "市编码")
    private String startCityCode;
    @ApiModelProperty(value = "区县编码")
    private String startAreaCode;
    @ApiModelProperty(value = "省编码")
    private String endProvinceCode;
    @ApiModelProperty(value = "市编码")
    private String endCityCode;
    @ApiModelProperty(value = "区县编码")
    private String endAreaCode;
    @ApiModelProperty(value = "起始预计提车日期")
    private Long beginExpectStartTime;
    @ApiModelProperty(value = "起始预计提车日期")
    private Long endExpectStartTime;
    @ApiModelProperty(value = "取车方式:1上门，2 自送/自取")
    private Integer takeType;
    @ApiModelProperty(value = "提车联系人")
    private String loadLinkName;
    @ApiModelProperty(value = "提车联系人userid")
    private Long loadLinkUserId;
    @ApiModelProperty(value = "提车联系人电话")
    private String loadLinkPhone;
    @ApiModelProperty(value = "起始实际开始装车时间")
    private Long beginLoadTime;
    @ApiModelProperty(value = "截止实际开始装车时间")
    private Long endLoadTime;
    @ApiModelProperty(value = "收车人名称")
    private String unloadLinkName;
    @ApiModelProperty(value = "收车联系人userId")
    private Long unloadLinkUserId;
    @ApiModelProperty(value = "收车人电话")
    private String unloadLinkPhone;
    @ApiModelProperty(value = "起始实际完成卸车时间")
    private Long beginUnloadTime;
    @ApiModelProperty(value = "截止实际完成卸车时间")
    private Long endUnloadTime;
}
