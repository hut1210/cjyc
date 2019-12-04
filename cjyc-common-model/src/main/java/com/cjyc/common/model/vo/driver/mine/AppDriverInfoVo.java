package com.cjyc.common.model.vo.driver.mine;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
@Data
public class AppDriverInfoVo implements Serializable {
    private static final long serialVersionUID = 2118409126120040116L;

    @ApiModelProperty("司机id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    @ApiModelProperty("承运商id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long carrierId;

    @ApiModelProperty("司机角色id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long roleId;

    @ApiModelProperty("角色：0个人司机，1下属司机，2管理员，3超级管理员")
    private Integer role;

    @ApiModelProperty("司机userId")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long userId;

    @ApiModelProperty("承运商类型：1个人承运商，2企业承运商")
    private Integer type;

    @ApiModelProperty("司机姓名")
    private String realName;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("身份证号")
    private String idCard;

    @ApiModelProperty("司机身份 0：普通司机 1：管理员")
    private Integer identity;

    @ApiModelProperty("营运状态：0营运中(空闲)，1停运中(繁忙)")
    private Integer businessState;

    @ApiModelProperty("状态：0待审核，2已审核，4取消，5冻结  7已驳回，9已停用（CommonStateEnum）")
    private Integer state;

    @ApiModelProperty("公司名称")
    private String companyName;

    @ApiModelProperty("车牌号")
    private String plateNo;

    @ApiModelProperty("头像")
    private String photoImg;

    public String getPhotoImg(){return StringUtils.isBlank(photoImg) ? "":photoImg;}
    public String getPlateNo(){return StringUtils.isBlank(plateNo) ? "":plateNo;}
}