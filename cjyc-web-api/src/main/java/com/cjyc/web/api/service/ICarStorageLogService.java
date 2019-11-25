package com.cjyc.web.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.dto.web.waybill.storeListDto;
import com.cjyc.common.model.entity.CarStorageLog;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;

/**
 * <p>
 * 出入库记录 服务类
 * </p>
 *
 * @author JPG
 * @since 2019-11-22
 */
public interface ICarStorageLogService extends IService<CarStorageLog> {

    ResultVo<PageVo<CarStorageLog>> inList(storeListDto dto);
}
