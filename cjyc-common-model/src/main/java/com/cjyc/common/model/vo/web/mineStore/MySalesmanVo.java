package com.cjyc.common.model.vo.web.mineStore;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;

/**
 * 我的业务中心-我的业务员列表响应信息
 */

@Data
@ApiModel
@Validated
public class MySalesmanVo implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "业务员记录标识")
    private Long id;
    @ApiModelProperty(value = "登录账号")
    private String account;
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "电话")
    private String phone;
    @ApiModelProperty(value = "角色")
    private String roles;
    @ApiModelProperty(value = "是否业务中心联系人")
    private boolean isContactPerson;
}
