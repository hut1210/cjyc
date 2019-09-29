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
 * 银行信息对照表
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("s_bank_info")
public class BankInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 开户银行编号
     */
    @TableField("open_bank_code")
    private String openBankCode;

    /**
     * 支付行号
     */
    @TableField("sub_bank_code")
    private String subBankCode;

    /**
     * 开户支行名称
     */
    @TableField("sub_bank")
    private String subBank;


}
