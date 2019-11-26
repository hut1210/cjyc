package com.cjyc.common.model.dto.driver.mine;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class RemoveBankCardDto implements Serializable {
    private static final long serialVersionUID = 2596164153020063347L;
    @ApiModelProperty(value = "银行卡id",required = true)
    @NotNull(message = "银行卡id")
    private Long cardId;


}