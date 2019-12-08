package com.cjyc.common.model.dao;

import com.cjyc.common.model.entity.TradeBillDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-12-04
 */
public interface ITradeBillDetailDao extends BaseMapper<TradeBillDetail> {

    int saveBatch(@Param("tradeBillId") Long tradeBillId, @Param("list") List<String> orderNos);
}
