package com.cjyc.common.model.vo.web.admin;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class CacheData {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long loginId;
    private String loginName;
    private String loginPhone;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long roleId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long deptId;
    private String token;
    private Integer loginType;
}
