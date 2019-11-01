package com.cjyc.common.model.dto.customer;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class OrderConditionDto extends BasePageDto {
    private static final long serialVersionUID = 399318965603713438L;
    public interface QueryPage{}
    public interface QueryUpdateAndDetail{}

    @ApiModelProperty("客户id")
    @NotNull(groups = {QueryPage.class, QueryUpdateAndDetail.class},message = "客户id不能为空" )
    private Long customerId;

    @ApiModelProperty("订单状态 查询分页参数0:待确认,1:运输中,2:已交付,3:全部；更新状态参数:取消订单传 113,确认下单传 2")
    @NotNull(groups = {QueryPage.class},message = "订单状态不能为空")
    private Integer state;

    @ApiModelProperty("订单号")
    @NotBlank(groups = {QueryUpdateAndDetail.class},message = "订单号不能为空")
    private String orderNo;

    @ApiModelProperty("关键字")
    private String key;

    @ApiModelProperty("排序 0:默认排序 1：时间升序 2：时间降序")
    @NotBlank(groups = {QueryPage.class},message = "排序不能为空")
    private String sort;

    @ApiModelProperty("业务中心编码")
    private String storeId;

    @ApiModelProperty("品牌")
    private String brand;

    @ApiModelProperty("车型")
    private String model;

    @ApiModelProperty("下单开始时间")
    private String startDate;

    @ApiModelProperty("下单结束时间")
    private String endDate;

    @ApiModelProperty("始发地编码")
    private String startCityCode;

    @ApiModelProperty("目的地编码")
    private String endCityCode;

    private Long startDateMS;
    private Long endDateMS;
}