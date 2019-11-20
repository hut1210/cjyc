package com.cjyc.driver.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.dto.driver.BaseDriverDto;
import com.cjyc.common.model.dto.driver.BaseDto;
import com.cjyc.common.model.entity.Driver;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.driver.mine.BinkCardVo;

import java.util.List;

public interface IMineService extends IService<Driver> {

    /**
     * 根据司机id获取绑定银行卡信息
     * @param dto
     * @return
     */
    ResultVo<List<BinkCardVo>> findBinkCard(BaseDto dto);

    /**
     * 查询司机管理信息
     * @param dto
     * @return
     */
    ResultVo findDriver(BaseDriverDto dto);


}
