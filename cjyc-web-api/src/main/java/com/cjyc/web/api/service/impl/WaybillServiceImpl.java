package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjkj.common.redis.lock.RedisDistributedLock;
import com.cjkj.common.redis.template.StringRedisUtil;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.web.waybill.CysWaybillDto;
import com.cjyc.common.model.dto.web.waybill.*;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.waybill.*;
import com.cjyc.common.system.service.ICsWaybillService;
import com.cjyc.web.api.service.ISendNoService;
import com.cjyc.web.api.service.IWaybillService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 运单表(业务员调度单) 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-17
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class WaybillServiceImpl extends ServiceImpl<IWaybillDao, Waybill> implements IWaybillService {

    @Resource
    private IWaybillDao waybillDao;
    @Resource
    private IOrderCarDao orderCarDao;
    @Resource
    private IOrderDao orderDao;
    @Resource
    private StringRedisUtil redisUtil;
    @Resource
    private RedisDistributedLock redisLock;
    @Resource
    private ISendNoService sendNoService;
    @Resource
    private ICarrierDao carrierDao;
    @Resource
    private IAdminDao adminDao;
    @Resource
    private IWaybillCarDao waybillCarDao;
    @Resource
    private IDriverDao driverDao;
    @Resource
    private ITaskDao taskDao;
    @Resource
    private ITaskCarDao taskCarDao;
    @Resource
    private ICityDao cityDao;
    @Resource
    private IVehicleRunningDao vehicleRunningDao;
    @Resource
    private IVehicleDao vehicleDao;
    @Resource
    private IStoreDao storeDao;
    @Resource
    private IStoreCityConDao storeCityConDao;
    @Resource
    private ICsWaybillService csWaybillService;

    /**
     * 提送车调度
     *
     * @param paramsDto 参数
     */
    @Override
    public ResultVo localDispatch(LocalDispatchListWaybillDto paramsDto) {
        return csWaybillService.localDisoatch(paramsDto);
    }

    /**
     * 干线调度
     *
     * @param paramsDto 参数
     * @author JPG
     * @since 2019/10/17 9:16
     */
    @Override
    public ResultVo trunkDispatch(TrunkDispatchListShellWaybillDto paramsDto) {
        return csWaybillService.trunkDispatch(paramsDto);
    }


    @Override
    public ResultVo cancelDispatch(CancelDispatchDto paramsDto) {
        return csWaybillService.cancelDispatch(paramsDto);
    }


    @Override
    public ResultVo<List<HistoryListWaybillVo>> historyList(HistoryListWaybillDto paramsDto) {
        List<HistoryListWaybillVo> list = waybillDao.findHistoryList(paramsDto);
        return BaseResultUtil.success(list);
    }

    @Override
    public ResultVo<List<CysWaybillVo>> cysList(CysWaybillDto reqDto) {

        return null;
    }

    @Override
    public ResultVo<PageVo<LocalListWaybillCarVo>> Locallist(LocalListWaybillCarDto paramsDto) {
        PageHelper.startPage(paramsDto.getCurrentPage(), paramsDto.getPageSize(), true);
        List<LocalListWaybillCarVo> list = waybillCarDao.findListLocal(paramsDto);
        PageInfo<LocalListWaybillCarVo> pageInfo = new PageInfo<>(list);
        if(paramsDto.getCurrentPage() > pageInfo.getPages()){
            pageInfo.setList(null);
        }
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<PageVo<TrunkListWaybillVo>> trunklist(TrunkListWaybillDto paramsDto) {

        PageHelper.startPage(paramsDto.getCurrentPage(), paramsDto.getPageSize(), true);
        List<TrunkListWaybillVo> list = waybillDao.findListTrunk(paramsDto);
        PageInfo<TrunkListWaybillVo> pageInfo = new PageInfo<>(list);
        if(paramsDto.getCurrentPage() > pageInfo.getPages()){
            pageInfo.setList(null);
        }
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<PageVo<TrunkListWaybillCarVo>> trunkCarlist(TrunkListWaybillCarDto paramsDto) {
        PageHelper.startPage(paramsDto.getCurrentPage(), paramsDto.getPageSize(), true);
        List<TrunkListWaybillCarVo> list = waybillCarDao.findListTrunk(paramsDto);
        PageInfo<TrunkListWaybillCarVo> pageInfo = new PageInfo<>(list);
        if(paramsDto.getCurrentPage() > pageInfo.getPages()){
            pageInfo.setList(null);
        }
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<GetWaybillVo> get(Long id) {

        Waybill waybill = waybillDao.selectById(id);
        List<WaybillCar> listCar = waybillCarDao.findListByWaybillId(id);
        return null;
    }

    @Override
    public ResultVo<List<GetWaybillCarVo> > getCarByType(Long orderCarId, Integer waybillType) {
       List<GetWaybillCarVo> list =waybillCarDao.findVoByType(orderCarId, waybillType);

        return BaseResultUtil.success(list);
    }

}
