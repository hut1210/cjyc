package com.cjyc.common.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 业务范围
 * @author JPG
 */
@Data
@ApiModel
public class BizScopeVo {
    @ApiModelProperty(value = "业务范围，业务中心ID")
    private List<Long> bizScopeStoreIds;

    //@ApiModelProperty(value = "业务范围，行政区域")
    //private List<String> bizScopeCityCode;
}
