package com.cjyc.common.model.dao;

import com.cjyc.common.model.dto.web.excel.ChangePriceExportDto;
import com.cjyc.common.model.dto.web.order.ListOrderChangeLogDto;
import com.cjyc.common.model.entity.OrderChangeLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.vo.web.order.ListOrderChangeLogVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-10-23
 */
public interface IOrderChangeLogDao extends BaseMapper<OrderChangeLog> {

    List<ListOrderChangeLogVo> findList(@Param("paramsDto") ListOrderChangeLogDto paramsDto);

    List<OrderChangeLog> findAll(ChangePriceExportDto reqDto);
}
