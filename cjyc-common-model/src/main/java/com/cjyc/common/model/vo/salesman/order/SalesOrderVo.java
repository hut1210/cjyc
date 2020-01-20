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

@Data
public class SalesOrderVo implements Serializable {
    private static final long serialVersionUID = -4665142592352685826L;
    @ApiModelProperty(value = "订单id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long orderId;
    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "客户付款方式：0到付（默认），1预付，2账期")
    private Integer payType;

    @ApiModelProperty(value = "起始城市")
    private String startCity;

    @ApiModelProperty(value = "目的城市")
    private String endCity;

    @ApiModelProperty(value = "订单状态：0待提交，2待分配，5待确认，10待复确认，15待预付款，25已确认，55运输中，88待付款，100已完成，111原返（待），112异常结束，113取消（待），114作废（待）")
    private Integer state;

    @ApiModelProperty(value = "总费用")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal totalFee;

    @ApiModelProperty(value = "被分配给业务员的名称")
    private String allotToUserName;

    @ApiModelProperty(value = "提车日期")
    @JsonSerialize(using = DateLongSerizlizer.class)
    private Long expectStartDate;

    @ApiModelProperty(value = "客户类型：1个人，2企业，3合伙人")
    private Integer customerType;

    @ApiModelProperty(value = "车辆总数")
    private Integer carNum;

    @ApiModelProperty(value = "订单所属业务中心")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long inputStoreId;

    public String getAllotToUserName(){return StringUtils.isBlank(allotToUserName) ? "":allotToUserName;}
    public String getStartCity(){return StringUtils.isBlank(startCity) ? "":startCity;}
    public String getEndCity(){return StringUtils.isBlank(endCity) ? "":endCity;}
    public Long getExpectStartDate(){return expectStartDate == null ? 0:expectStartDate;}
    public Long getInputStoreId(){return inputStoreId == null ? 0:inputStoreId;}
}