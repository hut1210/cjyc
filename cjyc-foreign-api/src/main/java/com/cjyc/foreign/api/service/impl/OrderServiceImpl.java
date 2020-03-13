package com.cjyc.foreign.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.web.order.SaveOrderCarDto;
import com.cjyc.common.model.dto.web.order.SaveOrderDto;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.enums.UserTypeEnum;
import com.cjyc.common.model.enums.customer.CustomerTypeEnum;
import com.cjyc.common.model.enums.order.OrderStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.system.config.LogoImgProperty;
import com.cjyc.common.system.service.ICsCustomerService;
import com.cjyc.common.system.service.ICsOrderService;
import com.cjyc.foreign.api.dto.req.OrderCarSubmitReqDto;
import com.cjyc.foreign.api.dto.req.OrderDetailReqDto;
import com.cjyc.foreign.api.dto.req.OrderSubmitReqDto;
import com.cjyc.foreign.api.dto.res.OrderCarDetailResDto;
import com.cjyc.foreign.api.dto.res.OrderDetailResDto;
import com.cjyc.foreign.api.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<IOrderDao, Order> implements IOrderService {
    @Resource
    private ICouponSendDao couponSendDao;
    @Resource
    private IOrderCarDao orderCarDao;
    @Resource
    private IWaybillCarDao waybillCarDao;
    @Resource
    private ICarSeriesDao carSeriesDao;
    @Resource
    private ICsCustomerService csCustomerService;
    @Resource
    private ICsOrderService csOrderService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVo<String> submitOrder(OrderSubmitReqDto reqDto) {
        //验证用户存不存在
        ResultVo<Customer> vo = csCustomerService.validateAndGetActive(reqDto.getLoginId());
        if(vo.getCode() != ResultEnum.SUCCESS.getCode()){
            return BaseResultUtil.fail(vo.getMsg());
        }

        // 验证订单费用是否正确
        BigDecimal totalFee = reqDto.getTotalFee();
        int carNum = reqDto.getOrderCarList().size();
        BigDecimal totalWlFee = reqDto.getLineWlFreightFee().multiply(new BigDecimal(carNum));
        if (totalFee == null) {
            reqDto.setTotalFee(totalWlFee);
        } else {
            if (!totalFee.equals(totalWlFee)) {
                return BaseResultUtil.fail("订单金额不正确!");
            }
        }

        // 干线费用
        List<OrderCarSubmitReqDto> carList = reqDto.getOrderCarList();
        if(!CollectionUtils.isEmpty(carList) && reqDto.getLineWlFreightFee() != null){
            carList.forEach(dto -> dto.setTrunkFee(reqDto.getLineWlFreightFee()));
        }

        // 封装订单入库参数
        SaveOrderDto paramDto = getSaveOrderDto(vo,reqDto);

        // 调用韵车系统-保存订单
        ResultVo resultVo = csOrderService.save(paramDto);

        return resultVo;
    }

    private SaveOrderDto getSaveOrderDto(ResultVo<Customer> vo,OrderSubmitReqDto reqDto) {
        Customer customer = vo.getData();
        SaveOrderDto paramDto = new SaveOrderDto();
        BeanUtils.copyProperties(reqDto,paramDto);
        paramDto.setLoginName(customer.getName());
        paramDto.setLoginPhone(customer.getContactPhone());
        paramDto.setLoginType(UserTypeEnum.CUSTOMER.code);
        paramDto.setCustomerId(customer.getId());
        paramDto.setCustomerName(customer.getName());
        paramDto.setCustomerType(customer.getType());
        paramDto.setState(OrderStateEnum.SUBMITTED.code);// 订单状态

        // 封装车辆信息
        List<SaveOrderCarDto> orderCarParamList = new ArrayList<>(10);
        List<OrderCarSubmitReqDto> orderCarList = reqDto.getOrderCarList();
        if (!CollectionUtils.isEmpty(orderCarList)) {
            SaveOrderCarDto orderCarDto = null;
            for (OrderCarSubmitReqDto orderCarSubmitReqDto : orderCarList) {
                orderCarDto = new SaveOrderCarDto();
                BeanUtils.copyProperties(orderCarSubmitReqDto,orderCarDto);
                orderCarParamList.add(orderCarDto);
            }
            paramDto.setOrderCarList(orderCarParamList);
        }
        return paramDto;
    }

    @Override
    public ResultVo<OrderDetailResDto> getOrderDetailByOrderNo(OrderDetailReqDto dto) {
        log.info("===>99车圈-查询订单详情,请求参数：{} ", JSON.toJSONString(dto));
        OrderDetailResDto orderDetailResDto = new OrderDetailResDto();
        // 查询订单信息
        LambdaQueryWrapper<Order> queryOrderWrapper = new QueryWrapper<Order>().lambda()
                .eq(Order::getCustomerId,dto.getLoginId()).eq(Order::getNo,dto.getOrderNo());
        Order order = super.getOne(queryOrderWrapper);
        if(order == null) {
            return BaseResultUtil.fail("订单号不存在,请检查");
        }
        // 填充返回数据
        fillData(orderDetailResDto, order);

        // 计算费用
        fillFee(orderDetailResDto, order);

        // 查询车辆信息
        getOrderCar(dto, orderDetailResDto);

        // 查询优惠券信息
        CouponSend couponSend = couponSendDao.selectById(orderDetailResDto.getCouponSendId());
        orderDetailResDto.setCouponName(couponSend == null ? "" : couponSend.getCouponName());
        ResultVo<OrderDetailResDto> resultVo = BaseResultUtil.success(orderDetailResDto);

        log.info("<===99车圈-查询订单详情,返回参数：{} ", JSON.toJSONString(resultVo));
        return resultVo;
    }

    private void fillData(OrderDetailResDto orderDetailResDto, Order order) {
        BeanUtils.copyProperties(order,orderDetailResDto);
        StringBuilder start = new StringBuilder();
        start.append(order.getStartProvince() == null ? "" : order.getStartProvince()+" ");
        start.append(order.getStartCity() == null ? "" : order.getStartCity()+" ");
        start.append(order.getStartArea() == null ? "" : order.getStartArea());
        orderDetailResDto.setStartProvinceCityAreaName(start.toString().trim());

        StringBuilder end = new StringBuilder();
        end.append(order.getEndProvince() == null ? "" : order.getEndProvince()+" ");
        end.append(order.getEndCity() == null ? "" : order.getEndCity()+" ");
        end.append(order.getEndArea() == null ? "" : order.getEndArea());
        orderDetailResDto.setEndProvinceCityAreaName(end.toString().trim());

        orderDetailResDto.setStartAddress(order.getStartCity()+order.getStartArea()+order.getStartAddress());
        orderDetailResDto.setEndAddress(order.getEndCity()+order.getEndArea()+order.getEndAddress());
    }

    private void fillFee(OrderDetailResDto orderDetailResDto, Order order) {
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
        orderDetailResDto.setPickFee(pickFee);
        orderDetailResDto.setBackFee(backFee);
        orderDetailResDto.setTrunkFee(trunkFee);
        orderDetailResDto.setAddInsuranceFee(addInsuranceFee);


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
            orderDetailResDto.setWlTotalFee(wlTotalFees);
            orderDetailResDto.setAgencyFee(agencyFees);
        }
    }

    private void getOrderCar(OrderDetailReqDto dto, OrderDetailResDto orderDetailResDto) {
        LambdaQueryWrapper<OrderCar> queryCarWrapper = new QueryWrapper<OrderCar>().lambda().eq(OrderCar::getOrderNo,dto.getOrderNo());
        List<OrderCar> orderCarList = orderCarDao.selectList(queryCarWrapper);
        List<OrderCarDetailResDto> orderCarDetailList = new ArrayList<>(10);
        if (!CollectionUtils.isEmpty(orderCarList)) {
            OrderCarDetailResDto orderCarDetailResDto = null;
            for (OrderCar orderCar : orderCarList) {
                orderCarDetailResDto = new OrderCarDetailResDto();
                BeanUtils.copyProperties(orderCar,orderCarDetailResDto);
                // 订单车辆信息
                orderCarDetailList.add(orderCarDetailResDto);

                // 查询车辆图片
                this.getCarImg(orderCar, orderCarDetailResDto);
                // 查询品牌logo图片
                List<CarSeries> carSeriesList = carSeriesDao.selectList(new QueryWrapper<CarSeries>().lambda()
                        .eq(CarSeries::getModel, orderCar.getModel())
                        .eq(CarSeries::getBrand, orderCar.getBrand()));
                if(!CollectionUtils.isEmpty(carSeriesList)) {
                    orderCarDetailResDto.setLogoImg(LogoImgProperty.logoImg+carSeriesList.get(0).getLogoImg());
                }
            }
        }
        orderDetailResDto.setOrderCarDetailList(orderCarDetailList);
    }

    private void getCarImg(OrderCar orderCar, OrderCarDetailResDto orderCarDetailResDto) {
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
        orderCarDetailResDto.setCarImgList(photoImgList);
    }
}
