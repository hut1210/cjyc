package com.cjyc.common.model.dao;

import com.cjyc.common.model.dto.web.carrier.SeleCarrierDto;
import com.cjyc.common.model.entity.Carrier;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.vo.web.carrier.CarrierVo;
import com.cjyc.common.model.vo.web.carrier.BaseCarrierVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 承运商信息表（个人也算承运商） Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
public interface ICarrierDao extends BaseMapper<Carrier> {

    /**
     * 根据司机id查询承运商id
     * @param driverId
     * @return
     */
    Carrier getCarrierById(@Param("driverId") Long driverId);

    /**
     * 根据条件查询承运商
     * @param dto
     * @return
     */
    List<CarrierVo> getCarrierByTerm(SeleCarrierDto dto);

    /**
     * 根据承运商id查看承运商信息
     * @param carrierId
     * @return
     */
    BaseCarrierVo showCarrierById(@Param("carrierId") Long carrierId);

}
