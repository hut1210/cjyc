package com.cjyc.common.model.dto.salesman.dispatch;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
/**
 * 调度信息列表实体
 */

@Data
@ApiModel
@Validated
public class DispatchListDto extends BasePageDto {
    @ApiModelProperty(value = "开始城市")
    private String startCity;
    @ApiModelProperty(value = "目的城市")
    private String endCity;
    @ApiModelProperty(value = "调度类型 0：全部 1：提车调度 2：干线调度 3：送车调度")
    private Integer dispatchType;
    @ApiModelProperty(value = "提车日期排序 1：升序 2：降序")
    private Integer pickDateSort;
    @ApiModelProperty(value = "提车开始日期")
    private Long pickDateSt;
    @ApiModelProperty(value = "提车结束日期")
    private Long pickDateEd;
    @ApiModelProperty(value = "订单号/运单号/VIN码")
    private String searchValue;
    @ApiModelProperty(value = "品牌")
    private String brand;
    @ApiModelProperty(value = "车系")
    private String model;
}
