package com.cjyc.customer.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.customer.invoice.InvoiceApplyQueryDto;
import com.cjyc.common.model.dto.customer.order.OrderDetailDto;
import com.cjyc.common.model.dto.customer.order.OrderQueryDto;
import com.cjyc.common.model.dto.customer.order.SimpleSaveOrderDto;
import com.cjyc.common.model.dto.web.order.SaveOrderDto;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.order.OrderCarStateEnum;
import com.cjyc.common.model.enums.order.OrderStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.invoice.InvoiceOrderVo;
import com.cjyc.common.model.vo.customer.order.OrderCarCenterVo;
import com.cjyc.common.model.vo.customer.order.OrderCenterDetailVo;
import com.cjyc.common.model.vo.customer.order.OrderCenterVo;
import com.cjyc.common.system.service.ICsOrderService;
import com.cjyc.customer.api.service.IOrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
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
    private IStoreDao storeDao;

    /**
     * 保存订单
     *
     * @param paramsDto
     * @author JPG
     * @since 2019/11/5 8:39
     */
    @Override
    public ResultVo save(SaveOrderDto paramsDto) {
        return comOrderService.save(paramsDto, OrderStateEnum.WAIT_SUBMIT);
    }

    @Override
    public ResultVo submit(SaveOrderDto paramsDto) {
        return comOrderService.save(paramsDto, OrderStateEnum.SUBMITTED);
    }

    @Override
    public ResultVo simpleSubmit(SimpleSaveOrderDto paramsDto) {
        Order order = orderDao.selectById(paramsDto.getOrderId());
        if(order == null || order.getId() == null){
            return BaseResultUtil.fail("订单不存在");
        }
        if(order.getState() > OrderStateEnum.WAIT_SUBMIT.code){
            return BaseResultUtil.fail("订单已经提交过");
        }
        order.setState(OrderStateEnum.SUBMITTED.code);
        orderDao.updateById(order);
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo<PageVo<OrderCenterVo>> getPage(OrderQueryDto dto) {
        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        List<OrderCenterVo> list = orderDao.selectPage(dto);
        PageInfo<OrderCenterVo> pageInfo = new PageInfo<>(list == null ? new ArrayList<>(0) : list);
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
                .between(Order::getState, OrderStateEnum.WAIT_SUBMIT.code,OrderStateEnum.WAIT_RECHECK.code);
        Integer waitConfirmCount = orderDao.selectCount(queryWrapper);
        if (!Objects.isNull(waitConfirmCount)) {
            map.put("waitConfirmCount",waitConfirmCount >= 99 ? "99+" : waitConfirmCount);
        }

        // 查询运输中订单数量
        queryWrapper = new QueryWrapper<Order>().lambda()
                .eq(Order::getCustomerId,loginId)
                .eq(Order::getState,OrderStateEnum.TRANSPORTING.code);
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

        // 查询车辆信息
        getOrderCar(dto, detailVo);

        // 查询优惠券信息
        CouponSend couponSend = couponSendDao.selectById(detailVo.getCouponSendId());
        detailVo.setCouponName(couponSend == null ? "" : couponSend.getCouponName());

        // 查询出发地，目的地业务中心详细地址
        if (order.getStartStoreId() != null) {
            Store startStore = storeDao.selectById(order.getStartStoreId());
            detailVo.setStartStoreNameDetail(startStore == null ? "" : startStore.getDetailAddr());
        }
        if (order.getEndStoreId() != null) {
            Store endStore = storeDao.selectById(order.getEndStoreId());
            detailVo.setEndStoreNameDetail(endStore == null ? "" : endStore.getDetailAddr());
        }

        return BaseResultUtil.success(detailVo);
    }

    private void fillData(OrderCenterDetailVo detailVo, Order order) {
        BeanUtils.copyProperties(order,detailVo);
        StringBuilder start = new StringBuilder();
        start.append(order.getStartProvince() == null ? "" : order.getStartProvince());
        start.append(" ");
        start.append(order.getStartCity() == null ? "" : order.getStartCity());
        start.append(" ");
        start.append(order.getStartArea() == null ? "" : order.getStartArea());
        detailVo.setStartProvinceCityAreaName(start.toString().trim());

        StringBuilder end = new StringBuilder();
        end.append(order.getEndProvince() == null ? "" : order.getEndProvince());
        end.append(" ");
        end.append(order.getEndCity() == null ? "" : order.getEndCity());
        end.append(" ");
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
                    orderCarCenter.setLogoImg(carSeriesList.get(0).getLogoImg());
                }
            }
        }
        detailVo.setOrderCarCenterVoList(orderCarCenterVoList);
        detailVo.setOrderCarFinishPayList(orderCarFinishPayList);
    }

    private void getCarImg(OrderCar orderCar, OrderCarCenterVo orderCarCenter) {
        List<String> photoImgList = new ArrayList<>(10);
        List<WaybillCar> waybillCarList = waybillCarDao.selectList(new QueryWrapper<WaybillCar>().lambda()
                .eq(WaybillCar::getOrderCarId, orderCar.getId()).select(WaybillCar::getLoadPhotoImg,WaybillCar::getUnloadPhotoImg));
        if (!CollectionUtils.isEmpty(waybillCarList)) {
            WaybillCar waybillCar = waybillCarList.get(0);
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
