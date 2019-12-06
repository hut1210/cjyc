package com.cjyc.common.model.vo.web.admin;

import com.cjyc.common.model.entity.Admin;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class AdminVo extends Admin {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long deptId;
}
