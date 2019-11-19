package com.cjyc.common.system.service;

import com.cjkj.common.model.ResultData;
import com.cjyc.common.model.entity.Driver;

public interface ICsDriverService {
    Driver getById(Long userId);

    Driver getByUserId(Long userId);

    /**
     * 将司机信息保存到物流平台
     * @param driver
     * @return
     */
    ResultData<Long> saveDriverToPlatform(Driver driver);

    /**
     * 将司机信息更新到平台用户
     * @param driver
     * @return
     */
    ResultData updateUserToPlatform(Driver driver);
}
