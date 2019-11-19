package com.cjyc.common.model.vo.web.admin;

import lombok.Data;

@Data
public class CacheData {

    private Long loginId;
    private String loginName;
    private String loginPhone;
    private Long roleId;
    private Long deptId;
    private String token;
    private Integer loginType;
}
