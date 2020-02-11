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
 * 银行信息对照表
 * </p>
 *
 * @author JPG
 * @since 2019-10-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("s_bank_info")
@ApiModel(value="BankInfo对象", description="银行信息对照表")
public class BankInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "开户银行编号")
    private String openBankCode;

    @ApiModelProperty(value = "支付行号")
    private String subBankCode;

    @ApiModelProperty(value = "开户支行名称")
    private String subBank;

    @ApiModelProperty(value = "开户行名称")
    private String bankName;


}
