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
 * 客户表（登录用户端APP用户）
 * </p>
 *
 * @author JPG
 * @since 2019-10-09
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
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * user_id(查询架构组数据时使用)
     */
    private Long userId;

    /**
     * 客户名称
     */
    private String name;

    /**
     * 别称
     */
    private String alias;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 联系人
     */
    private String contactMan;

    /**
     * 客户地址
     */
    private String contactAddress;

    /**
     * 客户性质
     */
    private String customerNature;

    /**
     * 公司性质/规模
     */
    private String companyNature;

    /**
     * 主要业务描述
     */
    private String majorBusDes;

    /**
     * 首字母
     */
    private String initials;

    /**
     * 头像
     */
    private String photoImg;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 身份证正面
     */
    private String idCardFrontImg;

    /**
     * 身份证反面
     */
    private String idCardBackImg;

    /**
     * 类型：1个人，2企业
     */
    private Integer type;

    /**
     * 账号来源：1App注册，2Applet注册，3业务员创建，4企业管理员创建，5合伙人创建
     */
    private Integer source;

    /**
     * 公司ID
     */
    private Long companyId;

    /**
     * 状态：0未注册，2已注册，7已冻结
     */
    private Integer state;

    /**
     * 注册时间，用户自己注册APP或者首次登陆操作APP时间
     */
    private Long registerTime;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 创建人
     */
    private Long createUserId;


}
