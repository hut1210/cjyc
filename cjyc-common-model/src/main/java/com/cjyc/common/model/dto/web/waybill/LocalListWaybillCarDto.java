package com.cjyc.common.model.dto.web.waybill;

import com.cjyc.common.model.dto.web.PageWebDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LocalListWaybillCarDto extends PageWebDto {
    @ApiModelProperty(value = "业务中心ID")
    private Long storeId;
    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "品牌")
    private String brand;
    @ApiModelProperty(value = "型号")
    private String model;
    @ApiModelProperty(value = "车牌号")
    private String plateNo;
    @ApiModelProperty(value = "vin码")
    private String vin;
    @ApiModelProperty(value = "状态")
    private Long state;

    @ApiModelProperty(value = "运单类型：1提车运单，2干线运单，3送车运单")
    private Integer type;
    @ApiModelProperty(value = "承运商类型：1干线-个人承运商，2干线-企业承运商，3同城-业务员，4同城-代驾，5同城-拖车，6客户自己")
    private Integer carrierType;

    @ApiModelProperty(value = "承运商")
    private String carrierName;
    @ApiModelProperty(value = "司机")
    private String driverName;
    @ApiModelProperty(value = "司机电话")
    private String driverPhone;
    @ApiModelProperty(value = "车牌号")
    private String vehiclePlateNo;


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
