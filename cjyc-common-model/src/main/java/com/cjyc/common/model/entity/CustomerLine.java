package com.cjyc.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @auther litan
 * @description: com.cjyc.common.model.entity.sys
 * @date:2019/10/12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("c_customer_line")
@ApiModel(value="CustomerLine实体", description="用户历史班线表")
public class CustomerLine implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long customerId;

    @ApiModelProperty(value = "锁定积分")
    private String lineCode;

    @ApiModelProperty(value = "可用积分")
    private String startAdress;

    @ApiModelProperty(value = "总收入积分")
    private String startContact;

    @ApiModelProperty(value = "总花费积分")
    private String startContactPhone;

    @ApiModelProperty(value = "状态：")
    private String endAdress;

    @ApiModelProperty(value = "创建时间")
    private String endContact;

    @ApiModelProperty(value = "更新时间")
    private String endContactPhone;

    @ApiModelProperty(value = "更新时间")
    private Long createTime;
}
