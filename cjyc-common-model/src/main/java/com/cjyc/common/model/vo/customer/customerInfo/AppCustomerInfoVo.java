package com.cjyc.common.model.vo.customer.customerInfo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
@Data
public class AppCustomerInfoVo implements Serializable {
    private static final long serialVersionUID = 1488779158063038194L;
    @ApiModelProperty("用户id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    @ApiModelProperty("用户userId")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long userId;

    @ApiModelProperty("状态：0待审核，1审核中，2已审核，3审核拒绝， 7已冻结")
    private Integer state;

    @ApiModelProperty("用户名")
    private String name;

    @ApiModelProperty("用户手机号")
    private String phone;

    @ApiModelProperty("头像")
    private String photoImg;

    @ApiModelProperty("类型：1个人,3-合伙人")
    private Integer type;

    public String getPhotoImg(){return StringUtils.isBlank(photoImg) ? "":photoImg;}
}