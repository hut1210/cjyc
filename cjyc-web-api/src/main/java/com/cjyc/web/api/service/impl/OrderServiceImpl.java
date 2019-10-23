package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.constant.NoConstant;
import com.cjyc.common.model.dao.IOrderCarDao;
import com.cjyc.common.model.dao.IOrderDao;
import com.cjyc.common.model.dao.IStoreDao;
import com.cjyc.common.model.dto.web.order.OrderAllotDto;
import com.cjyc.common.model.dto.web.order.OrderCarLineWaitDispatchCountListDto;
import com.cjyc.common.model.dto.web.order.OrderCarWaitDispatchListDto;
import com.cjyc.common.model.dto.web.order.OrderListDto;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.common.model.enums.SendNoTypeEnum;
import com.cjyc.common.model.enums.order.OrderCarStateEnum;
import com.cjyc.common.model.enums.order.OrderStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ListVo;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.order.OrderCarWaitDispatchVo;
import com.cjyc.common.model.vo.web.order.OrderVo;
import com.cjyc.web.api.dto.OrderCarDto;
import com.cjyc.web.api.dto.OrderCommitDto;
import com.cjyc.web.api.exception.ParameterException;
import com.cjyc.web.api.service.IOrderService;
import com.cjyc.web.api.service.ISendNoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
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
public class OrderServiceImpl extends ServiceImpl<IOrderDao, Order> implements IOrderService {

    @Resource
    private IOrderDao orderDao;
    @Resource
    private IOrderCarDao orderCarDao;
    @Resource
    private ISendNoService sendNoService;

