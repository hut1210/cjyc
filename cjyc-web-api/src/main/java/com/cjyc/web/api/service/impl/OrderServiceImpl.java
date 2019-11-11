package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.web.order.*;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.entity.defined.FullWaybillCar;
import com.cjyc.common.model.entity.defined.EndPointCity;
import com.cjyc.common.model.enums.order.OrderStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ListVo;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.order.*;
import com.cjyc.common.system.service.ICsLineNodeService;
import com.cjyc.common.system.service.ICsOrderService;
import com.cjyc.web.api.service.IOrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 订单表(客户下单) 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-09
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class OrderServiceImpl extends ServiceImpl<IOrderDao, Order> implements IOrderService {

    @Resource
    private IOrderDao orderDao;
    @Resource
    private ICsOrderService csOrderService;
    @Resource
    private IOrderCarDao orderCarDao;
    @Resource
    private IOrderChangeLogDao orderChangeLogDao;
    @Resource
    private IWaybillCarDao waybillCarDao;
    @Resource
    private IStoreDao storeDao;
    @Resource
    private ICsLineNodeService csLineNodeService;


    @Override
    public ResultVo save(SaveOrderDto paramsDto) {
        return csOrderService.save(paramsDto, OrderStateEnum.WAIT_SUBMIT);
    }

    @Override
    public ResultVo commit(CommitOrderDto paramsDto) {
        return csOrderService.commit(paramsDto);
    }

    @Override
    public ResultVo check(CheckOrderDto paramsDto) {
        return csOrderService.check(paramsDto);
    }

    @Override
    public ResultVo reject(RejectOrderDto paramsDto) {
        return csOrderService.reject(paramsDto);
    }

    @Override
    public ResultVo allot(AllotOrderDto paramsDto) {
        return csOrderService.allot(paramsDto);
    }
    @Override
    public ResultVo cancel(CancelOrderDto paramsDto) {
        return csOrderService.cancel(paramsDto);
    }
    @Override
    public ResultVo obsolete(CancelOrderDto paramsDto) {
        return csOrderService.obsolete(paramsDto);
    }

    @Override
    public ResultVo changePrice(ChangePriceOrderDto paramsDto) {
        return csOrderService.changePrice(paramsDto);
    }

    @Override
    public ResultVo replenishInfo(ReplenishOrderDto paramsDto) {
        return csOrderService.replenishInfo(paramsDto);
    }


    @Override
    public ResultVo<DispatchAddCarVo> getCarFromTo(CarFromToGetDto reqDto) {
        DispatchAddCarVo dispatchAddCarVo = new DispatchAddCarVo();
        Store store = storeDao.selectById(reqDto.getStoreId());
        //业务范围
        List<String> areaBizScope = storeDao.findAreaBizScope(store.getId());

        List<OrderCar> list = orderCarDao.findByIds(reqDto.getOrderCarIdList());

        List<CarFromToGetVo> childList = new ArrayList<>();
        if(list == null || list.isEmpty()){
            return BaseResultUtil.success();
        }
        for (OrderCar orderCar : list) {
            if(orderCar == null){
                continue;
            }
            CarFromToGetVo carFromToGetVo = new CarFromToGetVo();
            BeanUtils.copyProperties(orderCar, carFromToGetVo);

            //查询waybillcar最后一条记录
            FullWaybillCar prevWaybillCar = waybillCarDao.findLastPrevByBelongStoreId(orderCar.getId(), store.getId());
            copyStartAddress(prevWaybillCar, carFromToGetVo);
            FullWaybillCar nextWaybillCar = waybillCarDao.findLastNextByBelongStoreId(orderCar.getId(), store.getId());
            copyEndAddress(nextWaybillCar, carFromToGetVo);
            childList.add(carFromToGetVo);

        }
        dispatchAddCarVo.setList(childList);
        Set<String> citySet = new HashSet<>();
        for (CarFromToGetVo carFromToGetVo : childList) {
            citySet.add(carFromToGetVo.getStartCity());
            citySet.add(carFromToGetVo.getEndCity());
        }
        //计算推荐线路
        csLineNodeService.getGuideLine(citySet,store.getCity());

        return BaseResultUtil.success(dispatchAddCarVo);
    }

    @Override
    public List<ListOrderChangeLogVo> getChangeLogVoById(ListOrderChangeLogDto paramsDto) {
        return orderChangeLogDao.findList(paramsDto);
    }

    @Override
    public List<TransportInfoOrderCarVo> getTransportInfoVoById(Long orderId) {
        return orderCarDao.findTransportStateByOrderId(orderId);
    }



    private String getStoreAddress(Store sSto) {
        return (sSto.getProvince() == null ? "" : sSto.getProvince()) +
                (sSto.getCity() == null ? "" : sSto.getCity()) +
                (sSto.getAreaCode() == null ? "" : sSto.getAreaCode()) +
                (sSto.getDetailAddr() == null ? "" : sSto.getDetailAddr());
    }

    private void copyStartAddress(FullWaybillCar prevWaybillCar, CarFromToGetVo carFromToGetVo) {
        if(prevWaybillCar == null){
            return;
        }
        carFromToGetVo.setStartProvince(prevWaybillCar.getEndProvince());
        carFromToGetVo.setStartProvinceCode(prevWaybillCar.getEndProvinceCode());
        carFromToGetVo.setStartCity(prevWaybillCar.getEndCity());
        carFromToGetVo.setStartCityCode(prevWaybillCar.getEndCityCode());
        carFromToGetVo.setStartArea(prevWaybillCar.getEndArea());
        carFromToGetVo.setStartAreaCode(prevWaybillCar.getEndAreaCode());
        carFromToGetVo.setStartAddress(prevWaybillCar.getEndAddress());
        carFromToGetVo.setStartStoreId(prevWaybillCar.getEndStoreId());
        carFromToGetVo.setStartStoreName(prevWaybillCar.getEndStoreName());
        carFromToGetVo.setStartStoreFullAddress(prevWaybillCar.getEndStoreFullAddress());
    }
    private void copyEndAddress(FullWaybillCar nextWaybillCar, CarFromToGetVo carFromToGetVo) {
        if(nextWaybillCar == null){
            return;
        }
        carFromToGetVo.setEndProvince(nextWaybillCar.getStartProvince());
        carFromToGetVo.setEndProvinceCode(nextWaybillCar.getStartProvinceCode());
        carFromToGetVo.setEndCity(nextWaybillCar.getStartCity());
        carFromToGetVo.setEndCityCode(nextWaybillCar.getStartCityCode());
        carFromToGetVo.setEndArea(nextWaybillCar.getStartArea());
        carFromToGetVo.setEndAreaCode(nextWaybillCar.getStartAreaCode());
        carFromToGetVo.setEndAddress(nextWaybillCar.getStartAddress());
        carFromToGetVo.setEndStoreId(nextWaybillCar.getStartStoreId());
        carFromToGetVo.setEndStoreName(nextWaybillCar.getStartStoreName());
        carFromToGetVo.setEndStoreFullAddress(nextWaybillCar.getStartStoreFullAddress());
    }






    @Override
    public ResultVo<ListVo<Map<String, Object>>> waitDispatchCarCountList() {
        List<Map<String, Object>> list = orderCarDao.countListWaitDispatchCar();
        //查询统计
        Map<String, Object> countInfo = null;
        if (list != null && !list.isEmpty()) {
            countInfo = orderCarDao.countTotalWaitDispatchCar();
        }
        return BaseResultUtil.success(list, countInfo);
    }
    /**
     * 按线路统计待调度车辆（统计列表）
     *
     * @author JPG
     * @since 2019/10/16 10:04
     */
    @Override
    public ResultVo<ListVo<Map<String, Object>>> lineWaitDispatchCarCountList(LineWaitDispatchCountListOrderCarDto paramsDto, List<Long> bizScopeStoreIds) {
        //查询统计列表
        List<Map<String, Object>> list = orderCarDao.findlineWaitDispatchCarCountList(paramsDto);
        //查询统计
        Map<String, Object> countInfo = null;
        if (list != null && !list.isEmpty()) {
            countInfo = orderCarDao.countTotalWaitDispatchCarByStartCity(paramsDto);
        }
        return BaseResultUtil.success(list, countInfo);
    }


    @Override
    public ResultVo<PageVo<OrderCarWaitDispatchVo>> waitDispatchCarList(WaitDispatchListOrderCarDto paramsDto, List<Long> bizScope) {
        PageHelper.startPage(paramsDto.getCurrentPage(), paramsDto.getPageSize(), true);
        List<OrderCarWaitDispatchVo> list = orderCarDao.findWaitDispatchCarList(paramsDto, bizScope);
        PageInfo<OrderCarWaitDispatchVo> pageInfo = new PageInfo<>(list);
        if (paramsDto.getCurrentPage() > pageInfo.getPages()) {
            pageInfo.setList(null);
        }
        return BaseResultUtil.success(pageInfo);
    }


    @Override
    public OrderVo getVoById(Long orderId) {
        OrderVo orderVo = orderDao.findVoById(orderId);
        List<OrderCar> list = orderCarDao.findByOrderId(orderId);
        orderVo.setOrderCarList(list);
        return orderVo;
    }

    @Override
    public ResultVo<PageVo<ListOrderVo>> list(ListOrderDto paramsDto) {
        PageHelper.startPage(paramsDto.getCurrentPage(), paramsDto.getPageSize(), true);
        List<ListOrderVo> list = orderDao.findListSelective(paramsDto);
        PageInfo<ListOrderVo> pageInfo = new PageInfo<>(list);
        if (paramsDto.getCurrentPage() > pageInfo.getPages()) {
            pageInfo.setList(null);
        }
        //查询统计
        Map<String, Object> countInfo = orderDao.countForAllTab();

        return BaseResultUtil.success(pageInfo, countInfo);
    }

    @Override
    public ResultVo<PageVo<ListOrderCarVo>> carlist(ListOrderCarDto paramsDto) {
        PageHelper.startPage(paramsDto.getCurrentPage(), paramsDto.getPageSize(), true);
        List<ListOrderCarVo> list = orderCarDao.findListSelective(paramsDto);
        PageInfo<ListOrderCarVo> pageInfo = new PageInfo<>(list);
        if (paramsDto.getCurrentPage() > pageInfo.getPages()) {
            pageInfo.setList(null);
        }

        return BaseResultUtil.success(pageInfo);
    }



}
