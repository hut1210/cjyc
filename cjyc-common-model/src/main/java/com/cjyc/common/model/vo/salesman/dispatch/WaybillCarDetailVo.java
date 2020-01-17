package com.cjyc.common.model.vo.salesman.dispatch;

import com.cjyc.common.model.util.BigDecimalSerizlizer;
import com.cjyc.common.model.util.DateLongSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description 运单车辆详情
 * @Author Liu Xing Xiang
 * @Date 2019/11/20 13:26
 **/
@Data
public class WaybillCarDetailVo implements Serializable {
    private static final long serialVersionUID = -6283733541452871958L;
    @ApiModelProperty(value = "运单车辆id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "订单ID")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long orderId;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "运单车辆状态：0待指派，2已指派，5待装车，15待装车确认，45已装车，70已卸车，90确认交车, 100确认收车, 105待重连，120已重连")
    private Integer waybillCarState;

    @ApiModelProperty(value = "提车联系人")
    private String loadLinkName;

    @ApiModelProperty(value = "提车联系人电话")
    private String loadLinkPhone;

    @ApiModelProperty(value = "装车地址")
    private String startAddress;

    @ApiModelProperty(value = "收车人名称")
    private String unloadLinkName;

    @ApiModelProperty(value = "收车人电话")
    private String unloadLinkPhone;

    @ApiModelProperty(value = "卸车地址")
    private String endAddress;

    @ApiModelProperty(value = "提车日期")
    @JsonSerialize(using = DateLongSerizlizer.class)
    private Long expectStartTime;

    @ApiModelProperty(value = "交车日期")
    @JsonSerialize(using = DateLongSerizlizer.class)
    private Long unloadTime;

    @ApiModelProperty(value = "提车图片地址，逗号分隔")
    private String loadPhotoImg;

    @ApiModelProperty(value = "历史图片地址，逗号分隔")
    private String historyLoadPhotoImg;

    @ApiModelProperty(value = "交车图片地址，逗号分隔:已交付的运单")
    private String unloadPhotoImg;

    @ApiModelProperty(value = "指导线路")
    private String guideLine;

    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "logo图片")
    private String logoPhotoImg;

    @ApiModelProperty(value = "型号")
    private String model;

    @ApiModelProperty(value = "车牌号")
    private String plateNo;

    @ApiModelProperty(value = "vin码")
    private String vin;

    @ApiModelProperty(value = "客户付款方式：0到付（默认），1预付，2账期")
    private Integer payType;

    @ApiModelProperty(value = "提车方式:1 自送，2代驾上门，3拖车上门，4物流上门")
    private Integer pickType;

    @ApiModelProperty(value = "送车方式： 1 自提，2代驾上门，3拖车上门，4物流上门")
    private Integer backType;

    @ApiModelProperty(value = "最后一次运输标识：0否，1是")
    private boolean receiptFlag;

    @ApiModelProperty(value = "运单ID")
    private Long waybillId;

    @ApiModelProperty(value = "运单编号")
    private String waybillNo;

    @ApiModelProperty(value = "订单车辆ID")
    private Long orderCarId;

    @ApiModelProperty(value = "车辆编号")
    private String orderCarNo;

