package com.cjyc.common.model.vo.customer.customerLine;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class StoreListVo implements Serializable {
    private static final long serialVersionUID = -9047341490061552689L;

    @ApiModelProperty("业务中心集合")
    private List<BusinessStoreVo> storeVoList;
}