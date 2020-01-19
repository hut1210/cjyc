package com.cjyc.common.model.dto.web.waybill;

import com.cjyc.common.model.dto.web.BaseWebDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
public class TrunkMidwayUnload extends BaseWebDto {

    @NotNull(message = "运单ID不能为空")
    @ApiModelProperty(value = "运单ID")
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

    @NotNull(message = "运费不能为空")
    @ApiModelProperty(value = "运费")
    private BigDecimal freightFee;

    @NotEmpty(message = "车辆不能为空")
    @ApiModelProperty(value = "运单车辆ID列表")
    List<Long> waybillCarIdList;
}
