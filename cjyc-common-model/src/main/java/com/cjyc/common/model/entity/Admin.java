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
 * 韵车后台管理员表
 * </p>
 *
 * @author JPG
 * @since 2019-10-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_admin")
@ApiModel(value="Admin对象", description="韵车后台管理员表")
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "user_id(查询架构组数据时使用)")
    private Long userId;

    @ApiModelProperty(value = "编号")
    private String no;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "座机号")
    private String tel;

    @ApiModelProperty(value = "职位：1提送车业务员，2业务员，3调度员，4客服，81财务")
    private Integer job;

    @ApiModelProperty(value = "状态：0审核中，2在职，4取消审核，7已驳回，9已离职")
    private Integer state;

    @ApiModelProperty(value = "身份证号")
    private String idNumber;

    @ApiModelProperty(value = "性别: 0女，1男")
    private Integer sex;

    @ApiModelProperty(value = "上级ID")
    private Long leaderId;

    @ApiModelProperty(value = "所属业务中心ID")
    private Long storeId;

    @ApiModelProperty(value = "头像")
    private String photoImg;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "创建人")
    private Long createUserId;

    @ApiModelProperty(value = "审核人")
    private Long checkUserId;

    @ApiModelProperty(value = "入职时间")
    private Long hireTime;

    @ApiModelProperty(value = "离职时间")
    private Long leaveTime;

    @ApiModelProperty(value = "资金账户ID")
    private Long accountId;


}
