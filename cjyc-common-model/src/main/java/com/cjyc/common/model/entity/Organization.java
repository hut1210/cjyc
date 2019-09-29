package com.cjyc.common.model.entity;

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
 * 组织机构，关联架构sys_dept
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("s_organization")
public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableField("id")
    private Long id;

    /**
     * 机构编号
     */
    @TableField("no")
    private String no;

    /**
     * 机构名称
     */
    @TableField("name")
    private String name;

    /**
     * 机构类型：承运商，客户，加盟商，韵车体系
     */
    @TableField("type")
    private Integer type;

    /**
     * 联系人
     */
    @TableField("linkman")
    private String linkman;

    /**
     * 联系人联系方式
     */
    @TableField("linkman_phone")
    private String linkmanPhone;

    /**
     * 法人
     */
    @TableField("legal")
    private String legal;

    /**
     * 法人联系方式
     */
    @TableField("legal_phone")
    private String legalPhone;

    /**
     * 资格证
     */
    @TableField("license_img")
    private String licenseImg;

    /**
     * 状态
     */
    @TableField("state")
    private Integer state;

    /**
     * 创建人ID
     */
    @TableField("create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;

    /**
     * 审核人ID
     */
    @TableField("check_user_id")
    private Long checkUserId;

    /**
     * 审核时间
     */
    @TableField("check_time")
    private Long checkTime;


}
