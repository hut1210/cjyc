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
 * 用户-角色-机构信息表
 * </p>
 *
 * @author zcm
 * @since 2019-12-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("s_user_role_dept")
@ApiModel(value="UserRoleDept对象", description="用户-角色-机构信息表")
public class UserRoleDept implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户标识信息(b_admin、c_customer、d_driver 主键id)")
    private Long userId;

    @ApiModelProperty(value = "角色标识信息(s_role 主键id)")
    private Long roleId;

    @ApiModelProperty(value = "机构标识: 业务中心标识(s_store 主键 dept_type = 1 and dept_level = 5)、城市编码(s_city code值 dept_type = 1 and dept_level = 1, 2, 3, 4) 或 承运商标识（d_carrier 主键 dept_type = 2)")
    private String deptId;

    @ApiModelProperty(value = "机构级别(针对业务员) 1：全国 2：大区 3：省 4：城市 5：业务中心")
    private Integer deptLevel;

    @ApiModelProperty(value = "机构级别 1：内部 2：承运商 3：客户级别")
    private Integer deptType;

    @ApiModelProperty(value = "用户级别 1：业务员 2：司机 3：客户")
    private Integer userType;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "创建人")
    private Long createUserId;

    @ApiModelProperty(value = "最后修改时间")
    private Long updateTime;

    @ApiModelProperty(value = "最后修改人")
    private Long updateUserId;

    @ApiModelProperty(value = "状态  0：待审核 1：审核中  2:有效/启用 4：已取消  5：已冻结  7：已拒绝   9:无效/停用")
    private Integer state;

    @ApiModelProperty(value = "承运方式：2 : 代驾  3 : 干线  4：拖车  5：代驾+干线  6：代驾+拖车  7：干线+拖车  9：代驾+干线+拖车")
    private Integer mode;


}
