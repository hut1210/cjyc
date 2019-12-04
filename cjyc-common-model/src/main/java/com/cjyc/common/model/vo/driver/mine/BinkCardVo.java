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
    private Long cardId;

    @ApiModelProperty("银行卡名称")
    private String bankName;

    @ApiModelProperty("银行卡号")
    private String cardNo;

    @ApiModelProperty("颜色类型 1：红色 2：蓝色 3：绿色")
    private Integer cardColour;
}