package com.cjyc.common.model.entity.defined;

import lombok.Data;

import java.util.Set;

/**
 * 业务范围
 * @author JPG
 */
@Data
public class BizScope {
    /**
     *
     * @author JPG
     * @since 2019/12/11 16:42
     * @see com.cjyc.common.model.enums.BizScopeEnum
     */
    private int code;
    private Set<Long> storeIds;

}
