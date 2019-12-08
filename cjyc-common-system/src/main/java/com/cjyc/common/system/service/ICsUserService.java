package com.cjyc.common.system.service;

import com.cjyc.common.model.entity.defined.UserInfo;

public interface ICsUserService {
    UserInfo getUserInfo(Long loginId, Integer loginType);
}
