package com.cjyc.common.model.dao;

import com.cjyc.common.model.dto.web.waybill.storeListDto;
import com.cjyc.common.model.entity.CarStorageLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 出入库记录 Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-11-22
 */
public interface ICarStorageLogDao extends BaseMapper<CarStorageLog> {

    List<CarStorageLog> findList(@Param("paramsDto") storeListDto paramsDto);
}
