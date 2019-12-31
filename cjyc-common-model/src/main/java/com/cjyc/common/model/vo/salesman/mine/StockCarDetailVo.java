package com.cjyc.common.model.vo.salesman.mine;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class StockCarDetailVo extends StockCarVo {
    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "发车人")
    private String pickContactName;

    @ApiModelProperty(value = "发车联系方式")
    private String pickContactPhone;

    @ApiModelProperty(value = "出发地详细地址")
    private String startAddress;

    @ApiModelProperty(value = "收车人")
    private String backContactName;

    @ApiModelProperty(value = "收车人联系方式")
    private String backContactPhone;

    @ApiModelProperty(value = "目的地详细地址")
    private String endAddress;

    @ApiModelProperty(value = "运输")
    private List<StockTaskVo> stockVos;

}