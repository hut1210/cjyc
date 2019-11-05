package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.web.order.*;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.CommonStateEnum;
import com.cjyc.common.model.enums.PayModeEnum;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.enums.SendNoTypeEnum;
import com.cjyc.common.model.enums.customer.CustomerTypeEnum;
import com.cjyc.common.model.enums.order.OrderCarStateEnum;
import com.cjyc.common.model.enums.order.OrderChangeTypeEnum;
import com.cjyc.common.model.enums.order.OrderStateEnum;
import com.cjyc.common.model.exception.ParameterException;
import com.cjyc.common.model.exception.ServerException;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ListVo;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.city.FullCity;
import com.cjyc.common.model.vo.web.order.*;
import com.cjyc.common.system.service.ICsOrderService;
import com.cjyc.web.api.service.ICouponSendService;
import com.cjyc.web.api.service.ICustomerService;
import com.cjyc.web.api.service.IOrderService;
import com.cjyc.web.api.service.ISendNoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private ISendNoService sendNoService;
    @Resource
    private IOrderChangeLogDao orderChangeLogDao;
    @Resource
    private ICustomerService customerService;
    @Resource
    private ICityDao cityDao;
    @Resource
    private IWaybillCarDao waybillCarDao;
    @Resource
    private ICouponSendService couponSendService;
    @Resource
    private IStoreDao storeDao;


    @Override
    public ResultVo save(SaveOrderDto paramsDto) {
        return csOrderService.save(paramsDto, OrderStateEnum.WAIT_SUBMIT);
    }

    @Override
    public ResultVo<List<CarFromToGetVo>> getCarFromTo(CarFromToGetDto reqDto) {
        
        List<CarFromToGetVo> resList = new ArrayList<>();
        Store store = storeDao.selectById(reqDto.getStoreId());
        //业务范围
        List<String> areaBizScope = storeDao.findAreaBizScope(store.getId());

        List<OrderCar> list = orderCarDao.findByIds(reqDto.getOrderCarIdList());
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
            WaybillCar prevWaybillCar = waybillCarDao.findLastPrevByArea(orderCar.getId(), areaBizScope);
            copyStartAddress(prevWaybillCar, carFromToGetVo);
            WaybillCar nextWaybillCar = waybillCarDao.findLastNextByCity(orderCar.getId(), areaBizScope);
            copyEndAddress(nextWaybillCar, carFromToGetVo);

            Store sSto = storeDao.selectById(prevWaybillCar.getEndStoreId());
            carFromToGetVo.setStartStoreAddress(getStoreAddress(sSto));
            Store eSto = storeDao.selectById(nextWaybillCar.getStartStoreId());
            carFromToGetVo.setStartStoreAddress(getStoreAddress(eSto));
            resList.add(carFromToGetVo);

        }

        return BaseResultUtil.success(resList);
    }

    @Override
    public List<ListOrderChangeLogVo> getChangeLogVoById(ListOrderChangeLogDto paramsDto) {
        return orderChangeLogDao.findList(paramsDto);
    }

    @Override
    public List<TransportInfoOrderCarVo> getTransportInfoVoById(Long orderId) {
        List<TransportInfoOrderCarVo> list = orderCarDao.findTransportStateByOrderId(orderId);
        return list;
    }



    private String getStoreAddress(Store sSto) {
        return (sSto.getProvince() == null ? "" : sSto.getProvince()) +
                (sSto.getCity() == null ? "" : sSto.getCity()) +
                (sSto.getAreaCode() == null ? "" : sSto.getAreaCode()) +
                (sSto.getDetailAddr() == null ? "" : sSto.getDetailAddr());
    }

    private void copyStartAddress(WaybillCar prevWaybillCar, CarFromToGetVo carFromToGetVo) {
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
    }
    private void copyEndAddress(WaybillCar nextWaybillCar, CarFromToGetVo carFromToGetVo) {
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
        Order order = orderDao.selectById(paramsDto.getOrderId());
        if (order == null) {
            return BaseResultUtil.fail("[订单]-不存在");
        }
        Integer oldState = order.getState();
        if (oldState <= OrderStateEnum.WAIT_SUBMIT.code) {
            return BaseResultUtil.fail("[订单]-未提交，无法驳回");
        }
        if (oldState > OrderStateEnum.CHECKED.code) {
            return BaseResultUtil.fail("[订单]-已经运输无法驳回");
        }
        orderDao.updateStateById(OrderStateEnum.WAIT_SUBMIT.code, order.getId());
        //记录驳回信息
        //添加操作日志
        OrderChangeLog orderChangeLog = new OrderChangeLog();
        orderChangeLog.setOrderId(order.getId());
        orderChangeLog.setOrderNo(order.getNo());
        orderChangeLog.setName(OrderChangeTypeEnum.REJECT.name);
        orderChangeLog.setType(OrderChangeTypeEnum.REJECT.code);
        orderChangeLog.setOldContent(oldState.toString());
        orderChangeLog.setNewContent(String.valueOf(OrderStateEnum.WAIT_SUBMIT.code));
        orderChangeLog.setReason(paramsDto.getReason());
        orderChangeLog.setState(CommonStateEnum.CHECKED.code);
        orderChangeLog.setCreateTime(System.currentTimeMillis());
        orderChangeLog.setCreateUser(paramsDto.getUserName());
        orderChangeLog.setCreateUserId(paramsDto.getUserId());
        orderChangeLogDao.insert(orderChangeLog);
        //TODO 发送消息给创建人
        return BaseResultUtil.success();
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
        //查询列表
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
    public ResultVo allot(AllotOrderDto paramsDto) {
        Order order = orderDao.selectById(paramsDto.getOrderId());
        if (order == null || order.getState() >= OrderStateEnum.WAIT_RECHECK.code) {
            return BaseResultUtil.fail("订单不允许修改");
        }
        order.setAllotToUserId(paramsDto.getToUserId());
        order.setAllotToUserName(paramsDto.getToUserName());
        orderDao.updateById(order);
        return BaseResultUtil.success();
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

    @Override
    public ResultVo obsolete(CancelOrderDto paramsDto) {
        //作废订单
        Order order = orderDao.selectById(paramsDto.getOrderId());
        if (order == null) {
            return BaseResultUtil.fail("订单不存在");
        }
        if (order.getState() > OrderStateEnum.CHECKED.code) {
            return BaseResultUtil.fail("当前订单状态不允许作废");
        }
        Integer oldState = order.getState();
        order.setState(OrderStateEnum.F_OBSOLETE.code);
        orderDao.updateById(order);

        //添加操作日志
        OrderChangeLog orderChangeLog = new OrderChangeLog();
        orderChangeLog.setOrderId(order.getId());
        orderChangeLog.setOrderNo(order.getNo());
        orderChangeLog.setName(OrderChangeTypeEnum.OBSOLETE.name);
        orderChangeLog.setType(OrderChangeTypeEnum.OBSOLETE.code);
        orderChangeLog.setOldContent(oldState.toString());
        orderChangeLog.setNewContent(order.getState().toString());
        orderChangeLog.setReason(paramsDto.getReason());
        orderChangeLog.setState(CommonStateEnum.CHECKED.code);
        orderChangeLog.setCreateTime(System.currentTimeMillis());
        orderChangeLog.setCreateUser(paramsDto.getUserName());
        orderChangeLog.setCreateUserId(paramsDto.getUserId());
        orderChangeLogDao.insert(orderChangeLog);
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo changePrice(ChangePriceOrderDto paramsDto) {
        //处理参数
        //获取参数
        Long orderId = paramsDto.getOrderId();
        Order order = orderDao.selectById(orderId);

        /**2、更新或保存车辆信息*/
        List<ChangePriceOrderCarDto> orderCarList = paramsDto.getOrderCarList();

        //费用统计变量
        int noCount = 0;
        BigDecimal totalFee = BigDecimal.ZERO;
        for (ChangePriceOrderCarDto dto : orderCarList) {
            if (dto == null) {
                continue;
            }
            //统计数量
            noCount++;
            OrderCar orderCar = orderCarDao.selectById(dto.getId());
            if(orderCar == null){
                throw new ServerException("ID为{}的车辆，不存在", dto.getId());
            }
            //填充数据
            orderCar.setPickFee(dto.getPickFee() == null ? BigDecimal.ZERO : dto.getPickFee());
            orderCar.setTrunkFee(dto.getTrunkFee() == null ? BigDecimal.ZERO : dto.getTrunkFee());
            orderCar.setBackFee(dto.getBackFee() == null ? BigDecimal.ZERO : dto.getBackFee());
            orderCar.setAddInsuranceFee(dto.getAddInsuranceFee() == null ? BigDecimal.ZERO : dto.getAddInsuranceFee());
            orderCar.setAddInsuranceAmount(dto.getAddInsuranceAmount() == null ? 0 : dto.getAddInsuranceAmount());
            orderCarDao.updateById(orderCar);

            totalFee = orderCar.getPickFee()
                            .add(orderCar.getTrunkFee()
                            .add(orderCar.getBackFee())
                            .add(orderCar.getAddInsuranceFee()));
        }


        if(CustomerTypeEnum.COOPERATOR.code == order.getCustomerType()){
            totalFee = paramsDto.getTotalFee();
        }
        order.setTotalFee(totalFee);
        orderDao.updateById(order);
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo replenishInfo(ReplenishOrderDto paramsDto) {
        Order order = orderDao.selectById(paramsDto.getOrderId());
        if (order == null || order.getState() >= OrderStateEnum.TRANSPORTING.code) {
            return BaseResultUtil.fail("订单不允许修改");
        }
        List<ReplenishOrderCarDto> list = paramsDto.getOrderCarList();
        for (ReplenishOrderCarDto dto : list) {
            OrderCar orderCar = orderCarDao.selectById(dto.getId());
            orderCar.setBrand(dto.getBrand());
            orderCar.setModel(dto.getModel());
            orderCar.setPlateNo(dto.getPlateNo());
            orderCar.setVin(dto.getVin());
            orderCarDao.updateById(orderCar);
        }

        return BaseResultUtil.success();
    }

    @Override
    public ResultVo cancel(CancelOrderDto paramsDto) {
        //取消订单
        Order order = orderDao.selectById(paramsDto.getOrderId());
        if (order == null) {
            return BaseResultUtil.fail("订单不存在");
        }
        if (order.getState() >= OrderStateEnum.CHECKED.code) {
            return BaseResultUtil.fail("当前订单状态不允许取消");
        }
        Integer oldState = order.getState();
        order.setState(OrderStateEnum.F_CANCEL.code);
        orderDao.updateById(order);

        //添加操作日志
        OrderChangeLog orderChangeLog = new OrderChangeLog();
        orderChangeLog.setOrderId(order.getId());
        orderChangeLog.setOrderNo(order.getNo());
        orderChangeLog.setName(OrderChangeTypeEnum.CANCEL.name);
        orderChangeLog.setType(OrderChangeTypeEnum.CANCEL.code);
        orderChangeLog.setOldContent(oldState.toString());
        orderChangeLog.setNewContent(order.getState().toString());
        orderChangeLog.setReason(paramsDto.getReason());
        orderChangeLog.setState(CommonStateEnum.CHECKED.code);
        orderChangeLog.setCreateTime(System.currentTimeMillis());
        orderChangeLog.setCreateUser(paramsDto.getUserName());
        orderChangeLog.setCreateUserId(paramsDto.getUserId());
        orderChangeLogDao.insert(orderChangeLog);
        return BaseResultUtil.success();
    }

}
