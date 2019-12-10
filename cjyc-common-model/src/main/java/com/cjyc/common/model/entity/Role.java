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
 * 角色信息
 * </p>
 *
 * @author zcm
 * @since 2019-10-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("s_role")
@ApiModel(value="Role对象", description="角色信息")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "角色机构级别 1：全国 2：大区 3：省 4：城市 5：业务中心")
    private Integer roleLevel;

    @ApiModelProperty(value = "角色范围 1：内部 2：外部")
    private Integer roleRange;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "创建人")
    private Long createUserId;

    @ApiModelProperty(value = "最后修改时间")
    private Long updateTime;

    @ApiModelProperty(value = "最后修改人")
    private Long updateUserId;

    @ApiModelProperty(value = "角色状态 1：有效/启用 2: 无效/停用")
    private Integer state;

    @ApiModelProperty(value = "是否可登陆APP 1：可以 2：不可以")
    private Integer loginApp;

    @ApiModelProperty(value = "多个用英文逗号隔开，目前数据有：分配订单、提车调度、干线调度、送车调度")
    private String appBtns;
}
