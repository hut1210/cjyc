package com.cjyc.common.model.dto.web.mineCarrier;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: Hut
 * @Date: 2019/12/18 16:15
 */
@Data
public class SettlementDetailQueryDto extends BasePageDto {

    @ApiModelProperty("承运商Id")
    private Long carrierId;
}
