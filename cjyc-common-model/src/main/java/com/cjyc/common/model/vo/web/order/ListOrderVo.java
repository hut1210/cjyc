package com.cjyc.common.model.vo.web.order;

import com.cjyc.common.model.entity.Order;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class ListOrderVo extends Order {

    @ApiModelProperty("出发地址（全）")
    private String startFullAddress;
    @ApiModelProperty("目的地址（全）")
    private String endFullAddress;

    @ApiModelProperty("总物流费")
    private String wlTotalFee;
    @ApiModelProperty("大区")
    private String region;
    @ApiModelProperty("大区编码")
    private String regionCode;
    @ApiModelProperty("所属业务中心地址")
    private String inputStoreAddress;
    @ApiModelProperty("出发业务中心地址")
    private String startStoreAddress;
    @ApiModelProperty("目的业务中心地址")
    private String endStoreAddress;
    @ApiModelProperty("合同编号")
    private String contractNo;
    @ApiModelProperty("账期/天")
    private String settlePerio;
    @ApiModelProperty("总服务费")
    private String totalAgencyFee;


}
