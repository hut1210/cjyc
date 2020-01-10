package com.cjyc.common.model.dto.web.store;

import lombok.Data;

@Data
public class GetStoreDto {
    private Long loginId;
    private Long roleId;
    private String storeName;
}
