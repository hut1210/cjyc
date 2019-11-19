package com.cjyc.common.model.vo.driver.mine;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class BinkCardVo implements Serializable {
    private static final long serialVersionUID = 5058249513448361778L;

    @ApiModelProperty("绑定银行卡id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long bankId;

    @ApiModelProperty("银行卡名称")
    private String bankName;

    @ApiModelProperty("银行卡号")
    private String cardNo;
}