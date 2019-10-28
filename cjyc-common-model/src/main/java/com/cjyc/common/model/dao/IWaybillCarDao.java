package com.cjyc.common.model.dao;

import com.cjyc.common.model.dto.web.WayBillCarrierDto;
import com.cjyc.common.model.entity.WaybillCar;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.vo.web.WayBillCarrierVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 运单明细表(车辆表) Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
public interface IWaybillCarDao extends BaseMapper<WaybillCar> {

    List<WayBillCarrierDto> getWayBillCarrier(WayBillCarrierVo wayBillCarrierVo);

    int saveBatch(@Param("list") List<WaybillCar> list);

    List<WaybillCar> findVoByIds(@Param("waybillCarIdList") List<Long> waybillCarIdList);
}
