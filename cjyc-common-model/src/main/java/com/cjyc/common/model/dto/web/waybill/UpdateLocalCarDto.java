package com.cjyc.common.model.dto.web.waybill;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cjyc.common.model.entity.WaybillCar;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateLocalCarDto{

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "运单ID")
    private Long waybillId;

    @ApiModelProperty(value = "运单编号")
    private String waybillNo;

    @ApiModelProperty(value = "订单车辆ID")
    private Long orderCarId;

    @ApiModelProperty(value = "车辆编号")
    private String orderCarNo;

    @ApiModelProperty(value = "运费")
    private BigDecimal freightFee;

    @ApiModelProperty(value = "省")
    private String startProvince;

    @ApiModelProperty(value = "省编码")
    private String startProvinceCode;

    @ApiModelProperty(value = "市")
    private String startCity;

    @ApiModelProperty(value = "市编码")
    private String startCityCode;

    @ApiModelProperty(value = "区")
    private String startArea;

    @ApiModelProperty(value = "区县编码")
    private String startAreaCode;

    @ApiModelProperty(value = "装车地址")
    private String startAddress;

    @ApiModelProperty(value = "出发地业务中心名称")
    private String startStoreName;

    @ApiModelProperty(value = "出发地业务中心ID")
    private Long startStoreId;

    @ApiModelProperty(value = "省")
    private String endProvince;

    @ApiModelProperty(value = "省编码")
    private String endProvinceCode;

    @ApiModelProperty(value = "市")
    private String endCity;

    @ApiModelProperty(value = "市编码")
    private String endCityCode;

    @ApiModelProperty(value = "区")
    private String endArea;

    @ApiModelProperty(value = "区县编码")
    private String endAreaCode;

    @ApiModelProperty(value = "卸车地址")
    private String endAddress;

    @ApiModelProperty(value = "目的地业务中心名称")
    private String endStoreName;

    @ApiModelProperty(value = "目的地业务中心ID")
    private Long endStoreId;

    @ApiModelProperty(value = "线路ID")
    private Long lineId;

    @ApiModelProperty(value = "预计提车日期")
    private Long expectStartTime;

    @ApiModelProperty(value = "预计到达时间")
    private Long expectEndTime;

    @ApiModelProperty(value = "取车方式:1上门，2 自送/自取")
    private Integer takeType;

    @ApiModelProperty(value = "提车联系人")
    private String loadLinkName;

    @ApiModelProperty(value = "提车联系人userid")
    private Long loadLinkUserId;

    @ApiModelProperty(value = "提车联系人电话")
    private String loadLinkPhone;

    @ApiModelProperty(value = "收车人名称")
    private String unloadLinkName;

    @ApiModelProperty(value = "收车联系人userId")
    private Long unloadLinkUserId;

    @ApiModelProperty(value = "收车人电话")
    private String unloadLinkPhone;

    @ApiModelProperty(value = "最后一次运输标识：0否，1是")
    private Integer receiptFlag;

}
