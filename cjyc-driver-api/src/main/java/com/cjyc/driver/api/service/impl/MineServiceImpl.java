package com.cjyc.driver.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.IBankCardBindDao;
import com.cjyc.common.model.dao.ICarrierDriverConDao;
import com.cjyc.common.model.dao.IDriverDao;
import com.cjyc.common.model.dto.driver.BaseDriverDto;
import com.cjyc.common.model.dto.driver.BaseDto;
import com.cjyc.common.model.entity.Driver;
import com.cjyc.common.model.util.BaseResultUtil;
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

    @Override
    public ResultVo<List<BinkCardVo>> findBinkCard(BaseDto dto) {
        List<BinkCardVo> bankCardVos = bankCardBindDao.findBinkCardInfo(dto.getLoginId());
        return BaseResultUtil.success(CollectionUtils.isEmpty(bankCardVos) ? Collections.EMPTY_LIST:bankCardVos);
    }

    @Override
    public ResultVo findDriver(BaseDriverDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<Long> driverIds = driverDao.findDriverIds(dto.getLoginId());
        List<DriverInfoVo> driverInfo = null;
        if(!CollectionUtils.isEmpty(driverIds)){
            driverInfo = driverDao.findDriverInfo(driverIds);
        }
        PageInfo<DriverInfoVo> pageInfo = new PageInfo(driverInfo);
        return BaseResultUtil.success(pageInfo == null ? new PageInfo<DriverInfoVo>():pageInfo);
    }
}
