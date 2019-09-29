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
 * 韵车后台管理员表
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_admin")
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * user_id(查询架构组数据时使用)
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 编号
     */
    @TableField("no")
    private String no;

    /**
     * 姓名
     */
    @TableField("name")
    private String name;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 座机号
     */
    @TableField("tel")
    private String tel;

    /**
     * 职位：1提送车业务员，2业务员，3调度员，4客服，81财务
     */
    @TableField("job")
    private Integer job;

    /**
     * 状态：0审核中，2在职，4取消审核，7已驳回，9已离职
     */
    @TableField("state")
    private Integer state;

    /**
     * 身份证号
     */
    @TableField("id_number")
    private String idNumber;

    /**
     * 性别: 0女，1男
     */
    @TableField("sex")
    private Integer sex;

    /**
     * 上级ID
     */
    @TableField("leader_id")
    private Long leaderId;

    /**
     * 所属业务中心ID
     */
    @TableField("store_id")
    private Long storeId;

    /**
     * 头像
     */
    @TableField("photo_img")
    private String photoImg;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;

    /**
     * 创建人
     */
    @TableField("create_user_id")
    private Long createUserId;

    /**
     * 审核人
     */
    @TableField("check_user_id")
    private Long checkUserId;

    /**
     * 入职时间
     */
    @TableField("hire_time")
    private Long hireTime;

    /**
     * 离职时间
     */
    @TableField("leave_time")
    private Long leaveTime;

    /**
     * 资金账户ID
     */
    @TableField("account_id")
    private Long accountId;


}
