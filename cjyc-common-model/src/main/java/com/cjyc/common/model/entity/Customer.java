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
 * 客户表（登录用户端APP用户）
 * </p>
 *
 * @author JPG
 * @since 2019-11-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("c_customer")
@ApiModel(value="Customer对象", description="客户表（登录用户端APP用户）")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    @ApiModelProperty(value = "user_id(查询架构组数据时使用)")
    private Long userId;

    @ApiModelProperty(value = "客户编号")
    private String customerNo;

    @ApiModelProperty(value = "客户名称")
    private String name;

    @ApiModelProperty(value = "别称")
    private String alias;

    @ApiModelProperty(value = "联系人")
    private String contactMan;

    @ApiModelProperty(value = "手机号")
    private String contactPhone;

    @ApiModelProperty(value = "客户地址")
    private String contactAddress;

    @ApiModelProperty(value = "客户性质  0：电商 1：租赁 2：金融公司 3：经销商 4：其他")
    private Integer customerNature;

    @ApiModelProperty(value = "统一社会信用代码")
    private String socialCreditCode;

    @ApiModelProperty(value = "首字母")
    private String initial;

    @ApiModelProperty(value = "头像")
    private String photoImg;

    @ApiModelProperty(value = "性别")
    private Integer sex;

    @ApiModelProperty(value = "身份证号")
    private String idCard;

    @ApiModelProperty(value = "身份证正面")
    private String idCardFrontImg;

    @ApiModelProperty(value = "身份证反面")
    private String idCardBackImg;

    @ApiModelProperty(value = "类型：1个人，2企业（大客户）3-合伙人")
    private Integer type;

    @ApiModelProperty(value = "账号来源：1：App注册，2：Applet注册，3：韵车后台 4：升级创建")
    private Integer source;

    @ApiModelProperty(value = "公司ID")
    private Long companyId;

    @ApiModelProperty(value = "状态：0待审核，1未登录，2已审核，3审核拒绝， 7已冻结")
    private Integer state;

    @ApiModelProperty(value = "结算方式 0：时付  1：账期")
    private Integer payMode;

    @ApiModelProperty(value = "注册时间，用户自己注册APP或者首次登陆操作APP时间")
    private Long registerTime;

    @ApiModelProperty(value = "创建操作人id")
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "审核时间")
    private Long checkTime;

    @ApiModelProperty(value = "审核人id")
    private Long checkUserId;


}
