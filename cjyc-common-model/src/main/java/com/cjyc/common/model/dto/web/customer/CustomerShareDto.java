package com.cjyc.common.model.dto.web.customer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @auther litan
 * @description: com.cjyc.common.model.dto.web.customer
 * @date:2019/10/28
 */
@Data
public class CustomerShareDto {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("分享用户名")
    private String shareUserName;

    @ApiModelProperty(value = "注册用户名")
    private String registUserName;

    @ApiModelProperty(value = "注册开始时间")
    private String registStartDate;

    @ApiModelProperty(value = "注册结束时间")
    private String registEndDate;

    @ApiModelProperty(value = "分享渠道 0-全部 1-小程序 2-app 3-h5")
    private Integer channel;

}
