package com.cjyc.common.model.dto.promote;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description 分享列表分页查询参数类
 * @Author Liu Xing Xiang
 * @Date 2019/11/13 13:28
 **/
@Data
public class AdminPromoteQueryDto extends BasePageDto {
    private static final long serialVersionUID = -4588589004038815525L;
    @ApiModelProperty(value = "分享渠道")
    private String channel;

    @ApiModelProperty(value = "分享人名称")
    private String userName;

    @ApiModelProperty(value = "注册用户名称")
    private String customerName;

    @ApiModelProperty(value = "注册时间开始")
    private Long registerTimeStart;

    @ApiModelProperty(value = "注册时间结束")
    private Long registerTimeEnd;
}
