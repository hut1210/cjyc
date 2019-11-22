package com.cjyc.common.model.dto.web.waybill;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class UpdateTrunkMidwayFinishDto {
    @NotNull(message = "clientId不能为空")
    @ApiModelProperty(value = "1WEB管理后台, 2业务员APP, 4司机APP, 6用户端APP, 7用户端小程序", required = true)
    private int clientId;
    @NotNull(message = "userId不能为空")
    @ApiModelProperty(value = "操作人userid", required = true)
    private Long loginId;
    @ApiModelProperty(value = "操作人userName", required = true)
    private String loginName;
    @ApiModelProperty(value = "操作人userName", required = true)
    private String userPhone;
    @NotNull
    @ApiModelProperty(value = "运单ID")
    private Long waybillId;
    @ApiModelProperty(value = "是否创建后续新运单：true创建，false不创建")
    private Boolean createWaybillFlag;



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



}
