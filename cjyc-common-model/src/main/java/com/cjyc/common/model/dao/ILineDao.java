package com.cjyc.common.model.dao;

import com.cjyc.common.model.entity.Line;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 班线管理 Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
public interface ILineDao extends BaseMapper<Line> {

    /**
     * 根据城市编码查询运价
     * @param fromCode
     * @param toCode
     * @return
     */
    String getLinePriceByCode(@Param("fromCode") String fromCode,@Param("toCode") String toCode);

}
