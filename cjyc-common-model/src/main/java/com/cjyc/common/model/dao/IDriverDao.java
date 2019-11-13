package com.cjyc.common.model.dao;

import com.cjyc.common.model.dto.web.VerifyCarrierDto;
import com.cjyc.common.model.dto.web.driver.DispatchDriverDto;
import com.cjyc.common.model.dto.web.driver.SelectDriverDto;
import com.cjyc.common.model.dto.web.mimeCarrier.QueryMyDriverDto;
import com.cjyc.common.model.dto.web.user.DriverListDto;
import com.cjyc.common.model.entity.Driver;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.vo.web.carrier.BaseDriverVo;
import com.cjyc.common.model.vo.web.driver.DispatchDriverVo;
import com.cjyc.common.model.vo.web.driver.DriverVo;
import com.cjyc.common.model.vo.web.driver.ShowDriverVo;
import com.cjyc.common.model.vo.web.mimeCarrier.MyDriverVo;
import com.cjyc.common.model.vo.web.mimeCarrier.MyFreeDriverVo;
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
     * @param driverId
     * @return
     */
    ShowDriverVo getDriverById(@Param("driverId") Long driverId);

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
     * 查询该承该运商下的司机
     * @return
     */
    List<MyDriverVo> findMyDriver(QueryMyDriverDto dto);

    /**
     * 根据承运商id和司机姓名查询所有符合的司机
     * @param carrierId
     * @param realName
     * @return
     */
    List<MyFreeDriverVo> findMyAllDriver(@Param("carrierId") Long carrierId,@Param("realName") String realName);

    /**
     * 根据承运商查询绑定的司机
     * @param carrierId
     * @return
     */
    List<Long> findMyBusyDriver(@Param("carrierId") Long carrierId);

    /**
     * 查询符合条件的调度个人司机
     * @param dto
     * @return
     */
    List<DispatchDriverVo> getDispatchDriver(DispatchDriverDto dto);

    /**
     * 根据手机号查询个人司机/承运商中是否存在
     * @param
     * @return
     */
    String existCarrier(@Param("phone") String phone,@Param("idCard") String idCard,@Param("type") Integer type);

    /**
     * 通过承运商id获取司机
     * @param carrierId
     * @return
     */
    Driver findDriverByCarrierId(@Param("carrierId") Long carrierId);
}
