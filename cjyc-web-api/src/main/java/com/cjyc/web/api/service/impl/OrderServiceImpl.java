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
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ListVo;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.city.FullCityVo;
import com.cjyc.common.model.vo.web.order.*;
import com.cjyc.web.api.exception.ParameterException;
import com.cjyc.web.api.exception.ServerException;
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
@Transactional(rollbackFor = ServerException.class)
public class OrderServiceImpl extends ServiceImpl<IOrderDao, Order> implements IOrderService {

    @Resource
    private IOrderDao orderDao;
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
        //获取参数
        Long orderId = paramsDto.getOrderId();

        Order order = null;
        boolean newOrderFlag = false;
        if (orderId != null) {
            //更新订单
            order = orderDao.selectById(orderId);
        }
        if (order == null) {
            //新建订单
            newOrderFlag = true;
            order = new Order();

        }
        BeanUtils.copyProperties(paramsDto, order);
        //查询三级城市
        FullCityVo startFullCityVo = cityDao.findFullCityVo(paramsDto.getStartAreaCode());
        copyOrderStartCity(startFullCityVo, order);
        FullCityVo endFullCityVo = cityDao.findFullCityVo(paramsDto.getEndAreaCode());
        copyOrderEndCity(startFullCityVo, order);

        /**1、组装订单数据
         */
        if (order.getNo() == null) {
            order.setNo(sendNoService.getNo(SendNoTypeEnum.ORDER));
        }
        order.setState(OrderStateEnum.WAIT_SUBMIT.code);
        order.setSource(order.getSource() == null ? paramsDto.getClientId() : order.getSource());
        order.setCreateTime(System.currentTimeMillis());

        //更新或插入订单
        int row = newOrderFlag ? orderDao.insert(order) : orderDao.updateById(order);

        /**2、更新或保存车辆信息*/
        List<SaveOrderCarDto> carDtoList = paramsDto.getOrderCarList();
        if (carDtoList == null || carDtoList.isEmpty()) {
            //没有车辆，结束
            return BaseResultUtil.success();
        }

        //费用统计变量
        //删除旧的车辆数据
        if (!newOrderFlag) {
            orderCarDao.deleteBatchByOrderId(order.getId());
        }
        int noCount = 1;
        for (SaveOrderCarDto dto : carDtoList) {
            if (dto == null) {
                continue;
            }

            OrderCar orderCar = new OrderCar();
            //复制数据
            BeanUtils.copyProperties(dto, orderCar);
            //填充数据
            orderCar.setOrderNo(order.getNo());
            orderCar.setOrderId(order.getId());
            orderCar.setNo(order.getNo() + "-" + noCount);
            orderCar.setState(OrderCarStateEnum.WAIT_ROUTE.code);
            orderCarDao.insert(orderCar);
            //统计数量
            noCount++;
        }
        return BaseResultUtil.success();
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
        //处理参数
        //获取参数
        Long orderId = paramsDto.getOrderId();

        Order order = null;
        boolean newOrderFlag = false;
        if (orderId != null) {
            //更新订单
            order = orderDao.selectById(orderId);
        }
        if (order == null) {
            //新建订单
            newOrderFlag = true;
            order = new Order();
        }
        BeanUtils.copyProperties(paramsDto, order);
        //查询三级城市
        FullCityVo startFullCityVo = cityDao.findFullCityVo(paramsDto.getStartAreaCode());
        copyOrderStartCity(startFullCityVo, order);
        FullCityVo endFullCityVo = cityDao.findFullCityVo(paramsDto.getEndAreaCode());
        copyOrderEndCity(startFullCityVo, order);

        //验证用户
        Customer customer = new Customer();
        if (paramsDto.getCustomerId() != null) {
            customer = customerService.selectById(paramsDto.getCustomerId());
            if(customer == null){
                customer = customerService.selectByPhone(paramsDto.getCustomerPhone());
            }
            if(customer != null && customer.getName().equals(paramsDto.getCustomerName())){
                return BaseResultUtil.fail(ResultEnum.CREATE_NEW_CUSTOMER.getCode(),
                        "客户手机号存在，名称不一致：新名称（{0}）旧名称（{1}），请返回订单重新选择客户",
                        paramsDto.getCustomerName(),customer.getName());
            }
        }
        if (customer == null) {
            customer = new Customer();
            if (paramsDto.getCustomerType() == CustomerTypeEnum.INDIVIDUAL.code) {
                if (paramsDto.getCreateCustomerFlag()) {
                    customer.setName(paramsDto.getCustomerName());
                    customer.setContactMan(paramsDto.getCustomerName());
                    customer.setContactPhone(paramsDto.getCustomerPhone());
                    customer.setType(CustomerTypeEnum.INDIVIDUAL.code);
                    //customer.setInitial()
                    customer.setState(1);
                    customer.setPayMode(PayModeEnum.COLLECT.code);
                    customer.setCreateTime(System.currentTimeMillis());
                    customer.setCreateUserId(paramsDto.getUserId());
                    //添加
                    customerService.save(customer);
                    //订单中添加客户ID
                    order.setCustomerId(customer.getId());
                } else {
                    return BaseResultUtil.getVo(ResultEnum.CREATE_NEW_CUSTOMER.getCode(), ResultEnum.CREATE_NEW_CUSTOMER.getMsg());
                }
            } else {
                return BaseResultUtil.fail("企业客户/合伙人不存在");
            }
        }
        /**1、组装订单数据
         *
         */
        if (order.getNo() == null) {
            order.setNo(sendNoService.getNo(SendNoTypeEnum.ORDER));
        }
        order.setState(OrderStateEnum.SUBMITTED.code);
        order.setSource(order.getSource() == null ? paramsDto.getClientId() : order.getSource());
        order.setCreateTime(System.currentTimeMillis());

