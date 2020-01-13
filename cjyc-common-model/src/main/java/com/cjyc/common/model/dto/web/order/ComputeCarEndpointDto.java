package com.cjyc.common.model.dto.web.order;

import com.cjyc.common.model.dto.web.BaseWebDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class ComputeCarEndpointDto extends BaseWebDto {
    @ApiModelProperty("调度类型：1提车运单，2干线运单，3送车运单")
    private Long dispatchType = 2L;
    @NotEmpty(message = "车辆列表不能为空")
    @ApiModelProperty(value = "车辆ID列表",required = true)
    private List<Long> orderCarIdList;
}
