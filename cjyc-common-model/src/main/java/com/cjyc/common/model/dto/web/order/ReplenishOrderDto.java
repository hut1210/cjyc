package com.cjyc.common.model.dto.web.order;

import com.cjyc.common.model.dto.BaseLoginDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 入参
 * @author JPG
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class ReplenishOrderDto extends BaseLoginDto {


    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @NotEmpty(message = "车辆信息不能为空")
    private List<ReplenishOrderCarDto> orderCarList;

}
