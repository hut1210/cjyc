package com.cjyc.common.model.dao;

import com.cjyc.common.model.entity.CarrierDriverCon;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 承运商/司机关系表 Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
public interface ICarrierDriverConDao extends BaseMapper<CarrierDriverCon> {
}
