package com.cjyc.driver.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.dto.LoginDto;
import com.cjyc.common.model.entity.Driver;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.driver.login.DriverLoginVo;

public interface ILoginService extends IService<Driver> {

    /**
     * 司机登陆注册
     * @param dto
     * @return
     */
    ResultVo<DriverLoginVo> login(LoginDto dto);
}
