package com.cjyc.common.model.vo.customer.login;

import com.cjyc.common.model.entity.Customer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 客户表（登录用户端APP用户）
 * </p>
 *
 * @author JPG
 * @since 2019-10-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class CustomerLoginVo extends Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "authentication")
    private String authentication;

    @ApiModelProperty(value = "token")
    private String token;


}
