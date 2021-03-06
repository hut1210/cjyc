package com.cjyc.customer.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.customer.invoice.InvoiceApplyQueryDto;
import com.cjyc.common.model.dto.customer.order.OrderDetailDto;
import com.cjyc.common.model.dto.customer.order.OrderQueryDto;
import com.cjyc.common.model.dto.customer.order.SimpleSaveOrderDto;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.UserTypeEnum;
import com.cjyc.common.model.enums.customer.CustomerTypeEnum;
import com.cjyc.common.model.enums.message.PushMsgEnum;
import com.cjyc.common.model.enums.order.OrderCarStateEnum;
import com.cjyc.common.model.enums.order.OrderStateEnum;
import com.cjyc.common.model.enums.waybill.WaybillCarStateEnum;
import com.cjyc.common.model.exception.ServerException;
import com.cjyc.common.model.util.*;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.invoice.InvoiceOrderVo;
import com.cjyc.common.model.vo.customer.order.OrderCarCenterVo;
import com.cjyc.common.model.vo.customer.order.OrderCenterDetailVo;
import com.cjyc.common.model.vo.customer.order.OrderCenterVo;
import com.cjyc.common.system.config.LogoImgProperty;
import com.cjyc.common.system.service.ICsLineService;
import com.cjyc.common.system.service.ICsOrderService;
import com.cjyc.common.system.service.ICsPushMsgService;
import com.cjyc.customer.api.service.IOrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
/**
 * 订单
 * @author JPG
 */
@Service
@Slf4j
@Transactional(rollbackFor = RuntimeException.class)
public class OrderServiceImpl extends ServiceImpl<IOrderDao,Order> implements IOrderService{
    @Resource
    private IOrderDao orderDao;
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
    private ICsPushMsgService csPushMsgService;
    @Resource
    private ICsOrderService csOrderService;
    @Resource
    private ILineDao lineDao;


    @Override
    public ResultVo simpleSubmit(SimpleSaveOrderDto paramsDto) {
        BigDecimal totalFee = MoneyUtil.yuanToFen(paramsDto.getTotalFee());
        Order order = orderDao.selectById(paramsDto.getOrderId());
        if(order == null || order.getId() == null){
            return BaseResultUtil.fail("订单不存在");
        }
        if(order.getState() > OrderStateEnum.WAIT_SUBMIT.code){
            return BaseResultUtil.fail("订单已经提交过");
        }
        if (!RegexUtil.isMobileSimple(order.getPickContactPhone())) {
            return BaseResultUtil.fail("发车人手机号格式不正确");
        }
        if (!RegexUtil.isMobileSimple(order.getBackContactPhone())) {
            return BaseResultUtil.fail("收车人手机号格式不正确");
        }
        if(order.getLineId() == null || order.getLineId() <= 0){
            Line line = csLineService.getLineByCity(order.getStartCityCode(), order.getEndCityCode(), true);
            if(line == null){
                return BaseResultUtil.fail("线路不存在，请重新选择城市");
            }
            order.setLineId(line.getId());
        }

        fillOrderStoreInfoForSave(order);
        csOrderService.fillOrderInputStore(order);
        order.setState(OrderStateEnum.WAIT_CHECK.code);

        //合伙人订单验证订单金额
        if(CustomerTypeEnum.COOPERATOR.code == order.getCustomerType()){
            if(totalFee == null){
                return BaseResultUtil.fail("合伙人订单请输入支付订单金额");
            }
            List<OrderCar> ocList = orderCarDao.findListByOrderId(order.getId());
            BigDecimal totalWlFee = orderCarDao.sumTotalWlFee(order.getId());
            if(totalFee.compareTo(totalWlFee) < 0){
                return BaseResultUtil.fail("合伙人订单金额不能小于物流费({0}元)", MoneyUtil.fenToYuan(totalWlFee));
            }
            order.setTotalFee(totalFee);

            //均分价格
            csOrderService.shareTotalFee(totalFee, ocList);
            //更新车辆信息
            ocList.forEach(orderCar -> {
                if (orderCar.getTotalFee() == null || orderCar.getTotalFee().compareTo(BigDecimal.ZERO) <= 0) {
                    log.error("【均摊费用】失败{}", JSON.toJSONString(paramsDto));
                    throw new ServerException("订单车辆存在时，订单金额不能为零");
                }
                orderCarDao.updateById(orderCar);
            });

        }
        orderDao.updateById(order);
        //给客户发送消息
        csPushMsgService.send(paramsDto.getLoginId(), UserTypeEnum.CUSTOMER, PushMsgEnum.C_COMMIT_ORDER, order.getNo());
        //TODO 给所属业务中心业务员发送消息

        return BaseResultUtil.success();
    }

