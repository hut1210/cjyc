package com.cjyc.common.model.entity.auto;

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
 * 银行卡绑定信息表
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("s_bank_card_bind")
public class BankCardBind implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableField("id")
    private Long id;

    /**
     * 绑卡人员ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 绑卡人员类型
     */
    @TableField("user_type")
    private Integer userType;

    /**
     * 卡类型
     */
    @TableField("card_type")
    private Integer cardType;

    /**
     * 银行卡户主
     */
    @TableField("card_name")
    private String cardName;

    /**
     * 银行卡卡号
     */
    @TableField("card_no")
    private String cardNo;

    /**
     * 银行卡留存电话
     */
    @TableField("card_phone")
    private String cardPhone;

    /**
     * 身份证号
     */
    @TableField("id_card")
    private String idCard;

    /**
     * 开户银行编号
     */
    @TableField("bank_code")
    private String bankCode;

    /**
     * 开户银行名称
     */
    @TableField("bank_name")
    private String bankName;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

    /**
     * 状态：0不可用，1可用
     */
    @TableField("state")
    private Integer state;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;


}
