package com.cjyc.common.model.dto.salesman.order;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * 业务员APP：选择业务员查询dto
 */
@Data
@ApiModel
@Validated
public class SalesmanQueryDto extends BasePageDto {
    @NotNull(message = "订单所属业务中心ID不能为空")
    @ApiModelProperty(value = "订单所属业务中心ID", required = true)
    private Long inputStoreId;
    @ApiModelProperty(value = "姓名或电话号码")
    private String searchValue;
}