    @ApiModelProperty(value = "运费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
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

    @ApiModelProperty(value = "区县编码")
    private String startAreaCode;

    @ApiModelProperty(value = "出发地业务中心名称")
    private String startStoreName;

    @ApiModelProperty(value = "出发地业务中心ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long startStoreId;

    @ApiModelProperty(value = "起始地所属业务中心ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long startBelongStoreId;

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

    @ApiModelProperty(value = "区县编码")
    private String endAreaCode;

    @ApiModelProperty(value = "目的地业务中心名称")
    private String endStoreName;

    @ApiModelProperty(value = "目的地业务中心ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long endStoreId;

    @ApiModelProperty(value = "目的地所属业务中心ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long endBelongStoreId;

    @ApiModelProperty(value = "线路ID")
    private Long lineId;

    @ApiModelProperty(value = "预计到达时间")
    private Long expectEndTime;

    @ApiModelProperty(value = "提车联系人userid")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long loadLinkUserId;

    @ApiModelProperty(value = "实际开始装车时间")
    private Long loadTime;

    @ApiModelProperty(value = "收车联系人userId")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long unloadLinkUserId;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "干线线路费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal lineFreightFee;

    public BigDecimal getLineFreightFee() {
        return lineFreightFee == null ? new BigDecimal(0) : lineFreightFee;
    }
    public BigDecimal getFreightFee() {
        return freightFee == null ? new BigDecimal(0) : freightFee;
    }
    public String getStartProvince() {
        return startProvince == null ? "" : startProvince;
    }
    public String getStartProvinceCode() {
        return startProvinceCode == null ? "" : startProvinceCode;
    }
    public String getStartCity() {
        return startCity == null ? "" : startCity;
    }
    public String getStartCityCode() {
        return startCityCode == null ? "" : startCityCode;
    }
    public String getStartArea() {
        return startArea == null ? "" : startArea;
    }
    public String getStartAreaCode() {
        return startAreaCode == null ? "" : startAreaCode;
    }
    public String getStartStoreName() {
        return startStoreName == null ? "" : startStoreName;
    }
    public Long getStartStoreId() {
        return startStoreId == null ? 0 : startStoreId;
    }
    public Long getStartBelongStoreId() {
        return startBelongStoreId == null ? 0 : startBelongStoreId;
    }
    public String getEndProvince() {
        return endProvince == null ? "" : endProvince;
    }
    public String getEndProvinceCode() {
        return endProvinceCode == null ? "" : endProvinceCode;
    }
    public String getEndCity() {
        return endCity == null ? "" : endCity;
    }
    public String getEndCityCode() {
        return endCityCode == null ? "" : endCityCode;
    }
    public String getEndArea() {
        return endArea == null ? "" : endArea;
    }
    public String getEndAreaCode() {
        return endAreaCode == null ? "" : endAreaCode;
    }
    public String getEndStoreName() {
        return endStoreName == null ? "" : endStoreName;
    }
    public Long getEndStoreId() {
        return endStoreId == null ? 0 : endStoreId;
    }
    public Long getEndBelongStoreId() {
        return endBelongStoreId == null ? 0 : endBelongStoreId;
    }
    public Long getLineId() {
        return lineId == null ? 0 : lineId;
    }
    public Long getExpectEndTime() {
        return expectEndTime == null ? 0 : expectEndTime;
    }
    public Long getLoadLinkUserId() {
        return loadLinkUserId == null ? 0 : loadLinkUserId;
    }
    public Long getLoadTime() {
        return loadTime == null ? 0 : loadTime;
    }
    public Long getUnloadLinkUserId() {
        return unloadLinkUserId == null ? 0 : unloadLinkUserId;
    }
    public Long getCreateTime() {
        return createTime == null ? 0 : createTime;
    }
    public Integer getPickType() {
        return pickType == null ? -1 : pickType;
    }
    public Integer getBackType() {
        return backType == null ? -1 : backType;
    }
    public String getGuideLine() {
        return guideLine == null ? "" : guideLine;
    }
    public String getLogoPhotoImg() {
        return logoPhotoImg == null ? "" : logoPhotoImg;
    }
    public String getHistoryLoadPhotoImg() {
        return historyLoadPhotoImg == null ? "" : historyLoadPhotoImg;
    }
    public Integer getWaybillCarState() {
        return waybillCarState == null ? -1 : waybillCarState;
    }
    public String getStartAddress() {
        return startAddress == null ? "" : startAddress;
    }
    public String getEndAddress() {
        return endAddress == null ? "" : endAddress;
    }
    public String getBrand() {
        return brand == null ? "" : brand;
    }
    public String getModel() {
        return model == null ? "" : model;
    }
    public Integer getPayType() {
        return payType == null ? -1 : payType;
    }
    public Long getExpectStartTime() {
        return expectStartTime == null ? 0 : expectStartTime;
    }
    public Long getUnloadTime() {
        return unloadTime == null ? 0 : unloadTime;
    }
    public String getLoadPhotoImg() {
        return loadPhotoImg == null ? "" : loadPhotoImg;
    }
    public String getUnloadPhotoImg() {
        return unloadPhotoImg == null ? "" : unloadPhotoImg;
    }
    public String getPlateNo() {
        return plateNo == null ? "" : plateNo;
    }
    public String getVin() {
        return vin == null ? "" : vin;
    }
    public String getLoadLinkName() {
        return loadLinkName == null ? "" : loadLinkName;
    }
    public String getLoadLinkPhone() {
        return loadLinkPhone == null ? "" : loadLinkPhone;
    }
    public String getUnloadLinkName() {
        return unloadLinkName == null ? "" : unloadLinkName;
    }
    public String getUnloadLinkPhone() {
        return unloadLinkPhone == null ? "" : unloadLinkPhone;
    }
}
