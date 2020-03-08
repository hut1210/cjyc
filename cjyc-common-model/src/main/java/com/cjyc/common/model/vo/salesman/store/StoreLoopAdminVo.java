package com.cjyc.common.model.vo.salesman.store;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class StoreLoopAdminVo extends StoreVo {
    @ApiModelProperty(value = "目的地业务中心联系人ID")
    private Long storeLooplinkUserId;
    @ApiModelProperty(value = "目的地业务中心联系人")
    private String storeLooplinkName;
    @ApiModelProperty(value = "目的地业务中心联系人手机号")
    private String storeLooplinkPhone;

    public Long getStoreLooplinkUserId() {
        return storeLooplinkUserId == null ? 0 : storeLooplinkUserId;
    }

    public String getStoreLooplinkName() {
        return StringUtils.isBlank(storeLooplinkName) ? "" : storeLooplinkName;
    }

    public String getStoreLooplinkPhone() {
        return StringUtils.isBlank(storeLooplinkPhone) ? "" : storeLooplinkPhone;
    }
}
