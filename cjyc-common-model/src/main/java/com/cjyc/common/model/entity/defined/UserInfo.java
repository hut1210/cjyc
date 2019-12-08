package com.cjyc.common.model.entity.defined;

import com.cjyc.common.model.enums.UserTypeEnum;
import lombok.Data;

@Data
public class UserInfo {
    private Long id;
    private String name;
    private String phone;
    private UserTypeEnum userType;
}
