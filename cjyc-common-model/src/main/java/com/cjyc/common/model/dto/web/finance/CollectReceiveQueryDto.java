package com.cjyc.common.model.dto.web.finance;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Hut
 * @Date: 2019/12/16 15:17
 */
@Data
public class CollectReceiveQueryDto extends BasePageDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "车辆编号")
    private String no;

    @ApiModelProperty(value = "vin码")
    private String vin;

    @ApiModelProperty(value = "订单编号")
    private Long orderNo;

    @ApiModelProperty(value = "交付日期开始时间")
    private Long completeStartTime;

    @ApiModelProperty(value = "交付日期结束时间")
    private Long completeEndTime;

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

    @ApiModelProperty(value = "客户类型")
    private Integer type;

    @ApiModelProperty(value = "客户账号")
    private String contactPhone;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "业务中心")
    private String inputStoreName;

    @ApiModelProperty(value = "大区")
    private String area;

    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "车系")
    private String model;

    @ApiModelProperty(value = "代收款人")
    private String collectMan;

    @ApiModelProperty(value = "代收款人电话")
    private String collectManPhone;

    @ApiModelProperty(value = "回款状态")
    private String returnStatus;
}
