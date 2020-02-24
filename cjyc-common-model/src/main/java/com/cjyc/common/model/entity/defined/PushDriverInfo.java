package com.cjyc.common.model.entity.defined;

import com.cjyc.common.model.enums.UserTypeEnum;
import lombok.Data;

import java.util.List;

@Data
public class PushDriverInfo {
    private UserTypeEnum userTypeEnum;
    private List<String> orderCarNos;

}
