package com.cjyc.common.model.entity.defined;

import com.cjyc.common.model.enums.UserTypeEnum;
import com.cjyc.common.model.enums.message.PushMsgEnum;
import lombok.Data;

import java.util.Map;

@Data
public class PushInfo {
    private Long toUserId;
    private UserTypeEnum userTypeEnum;
    private PushMsgEnum pushMsgEnum;
    private Map<String, Object> params;

}
