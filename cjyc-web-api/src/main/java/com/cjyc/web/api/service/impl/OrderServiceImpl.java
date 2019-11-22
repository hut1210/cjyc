package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.web.order.*;
import com.cjyc.common.model.entity.Line;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.common.model.entity.Store;
import com.cjyc.common.model.entity.defined.BizScope;
import com.cjyc.common.model.entity.defined.FullCity;
import com.cjyc.common.model.entity.defined.FullWaybillCar;
import com.cjyc.common.model.enums.BizScopeEnum;
import com.cjyc.common.model.enums.city.CityLevelEnum;
import com.cjyc.common.model.enums.order.OrderStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ListVo;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.OrderCarVo;
import com.cjyc.common.model.vo.web.order.*;
import com.cjyc.common.model.vo.web.waybill.WaybillCarVo;
import com.cjyc.common.system.service.*;
import com.cjyc.common.system.service.sys.ICsSysService;
import com.cjyc.web.api.service.IOrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
    @Resource
    private ICsLineService csLineService;
    @Resource
    private ICsCityService csCityService;
    @Resource
    private ICsSysService csSysService;




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
        Store store = storeDao.selectById(2L);
        //业务范围
        List<OrderCarVo> list = orderCarDao.findVoListByIds(reqDto.getOrderCarIdList());
        List<WaybillCarVo> childList = new ArrayList<>();
        if(CollectionUtils.isEmpty(list)){
            return BaseResultUtil.fail("车辆不存在");
        }
        for (OrderCarVo orderCarVo : list) {
            if(orderCarVo == null){
                continue;
            }
            WaybillCarVo waybillCarVo = new WaybillCarVo();
            copyOrderCarToWaybillCar(orderCarVo, waybillCarVo);
            Long expectStartTime = orderCarVo.getExpectStartDate();
          /*  if(expectStartTime == null || expectStartTime < LocalDateTimeUtil.getMillisByLDT()){

            }*/
            waybillCarVo.setExpectStartTime(orderCarVo.getExpectStartDate());
            //查询waybillcar最后一条记录，重置
            FullWaybillCar prevWaybillCar = waybillCarDao.findLastPrevByBelongStoreId(orderCarVo.getId(), store.getId());
            if(prevWaybillCar != null){
                copyPrevInfo(prevWaybillCar, waybillCarVo);
                waybillCarVo.setStartFixedFlag(true);
            }else{
                //查询城市数据
                FullCity fullCity = csCityService.findFullCity(orderCarVo.getStartAreaCode(), CityLevelEnum.PROVINCE);
                if(fullCity != null){
                    copyStartToWaybillCarVo(fullCity, waybillCarVo);
                }
                waybillCarVo.setStartFixedFlag(true);
            }
            FullWaybillCar nextWaybillCar = waybillCarDao.findLastNextByBelongStoreId(orderCarVo.getId(), store.getId());
            //查询
            if(nextWaybillCar != null){
                copyNextInfo(nextWaybillCar, waybillCarVo);
                waybillCarVo.setEndFixedFlag(true);
            }else{
                //查询城市数据
                FullCity fullCity = csCityService.findFullCity(orderCarVo.getEndAreaCode(), CityLevelEnum.PROVINCE);
                if(fullCity != null){
                    copyEndToWaybillCarVo(fullCity, waybillCarVo);
                }
                waybillCarVo.setEndFixedFlag(false);
            }
            //查询线路价卡
            Line line = csLineService.getLineByCity(waybillCarVo.getStartCityCode(), waybillCarVo.getEndCityCode(), true);
            if(line != null){
                waybillCarVo.setLineFreightFee(line.getDefaultFreightFee());
                waybillCarVo.setLineId(line.getId());
                waybillCarVo.setHasLine(true);
            }
            childList.add(waybillCarVo);
        }
        dispatchAddCarVo.setList(childList);
        Set<String> citySet = new HashSet<>();
        for (WaybillCarVo waybillCarVo : childList) {
            citySet.add(waybillCarVo.getStartCity());
            citySet.add(waybillCarVo.getEndCity());
        }
        //计算推荐线路
        List<String> guideLines = csLineNodeService.getGuideLine(citySet, store.getCity());
        dispatchAddCarVo.setGuideLine(guideLines == null ? store.getCity() : guideLines.get(0));
        return BaseResultUtil.success(dispatchAddCarVo);
    }

    private void copyStartToWaybillCarVo(FullCity fullCity, WaybillCarVo waybillCarVo) {
        waybillCarVo.setStartProvince(fullCity.getProvince());
        waybillCarVo.setStartProvinceCode(fullCity.getProvinceCode());
        waybillCarVo.setStartCity(fullCity.getCity());
        waybillCarVo.setStartCityCode(fullCity.getCityCode());
        waybillCarVo.setStartArea(fullCity.getArea());
        waybillCarVo.setStartAreaCode(fullCity.getAreaCode());
    }

    private void copyEndToWaybillCarVo(FullCity fullCity, WaybillCarVo waybillCarVo) {
        waybillCarVo.setEndProvince(fullCity.getProvince());
        waybillCarVo.setEndProvinceCode(fullCity.getProvinceCode());
        waybillCarVo.setEndCity(fullCity.getCity());
        waybillCarVo.setEndCityCode(fullCity.getCityCode());
        waybillCarVo.setEndArea(fullCity.getArea());
        waybillCarVo.setEndAreaCode(fullCity.getAreaCode());
    }

    private void copyOrderCarToWaybillCar(OrderCarVo orderCarVo, WaybillCarVo waybillCarVo) {
        BeanUtils.copyProperties(orderCarVo, waybillCarVo);
        waybillCarVo.setOrderCarId(orderCarVo.getId());
        waybillCarVo.setOrderCarNo(orderCarVo.getNo());
        waybillCarVo.setFreightFee(orderCarVo.getTrunkFee());
        waybillCarVo.setLineId(orderCarVo.getLineId());
        waybillCarVo.setExpectStartTime(orderCarVo.getExpectStartDate());
        waybillCarVo.setLoadLinkName(orderCarVo.getPickContactName());
        waybillCarVo.setLoadLinkPhone(orderCarVo.getPickContactPhone());
        waybillCarVo.setUnloadLinkName(orderCarVo.getBackContactName());
        waybillCarVo.setUnloadLinkPhone(orderCarVo.getBackContactPhone());
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

    private void copyPrevInfo(FullWaybillCar prevWaybillCar, WaybillCarVo carFromToGetVo) {
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
        //carFromToGetVo.setStartStoreFullAddress(prevWaybillCar.getEndStoreFullAddress());
        carFromToGetVo.setLoadLinkName(prevWaybillCar.getUnloadLinkName());
        carFromToGetVo.setLoadLinkPhone(prevWaybillCar.getUnloadLinkPhone());
        carFromToGetVo.setLoadLinkUserId(prevWaybillCar.getUnloadLinkUserId());
    }
    private void copyNextInfo(FullWaybillCar nextWaybillCar, WaybillCarVo carFromToGetVo) {
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
        //carFromToGetVo.setEndStoreFullAddress(nextWaybillCar.getStartStoreFullAddress());
        carFromToGetVo.setUnloadLinkName(nextWaybillCar.getLoadLinkName());
        carFromToGetVo.setUnloadLinkPhone(nextWaybillCar.getLoadLinkPhone());
        carFromToGetVo.setUnloadLinkUserId(nextWaybillCar.getLoadLinkUserId());
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

        //查询角色业务中心范围
        BizScope bizScope = csSysService.getBizScopeByRoleId(paramsDto.getRoleId(), true);
        if(bizScope == null || bizScope.getCode() == BizScopeEnum.NONE.code){
            return BaseResultUtil.fail("没有数据权限");
        }
        paramsDto.setBizScope(bizScope.getCode() == 0 ? null : bizScope.getStoreIds());

        //分页查询
        PageHelper.startPage(paramsDto.getCurrentPage(), paramsDto.getPageSize(), true);
        List<ListOrderVo> list = orderDao.findListSelective(paramsDto);
        PageInfo<ListOrderVo> pageInfo = new PageInfo<>(list);
        if (paramsDto.getCurrentPage() > pageInfo.getPages()) {
            pageInfo.setList(null);
        }
        //查询统计
        Map<String, Object> countInfo = orderDao.countForAllTab(paramsDto.getLoginId(), paramsDto.getBizScope());
        return BaseResultUtil.success(pageInfo, countInfo);
    }

    @Override
    public ResultVo<PageVo<ListOrderCarVo>> carlist(ListOrderCarDto paramsDto) {

        //查询角色业务中心范围
        BizScope bizScope = csSysService.getBizScopeByRoleId(paramsDto.getRoleId(), true);
        if(bizScope == null || bizScope.getCode() == BizScopeEnum.NONE.code){
            return BaseResultUtil.fail("没有数据权限");
        }
        paramsDto.setBizScope(bizScope.getCode() == 0 ? null : bizScope.getStoreIds());
        PageHelper.startPage(paramsDto.getCurrentPage(), paramsDto.getPageSize(), true);
        List<ListOrderCarVo> list = orderCarDao.findListSelective(paramsDto);
        PageInfo<ListOrderCarVo> pageInfo = new PageInfo<>(list);
        if (paramsDto.getCurrentPage() > pageInfo.getPages()) {
            pageInfo.setList(null);
        }

        return BaseResultUtil.success(pageInfo);
    }



}
