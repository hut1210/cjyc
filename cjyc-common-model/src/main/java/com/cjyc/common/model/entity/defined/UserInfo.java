package com.cjyc.common.model.entity.defined;

import com.cjyc.common.model.enums.UserTypeEnum;
import lombok.Data;

@Data
public class UserInfo {
    private Long id;
    private String name;
    private String phone;
    private UserTypeEnum userType;

    public UserInfo() {
    }

    public UserInfo(Long id, String name, String phone, UserTypeEnum userType) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.userType = userType;
    }
}
