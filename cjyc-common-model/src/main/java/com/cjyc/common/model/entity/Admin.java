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
 * 韵车后台管理员表
 * </p>
 *
 * @author JPG
 * @since 2019-10-08
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
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * user_id(查询架构组数据时使用)
     */
    private Long userId;

    /**
     * 编号
     */
    private String no;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 座机号
     */
    private String tel;

    /**
     * 职位：1提送车业务员，2业务员，3调度员，4客服，81财务
     */
    private Integer job;

    /**
     * 状态：0审核中，2在职，4取消审核，7已驳回，9已离职
     */
    private Integer state;

    /**
     * 身份证号
     */
    private String idNumber;

    /**
     * 性别: 0女，1男
     */
    private Integer sex;

    /**
     * 上级ID
     */
    private Long leaderId;

    /**
     * 所属业务中心ID
     */
    private Long storeId;

    /**
     * 头像
     */
    private String photoImg;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 创建人
     */
    private Long createUserId;

    /**
     * 审核人
     */
    private Long checkUserId;

    /**
     * 入职时间
     */
    private Long hireTime;

    /**
     * 离职时间
     */
    private Long leaveTime;

    /**
     * 资金账户ID
     */
    private Long accountId;


}
