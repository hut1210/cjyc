package com.cjyc.common.model.entity.defined;

import lombok.Data;

import java.util.Set;

/**
 * 业务范围
 * @author JPG
 */
@Data
public class BizScope {
    private int code;
    private Set<Long> storeIds;

}
