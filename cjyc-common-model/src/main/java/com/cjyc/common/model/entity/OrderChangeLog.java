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
 * 
 * </p>
 *
 * @author JPG
 * @since 2019-10-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("w_order_change_log")
@ApiModel(value="OrderChangeLog对象", description="")
public class OrderChangeLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "订单ID")
    private Long orderId;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "操作名称")
    private String name;

    @ApiModelProperty(value = "操作类型")
    private Integer type;

    @ApiModelProperty(value = "旧内容，Json格式")
    private String oldContent;

    @ApiModelProperty(value = "新内容，Json格式")
    private String newContent;

    @ApiModelProperty(value = "原因")
    private String reason;

    @ApiModelProperty(value = "状态：0待审核，2审核通过，4已取消，7已驳回")
    private Integer state;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "创建人ID")
    private Long createUserId;

    @ApiModelProperty(value = "审核时间")
    private Long checkTime;

    @ApiModelProperty(value = "审核人")
    private String checkUser;

    @ApiModelProperty(value = "审核人ID")
    private Long checkUserId;


}
