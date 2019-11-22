package com.cjyc.driver.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.IBankCardBindDao;
import com.cjyc.common.model.dao.ICarrierDriverConDao;
import com.cjyc.common.model.dao.IDriverDao;
import com.cjyc.common.model.dto.driver.BaseDriverDto;
import com.cjyc.common.model.dto.driver.BaseDto;
import com.cjyc.common.model.dto.driver.mine.DriverVehicleDto;
import com.cjyc.common.model.dto.driver.mine.FrozenDto;
import com.cjyc.common.model.entity.CarrierDriverCon;
import com.cjyc.common.model.entity.Driver;
import com.cjyc.common.model.enums.CommonStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.driver.mine.BinkCardVo;
import com.cjyc.common.model.vo.driver.mine.DriverInfoVo;
import com.cjyc.driver.api.service.IMineService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class MineServiceImpl extends ServiceImpl<IDriverDao, Driver> implements IMineService {

    @Resource
    private ICarrierDriverConDao carrierDriverConDao;
    @Resource
    private IDriverDao driverDao;
    @Resource
    private IBankCardBindDao bankCardBindDao;

    private static final Long NOW = LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now());

    @Override
    public ResultVo<List<BinkCardVo>> findBinkCard(BaseDto dto) {
        List<BinkCardVo> bankCardVos = bankCardBindDao.findBinkCardInfo(dto.getLoginId());
        return BaseResultUtil.success(CollectionUtils.isEmpty(bankCardVos) ? Collections.EMPTY_LIST:bankCardVos);
    }

    @Override
    public ResultVo<PageVo<DriverInfoVo>> findDriver(BaseDriverDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<Long> driverIds = driverDao.findDriverIds(dto.getLoginId());
        List<DriverInfoVo> driverInfo = null;
        if(!CollectionUtils.isEmpty(driverIds)){
            driverInfo = driverDao.findDriverInfo(driverIds);
        }
        PageInfo<DriverInfoVo> pageInfo = new PageInfo(driverInfo);
        return BaseResultUtil.success(pageInfo == null ? new PageInfo<>():pageInfo);
    }

    @Override
    public ResultVo frozenDriver(FrozenDto dto) {
        CarrierDriverCon cdc = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>().lambda()
                                .eq(CarrierDriverCon::getDriverId,dto.getDriverId())
                                .eq(CarrierDriverCon::getCarrierId,dto.getCarrierId()));
        if(cdc == null){
            return BaseResultUtil.fail("该司机不存在");
        }
        Driver driver = driverDao.selectOne(new QueryWrapper<Driver>().lambda().eq(Driver::getId, dto.getDriverId()));
        if(driver == null){
            return BaseResultUtil.fail("该司机不存在");
        }
        driver.setCheckUserId(dto.getLoginId());
        driver.setCheckTime(NOW);
        driverDao.updateById(driver);

        cdc.setState(CommonStateEnum.FROZEN.code);
        carrierDriverConDao.updateById(cdc);
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo findVehicle(DriverVehicleDto dto) {

        return null;
    }


}
