package com.cjyc.common.model.dao;

import com.cjyc.common.model.dto.driver.AppDriverDto;
import com.cjyc.common.model.dto.web.carrier.*;
import com.cjyc.common.model.dto.web.driver.DriverDto;
import com.cjyc.common.model.dto.CarrierDriverDto;
import com.cjyc.common.model.entity.Carrier;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.vo.driver.login.BaseLoginVo;
import com.cjyc.common.model.vo.driver.mine.AppDriverInfoVo;
import com.cjyc.common.model.vo.web.carrier.*;
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

    /**
     * 调度出符合条件的承运商
     * @param dto
     * @return
     */
    List<DispatchCarrierVo> findDispatchCarrier(DispatchCarrierDto dto);

    /**
     * 调度中心中提车干线调度中代驾和拖车列表
     * @param dto
     * @return
     */
    List<TrailCarrierVo> findTrailDriver(TrailCarrierDto dto);

    /**
     * 根据手机号查询个人司机/承运商中是否存在
     * @param
     * @return
     */
    ExistCarrierVo existCarrier(DriverDto dto);

    /**
     * 判断个人承运商下司机是否存在
     * @param dto
     * @return
     */
    Integer existPersonalCarrier(CarrierDriverDto dto);

    /**
     * 判断在该承运商下司机是否存在
     * @param dto
     * @return
     */
    Integer existBusinessCarrier(CarrierDriverDto dto);

    /**
     * 修改承运商时验证是否为该承运商下的司机
     * @param dto
     * @return
     */
    Integer existBusinessDriver(CarrierDto dto);

    List<Carrier> findBelongListByDriverId(Long driverId);

    List<Long> getBelongIdsListByDriver(Long driverId);

    /**
     * 根据司机id获取信息
     * @param driverId
     * @return
     */
    List<BaseLoginVo> findDriverLogin(@Param("driverId") Long driverId);

    /**
     * 获取司机信息
     * @param roleId
     * @param loginId
     * @return
     */
    AppDriverInfoVo findAppDriverInfo(@Param("roleId") Long roleId,@Param("loginId") Long loginId);

    Carrier findByDeptId(Long deptId);
}
