package com.cjyc.common.model.dto.web.order;

import com.cjyc.common.model.dto.web.PageWebDto;
import com.cjyc.common.model.dto.web.line.FromToCityDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class WaitDispatchTrunkDto extends PageWebDto {

    @ApiModelProperty(value = "省编号")
    private String startProvinceCode;

    @ApiModelProperty(value = "市编号")
    private String startCityCode;

    @ApiModelProperty(value = "区编号")
    private String startAreaCode;

    @ApiModelProperty(value = "省编号")
    private String endProvinceCode;

    @ApiModelProperty(value = "市编号")
    private String endCityCode;

    @ApiModelProperty(value = "区编号")
    private String endAreaCode;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "车辆编码")
    private String carNo;

    @ApiModelProperty(value = "vin码")
    private String vin;

    @ApiModelProperty(value = "起始目的城市列表")
    private List<FromToCityDto> fromToCityList;


}