    @Override
    public ResultVo saveAndUpdate(OrderCommitDto paramsDto) {
        //去除null
        paramsDto.setWlTotalFee(paramsDto.getWlTotalFee() == null ? BigDecimal.ZERO : paramsDto.getWlTotalFee());
        paramsDto.setRealTotalFee(paramsDto.getRealTotalFee() == null ? BigDecimal.ZERO : paramsDto.getRealTotalFee());
        paramsDto.setCouponOffsetFee(paramsDto.getCouponOffsetFee() == null ? BigDecimal.ZERO : paramsDto.getCouponOffsetFee());
        //
        Long orderId = paramsDto.getOrderId();

        Order order = null;
        Boolean newOrderFalg = false;
        //更新订单
        if(orderId != null){
            order = orderDao.selectById(orderId);
            BeanUtils.copyProperties(paramsDto, order);
        }
        //新建订单
        if(order == null){
            newOrderFalg = true;
            order = new Order();
            BeanUtils.copyProperties(paramsDto, order);
        }
        //发号
        if(order.getNo() == null && paramsDto.getSaveType() == 2){
            order.setNo(sendNoService.getNo(SendNoTypeEnum.ORDER));
        }

        //组装数据
        order.setState(paramsDto.getState());
        order.setSource(order.getSource() == null ? paramsDto.getClientId() : order.getSource());
        order.setAgencyFee(paramsDto.getWlTotalFee().subtract(paramsDto.getRealTotalFee()));
        BigDecimal subtract = paramsDto.getWlTotalFee().subtract(paramsDto.getRealTotalFee()).subtract(paramsDto.getCouponOffsetFee());
        order.setTotalFee(subtract.compareTo(BigDecimal.ZERO) <=0 ? BigDecimal.ZERO : subtract);
        order.setCreateTime(System.currentTimeMillis());

        //更新或插入订单
        int row = 0;
        if(order.getId() == null){
            row = orderDao.insert(order);
        }else{
            row = orderDao.updateById(order);
        }

        if(row < 1){
            BaseResultUtil.fail("服务器错误");
        }

        //更新或保存车辆信息
        List<OrderCar> orderCarSavelist = new ArrayList<>();
        List<OrderCar> orderCarUpdatelist = new ArrayList<>();
        List<OrderCarDto> carDtoList =  paramsDto.getOrderCarDtoList();

        int noCount = 1;
        BigDecimal totalPickFee = BigDecimal.ZERO;
        BigDecimal totalTrunkFee = BigDecimal.ZERO;
        BigDecimal totalBackFee = BigDecimal.ZERO;
        BigDecimal totalInsuranceFee = BigDecimal.ZERO;
        BigDecimal totalAgencyFee = order.getAgencyFee();
        BigDecimal totalCouponOffsetFee = order.getCouponOffsetFee();
        BigDecimal totalFee = BigDecimal.ZERO;

        //计算均摊费用
        BigDecimal[] bigDecimals1 = totalAgencyFee.divideAndRemainder(new BigDecimal(paramsDto.getCarNum()));
        BigDecimal agencyFee = bigDecimals1[0];
        BigDecimal remainder = bigDecimals1[1];
        BigDecimal[] bigDecimals2 = totalCouponOffsetFee.divideAndRemainder(new BigDecimal(paramsDto.getCarNum()));
        BigDecimal couponOffsetFee = bigDecimals2[0];
        BigDecimal remainder2 = bigDecimals2[1];


        for(OrderCarDto orderCarDto : carDtoList){
            if(orderCarDto == null){
                continue;
            }
            OrderCar orderCar = new OrderCar();
            BeanUtils.copyProperties(orderCarDto,orderCar);

            orderCar.setOrderNo(order.getNo());
            orderCar.setOrderId(order.getId());
            orderCar.setNo(order.getNo() + "_" + noCount);
            if(paramsDto.getSaveType() != 2) {
                orderCar.setState(OrderCarStateEnum.WAIT_ROUTE.code);
            }else{
                orderCar.setState(OrderCarStateEnum.WAIT_PICK_DISPATCH.code);
            }
            orderCar.setPickFee(orderCarDto.getPickFee() == null ? BigDecimal.ZERO : orderCarDto.getPickFee());
            orderCar.setTrunkFee(orderCarDto.getTrunkFee() == null ? BigDecimal.ZERO : orderCarDto.getTrunkFee());
            orderCar.setBackFee(orderCarDto.getBackFee() == null ? BigDecimal.ZERO : orderCarDto.getBackFee());
            orderCar.setInsuranceFee(orderCarDto.getInsuranceFee() == null ? BigDecimal.ZERO : orderCarDto.getInsuranceFee());


            //计算均摊服务费
            if(remainder.compareTo(BigDecimal.ZERO) > 0){
                orderCar.setAgencyFee(agencyFee.add(BigDecimal.ONE));
                remainder = remainder.subtract(BigDecimal.ONE);
            }else{
                orderCar.setAgencyFee(agencyFee);
            }

            //计算均摊物流券抵扣费用
            if(remainder.compareTo(BigDecimal.ZERO) > 0){
                orderCar.setCouponOffsetFee(couponOffsetFee.add(BigDecimal.ONE));
                remainder2 = remainder.subtract(BigDecimal.ONE);
            }else{
                orderCar.setCouponOffsetFee(couponOffsetFee);
            }
            orderCar.setTotalFee(order.getPickFee()
                    .add(order.getTrunkFee())
                    .add(order.getBackFee())
                    .add(order.getInsuranceFee())
                    .subtract(order.getAgencyFee())
                    .subtract(order.getCouponOffsetFee()));

            orderCarSavelist.add(orderCar);

            //计算统计费用
            totalPickFee = totalPickFee.add(order.getPickFee());
            totalTrunkFee = totalTrunkFee.add(orderCar.getPickFee());
            totalBackFee = totalBackFee.add(orderCar.getBackFee());
            totalInsuranceFee = totalInsuranceFee.add(orderCar.getInsuranceFee());
            totalCouponOffsetFee = totalCouponOffsetFee.add(orderCar.getCouponOffsetFee());
            totalFee = totalFee.add(orderCar.getTotalFee());
            //统计数量
            noCount++;

        }
        //验证车数量是否相等
        if(noCount != carDtoList.size()){
            throw new ParameterException("车数量与实际数量不符");
        }

        //验证金额是否相等
        if(totalFee.compareTo(order.getTotalFee()) != 0){
            throw new ParameterException("主单金额与车辆总金额不符");
        }
        //删除旧的车辆数据
        if(!newOrderFalg){
            orderCarDao.deleteBatchByOrderId(order.getId());
        }
        //批量保存车辆
        orderCarDao.saveBatch(orderCarSavelist);

        order.setPickFee(totalPickFee);
        order.setTrunkFee(totalTrunkFee);
        order.setBackFee(totalBackFee);
        order.setInsuranceFee(totalInsuranceFee);
        order.setAgencyFee(totalAgencyFee);

        orderDao.updateById(order);
        return BaseResultUtil.success();
    }