    private Order fillOrderStoreInfoForSave(Order order) {
        Long inputStoreId = order.getInputStoreId();
        order.setInputStoreId(inputStoreId == null || inputStoreId == -5 ? null : inputStoreId);
        Long startStoreId = order.getStartStoreId();
        order.setStartStoreId(startStoreId == null || startStoreId == -5 ? null : startStoreId);
        Long endStoreId = order.getEndStoreId();
        order.setEndStoreId(endStoreId == null || endStoreId == -5 ? null : endStoreId);
        return order;
    }

    @Override
    public ResultVo<PageVo<OrderCenterVo>> getPage(OrderQueryDto dto) {
        log.info("====>用户端-查询订单列表,请求json数据 :: "+ JsonUtils.objectToJson(dto));
        if (dto.getEndDate() != null && dto.getEndDate() != 0) {
            dto.setEndDate(TimeStampUtil.convertEndTime(dto.getEndDate()));
        }
        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        List<OrderCenterVo> list = orderDao.selectPage(dto);

        // 刪除车辆信息为空的元素
        //list.removeIf(item -> CollectionUtils.isEmpty(item.getOrderCarCenterVoList()));

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
        log.info("====>用户端-查询订单详情,请求json数据 :: "+JsonUtils.objectToJson(dto));
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

        return BaseResultUtil.success(detailVo);
    }

    private void fillFee(OrderCenterDetailVo detailVo, Order order) {
        // 查询线路费
        Line line1 = lineDao.selectById(order.getLineId());
        detailVo.setLineWlFreightFee(line1.getDefaultWlFee());

        List<OrderCar> orderCarList = orderCarDao.selectList(new QueryWrapper<OrderCar>().lambda().eq(OrderCar::getOrderId, order.getId()));
        // 计算合伙人物流费 与 合伙人服务费
        if (CustomerTypeEnum.COOPERATOR.code == order.getCustomerType()) {
            BigDecimal agencyFees  = new BigDecimal(0);
            BigDecimal wlTotalFees = new BigDecimal(0);
            if (!CollectionUtils.isEmpty(orderCarList)) {
                for (OrderCar orderCar : orderCarList) {
                    // 合伙人物流费：干线费 + 提车费 + 送车费 + 保险费
                    BigDecimal wlTotalFee = orderCar.getTrunkFee().add(orderCar.getPickFee())
                            .add(orderCar.getBackFee()).add(orderCar.getAddInsuranceFee());
                    wlTotalFees = wlTotalFees.add(wlTotalFee);

                    // 合伙人服务费：总费用 - 物流费
                    BigDecimal agencyFee = orderCar.getTotalFee().subtract(wlTotalFee);
                    agencyFees = agencyFees.add(agencyFee);
                }
            } else {
                Line line = lineDao.selectById(order.getLineId());
                // 合伙人下简单 物流费
                BigDecimal trunkWLFee = line == null ? BigDecimal.ZERO : line.getDefaultWlFee().multiply(BigDecimal.valueOf(order.getCarNum()));
                wlTotalFees = wlTotalFees.add(trunkWLFee);
                // 合伙人下简单 服务费
                agencyFees = agencyFees.add(order.getTotalFee().subtract(trunkWLFee));
            }
            detailVo.setTrunkFee(wlTotalFees);
            detailVo.setWlTotalFee(wlTotalFees);
            detailVo.setAgencyFee(agencyFees);
            return;
        }

        // 计算提车费，拖车送车费，物流费，保险费
        if (!CollectionUtils.isEmpty(orderCarList)) {
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
        } else {
            detailVo.setTrunkFee(order.getTotalFee());
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

        String startAddress = order.getStartAddress() == null ? "" : order.getStartAddress();
        String endAddress = order.getEndAddress() == null ? "" : order.getEndAddress();
        detailVo.setStartAddress(order.getStartCity()+order.getStartArea()+startAddress);
        detailVo.setEndAddress(order.getEndCity()+order.getEndArea()+endAddress);
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
                List<CarSeries> carSeriesList = carSeriesDao.selectList(new QueryWrapper<CarSeries>().lambda()
                        .eq(CarSeries::getModel, orderCar.getModel())
                        .eq(CarSeries::getBrand, orderCar.getBrand()));
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
                .eq(WaybillCar::getOrderCarId, orderCar.getId())
                .le(WaybillCar::getState, WaybillCarStateEnum.UNLOADED.code)
                .select(WaybillCar::getLoadPhotoImg,WaybillCar::getUnloadPhotoImg));
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
