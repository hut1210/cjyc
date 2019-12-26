package com.cjyc.common.model.dto.web.pingxx;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author: Hut
 * @Date: 2019/12/26 14:00
 */
@Data
public class WebOutOfStockDto {

    @ApiModelProperty(value = "IP（不用传）")
    private String ip;

    @ApiModelProperty(value = "支付渠道 微信：wx_pub_qr，支付宝：alipay_qr")
    private String channel;

    @ApiModelProperty(value = "用户ID",required = true)
    private Long loginId;

    @ApiModelProperty(value = "任务Id")
    private Long taskId;

    @NotEmpty(message = "任务车辆ID")
    @ApiModelProperty(value = "任务车辆ID")
    private List<String> taskCarIdList;
}
