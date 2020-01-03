package com.cjyc.common.model.dto.web.finance;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Hut
 * @Date: 2020/01/03 9:33
 **/
@Data
public class PayableQueryDto extends BasePageDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "运单单号")
    private String waybillNo;

    @ApiModelProperty(value = "交付日期")
    private Long completeStartTime;

    @ApiModelProperty(value = "交付日期")
    private Long completeEndTime;

    @ApiModelProperty(value = "承运商")
    private String carrierName;

    @ApiModelProperty(value = "司机")
    private String driverName;

    @ApiModelProperty(value = "司机电话")
    private String driverPhone;

    @ApiModelProperty(value = "车牌号")
    private String vehiclePlateNo;

    @ApiModelProperty(value = "运单类型")
    private Integer waybillType;
}
