package com.cjyc.common.model.dao;

import com.cjyc.common.model.dto.web.dispatch.ValidateLineDto;
import com.cjyc.common.model.dto.web.line.SelectLineDto;
import com.cjyc.common.model.entity.Line;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.vo.salesman.dispatch.ValidateLineVo;
import com.cjyc.common.model.vo.web.line.LineVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
    Line getLinePriceByCode(@Param("fromCode") String fromCode,@Param("toCode") String toCode);

    /**
     * 根据条件查询班线
     * @param dto
     * @return
     */
    List<LineVo> findAllLine(SelectLineDto dto);

    Line findOneByCity(@Param("startCityCode") String startCityCode, @Param("endCityCode")String endCityCode);

    List<Line> findListByCity(@Param("startCityCode") String startCityCode, @Param("endCityCode")String endCityCode);

    ValidateLineVo validateCarLine(ValidateLineDto paramsDto);

    Line findOneByArea(@Param("startAreaCode") String startAreaCode, @Param("endAreaCode") String endAreaCode);
}
