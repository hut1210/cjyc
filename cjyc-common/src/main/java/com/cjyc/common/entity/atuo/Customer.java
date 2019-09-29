package com.cjyc.common.entity.atuo;

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
 * 客户表（登录用户端APP用户）
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("c_customer")
public class Customer implements Serializable {

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
     * 客户名称
     */
    @TableField("name")
    private String name;

    /**
     * 别称
     */
    @TableField("alias")
    private String alias;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 首字母
     */
    @TableField("initials")
    private String initials;

    /**
     * 头像
     */
    @TableField("photo_img")
    private String photoImg;

    /**
     * 性别
     */
    @TableField("sex")
    private Integer sex;

    /**
     * 身份证号
     */
    @TableField("id_card")
    private String idCard;

    /**
     * 身份证正面
     */
    @TableField("id_card_front_img")
    private String idCardFrontImg;

    /**
     * 身份证反面
     */
    @TableField("id_card_back_img")
    private String idCardBackImg;

    /**
     * 类型：1个人，2企业
     */
    @TableField("type")
    private Integer type;

    /**
     * 账号来源：1App注册，2Applet注册，3业务员创建，4企业管理员创建，5合伙人创建
     */
    @TableField("source")
    private Integer source;

    /**
     * 公司ID
     */
    @TableField("company_id")
    private Long companyId;

    /**
     * 状态：0未注册，2已注册，7已冻结
     */
    @TableField("state")
    private Integer state;

    /**
     * 注册时间，用户自己注册APP或者首次登陆操作APP时间
     */
    @TableField("register_time")
    private Long registerTime;

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


}
