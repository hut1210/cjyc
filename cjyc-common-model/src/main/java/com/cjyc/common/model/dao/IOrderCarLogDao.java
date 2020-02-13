package com.cjyc.common.model.dao;

import com.cjyc.common.model.entity.OrderCarLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.vo.customer.order.OutterOrderCarLogVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 车辆物流轨迹 Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-11-28
 */
public interface IOrderCarLogDao extends BaseMapper<OrderCarLog> {

    List<OutterOrderCarLogVo> findCarLogByOrderNoAndCarNo(@Param("orderNo") String orderNo, @Param("orderCarNo") String orderCarNo);
}
