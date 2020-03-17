package com.cjyc.foreign.api.dto.res;

import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 订单详情返回参数
 * @Author Liu Xing Xiang
 * @Date 2020/3/11 17:13
 **/
@Data
public class OrderDetailResDto implements Serializable {
    private static final long serialVersionUID = 8407119907104446329L;
    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "订单编号")
    private String no;

    @ApiModelProperty(value = "客户ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long customerId;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "客户电话")
    private String customerPhone;

    @ApiModelProperty(value = "客户类型：1个人，2企业，3合伙人")
    private Integer customerType;

    @ApiModelProperty(value = "订单所属业务中心ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long inputStoreId;

    @ApiModelProperty(value = "订单所属业务中心名称")
    private String inputStoreName;

    @ApiModelProperty(value = "始发城市省")
    private String startProvince;

    @ApiModelProperty(value = "始发城市省编号")
    private String startProvinceCode;

    @ApiModelProperty(value = "始发城市")
    private String startCity;

    @ApiModelProperty(value = "始发城市编号")
    private String startCityCode;

    @ApiModelProperty(value = "始发城市区")
    private String startArea;

    @ApiModelProperty(value = "始发城市区编号")
    private String startAreaCode;

    @ApiModelProperty(value = "始发城市详细地址")
    private String startAddress;

    @ApiModelProperty(value = "始发城市业务中心ID: -1不经过业务中心")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long startStoreId;

    @ApiModelProperty(value = "始发城市业务中心名称")
    private String startStoreName;

    @ApiModelProperty(value = "始发城市业务所属中心id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long startBelongStoreId;

    @ApiModelProperty(value = "目的地城市省")
    private String endProvince;

    @ApiModelProperty(value = "目的地城市省编号")
    private String endProvinceCode;

    @ApiModelProperty(value = "目的地城市")
    private String endCity;

    @ApiModelProperty(value = "目的地城市编号")
    private String endCityCode;

    @ApiModelProperty(value = "目的地城市区")
    private String endArea;

    @ApiModelProperty(value = "目的地城市区编号")
    private String endAreaCode;

    @ApiModelProperty(value = "目的地城市详细地址")
    private String endAddress;

    @ApiModelProperty(value = "目的地城市业务中心ID: -1不经过业务中心")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long endStoreId;

    @ApiModelProperty(value = "目的地城市业务中心名称")
    private String endStoreName;

    @ApiModelProperty(value = "目的地城市业务所属中心id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long endBelongStoreId;

    @ApiModelProperty(value = "预计出发时间（提车日期）")
    private Long expectStartDate;

    @ApiModelProperty(value = "预计到达时间")
    private Long expectEndDate;

    @ApiModelProperty(value = "车辆总数")
    private Integer carNum;

    @ApiModelProperty(value = "线路ID")
    private Long lineId;

    @ApiModelProperty(value = "提车方式:1 自送，2代驾上门，3拖车上门，4物流上门")
    private Integer pickType;

    @ApiModelProperty(value = "发车人")
    private String pickContactName;

    @ApiModelProperty(value = "发车人联系方式")
    private String pickContactPhone;

    @ApiModelProperty(value = "送车方式： 1 自提，2代驾上门，3拖车上门，4物流上门")
    private Integer backType;

    @ApiModelProperty(value = "收车人")
    private String backContactName;

    @ApiModelProperty(value = "收车人联系方式")
    private String backContactPhone;

    @ApiModelProperty(value = "订单来源：1WEB管理后台, 2业务员APP, 4司机APP, 6用户端APP, 7用户端小程序,9-99车圈")
    private Integer source;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "创建人：客户/业务员")
    private String createUserName;

    @ApiModelProperty(value = "创建人userid")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createUserId;

    @ApiModelProperty(value = "被分配给业务员的名称")
    private String allotToUserName;

    @ApiModelProperty(value = "被分配给业务员的userid")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long allotToUserId;

    @ApiModelProperty(value = "确认时间")
    private Long checkTime;

    @ApiModelProperty(value = "确认人：业务员")
    private String checkUserName;

    @ApiModelProperty(value = "确认人userid")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long checkUserId;

    @ApiModelProperty(value = "订单状态：0待提交，2待分配，5待确认，10待复确认，15待预付款，25已确认，" +
            "55运输中，88待付款，100已完成，111原返（待），112异常结束，113取消（待），114作废（待）")
    private Integer state;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否开票：0否（默认根据设置），1是")
    private Integer invoiceFlag;

    @ApiModelProperty(value = "发票类型：0无， 1-普通(个人) ，2增值普票(企业) ，3增值专用发票")
    private Integer invoiceType;

    @ApiModelProperty(value = "物流券抵消金额")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal couponOffsetFee;

    @ApiModelProperty(value = "应收总价：收车后客户应支付平台的费用，计算值")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal totalFee;

    @ApiModelProperty(value = "客户付款方式：0到付（默认），1预付，2账期")
    private Integer payType;

    @ApiModelProperty(value = "客户支付尾款状态：0未支付，1部分支付，2支付完成")
    private Integer wlPayState;

    @ApiModelProperty(value = "上次客户支付尾款时间")
    private Long wlPayTime;

    @ApiModelProperty(value = "订单完结时间")
    private Long finishTime;

    @ApiModelProperty(value = "订单车辆信息列表")
    private List<OrderCarDetailResDto> orderCarDetailList;

    @ApiModelProperty(value = "订单车辆运输信息列表")
    private List<OrderCarTransportDetailResDto> orderCarTransportDetailList;

    public Long getId() {
        return id == null ? -1 : id;
    }

    public String getNo() {
        return no == null ? "" : no;
    }

    public Long getCustomerId() {
        return customerId == null ? -1 : customerId;
    }

    public String getCustomerName() {
        return customerName == null ? "" : customerName;
    }

    public String getCustomerPhone() {
        return customerPhone == null ? "" : customerPhone;
    }

    public Integer getCustomerType() {
        return customerType == null ? -1 : customerType;
    }

    public Long getInputStoreId() {
        return inputStoreId == null ? -1 : inputStoreId;
    }

    public String getInputStoreName() {
        return inputStoreName == null ? "" : inputStoreName;
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

    public String getStartAddress() {
        return startAddress == null ? "" : startAddress;
    }

    public Long getStartStoreId() {
        return startStoreId == null ? -1 : startStoreId;
    }

    public String getStartStoreName() {
        return startStoreName == null ? "" : startStoreName;
    }

    public Long getStartBelongStoreId() {
        return startBelongStoreId == null ? -1 : startBelongStoreId;
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

    public String getEndAddress() {
        return endAddress == null ? "" : endAddress;
    }

    public Long getEndStoreId() {
        return endStoreId == null ? -1 : endStoreId;
    }

    public String getEndStoreName() {
        return endStoreName == null ? "" : endStoreName;
    }

    public Long getEndBelongStoreId() {
        return endBelongStoreId == null ? -1 : endBelongStoreId;
    }

    public Long getExpectStartDate() {
        return expectStartDate == null ? 0 : expectStartDate;
    }

    public Long getExpectEndDate() {
        return expectEndDate == null ? 0 : expectEndDate;
    }

    public Integer getCarNum() {
        return carNum == null ? 0 : carNum;
    }

    public Long getLineId() {
        return lineId == null ? -1 : lineId;
    }

    public Integer getPickType() {
        return pickType == null ? -1 : pickType;
    }

    public String getPickContactName() {
        return pickContactName == null ? "" : pickContactName;
    }

    public String getPickContactPhone() {
        return pickContactPhone == null ? "" : pickContactPhone;
    }

    public Integer getBackType() {
        return backType == null ? -1 : backType;
    }

    public String getBackContactName() {
        return backContactName == null ? "" : backContactName;
    }

    public String getBackContactPhone() {
        return backContactPhone == null ? "" : backContactPhone;
    }

    public Integer getSource() {
        return source == null ? -1 : source;
    }

    public Long getCreateTime() {
        return createTime == null ? 0 : createTime;
    }

    public String getCreateUserName() {
        return createUserName == null ? "" : createUserName;
    }

    public Long getCreateUserId() {
        return createUserId == null ? -1 : createUserId;
    }

    public String getAllotToUserName() {
        return allotToUserName == null ? "" : allotToUserName;
    }

    public Long getAllotToUserId() {
        return allotToUserId == null ? -1 : allotToUserId;
    }

    public Long getCheckTime() {
        return checkTime == null ? 0 : checkTime;
    }

    public String getCheckUserName() {
        return checkUserName == null ? "" : checkUserName;
    }

    public Long getCheckUserId() {
        return checkUserId == null ? -1 : checkUserId;
    }

    public Integer getState() {
        return state == null ? -1 : state;
    }

    public String getRemark() {
        return remark == null ? "" : remark;
    }

    public Integer getInvoiceFlag() {
        return invoiceFlag == null ? -1 : invoiceFlag;
    }

    public Integer getInvoiceType() {
        return invoiceType == null ? -1 : invoiceType;
    }

    public BigDecimal getCouponOffsetFee() {
        return couponOffsetFee == null ? BigDecimal.ZERO : couponOffsetFee;
    }

    public BigDecimal getTotalFee() {
        return totalFee == null ? BigDecimal.ZERO : totalFee;
    }

    public Integer getPayType() {
        return payType == null ? -1 : payType;
    }

    public Integer getWlPayState() {
        return wlPayState == null ? -1 : wlPayState;
    }

    public Long getWlPayTime() {
        return wlPayTime == null ? 0 : wlPayTime;
    }

    public Long getFinishTime() {
        return finishTime == null ? 0 : finishTime;
    }

    public List<OrderCarDetailResDto> getOrderCarDetailList() {
        return orderCarDetailList == null ? new ArrayList<>(0) : orderCarDetailList;
    }

    public List<OrderCarTransportDetailResDto> getOrderCarTransportDetailList() {
        return orderCarTransportDetailList == null ? new ArrayList<>(0) : orderCarTransportDetailList;
    }
}
