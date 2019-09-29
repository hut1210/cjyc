package com.cjyc.common.model.entity.auto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 司机信息表（登录司机端APP用户）
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("d_driver")
public class Driver implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * user_id(查询架构组数据时使用)
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 昵称
     */
    @TableField("name")
    private String name;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 紧急联系方式
     */
    @TableField("emergency_phone")
    private String emergencyPhone;

    /**
     * 类型：0业务员，1自营司机，2社会司机
     */
    @TableField("type")
    private Integer type;

    /**
     * 承运方式：1代驾，2托运，3全支持
     */
    @TableField("mode")
    private Integer mode;

    /**
     * 身份证号
     */
    @TableField("id_card")
    private String idCard;

    /**
     * 真实姓名
     */
    @TableField("real_name")
    private String realName;

    /**
     * 年龄
     */
    @TableField("age")
    private Integer age;

    /**
     * 性别
     */
    @TableField("sex")
    private Integer sex;

    /**
     * 地址
     */
    @TableField("address")
    private String address;

    /**
     * 身份证正面
     */
    @TableField("id_card_front_img")
    private String idCardFrontImg;

    /**
     * 身份证反面
     */
    @TableField("id_card_back_img")
    private String idCardBackImg;

    /**
     * 驾驶证正面
     */
    @TableField("driver_licence_front_img")
    private String driverLicenceFrontImg;

    /**
     * 驾驶证反面
     */
    @TableField("driver_licence_back_img")
    private String driverLicenceBackImg;

    /**
     * 驾驶证过期时间
     */
    @TableField("driver_licence_expire")
    private String driverLicenceExpire;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 状态：0待审核，2审核通过，4已驳回，7已冻结
     */
    @TableField("state")
    private Integer state;

    /**
     * 是否缴纳保证金：0否，1是
     */
    @TableField("deposit_pay_state")
    private Integer depositPayState;

    /**
     * 营运状态：0营运中，1停运中
     */
    @TableField("bussiness_state")
    private Integer bussinessState;

    /**
     * 账号来源：1App注册，2Applet注册，3业务员创建，4承运商管理员创建，11掌控接口，12otm接口
     */
    @TableField("source")
    private Integer source;


}
