package com.cjyc.common.model.vo.salesman.order;

import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
import com.cjyc.common.model.serizlizer.DateLongSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

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
    @JsonSerialize(using = DateLongSerizlizer.class)
    private Long createTime;
    @ApiModelProperty(value = "订单状态：0待提交，2待分配，5待确认，10待复确认，15待预付款，25已确认，55运输中，88待付款，100已完成，111原返（待），112异常结束，113取消（待），114作废（待）")
    private Integer state;
    @ApiModelProperty(value = "1:客户下单 2:业务员下单")
    private Integer flag;

    @ApiModelProperty(value = "订单所属业务中心ID")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long inputStoreId;
    @ApiModelProperty(value = "订单所属业务中心名称")
    private String inputStoreName;
    @ApiModelProperty(value = "出发地业务中心ID: -1不经过业务中心")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long startStoreId;
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
    @JsonSerialize(using = DateLongSerizlizer.class)
    private Long expectStartDate;
    @ApiModelProperty(value = "预计到达日期")
    @JsonSerialize(using = DateLongSerizlizer.class)
    private Long expectEndDate;
    @ApiModelProperty(value = "客户付款方式：0到付（默认），1预付，2账期")
    private Integer payType;

    @ApiModelProperty(value = "客户类型：1个人，2企业，3合伙人")
    private Integer customerType;
    @ApiModelProperty(value = "客户id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long customerId;
    @ApiModelProperty(value = "客户名称")
    private String name;
    @ApiModelProperty(value = "客户手机号")
    private String customerPhone;
    @ApiModelProperty(value = "合同ID")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long customerContractId;
    @ApiModelProperty(value = "大客户联系人")
    private String contactMan;
    @ApiModelProperty(value = "联系人手机号")
    private String contactPhone;
    @ApiModelProperty(value = "大客户合同编号")
    private String contractNo;
    @ApiModelProperty(value = "账期/天")
    private Integer settlePeriod;

    @ApiModelProperty(value = "班线id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long lineId;
    @ApiModelProperty(value = "目的地业务中心ID: -1不经过业务中心")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long endStoreId;

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

    @ApiModelProperty(value = "收车人")
    private String backContactName;
    @ApiModelProperty(value = "收车人联系方式")
    private String backContactPhone;
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
    private BigDecimal totalPickFee;
    @ApiModelProperty(value = "送车费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal totalBackFee;
    @ApiModelProperty(value = "追加保险费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal totalAddInsuranceFee;
    @ApiModelProperty(value = "干线/物流费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal totalTrunkFee;
    @ApiModelProperty(value = "总计")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal totalFee;
    @ApiModelProperty(value = "默认物流费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal defaultWlFee;

    @ApiModelProperty(value = "车辆详情")
    private List<SalesOrderCarVo> carVoList;

    public Long getEndBelongStoreId(){return endBelongStoreId == null ? 0:endBelongStoreId;}
    public Long getExpectStartDate(){return expectStartDate == null ? 0:expectStartDate;}
    public Long getExpectEndDate(){return expectEndDate == null ? 0:expectEndDate;}
    public Long getInputStoreId(){return inputStoreId == null ? 0:inputStoreId;}
    public String getInputStoreName() {return StringUtils.isBlank(inputStoreName) ? "":inputStoreName;}
    public Long getStartBelongStoreId(){return startBelongStoreId == null ? 0:startBelongStoreId;}
    public Long getStartStoreId(){return startStoreId == null ? 0:startStoreId;}
    public Long getEndStoreId(){return endStoreId == null ? 0:endStoreId;}
    public String getStartStoreName(){return StringUtils.isBlank(startStoreName) ? "":startStoreName;}
    public String getEndStoreName(){return StringUtils.isBlank(endStoreName) ? "":endStoreName;}
    public Integer getCustomerType(){return customerType == null ? 0:customerType;}
    public String getName(){return StringUtils.isBlank(name) ? "":name;}
    public String getCustomerPhone(){return StringUtils.isBlank(customerPhone) ? "":customerPhone;}
    public Long getCustomerId(){return customerId == null ? 0:customerId;}
    public Long getCustomerContractId(){return customerContractId == null ? 0:customerContractId;}
    public String getContactMan(){return StringUtils.isBlank(contactMan) ? "":contactMan;}
    public String getContactPhone(){return StringUtils.isBlank(contactPhone) ? "":contactPhone;}
    public String getContractNo(){return StringUtils.isBlank(contractNo) ? "":contractNo;}
    public Integer getSettlePeriod(){return settlePeriod == null ? -1:settlePeriod;}

    public String getPickContactName(){return StringUtils.isBlank(pickContactName) ? "":pickContactName;}
    public String getPickContactPhone(){return StringUtils.isBlank(pickContactPhone) ? "":pickContactPhone;}
    public String getStartProvinceCode(){return StringUtils.isBlank(startProvinceCode) ? "":startProvinceCode;}
    public String getStartProvince(){return StringUtils.isBlank(startProvince) ? "":startProvince;}
    public String getStartCityCode(){return StringUtils.isBlank(startCityCode) ? "":startCityCode;}
    public String getStartAreaCode(){return StringUtils.isBlank(startAreaCode) ? "":startAreaCode;}
    public String getStartArea(){return StringUtils.isBlank(startArea) ? "":startArea;}
    public String getStartAddress(){return StringUtils.isBlank(startAddress) ? "":startAddress;}
    public String getBackContactName(){return StringUtils.isBlank(backContactName) ? "":backContactName;}
    public String getBackContactPhone(){return StringUtils.isBlank(backContactPhone) ? "":backContactPhone;}
    public String getEndProvinceCode(){return StringUtils.isBlank(endProvinceCode) ? "":endProvinceCode;}
    public String getEndProvince(){return StringUtils.isBlank(endProvince) ? "":endProvince;}
    public String getEndCityCode(){return StringUtils.isBlank(endCityCode) ? "":endCityCode;}
    public String getEndCity() {return StringUtils.isBlank(endCity) ? "":endCity;}
    public String getEndAreaCode(){return StringUtils.isBlank(endAreaCode) ? "":endAreaCode;}
    public String getEndArea(){return StringUtils.isBlank(endArea) ? "":endArea;}
    public String getEndAddress(){return StringUtils.isBlank(endAddress) ? "":endAddress;}
    public BigDecimal getTotalPickFee(){return totalPickFee == null ? BigDecimal.ZERO:totalPickFee;}
    public BigDecimal getTotalBackFee(){return totalBackFee == null ? BigDecimal.ZERO:totalBackFee;}
    public BigDecimal getTotalAddInsuranceFee(){return totalAddInsuranceFee == null ? BigDecimal.ZERO:totalAddInsuranceFee;}
    public BigDecimal getTotalTrunkFee(){return totalTrunkFee == null ? BigDecimal.ZERO:totalTrunkFee;}
    public BigDecimal getTotalFee(){return totalFee == null ? BigDecimal.ZERO:totalFee;}
    public BigDecimal getDefaultWlFee(){return defaultWlFee == null ? BigDecimal.ZERO:defaultWlFee;}
}