package com.cjyc.common.model.vo.customer.order;

import com.cjyc.common.model.util.BigDecimalSerizlizer;
import com.cjyc.common.model.util.DataLongSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Description 订单明细实体类
 * @Author LiuXingXiang
 * @Date 2019/10/31 18:45
 **/
@Data
public class OrderCenterDetailVo implements Serializable {
    private static final long serialVersionUID = 2193129578098454937L;
    @ApiModelProperty(value = "订单主键id")
    private Long id;

    @ApiModelProperty(value = "订单编号")
    private String no;

    @ApiModelProperty(value = "订单状态：0待提交，2待分配，5待确认，10待复确认，15待预付款，25已确认，55运输中，" +
            "88待付款，100已完成，111原返（待），112异常结束，113取消（待），114作废（待）")
    private Integer state;

    @ApiModelProperty(value = "下单时间")
    @JsonSerialize(using = DataLongSerizlizer.class)
    private Long createTime;

    @ApiModelProperty(value = "托运车辆信息列表")
    private List<OrderCarCenterVo> orderCarCenterVoList;

    @ApiModelProperty(value = "托运车辆信息列表:已交付车辆")
    private List<OrderCarCenterVo> orderCarFinishPayList;

    @ApiModelProperty(value = "提车时间")
    @JsonSerialize(using = DataLongSerizlizer.class)
    private Long expectStartDate;

    @ApiModelProperty(value = "发车人")
    private String pickContactName;

    @ApiModelProperty(value = "发车人联系方式")
    private String pickContactPhone;

    @ApiModelProperty(value = "提车方式:1 自送，2代驾上门，3拖车上门，4物流上门")
    private Integer pickType;

    @ApiModelProperty(value = "出发地省编号")
    private String startProvinceCode;

    @ApiModelProperty(value = "出发地市编号")
    private String startCityCode;

    @ApiModelProperty(value = "出发地市名称")
    private String startCity;

    @ApiModelProperty(value = "出发地区编号")
    private String startAreaCode;

    @ApiModelProperty(value = "出发地省市区名称")
    private String startProvinceCityAreaName;

    @ApiModelProperty(value = "出发地详细地址")
    private String startAddress;

    @ApiModelProperty(value = "收车人")
    private String backContactName;

    @ApiModelProperty(value = "收车人联系方式")
    private String backContactPhone;

    @ApiModelProperty(value = "送车方式： 1 自提，2代驾上门，3拖车上门，4物流上门")
    private Integer backType;

    @ApiModelProperty(value = "收车省编号")
    private String endProvinceCode;

    @ApiModelProperty(value = "收车市编号")
    private String endCityCode;

    @ApiModelProperty(value = "收车市名称")
    private String endCity;

    @ApiModelProperty(value = "收车区编号")
    private String endAreaCode;

    @ApiModelProperty(value = "收车省市区名称")
    private String endProvinceCityAreaName;

    @ApiModelProperty(value = "目的地详细地址")
    private String endAddress;

