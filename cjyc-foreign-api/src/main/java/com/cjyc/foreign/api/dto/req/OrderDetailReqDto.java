package com.cjyc.foreign.api.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description 订单详情查询参数类
 * @Author Liu Xing Xiang
 * @Date 2020/3/11 17:09
 **/
@Data
public class OrderDetailReqDto implements Serializable {
    private static final long serialVersionUID = 7823889126670980859L;
    @ApiModelProperty(value = "订单编号",required = true)
    @NotBlank(message = "订单编号不能为空")
    private String orderNo;

    @ApiModelProperty(value = "客户登录id",required = true)
    @NotNull(message = "客户登录id不能为空" )
    private Long loginId;
}
