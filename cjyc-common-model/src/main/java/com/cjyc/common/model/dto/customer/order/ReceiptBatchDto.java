package com.cjyc.common.model.dto.customer.order;

import com.cjyc.common.model.dto.BaseLoginDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class ReceiptBatchDto extends BaseLoginDto {
    @NotEmpty
    @ApiModelProperty(value = "订单车辆编号",required = true)
    private List<String> orderCarNos;
}