        //更新或插入订单
        int row = newOrderFlag ? orderDao.insert(order) : orderDao.updateById(order);
        if (row <= 0) {
            return BaseResultUtil.fail("订单未修改，提交失败");
        }

        /**2、更新或保存车辆信息*/
        List<CommitOrderCarDto> carDtoList = paramsDto.getOrderCarList();
        if (carDtoList == null || carDtoList.isEmpty()) {
            throw new ParameterException("订单车辆不能为空");
        }
        //删除旧的车辆数据
        if (!newOrderFlag) {
            orderCarDao.deleteBatchByOrderId(order.getId());
        }
        //费用统计变量
        int noCount = 0;
        for (CommitOrderCarDto dto : carDtoList) {
            if (dto == null) {
                continue;
            }
            //统计数量
            noCount++;
            OrderCar orderCar = new OrderCar();
            //复制数据
            BeanUtils.copyProperties(dto, orderCar);
            //填充数据
            orderCar.setOrderNo(order.getNo());
            orderCar.setOrderId(order.getId());
            orderCar.setNo(order.getNo() + "-" + noCount);
            orderCar.setState(OrderCarStateEnum.WAIT_ROUTE.code);
            orderCar.setPickFee(dto.getPickFee() == null ? BigDecimal.ZERO : dto.getPickFee());
            orderCar.setTrunkFee(dto.getTrunkFee() == null ? BigDecimal.ZERO : dto.getTrunkFee());
            orderCar.setBackFee(dto.getBackFee() == null ? BigDecimal.ZERO : dto.getBackFee());
            orderCar.setAddInsuranceFee(dto.getAddInsuranceFee() == null ? BigDecimal.ZERO : dto.getAddInsuranceFee());
            orderCarDao.insert(orderCar);
        }
        order.setCarNum(noCount);
        if (noCount == 0) {
            throw new ParameterException("订单至少包含一辆车");
        }
        orderDao.updateById(order);
        return BaseResultUtil.success();
    }

    private void copyOrderStartCity(FullCityVo vo, Order order) {
        if(vo != null){
            return;
        }
        order.setStartProvince(vo.getProvince());
        order.setStartProvinceCode(vo.getProvinceCode());
        order.setStartCity(vo.getCity());
        order.setStartCityCode(vo.getCityCode());
        order.setStartArea(vo.getArea());
        order.setStartAreaCode(vo.getAreaCode());
    }
    private void copyOrderEndCity(FullCityVo vo, Order order) {
        if(vo != null){
            return;
        }
        order.setEndProvince(vo.getProvince());
        order.setEndProvinceCode(vo.getProvinceCode());
        order.setEndCity(vo.getCity());
        order.setEndCityCode(vo.getCityCode());
        order.setEndArea(vo.getArea());
        order.setEndAreaCode(vo.getAreaCode());
    }

    @Override
    public ResultVo check(CheckOrderDto reqDto) {
        Order order = orderDao.selectById(reqDto.getOrderId());
        if (order == null) {
            return BaseResultUtil.fail("[订单]-不存在");
        }
        if (order.getState() <= OrderStateEnum.WAIT_SUBMIT.code) {
            return BaseResultUtil.fail("[订单]-未提交，无法审核");
        }
        if (order.getState() >= OrderStateEnum.CHECKED.code) {
            return BaseResultUtil.fail("[订单]-已经审核过，无法审核");
        }
        //验证必要信息是否完全
        validateOrderFeild(order);

        List<OrderCar> orderCarList = orderCarDao.findByOrderId(order.getId());
        if (orderCarList == null || list().isEmpty()) {
            return BaseResultUtil.fail("[订单车辆]-为空");
        }
        //验证物流券费用
        /*BigDecimal wlTotalFee = orderCarDao.getWLTotalFee(reqDto.getOrderId());
        BigDecimal couponAmount = BigDecimal.ZERO;
        if (order.getCouponSendId() != null) {
            couponAmount = couponSendService.getAmountById(order.getCouponSendId(), wlTotalFee);
        }
        order.setCouponOffsetFee(couponAmount);*/

        //均摊优惠券费用
        BigDecimal totalCouponOffsetFee = order.getCouponOffsetFee() == null ? BigDecimal.ZERO : order.getCouponOffsetFee();
        if (totalCouponOffsetFee.compareTo(BigDecimal.ZERO) > 0) {
            shareCouponOffsetFee(order, orderCarList);
        }

        //均摊总费用
        BigDecimal totalFee = order.getTotalFee() == null ? BigDecimal.ZERO : order.getTotalFee();
        if (totalFee.compareTo(BigDecimal.ZERO) > 0) {
            shareTotalFee(order, orderCarList);
        }
        for (OrderCar orderCar : orderCarList) {
            orderCarDao.updateById(orderCar);
        }

        //根据到付和预付置不同状态
        if(order.getPayType() != PayModeEnum.PREPAID.code){
            order.setState(OrderStateEnum.WAIT_PREPAY.code);
        }else{
            order.setState(OrderStateEnum.CHECKED.code);
        }
        orderDao.updateById(order);

        //TODO 处理优惠券为使用状态，优惠券有且仅能验证一次，修改时怎么保证
        //TODO 路由轨迹

        return BaseResultUtil.success();
    }

    /**
     * 验证订单属性
     *
     * @param order
     * @author JPG
     * @since 2019/10/29 9:16
     */
    private void validateOrderFeild(Order order) {
        if (order.getId() == null || order.getNo() == null) {
            throw new ParameterException("[订单]-订单编号不能为空");
        }
        if (order.getCustomerId() == null) {
            throw new ParameterException("[订单]-客户不存在");
        }
        if (order.getStartProvinceCode() == null
                || order.getStartCityCode() == null
                || order.getStartAreaCode() == null
                || order.getStartAddress() == null
                || order.getEndProvinceCode() == null
                || order.getEndCityCode() == null
                || order.getEndAreaCode() == null
                || order.getEndAddress() == null) {
            throw new ParameterException("[订单]-地址不完整");
        }
        if (order.getCarNum() == null || order.getCarNum() <= 0) {
            throw new ParameterException("[订单]-车辆数不能小于一辆");
        }
        if (order.getPickType() == null
                || order.getPickContactPhone() == null) {
            throw new ParameterException("[订单]-提车联系人不能为空");
        }
        if (order.getBackType() == null
                || order.getBackContactPhone() == null) {
            throw new ParameterException("收车联系人不能为空");
        }

    }

    /**
     * 均摊服务费
     *
     * @param order
     * @param orderCarSavelist
     * @author JPG
     * @since 2019/10/29 8:30
     */
    private void shareTotalFee(Order order, List<OrderCar> orderCarSavelist) {
        BigDecimal totalFee = order.getTotalFee() == null ? BigDecimal.ZERO : order.getTotalFee();
        BigDecimal[] totalFeeArray = totalFee.divideAndRemainder(new BigDecimal(order.getCarNum()));
        BigDecimal totalFeeAvg = totalFeeArray[0];
        BigDecimal totalFeeRemainder = totalFeeArray[1];
        for (OrderCar orderCar : orderCarSavelist) {
            //合伙人计算均摊服务费
            if (totalFeeRemainder.compareTo(BigDecimal.ZERO) > 0) {
                orderCar.setTotalFee(totalFeeAvg.add(BigDecimal.ONE));
                totalFeeRemainder = totalFeeRemainder.subtract(BigDecimal.ONE);
            } else {
                orderCar.setTotalFee(totalFeeAvg);
            }
        }
    }

    /**
     * 均摊优惠券
     *
     * @param order
     * @param orderCarSavelist
     * @author JPG
     * @since 2019/10/29 8:27
     */
    private void shareCouponOffsetFee(Order order, List<OrderCar> orderCarSavelist) {
        BigDecimal couponOffsetFee = order.getCouponOffsetFee() == null ? BigDecimal.ZERO : order.getCouponOffsetFee();
        BigDecimal[] couponOffsetFeeArray = couponOffsetFee.divideAndRemainder(new BigDecimal(order.getCarNum()));
        BigDecimal couponOffsetFeeAvg = couponOffsetFeeArray[0];
        BigDecimal couponOffsetFeeRemainder = couponOffsetFeeArray[1];
        for (OrderCar orderCar : orderCarSavelist) {
            if (couponOffsetFeeRemainder.compareTo(BigDecimal.ZERO) > 0) {
                orderCar.setCouponOffsetFee(couponOffsetFeeAvg.add(BigDecimal.ONE));
                couponOffsetFeeRemainder = couponOffsetFeeRemainder.subtract(BigDecimal.ONE);
            } else {
                orderCar.setCouponOffsetFee(couponOffsetFeeAvg);
            }
        }
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
        PageHelper.offsetPage(paramsDto.getCurrentPage(), paramsDto.getPageSize(), true);
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
        Order order = orderDao.selectById(paramsDto.getUserId());
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
