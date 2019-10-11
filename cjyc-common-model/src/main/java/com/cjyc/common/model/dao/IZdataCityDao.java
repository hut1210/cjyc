package com.cjyc.common.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.entity.ZdataCity;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
public interface IZdataCityDao extends BaseMapper<ZdataCity> {



    List<ZdataCity> findList();
}
