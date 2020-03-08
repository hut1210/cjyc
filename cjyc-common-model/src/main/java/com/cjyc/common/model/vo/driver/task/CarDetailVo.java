package com.cjyc.common.model.vo.driver.task;

import com.cjyc.common.model.serizlizer.DateLongSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @Description 运单车辆详情
 * @Author Liu Xing Xiang
 * @Date 2019/11/20 13:26
 **/
@Data
public class CarDetailVo implements Serializable {
    private static final long serialVersionUID = -6283733541452871958L;
    @ApiModelProperty(value = "任务单车辆ID")
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
    private Boolean receiptFlag;

    public Integer getPickType() {
        return pickType == null ? -1 : pickType;
    }
    public Integer getBackType() {
        return backType == null ? -1 : backType;
    }
    public String getGuideLine() {
        return StringUtils.isBlank(guideLine) ? "" : guideLine;
    }
    public String getLogoPhotoImg() {
        return StringUtils.isBlank(logoPhotoImg) ? "" : logoPhotoImg;
    }
    public String getHistoryLoadPhotoImg() {
        return StringUtils.isBlank(historyLoadPhotoImg) ? "" : historyLoadPhotoImg;
    }
    public Integer getWaybillCarState() {
        return waybillCarState == null ? -1 : waybillCarState;
    }
    public String getStartAddress() {
        return StringUtils.isBlank(startAddress) ? "" : startAddress;
    }
    public String getEndAddress() {
        return StringUtils.isBlank(endAddress) ? "" : endAddress;
    }
    public String getBrand() {
        return StringUtils.isBlank(brand) ? "" : brand;
    }
    public String getModel() {
        return StringUtils.isBlank(model) ? "" : model;
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
        return StringUtils.isBlank(loadPhotoImg) ? "" : loadPhotoImg;
    }
    public String getUnloadPhotoImg() {
        return StringUtils.isBlank(unloadPhotoImg) ? "" : unloadPhotoImg;
    }
    public String getPlateNo() {
        return StringUtils.isBlank(plateNo) ? "" : plateNo;
    }
    public String getVin() {
        return StringUtils.isBlank(vin) ? "" : vin;
    }
    public String getLoadLinkName() {
        return StringUtils.isBlank(loadLinkName) ? "" : loadLinkName;
    }
    public String getLoadLinkPhone() {
        return StringUtils.isBlank(loadLinkPhone) ? "" : loadLinkPhone;
    }
    public String getUnloadLinkName() {
        return StringUtils.isBlank(unloadLinkName) ? "" : unloadLinkName;
    }
    public String getUnloadLinkPhone() {
        return StringUtils.isBlank(unloadLinkPhone) ? "" : unloadLinkPhone;
    }
    public Integer getReceiptFlag() {
        if (receiptFlag == null) {
            return -1;
        }
        return receiptFlag == false ? 0 : 1;
    }
}
