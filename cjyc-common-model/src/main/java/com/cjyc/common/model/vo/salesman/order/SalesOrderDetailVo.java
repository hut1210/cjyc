package com.cjyc.common.model.vo.salesman.order;

import com.cjyc.common.model.util.BigDecimalSerizlizer;
import com.cjyc.common.model.util.DataLongSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class SalesOrderDetailVo implements Serializable {
    private static final long serialVersionUID = -3343355785560447452L;
    @ApiModelProperty(value = "订单id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long orderId;
    @ApiModelProperty(value = "订单编号")
    private String orderNo;
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = DataLongSerizlizer.class)
    private Long createTime;
    @ApiModelProperty(value = "订单状态：0待提交，2待分配，5待确认，10待复确认，15待预付款，25已确认，55运输中，88待付款，100已完成，111原返（待），112异常结束，113取消（待），114作废（待）")
    private Integer state;

    @ApiModelProperty(value = "订单所属业务中心ID")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long inputStoreId;
    @ApiModelProperty(value = "订单所属业务中心名称")
    private String inputStoreName;
    @ApiModelProperty(value = "收车所属业务中心ID")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long startBelongStoreId;
    @ApiModelProperty(value = "收车所属业务中心名称")
    private String startStoreName;
    @ApiModelProperty(value = "送车所属业务中心ID")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long endBelongStoreId;
    @ApiModelProperty(value = "送车所属业务中心名称")
    private String endStoreName;
    @ApiModelProperty(value = "提车日期")
    @JsonSerialize(using = DataLongSerizlizer.class)
    private Long expectStartDate;
    @ApiModelProperty(value = "预计到达日期")
    @JsonSerialize(using = DataLongSerizlizer.class)
    private Long expectEndDate;
    @ApiModelProperty(value = "客户付款方式：0到付（默认），1预付，2账期")
    private Integer payType;

    @ApiModelProperty(value = "发车人")
    private String pickContactName;
    @ApiModelProperty(value = "发车人联系方式")
    private String pickContactPhone;
    @ApiModelProperty(value = "起始省编号")
    private String startProvinceCode;
    @ApiModelProperty(value = "起始省")
    private String startProvince;
    @ApiModelProperty(value = "起始城市编码")
    private String startCityCode;
    @ApiModelProperty(value = "起始城市")
    private String startCity;
    @ApiModelProperty(value = "起始区编号")
    private String startAreaCode;
    @ApiModelProperty(value = "起始区")
    private String startArea;
    @ApiModelProperty(value = "出发地详细地址")
    private String startAddress;
    @ApiModelProperty(value = "提车方式:1 自送，2代驾上门，3拖车上门，4物流上门")
    private Integer pickType;
    @ApiModelProperty(value = "目的省编号")
    private String endProvinceCode;
    @ApiModelProperty(value = "目的省")
    private String endProvince;
    @ApiModelProperty(value = "目的城市编号")
    private String endCityCode;
    @ApiModelProperty(value = "目的城市")
    private String endCity;
    @ApiModelProperty(value = "目的区编号")
    private String endAreaCode;
    @ApiModelProperty(value = "目的区")
    private String endArea;
    @ApiModelProperty(value = "目的地详细地址")
    private String endAddress;
    @ApiModelProperty(value = "送车方式： 1 自提，2代驾上门，3拖车上门，4物流上门")
    private Integer backType;
    @ApiModelProperty(value = "车辆总数")
    private Integer carNum;

    @ApiModelProperty(value = "提车费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal pickFee;
    @ApiModelProperty(value = "送车费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal backFee;
    @ApiModelProperty(value = "追加保险费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal addInsuranceFee;
    @ApiModelProperty(value = "干线/物流费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal trunkFee;
    @ApiModelProperty(value = "总计")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal totalFee;

    @ApiModelProperty(value = "车辆详情")
    private List<SalesOrderCarVo> carVoList;
    public Long getEndBelongStoreId(){return endBelongStoreId == null ? 0:endBelongStoreId;}
    public Long getExpectStartDate(){return expectStartDate == null ? 0:expectStartDate;}
    public Long getExpectEndDate(){return expectEndDate == null ? 0:expectEndDate;}

}