    @ApiModelProperty(value = "代驾提车费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal pickFee;

    @ApiModelProperty(value = "拖车送车费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal backFee;

    @ApiModelProperty(value = "物流费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal trunkFee;

    @ApiModelProperty(value = "预付款")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal depositFee;

    @ApiModelProperty(value = "保险费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal addInsuranceFee;

    @ApiModelProperty(value = "应收总价：收车后客户应支付平台的费用，计算值")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal totalFee;

    @ApiModelProperty(value = "客户付款方式：0到付（默认），1预付，2账期")
    private Integer payType;

    @ApiModelProperty(value = "优惠券id:如果ID为0，则说明没有优惠券")
    private Long couponSendId;

    @ApiModelProperty(value = "优惠券名称")
    private String couponName;

    @ApiModelProperty(value = "车辆总数")
    private Integer carNum;

    @ApiModelProperty(value = "出发地业务中心id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long startStoreId;

    @ApiModelProperty(value = "出发地业务中心名称")
    private String startStoreName;

    @ApiModelProperty(value = "出发地业务中心详细地址")
    private String startStoreNameDetail;

    @ApiModelProperty(value = "目的地业务中心id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long endStoreId;

    @ApiModelProperty(value = "目的地业务中心名称")
    private String endStoreName;

    @ApiModelProperty(value = "目的地业务中心详细地址")
    private String endStoreNameDetail;

    public Long getStartStoreId() {
        return startStoreId == null ? 0 : startStoreId;
    }
    public String getStartStoreName() {
        return startStoreName == null ? "" : startStoreName;
    }
    public String getStartStoreNameDetail() {
        return startStoreNameDetail == null ? "" : startStoreNameDetail;
    }
    public Long getEndStoreId() {
        return endStoreId == null ? 0 : endStoreId;
    }
    public String getEndStoreName() {
        return endStoreName == null ? "" : endStoreName;
    }
    public String getEndStoreNameDetail() {
        return endStoreNameDetail == null ? "" : endStoreNameDetail;
    }
    public String getStartCity() {
        return startCity == null ? "" : startCity;
    }
    public String getEndCity() {
        return endCity == null ? "" : endCity;
    }
    public String getStartProvinceCode() {
        return startProvinceCode == null ? "" : startProvinceCode;
    }
    public String getStartCityCode() {
        return startCityCode == null ? "" : startCityCode;
    }
    public String getStartAreaCode() {
        return startAreaCode == null ? "" : startAreaCode;
    }
    public String getStartProvinceCityAreaName() {
        return startProvinceCityAreaName == null ? "" : startProvinceCityAreaName;
    }
    public String getEndProvinceCode() {
        return endProvinceCode == null ? "" : endProvinceCode;
    }
    public String getEndCityCode() {
        return endCityCode == null ? "" : endCityCode;
    }
    public String getEndAreaCode() {
        return endAreaCode == null ? "" : endAreaCode;
    }
    public String getEndProvinceCityAreaName() {
        return endProvinceCityAreaName == null ? "" : endProvinceCityAreaName;
    }
    public Long getCreateTime() {
        return createTime == null ? 0 : createTime;
    }
    public Long getExpectStartDate() {
        return expectStartDate == null ? 0 : expectStartDate;
    }
    public String getPickContactName() {
        return pickContactName == null ? "" : pickContactName;
    }
    public String getPickContactPhone() {
        return pickContactPhone == null ? "" : pickContactPhone;
    }
    public String getStartAddress() {
        return startAddress == null ? "" : startAddress;
    }
    public String getBackContactName() {
        return backContactName == null ? "" : backContactName;
    }
    public String getBackContactPhone() {
        return backContactPhone == null ? "" : backContactPhone;
    }
    public String getEndAddress() {
        return endAddress == null ? "" : endAddress;
    }
    public BigDecimal getPickFee() {
        return pickFee == null ? new BigDecimal(0) : pickFee;
    }
    public BigDecimal getBackFee() {
        return backFee == null ? new BigDecimal(0) : backFee;
    }
    public BigDecimal getTrunkFee() {
        return trunkFee == null ? new BigDecimal(0) : trunkFee;
    }
    public BigDecimal getDepositFee() {
        return depositFee == null ? new BigDecimal(0) : depositFee;
    }
    public BigDecimal getAddInsuranceFee() {
        return addInsuranceFee == null ? new BigDecimal(0) : addInsuranceFee;
    }
    public BigDecimal getTotalFee() {
        return totalFee == null ? new BigDecimal(0) : totalFee;
    }
    public String getCouponName() {
        return couponName == null ? "" : couponName;
    }
    public Long getCouponSendId() {
        return couponSendId == null ? 0 : couponSendId;
    }
    public Integer getCarNum() {
        return carNum == null ? 0 : carNum;
    }
}
