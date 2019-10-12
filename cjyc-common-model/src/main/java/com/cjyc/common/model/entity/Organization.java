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
 * 组织机构，关联架构sys_dept
 * </p>
 *
 * @author JPG
 * @since 2019-10-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("s_organization")
@ApiModel(value="Organization对象", description="组织机构，关联架构sys_dept")
public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "机构编号")
    private String no;

    @ApiModelProperty(value = "机构名称")
    private String name;

    @ApiModelProperty(value = "机构类型：承运商，客户，加盟商，韵车体系")
    private Integer type;

    @ApiModelProperty(value = "联系人")
    private String linkman;

    @ApiModelProperty(value = "联系人联系方式")
    private String linkmanPhone;

    @ApiModelProperty(value = "法人")
    private String legal;

    @ApiModelProperty(value = "法人联系方式")
    private String legalPhone;

    @ApiModelProperty(value = "资格证")
    private String licenseImg;

    @ApiModelProperty(value = "状态")
    private Integer state;

    @ApiModelProperty(value = "创建人ID")
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "审核人ID")
    private Long checkUserId;

    @ApiModelProperty(value = "审核时间")
    private Long checkTime;


}
