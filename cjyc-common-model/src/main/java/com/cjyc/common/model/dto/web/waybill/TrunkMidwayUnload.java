package com.cjyc.common.model.dto.web.waybill;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class TrunkMidwayUnload {
    @ApiModelProperty(value = "区县编码")
    private String waybillId;

    @ApiModelProperty(value = "区县编码")
    private String endAreaCode;

    @ApiModelProperty(value = "卸车地址")
    private String endAddress;

    @ApiModelProperty(value = "目的地业务中心名称")
    private String endStoreName;

    @ApiModelProperty(value = "目的地业务中心ID")
    private Long endStoreId;

    @ApiModelProperty(value = "收车人名称")
    private String unloadLinkName;

    @ApiModelProperty(value = "收车联系人userId")
    private Long unloadLinkUserId;

    @ApiModelProperty(value = "收车人电话")
    private String unloadLinkPhone;

    @ApiModelProperty(value = "实际完成卸车时间")
    private Long unloadTime;

    @ApiModelProperty(value = "运费")
    private BigDecimal freightFee;

    @ApiModelProperty(value = "运单车辆ID列表")
    List<Long> waybillCarIdList;
}
