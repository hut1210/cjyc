package com.cjyc.foreign.api.dto.res;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
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
    @Excel(name = "目的城市", orderNum = "14")
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
}
