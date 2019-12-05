package com.cjyc.common.model.vo.driver.mine;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BankCardVos implements Serializable {
    private static final long serialVersionUID = -6704083389865218571L;
    @ApiModelProperty("银行卡")
    private List<BankCardVo> cardVos;
}