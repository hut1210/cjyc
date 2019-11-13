package com.cjyc.common.model.vo.web.promote;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description 分享信息返回参数类
 * @Author Liu Xing Xiang
 * @Date 2019/11/13 14:20
 **/
@Data
public class AdminPromoteVo implements Serializable {
    private static final long serialVersionUID = 1660092563378232781L;
    @ApiModelProperty(value = "分享渠道")
    private String channel;

    @ApiModelProperty(value = "分享人ID")
    private Long userId;

    @ApiModelProperty(value = "分享人名称")
    private String userName;

    @ApiModelProperty(value = "注册用户ID")
    private Long customerId;

    @ApiModelProperty(value = "注册用户名称")
    private String customerName;

    @ApiModelProperty(value = "注册时间")
    private Long createTime;
}