    @Override
    public ResultVo<ListVo<Map<String, Object>>> waitDispatchCarCountList() {
        List<Map<String, Object>> list = orderCarDao.countListWaitDispatchCar();
        //查询统计
        Map<String, Object> countInfo = null;
        if (list != null || !list.isEmpty()) {
            countInfo = orderCarDao.countTotalWaitDispatchCar();
        }
        return BaseResultUtil.success(list, countInfo);
    }

    @Override
    public ResultVo<PageVo<OrderCarWaitDispatchVo>> waitDispatchCarList(OrderCarWaitDispatchListDto paramsDto, List<Long> bizScope) {
        PageHelper.startPage(paramsDto.getCurrentPage(), paramsDto.getPageSize(), true);
        List<OrderCarWaitDispatchVo> list = orderCarDao.findWaitDispatchCarList(paramsDto, bizScope);
        PageInfo<OrderCarWaitDispatchVo> pageInfo = new PageInfo<>(list);
        if (paramsDto.getCurrentPage() > pageInfo.getPages()) {
            pageInfo.setList(null);
        }
        return BaseResultUtil.success(pageInfo);
    }

    /**
     * 按线路统计待调度车辆（统计列表）
     *
     * @author JPG
     * @since 2019/10/16 10:04
     */
    @Override
    public ResultVo<ListVo<Map<String, Object>>> lineWaitDispatchCarCountList(OrderCarLineWaitDispatchCountListDto paramsDto, List<Long> bizScopeStoreIds) {
        //查询列表
        List<Map<String, Object>> list = orderCarDao.findlineWaitDispatchCarCountList(paramsDto, bizScopeStoreIds);

        //统计结果
        return null;
    }

    @Override
    public OrderVo getVoById(Long orderId) {
        OrderVo orderVo = orderDao.findVoById(orderId);
        List<OrderCar> list = orderCarDao.findByOrderId(orderId);
        orderVo.setOrderCarList(list);
        return orderVo;
    }

    @Override
    public ResultVo allot(OrderAllotDto orderAllotDto) {
        Order order = orderDao.selectById(orderAllotDto.getOrderId());
        if(order == null || order.getState() >= OrderStateEnum.WAIT_RECHECK.code){
            BaseResultUtil.fail("订单不允许修改");
        }
        order.setCheckUserId(orderAllotDto.getToUserId());
        order.setCheckUserName(orderAllotDto.getToUserName());
        orderDao.updateById(order);
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo selectList(OrderListDto paramsDto) {
        PageHelper.offsetPage(paramsDto.getCurrentPage(), paramsDto.getPageSize(), true);
        List<Order> list =  orderDao.findListSelective(paramsDto);
        PageInfo<Order> pageInfo = new PageInfo<>(list);
        if(paramsDto.getCurrentPage() > pageInfo.getPages()){
            pageInfo.setList(null);
        }

        return BaseResultUtil.success(pageInfo);
    }

}
