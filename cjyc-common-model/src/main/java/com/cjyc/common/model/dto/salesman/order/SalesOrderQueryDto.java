package com.cjyc.common.model.dto.salesman.order;

import com.cjyc.common.model.dto.salesman.PageSalesDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class SalesOrderQueryDto extends PageSalesDto {
    @ApiModelProperty(value = "标志 0：下单 1：接单 2：全部")
    @NotNull(message = "标志不能为空")
    private Integer flag;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "客户类型：1个人，2企业，3合伙人")
    private Integer customerType;

    @ApiModelProperty(value = "起始城市")
    private String startCity;

    @ApiModelProperty(value = "目的城市")
    private String endCity;

    @ApiModelProperty(value = "起始提车时间")
    private Long expectStartTime;

    @ApiModelProperty(value = "结束提车时间")
    private Long expectEndTime;

    @ApiModelProperty(value = "下单开始时间")
    private Long createStartTime;

    @ApiModelProperty(value = "下单结束时间")
    private Long createEndTime;

    @ApiModelProperty(value = "业务中心ids，不需要传")
    private Set<Long> storeIds;
}