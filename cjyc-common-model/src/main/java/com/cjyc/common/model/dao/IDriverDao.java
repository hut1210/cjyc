package com.cjyc.common.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.dto.FreeDto;
import com.cjyc.common.model.dto.driver.AppDriverDto;
import com.cjyc.common.model.dto.driver.mine.CarrierDriverNameDto;
import com.cjyc.common.model.dto.driver.mine.SocietyDriverDto;
import com.cjyc.common.model.dto.driver.task.DriverQueryDto;
import com.cjyc.common.model.dto.web.carrier.TransportDto;
import com.cjyc.common.model.dto.web.driver.CarrierDriverListDto;
import com.cjyc.common.model.dto.web.driver.DispatchDriverDto;
import com.cjyc.common.model.dto.web.driver.SelectDriverDto;
import com.cjyc.common.model.dto.web.mineCarrier.QueryMyDriverDto;
import com.cjyc.common.model.dto.web.user.DriverListDto;
import com.cjyc.common.model.entity.Driver;
import com.cjyc.common.model.vo.driver.mine.DriverInfoVo;
import com.cjyc.common.model.vo.driver.mine.DriverVehicleVo;
import com.cjyc.common.model.vo.driver.mine.SocietyDriverVo;
import com.cjyc.common.model.vo.driver.task.TaskDriverVo;
import com.cjyc.common.model.vo.web.carrier.BaseDriverVo;
import com.cjyc.common.model.vo.web.carrier.TransportDriverVo;
import com.cjyc.common.model.vo.web.driver.DispatchDriverVo;
import com.cjyc.common.model.vo.web.driver.DriverVo;
import com.cjyc.common.model.vo.web.driver.ShowDriverVo;
import com.cjyc.common.model.vo.web.mineCarrier.MyDriverVo;
import com.cjyc.common.model.vo.FreeDriverVo;
import com.cjyc.common.model.vo.web.user.DriverListVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 司机信息表（登录司机端APP用户） Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
public interface IDriverDao extends BaseMapper<Driver> {

    /**
     * 查询司机列表
     * @author JPG
     * @since 2019/10/16 17:32
     * @param paramsDto
     */
    List<DriverListVo> findList(DriverListDto paramsDto);

    /**
     * 根据承运商查询承运商唯一的司机
     * @author JPG
     * @since 2019/10/17 15:23
     * @param carrierId
     */
    Driver findTopByCarrierId(Long carrierId);

    /**
     * 根据条件查询司机信息
     * @param dto
     * @return
     */
    List<DriverVo> getDriverByTerm(SelectDriverDto dto);

    /**
     * 根据司机id查看司机信息
     * @param carrierId
     * @return
     */
    ShowDriverVo getDriverById(@Param("carrierId") Long carrierId);

    /**
     * 根据承运商id查询司机信息
     * @param carrierId
     * @return
     */
    Driver getDriverByCarrierId(@Param("carrierId") Long carrierId);

    /**
     * 根据司机id获取承运商id
     * @param driverId
     * @return
     */
    Long getCarrIdByDriverId(@Param("driverId") Long driverId);

    /**
     * 根据司机ids获取司机信息
     * @param driverIds
     * @return
     */
    List<BaseDriverVo> getDriversByIds(List<Long> driverIds);

    Driver findByUserId(Long userId);

    /**
     * 获取承运商下的司机
     * @param dto
     * @return
     */
    List<TransportDriverVo> findTransportDriver(TransportDto dto);

    /**
     * 查询该承该运商下的司机
     * @return
     */
    List<MyDriverVo> findMyDriver(QueryMyDriverDto dto);

    /**
     * 查询该承运商下的符合条件的司机
     * @param dto
     * @return
     */
    List<FreeDriverVo> findCarrierDriver(FreeDto dto);

    /**
     * 根据承运商查询绑定的司机
     * @param carrierId
     * @return
     */
    List<Long> findCarrierBusyDriver(@Param("carrierId") Long carrierId);

    /**
     * 查询符合条件的调度个人司机
     * @param dto
     * @return
     */
    List<DispatchDriverVo> getDispatchDriver(DispatchDriverDto dto);

    /**
     * 通过承运商id获取司机
     * @param carrierId
     * @return
     */
    Driver findDriverByCarrierId(@Param("carrierId") Long carrierId);

    List<Driver> findAll();

    List<String> findAllNo();

    /**
     * 根据管理员司机id获取符合条件的司机集合
     * @param carrierId
     * @return
     */
    List<Long> findDriverIds(@Param("carrierId") Long carrierId);

    /**
     * 根据承运商获取司机信息
     * @param carrierId
     * @return
     */
    List<DriverInfoVo> findDriverInfo(@Param("carrierId") Long carrierId);

    /**
     * 功能描述: 查询司机列表
     * @author liuxingxiang
     * @date 2019/11/21
     * @param dto
     * @return java.util.List<com.cjyc.common.model.vo.driver.task.TaskDriverVo>
     */
    List<TaskDriverVo> selectDriverList(DriverQueryDto dto);

    /**
     * 获取司机端我的车辆
     * @param carrierId
     * @return
     */
    List<DriverVehicleVo> findVehicle(@Param("carrierId") Long carrierId);

    /**
     * 获取该承运商下符合条件的司机
     * @param
     * @return
     */
    List<FreeDriverVo> findCarrierAllDriver(@Param("carrierId") Long carrierId,@Param("realName") String realName);

    /**
     * 司机端查询个人司机信息
     * @param dto
     * @return
     */
    SocietyDriverVo findPersonInfo(AppDriverDto dto);

    /**
     * 验证在企业承运商下是否存在
     * @param dto
     * @return
     */
    Integer existEnterPriseDriver(SocietyDriverDto dto);

    /**
     * 验证在个人承运商下是否存在
     * @param dto
     * @return
     */
    Integer existPersonDriver(SocietyDriverDto dto);

    /**
     * 查询承运商下属
     * @author JPG
     * @since 2019/11/28 10:23
     * @param dto
     */
    List<DispatchDriverVo> findCarrierDrvierList(CarrierDriverListDto dto);
}
