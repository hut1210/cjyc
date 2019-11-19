package com.cjyc.driver.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.entity.Driver;
import com.cjyc.common.model.vo.ResultVo;

public interface IMineService extends IService<Driver> {

    /**
     * 根据司机id获取绑定银行卡信息
     * @param loginId
     * @return
     */
    ResultVo findBinkCardInfo(Long loginId);


}
