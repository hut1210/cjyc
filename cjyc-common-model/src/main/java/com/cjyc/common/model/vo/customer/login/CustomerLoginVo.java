package com.cjyc.common.model.vo.customer.login;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
public class CustomerLoginVo  implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    @ApiModelProperty("用户userId")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long userId;

    @ApiModelProperty("状态：0待审核，1未登录，2已审核，3审核拒绝， 7已冻结")
    private Integer state;

    @ApiModelProperty("用户名")
    private String name;

    @ApiModelProperty("联系人")
    private String contactMan;

    @ApiModelProperty("用户手机号")
    private String phone;

    @ApiModelProperty("头像")
    private String photoImg;

    @ApiModelProperty("类型：1个人,3-合伙人")
    private Integer type;

    @ApiModelProperty("token值")
    private String accessToken;
}
