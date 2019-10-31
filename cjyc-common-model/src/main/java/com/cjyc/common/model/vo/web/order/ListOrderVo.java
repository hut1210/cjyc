package com.cjyc.common.model.vo.web.order;

import com.cjyc.common.model.entity.Order;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class ListOrderVo extends Order {


}
