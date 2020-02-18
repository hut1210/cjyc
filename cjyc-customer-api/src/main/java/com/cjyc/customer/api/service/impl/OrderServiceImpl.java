package com.cjyc.customer.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjkj.common.redis.lock.RedisDistributedLock;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.customer.invoice.InvoiceApplyQueryDto;
import com.cjyc.common.model.dto.customer.order.OrderDetailDto;
import com.cjyc.common.model.dto.customer.order.OrderQueryDto;
import com.cjyc.common.model.dto.customer.order.SimpleSaveOrderDto;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.customer.CustomerTypeEnum;
import com.cjyc.common.model.enums.order.OrderCarStateEnum;
import com.cjyc.common.model.enums.order.OrderPickTypeEnum;
import com.cjyc.common.model.enums.order.OrderStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.TimeStampUtil;
import com.cjyc.common.model.vo.ListVo;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.invoice.InvoiceOrderVo;
import com.cjyc.common.model.vo.customer.order.*;
import com.cjyc.common.system.config.LogoImgProperty;
import com.cjyc.common.system.service.ICsLineService;
import com.cjyc.common.system.service.ICsOrderService;
import com.cjyc.common.system.service.ICsSendNoService;
import com.cjyc.common.system.util.RedisUtils;
import com.cjyc.customer.api.service.IOrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @auther litan
 * @description: com.cjyc.customer.api.system.impl
 * @date:2019/10/8
 */
