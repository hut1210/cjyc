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
 * 系统审核表
 * </p>
 *
 * @author JPG
 * @since 2020-02-27
 */
@Data
@Accessors(chain = true)
@TableName("s_check")
@ApiModel(value="Check对象", description="系统审核表")
public class Check implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "审核手机号")
    private String phone;

    @ApiModelProperty(value = "合伙人名称")
    private String name;

    @ApiModelProperty(value = "联系人")
    private String contactMan;

    @ApiModelProperty(value = "地址")
    private String contactAddress;

    @ApiModelProperty(value = "统一社会信用代码")
    private String socialCreditCode;

    @ApiModelProperty(value = "状态  0：待审核 1：审核中  2:有效/启用 4：已取消  5：已冻结  7：已拒绝   9:无效/停用")
    private Integer state;

    @ApiModelProperty(value = "类型  0 : C端用户升级合伙人 ")
    private Integer type;

    @ApiModelProperty(value = "账号来源： 4：升级创建")
    private Integer source;

    @ApiModelProperty(value = "创建人id")
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "审核人id")
    private Long checkUserId;

    @ApiModelProperty(value = "审核时间")
    private Long checkTime;


}
