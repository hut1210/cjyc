package com.cjyc.web.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.dto.web.mimeCarrier.MyDriverDto;
import com.cjyc.common.model.entity.Carrier;
import com.cjyc.common.model.vo.ResultVo;

public interface IMimeCarrierService extends IService<Carrier> {

    /**
     * 新增承运商下司机
     * @param dto
     * @return
     */
    ResultVo saveDriver(MyDriverDto dto);
}
