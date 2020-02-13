package com.cjyc.common.model.dto.driver.mine;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class BankInfoDto extends BasePageDto implements Serializable {
    private static final long serialVersionUID = -7254134571125596886L;

    @ApiModelProperty(value = "银行名称")
    private String keyword;
}