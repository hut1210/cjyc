package com.cjyc.foreign.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.web.order.SaveOrderCarDto;
import com.cjyc.common.model.dto.web.order.SaveOrderDto;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.enums.UserTypeEnum;
import com.cjyc.common.model.enums.order.OrderStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.order.TransportInfoOrderCarVo;
import com.cjyc.common.system.service.ICsCustomerService;
import com.cjyc.common.system.service.ICsOrderService;
import com.cjyc.foreign.api.dto.req.OrderCarSubmitReqDto;
import com.cjyc.foreign.api.dto.req.OrderDetailReqDto;
import com.cjyc.foreign.api.dto.req.OrderSubmitReqDto;
import com.cjyc.foreign.api.dto.res.OrderCarDetailResDto;
import com.cjyc.foreign.api.dto.res.OrderCarTransportDetailResDto;
import com.cjyc.foreign.api.dto.res.OrderDetailResDto;
import com.cjyc.foreign.api.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
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
        Order order = super.getOne(new QueryWrapper<Order>().lambda().eq(Order::getNo,dto.getOrderNo()));
        if(order == null) {
            return BaseResultUtil.fail("订单号不存在,请检查");
        }
        // 封装订单信息
        BeanUtils.copyProperties(order,orderDetailResDto);
        // 查询订单车辆信息
        getOrderCar(orderDetailResDto);
        // 查询运输信息
        getTransportInfo(orderDetailResDto);

        ResultVo<OrderDetailResDto> resultVo = BaseResultUtil.success(orderDetailResDto);
        log.info("<===99车圈-查询订单详情,返回参数：{} ", JSON.toJSONString(resultVo));
        return resultVo;
    }

    private void getTransportInfo(OrderDetailResDto orderDetailResDto) {
        List<TransportInfoOrderCarVo> transportInfoList = orderCarDao.findTransportStateByOrderId(orderDetailResDto.getId());
        List<OrderCarTransportDetailResDto> orderCarTransportDetailList = new ArrayList<>(10);
        if (!CollectionUtils.isEmpty(transportInfoList)) {
            OrderCarTransportDetailResDto orderCarTransportDetail= null;
            for (TransportInfoOrderCarVo transportInfoOrderCarVo : transportInfoList) {
                orderCarTransportDetail = new OrderCarTransportDetailResDto();
                BeanUtils.copyProperties(transportInfoOrderCarVo,orderCarTransportDetail);
                orderCarTransportDetailList.add(orderCarTransportDetail);
            }
        }
        orderDetailResDto.setOrderCarTransportDetailList(orderCarTransportDetailList);
    }

    private void getOrderCar(OrderDetailResDto orderDetailResDto) {
        List<OrderCar> orderCarList = orderCarDao.selectList(new QueryWrapper<OrderCar>().lambda().eq(OrderCar::getOrderId,orderDetailResDto.getId()));
        List<OrderCarDetailResDto> orderCarDetailList = new ArrayList<>(10);
        if (!CollectionUtils.isEmpty(orderCarList)) {
            OrderCarDetailResDto orderCarDetailResDto = null;
            for (OrderCar orderCar : orderCarList) {
                orderCarDetailResDto = new OrderCarDetailResDto();
                BeanUtils.copyProperties(orderCar,orderCarDetailResDto);
                orderCarDetailList.add(orderCarDetailResDto);
            }
        }
        orderDetailResDto.setOrderCarDetailList(orderCarDetailList);
    }

}