@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<IOrderDao,Order> implements IOrderService{
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private RedisDistributedLock redisLock;
    @Resource
    private IOrderDao orderDao;
    @Resource
    private ICsOrderService comOrderService;
    @Resource
    private IOrderCarDao orderCarDao;
    @Resource
    private ICouponSendDao couponSendDao;
    @Resource
    private IWaybillCarDao waybillCarDao;
    @Resource
    private ICarSeriesDao carSeriesDao;
    @Resource
    private ICsLineService csLineService;
    @Resource
    private IOrderCarLogDao orderCarLogDao;


    @Override
    public ResultVo simpleSubmit(SimpleSaveOrderDto paramsDto) {
        Order order = orderDao.selectById(paramsDto.getOrderId());
        if(order == null || order.getId() == null){
            return BaseResultUtil.fail("订单不存在");
        }
        if(order.getState() > OrderStateEnum.WAIT_SUBMIT.code){
            return BaseResultUtil.fail("订单已经提交过");
        }
        if(order.getLineId() == null){
            Line line = csLineService.getLineByCity(order.getStartCityCode(), order.getEndCityCode(), true);
            if(line == null){
                return BaseResultUtil.fail("线路不存在，请重新选择城市");
            }
            order.setLineId(line.getId());
        }
        order.setState(OrderStateEnum.SUBMITTED.code);
        orderDao.updateById(order);
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo<OutterLogVo> ListOrderCarLog(String orderCarNo) {
        OutterLogVo outterLogVo = new OutterLogVo();
        String state = orderCarDao.findOutterState(orderCarNo);
        outterLogVo.setOutterState(state);
        List<OutterOrderCarLogVo> list = orderCarLogDao.findCarLogByOrderNoAndCarNo(orderCarNo.split("-")[0], orderCarNo);
        outterLogVo.setList(list);
        return BaseResultUtil.success(outterLogVo);
    }

    @Override
    public ResultVo<PageVo<OrderCenterVo>> getPage(OrderQueryDto dto) {
        if (dto.getEndDate() != null && dto.getEndDate() != 0) {
            dto.setEndDate(TimeStampUtil.convertEndTime(dto.getEndDate()));
        }
        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        List<OrderCenterVo> list = orderDao.selectPage(dto);
        PageInfo<OrderCenterVo> pageInfo = new PageInfo<>(list);
        Map<String, Object> orderCount = getOrderCount(dto.getLoginId());
        return BaseResultUtil.success(pageInfo,orderCount);
    }

    public Map<String, Object> getOrderCount(Long loginId) {
        Map<String,Object> map = new HashMap<>(4);
        map.put("waitConfirmCount",0);
        map.put("transitCount",0);
        map.put("payCount",0);
        map.put("allCount",0);
        // 查询待确认订单数量
        LambdaQueryWrapper<Order> queryWrapper = new QueryWrapper<Order>().lambda()
                .eq(Order::getCustomerId,loginId)
                .between(Order::getState, OrderStateEnum.WAIT_SUBMIT.code,OrderStateEnum.CHECKED.code);
        Integer waitConfirmCount = orderDao.selectCount(queryWrapper);
        if (!Objects.isNull(waitConfirmCount)) {
            map.put("waitConfirmCount",waitConfirmCount >= 99 ? "99+" : waitConfirmCount);
        }

        // 查询运输中订单数量
        queryWrapper = new QueryWrapper<Order>().lambda()
                .eq(Order::getCustomerId,loginId)
                .gt(Order::getState,OrderStateEnum.CHECKED.code)
                .lt(Order::getState,OrderStateEnum.FINISHED.code);
        Integer transitCount = orderDao.selectCount(queryWrapper);
        if (!Objects.isNull(transitCount)) {
            map.put("transitCount",transitCount >= 99 ? "99+" : transitCount);
        }

        // 查询已交付订单数量
        queryWrapper = new QueryWrapper<Order>().lambda()
                .eq(Order::getCustomerId,loginId)
                .eq(Order::getState,OrderStateEnum.FINISHED.code);
        Integer payCount = orderDao.selectCount(queryWrapper);
        if (!Objects.isNull(payCount)) {
            map.put("payCount",payCount >= 99 ? "99+" : payCount);
        }

        // 查询所有订单数量
        queryWrapper = new QueryWrapper<Order>().lambda()
                .eq(Order::getCustomerId,loginId);
        Integer allCount = orderDao.selectCount(queryWrapper);
        if (!Objects.isNull(allCount)) {
            map.put("allCount",allCount >= 99 ? "99+" : allCount);
        }
        return map;
    }

    @Override
    public ResultVo<OrderCenterDetailVo> getDetail(OrderDetailDto dto) {
        OrderCenterDetailVo detailVo = new OrderCenterDetailVo();
        // 查询订单信息
        LambdaQueryWrapper<Order> queryOrderWrapper = new QueryWrapper<Order>().lambda()
                .eq(Order::getCustomerId,dto.getLoginId()).eq(Order::getNo,dto.getOrderNo());
        Order order = super.getOne(queryOrderWrapper);
        if(order == null) {
            return BaseResultUtil.fail("订单号不存在,请检查");
        }
        // 填充返回数据
        fillData(detailVo, order);

        // 计算费用
        fillFee(detailVo, order);

        // 查询车辆信息
        getOrderCar(dto, detailVo);

        // 查询优惠券信息
        CouponSend couponSend = couponSendDao.selectById(detailVo.getCouponSendId());
        detailVo.setCouponName(couponSend == null ? "" : couponSend.getCouponName());

        // 查询出发地，目的地业务中心详细地址
        /*if (order.getStartStoreId() != null) {
            Store startStore = storeDao.selectById(order.getStartStoreId());
            detailVo.setStartStoreNameDetail(startStore == null ? "" : startStore.getDetailAddr());
        }
        if (order.getEndStoreId() != null) {
            Store endStore = storeDao.selectById(order.getEndStoreId());
            detailVo.setEndStoreNameDetail(endStore == null ? "" : endStore.getDetailAddr());
        }*/

        // 自提自送订单设置业务中心地址
        /*if (OrderPickTypeEnum.SELF.code == order.getPickType()) {
            detailVo.setStartAddress(detailVo.getStartStoreNameDetail());
        }
        if (OrderPickTypeEnum.SELF.code == order.getBackType()) {
            detailVo.setEndAddress(detailVo.getEndStoreNameDetail());
        }*/

        return BaseResultUtil.success(detailVo);
    }

    private void fillFee(OrderCenterDetailVo detailVo, Order order) {
        List<OrderCar> orderCarList = orderCarDao.selectList(new QueryWrapper<OrderCar>().lambda().eq(OrderCar::getOrderId, order.getId()));
        // 计算待确认订单的代驾提车费，拖车送车费，物流费，保险费
        BigDecimal pickFee = new BigDecimal(0);
        BigDecimal backFee = new BigDecimal(0);
        BigDecimal trunkFee = new BigDecimal(0);
        BigDecimal addInsuranceFee = new BigDecimal(0);
        for (OrderCar orderCar : orderCarList) {
            pickFee = pickFee.add(orderCar.getPickFee());
            backFee = backFee.add(orderCar.getBackFee());
            trunkFee = trunkFee.add(orderCar.getTrunkFee());
            addInsuranceFee = addInsuranceFee.add(orderCar.getAddInsuranceFee());
        }
        detailVo.setPickFee(pickFee);
        detailVo.setBackFee(backFee);
        detailVo.setTrunkFee(trunkFee);
        detailVo.setAddInsuranceFee(addInsuranceFee);


        // 计算合伙人物流费 与 车辆代收中介费
        if (CustomerTypeEnum.COOPERATOR.code == order.getCustomerType()) {
            BigDecimal agencyFees  = new BigDecimal(0);
            BigDecimal wlTotalFees = new BigDecimal(0);
            for (OrderCar orderCar : orderCarList) {
                // 代收中介费
                BigDecimal agencyFee = orderCar.getTotalFee().subtract(orderCar.getTrunkFee()).subtract(orderCar.getPickFee())
                        .subtract(orderCar.getBackFee()).subtract(orderCar.getAddInsuranceFee());
                agencyFees = agencyFees.add(agencyFee);
                // 物流费
                BigDecimal wlTotalFee = orderCar.getTrunkFee().add(orderCar.getPickFee())
                        .add(orderCar.getBackFee()).add(orderCar.getAddInsuranceFee());
                wlTotalFees = wlTotalFees.add(wlTotalFee);
            }
            detailVo.setWlTotalFee(wlTotalFees);
            detailVo.setAgencyFee(agencyFees);
        }
    }

    private void fillData(OrderCenterDetailVo detailVo, Order order) {
        BeanUtils.copyProperties(order,detailVo);
        StringBuilder start = new StringBuilder();
        start.append(order.getStartProvince() == null ? "" : order.getStartProvince()+" ");
        start.append(order.getStartCity() == null ? "" : order.getStartCity()+" ");
        start.append(order.getStartArea() == null ? "" : order.getStartArea());
        detailVo.setStartProvinceCityAreaName(start.toString().trim());

        StringBuilder end = new StringBuilder();
        end.append(order.getEndProvince() == null ? "" : order.getEndProvince()+" ");
        end.append(order.getEndCity() == null ? "" : order.getEndCity()+" ");
        end.append(order.getEndArea() == null ? "" : order.getEndArea());
        detailVo.setEndProvinceCityAreaName(end.toString().trim());
    }

    private void getOrderCar(OrderDetailDto dto, OrderCenterDetailVo detailVo) {
        LambdaQueryWrapper<OrderCar> queryCarWrapper = new QueryWrapper<OrderCar>().lambda().eq(OrderCar::getOrderNo,dto.getOrderNo());
        List<OrderCar> orderCarList = orderCarDao.selectList(queryCarWrapper);
        List<OrderCarCenterVo> orderCarCenterVoList = new ArrayList<>(10);
        List<OrderCarCenterVo> orderCarFinishPayList = new ArrayList<>(10);
        if (!CollectionUtils.isEmpty(orderCarList)) {
            for (OrderCar orderCar : orderCarList) {
                OrderCarCenterVo orderCarCenter = new OrderCarCenterVo();
                BeanUtils.copyProperties(orderCar,orderCarCenter);
                if (OrderCarStateEnum.SIGNED.code == orderCar.getState()) {
                    // 已交付订单车辆信息
                    orderCarFinishPayList.add(orderCarCenter);
                } else {
                    // 待确认，已交付，运输中，全部订单车辆信息
                    orderCarCenterVoList.add(orderCarCenter);
                }
                // 查询车辆图片
                this.getCarImg(orderCar, orderCarCenter);
                // 查询品牌logo图片
                List<CarSeries> carSeriesList = carSeriesDao.selectList(new QueryWrapper<CarSeries>().lambda().eq(CarSeries::getModel, orderCar.getModel()));
                if(!CollectionUtils.isEmpty(carSeriesList)) {
                    orderCarCenter.setLogoImg(LogoImgProperty.logoImg+carSeriesList.get(0).getLogoImg());
                }
            }
        }
        detailVo.setOrderCarCenterVoList(orderCarCenterVoList);
        detailVo.setOrderCarFinishPayList(orderCarFinishPayList);
    }

    private void getCarImg(OrderCar orderCar, OrderCarCenterVo orderCarCenter) {
        List<String> photoImgList = new ArrayList<>(20);
        List<WaybillCar> waybillCarList = waybillCarDao.selectList(new QueryWrapper<WaybillCar>().lambda()
                .eq(WaybillCar::getOrderCarId, orderCar.getId()).select(WaybillCar::getLoadPhotoImg,WaybillCar::getUnloadPhotoImg));
        if (!CollectionUtils.isEmpty(waybillCarList)) {
            for (WaybillCar waybillCar : waybillCarList) {
                String loadPhotoImg = waybillCar == null ? "" : waybillCar.getLoadPhotoImg();
                String unloadPhotoImg = waybillCar == null ? "" : waybillCar.getUnloadPhotoImg();
                if (!StringUtils.isEmpty(loadPhotoImg)) {
                    String[] array = loadPhotoImg.split(",");
                    Collections.addAll(photoImgList,array);
                }
                if (!StringUtils.isEmpty(unloadPhotoImg)) {
                    String[] array = unloadPhotoImg.split(",");
                    Collections.addAll(photoImgList,array);
                }
            }
        }
        orderCarCenter.setCarImgList(photoImgList);
    }

    @Override
    public ResultVo getUnInvoicePage(InvoiceApplyQueryDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<InvoiceOrderVo> list = orderCarDao.selectUnInvoiceOrderList(dto.getLoginId());
        PageInfo<InvoiceOrderVo> pageInfo = new PageInfo<>(list);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo getInvoiceApplyOrderPage(InvoiceApplyQueryDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<InvoiceOrderVo> list = orderCarDao.selectInvoiceOrderList(dto);
        PageInfo<InvoiceOrderVo> pageInfo = new PageInfo<>(list);
        return BaseResultUtil.success(pageInfo);
    }


}
