package com.cjyc.common.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2019-10-08
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
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 公司名称
     */
    private String name;

    /**
     * 承运商类型：1个人承运商，2企业承运商
     */
    private Integer type;

    /**
     * 注册名称
     */
    private String registerName;

    /**
     * 注册电话
     */
    private String registerPhone;

    /**
     * 法人姓名
     */
    private String legalName;

    /**
     * 法人身份证照片
     */
    private String legalIdcardImg;

    /**
     * 道路运输许可证照片
     */
    private String transportLicenseImg;

    /**
     * 公司联系人
     */
    private String linkman;

    /**
     * 公司联系人手机号
     */
    private String linkmanPhone;

    /**
     * 管理员数量
     */
    private Integer adminNum;

    /**
     * 默认管理员ID
     */
    private Long defaultAdminId;

    /**
     * 司机数量
     */
    private Integer driverNum;

    /**
     * 结算方式：1时付，2账期
     */
    private Integer settleType;

    /**
     * 账期/月
     */
    private Integer settlePeriod;

    /**
     * 结算公司：0韵车，1otm，2掌控
     */
    private Integer settleCorporation;

    /**
     * 创建者姓名
     */
    private String createUser;

    /**
     * 创建者ID
     */
    private Long createUserId;

    /**
     * 创建时间
     */
    private Long createTime;


}
