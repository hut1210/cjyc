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
 * 组织机构，关联架构sys_dept
 * </p>
 *
 * @author JPG
 * @since 2019-10-08
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
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 机构编号
     */
    private String no;

    /**
     * 机构名称
     */
    private String name;

    /**
     * 机构类型：承运商，客户，加盟商，韵车体系
     */
    private Integer type;

    /**
     * 联系人
     */
    private String linkman;

    /**
     * 联系人联系方式
     */
    private String linkmanPhone;

    /**
     * 法人
     */
    private String legal;

    /**
     * 法人联系方式
     */
    private String legalPhone;

    /**
     * 资格证
     */
    private String licenseImg;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 审核人ID
     */
    private Long checkUserId;

    /**
     * 审核时间
     */
    private Long checkTime;


}
