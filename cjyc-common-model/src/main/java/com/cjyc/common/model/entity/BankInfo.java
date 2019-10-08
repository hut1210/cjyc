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
 * 银行信息对照表
 * </p>
 *
 * @author JPG
 * @since 2019-10-08
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
    private String openBankCode;

    /**
     * 支付行号
     */
    private String subBankCode;

    /**
     * 开户支行名称
     */
    private String subBank;


}
