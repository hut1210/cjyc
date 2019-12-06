package com.cjyc.common.model.vo.web.customer;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class CustomerFuzzyListVo {

    @ApiModelProperty(value = "user_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @ApiModelProperty(value = "客户名称")
    private String name;

    @ApiModelProperty(value = "别称")
    private String alias;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "联系人")
    private String contactMan;

    @ApiModelProperty(value = "客户地址")
    private String contactAddress;

    @ApiModelProperty(value = "类型：1个人，2企业（大客户）3-合伙人")
    private Integer type;


}
