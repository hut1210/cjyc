package com.cjyc.common.model.vo.salesman.login;

import com.cjkj.usercenter.dto.common.auth.AuthLoginResp;
import com.cjyc.common.model.entity.Admin;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 业务员App登录响应信息
 */
@Data
@ApiModel
@Accessors(chain = true)
public class SalesmanAppLoginVo extends AuthLoginResp {
    @ApiModelProperty(value = "角色列表")
    private List<String> roleList;
    @ApiModelProperty(value = "业务员信息")
    private Admin admin;
}
