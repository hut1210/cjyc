package com.cjyc.common.model.dto.web.order;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 入参
 * @author JPG
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class ReplenishOrderDto {

    private Long loginId;
    private String loginName;
    private Long orderId;
    private List<ReplenishOrderCarDto> orderCarList;

}
