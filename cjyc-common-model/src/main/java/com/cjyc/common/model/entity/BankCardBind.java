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
 * 银行卡绑定信息表
 * </p>
 *
 * @author JPG
 * @since 2019-10-08
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
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 绑卡人员ID
     */
    private Long userId;

    /**
     * 绑卡人员类型
     */
    private Integer userType;

    /**
     * 卡类型
     */
    private Integer cardType;

    /**
     * 银行卡户主
     */
    private String cardName;

    /**
     * 银行卡卡号
     */
    private String cardNo;

    /**
     * 银行卡留存电话
     */
    private String cardPhone;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 开户银行编号
     */
    private String bankCode;

    /**
     * 开户银行名称
     */
    private String bankName;

    /**
     * 描述
     */
    private String description;

    /**
     * 状态：0不可用，1可用
     */
    private Integer state;

    /**
     * 创建时间
     */
    private Long createTime;


}
