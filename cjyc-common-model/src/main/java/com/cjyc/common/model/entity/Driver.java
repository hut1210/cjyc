package com.cjyc.common.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 司机信息表（登录司机端APP用户）
 * </p>
 *
 * @author JPG
 * @since 2019-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("d_driver")
@ApiModel(value="Driver对象", description="司机信息表（登录司机端APP用户）")
public class Driver implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    @ApiModelProperty(value = "user_id(查询架构组数据时使用)")
    private Long userId;

    @ApiModelProperty(value = "昵称")
    private String name;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "紧急联系方式")
    private String emergencyPhone;

    @ApiModelProperty(value = "类型：1自营司机，2社会司机")
    private Integer type;

    @ApiModelProperty(value = "司机身份 0：普通司机 1：管理员")
    private Integer identity;

    @ApiModelProperty(value = "身份证号")
    private String idCard;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "性别")
    private Integer sex;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "身份证正面")
    private String idCardFrontImg;

    @ApiModelProperty(value = "身份证反面")
    private String idCardBackImg;

    @ApiModelProperty(value = "驾驶证正面")
    private String driverLicenceFrontImg;

    @ApiModelProperty(value = "驾驶证反面")
    private String driverLicenceBackImg;

    @ApiModelProperty(value = "行驶证正面")
    private String travelLicenceFrontImg;

    @ApiModelProperty(value = "行驶证反面")
    private String travelLicenceBackImg;

    @ApiModelProperty(value = "营运证正面")
    private String taxiLicenceFrontImg;

    @ApiModelProperty(value = "营运证反面")
    private String taxiLicenceBackImg;

    @ApiModelProperty(value = "从业证正面")
    private String qualifiCertFrontImg;

    @ApiModelProperty(value = "从业证反面")
    private String qualifiCertBackImg;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否缴纳保证金：0否，1是")
    private Integer depositPayState;

    @ApiModelProperty(value = "营运状态：0营运中，1停运中")
    private Integer businessState;

    @ApiModelProperty(value = "账号来源：1App注册，2Applet注册，3业务员创建，4承运商管理员创建，11掌控接口，12otm接口")
    private Integer source;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "创建人id")
    private Long createUserId;

    @ApiModelProperty(value = "操作时间")
    private Long checkTime;

    @ApiModelProperty(value = "操作人id")
    private Long checkUserId;


}
