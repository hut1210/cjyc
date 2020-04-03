package com.cjyc.common.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * <p>
 * 银行编码+支付行号对应关系表
 * </p>
 *
 * @author RenPl
 * @since 2020-4-3
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("s_bank_subank_relation")
@ApiModel(value="BankSubankRelation对象", description="银行编码+支付行号对应关系表")
public class BankSubankRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "开户银行编号")
    private String bankCode;

    @ApiModelProperty(value = "支付行号")
    private String payBankNo;
}
