package com.cjyc.common.model.vo.web.finance;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: Hut
 * @Date: 2019/12/9 14:34
 * 已收款
 */
@Data
public class ReceivableVo extends WaitForBackVo {

    @ApiModelProperty(value = "收款渠道")
    private String channel;

    @ApiModelProperty(value = "收款信息")
    private String information;
}
