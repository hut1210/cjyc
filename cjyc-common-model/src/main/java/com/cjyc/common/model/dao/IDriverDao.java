package com.cjyc.common.model.dao;

import com.cjyc.common.model.dto.web.driver.SelectDriverDto;
import com.cjyc.common.model.dto.web.user.DriverListDto;
import com.cjyc.common.model.entity.Driver;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.vo.web.carrier.BaseDriverVo;
import com.cjyc.common.model.vo.web.driver.DriverVo;
import com.cjyc.common.model.vo.web.driver.ShowDriverVo;
import com.cjyc.common.model.vo.web.user.DriverListVo;
import io.swagger.annotations.ApiParam;
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
     * 根据司机id/userId查看司机信息
     * @param id
     * @param userId
     * @return
     */
    ShowDriverVo getDriverById(@Param("id") Long id,@Param("userId") Long userId);

    /**
     * 根据承运商id查询司机信息
     * @param carrierId
     * @return
     */
    Driver getDriverByDriverId(@Param("carrierId") Long carrierId);

    Driver findByUserId(Long userId);

    Long getCarrIdByDriverId(Long driverId);

    List<BaseDriverVo> getDriversByIds(List<Long> idsList);
}
