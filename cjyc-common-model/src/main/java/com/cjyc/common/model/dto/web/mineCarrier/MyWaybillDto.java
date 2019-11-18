package com.cjyc.common.model.dto.web.mineCarrier;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class MyWaybillDto extends BasePageDto implements Serializable {
    private static final long serialVersionUID = -8220538502120941261L;

    @ApiModelProperty("承运商id")
    @NotNull(message = "承运商id不能为空")
    private Long carrierId;

    @ApiModelProperty("运单编号")
    private String billNo;
}