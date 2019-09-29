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
 * 承运商信息表（个人也算承运商）
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("d_carrier")
public class Carrier implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 公司名称
     */
    @TableField("name")
    private String name;

    /**
     * 承运商类型：1个人承运商，2企业承运商
     */
    @TableField("type")
    private Integer type;

    /**
     * 注册名称
     */
    @TableField("register_name")
    private String registerName;

    /**
     * 注册电话
     */
    @TableField("register_phone")
    private String registerPhone;

    /**
     * 法人姓名
     */
    @TableField("legal_name")
    private String legalName;

    /**
     * 法人身份证照片
     */
    @TableField("legal_idcard_img")
    private String legalIdcardImg;

    /**
     * 道路运输许可证照片
     */
    @TableField("transport_license_img")
    private String transportLicenseImg;

    /**
     * 公司联系人
     */
    @TableField("linkman")
    private String linkman;

    /**
     * 公司联系人手机号
     */
    @TableField("linkman_phone")
    private String linkmanPhone;

    /**
     * 管理员数量
     */
    @TableField("admin_num")
    private Integer adminNum;

    /**
     * 默认管理员ID
     */
    @TableField("default_admin_id")
    private Long defaultAdminId;

    /**
     * 司机数量
     */
    @TableField("driver_num")
    private Integer driverNum;

    /**
     * 结算方式：1时付，2账期
     */
    @TableField("settle_type")
    private Integer settleType;

    /**
     * 账期/月
     */
    @TableField("settle_period")
    private Integer settlePeriod;

    /**
     * 结算公司：0韵车，1otm，2掌控
     */
    @TableField("settle_corporation")
    private Integer settleCorporation;

    /**
     * 创建者姓名
     */
    @TableField("create_user")
    private String createUser;

    /**
     * 创建者ID
     */
    @TableField("create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;


}
