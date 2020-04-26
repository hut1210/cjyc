package com.cjyc.foreign.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.constant.AccountConstant;
import com.cjyc.common.model.dao.IOrderCarDao;
import com.cjyc.common.model.dao.IOrderDao;
import com.cjyc.common.model.dto.web.order.CancelOrderDto;
import com.cjyc.common.model.dto.web.order.SaveOrderCarDto;
import com.cjyc.common.model.dto.web.order.SaveOrderDto;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.entity.Line;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.enums.UserTypeEnum;
import com.cjyc.common.model.enums.order.OrderStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.order.TransportInfoOrderCarVo;
import com.cjyc.common.system.service.ICsCustomerService;
import com.cjyc.common.system.service.ICsLineService;
import com.cjyc.common.system.service.ICsOrderCarLogService;
import com.cjyc.common.system.service.ICsOrderService;
import com.cjyc.foreign.api.dto.req.*;
import com.cjyc.foreign.api.dto.res.OrderCarDetailResDto;
import com.cjyc.foreign.api.dto.res.OrderCarTransportDetailResDto;
import com.cjyc.foreign.api.dto.res.OrderDetailResDto;
import com.cjyc.foreign.api.service.IOrderService;
import com.cjyc.foreign.api.utils.LoginAccountUtil;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<IOrderDao, Order> implements IOrderService {
    @Resource
    private IOrderCarDao orderCarDao;
    @Resource
    private IOrderDao orderDao;
    @Resource
    private ICsCustomerService csCustomerService;
    @Resource
    private ICsOrderService csOrderService;
    @Resource
    private ICsLineService csLineService;
    @Resource
    private ICsOrderCarLogService csOrderCarLogService;

    @Override
    public ResultVo<String> submitOrder(OrderSubmitReqDto reqDto) {
        log.info("===>99车圈-下单,请求参数：{}",JSON.toJSONString(reqDto));
        ResultVo resultVo = null;
        try {
            // 验证用户
            Customer customer = csCustomerService.getByPhone(reqDto.getCustomerPhone(), true);
            if(customer == null){
                log.info("用户不存在");
                return BaseResultUtil.fail("用户不存在");
            }

            // 验证费用
            ResultVo<String> validateFee = validateFee(reqDto);
            if (validateFee != null) {
                return validateFee;
            }

            // 封装订单入库参数
            SaveOrderDto paramDto = getSaveOrderDto(customer,reqDto);

            // 调用韵车系统-保存订单
            log.info("===>调用韵车系统保存订单接口,请求参数：{}",JSON.toJSONString(paramDto));
            resultVo = csOrderService.save(paramDto);
            log.info("<===调用韵车系统保存订单接口,返回参数：{}",JSON.toJSONString(resultVo));
            if (resultVo.getCode() == ResultEnum.SUCCESS.getCode()) {
                int index = resultVo.getMsg().indexOf("D");
                if (index > -1) {
                    resultVo = BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),
                            resultVo.getMsg().substring(index));
                }
            }
        } catch (Exception e) {
            log.error("===>下单异常：{}",e.getMessage());
            resultVo = BaseResultUtil.fail("下单异常...");
        }

        log.info("<===99车圈-下单,返回参数：{}",JSON.toJSONString(resultVo));
        return resultVo;
    }

    private ResultVo<String> validateFee(OrderSubmitReqDto reqDto) {
        // 验证保险费
        BigDecimal totalInsuranceFee = BigDecimal.ZERO;// 总保险费-计算值
        List<OrderCarSubmitReqDto> orderCarList = reqDto.getOrderCarList();
        for (OrderCarSubmitReqDto carSubmitReqDto : orderCarList) {
            int valuation = carSubmitReqDto.getValuation();// 车值
            BigDecimal addInsuranceFee = BigDecimal.ZERO;// 每辆车保险费-计算值
            // 计算每辆车保险费
            if (valuation > 10) {
                addInsuranceFee = addInsuranceFee.add(BigDecimal.valueOf(50));
                valuation = valuation - 10;
            }
            while (valuation - 10 > 0) {
                addInsuranceFee = addInsuranceFee.add(BigDecimal.valueOf(50));
                valuation = valuation - 10;
            }

            carSubmitReqDto.setAddInsuranceFee(addInsuranceFee);// 设置正确的保险费
            totalInsuranceFee = totalInsuranceFee.add(addInsuranceFee);
        }

        // 验证线路费
        Line line = csLineService.getlineByArea(reqDto.getStartAreaCode(), reqDto.getEndAreaCode());
        if (line == null) {
            log.info("===>线路不存在，请重新选择城市");
            return BaseResultUtil.fail("线路不存在，请重新选择城市");
        }
        BigDecimal lineFee = line.getDefaultWlFee() == null ? BigDecimal.ZERO : line.getDefaultWlFee().divide(new BigDecimal(100));
        if (lineFee.doubleValue() != reqDto.getLineWlFreightFee().doubleValue()) {
            log.info("===>线路费金额不正确");
            return BaseResultUtil.fail("线路费金额不正确!");
        }

        // 设置线路费与订单总价
        reqDto.setLineWlFreightFee(lineFee);
        reqDto.setTotalFee(lineFee.multiply(BigDecimal.valueOf(orderCarList.size())).add(totalInsuranceFee));

        // 设置车辆干线费用
        orderCarList.forEach(dto -> dto.setTrunkFee(lineFee));

        return null;
    }

    private SaveOrderDto getSaveOrderDto(Customer customer,OrderSubmitReqDto reqDto) {
        SaveOrderDto paramDto = new SaveOrderDto();
        BeanUtils.copyProperties(reqDto,paramDto);
        paramDto.setLoginId(customer.getId());
        paramDto.setLoginName(customer.getName());
        paramDto.setLoginPhone(customer.getContactPhone());
        paramDto.setLoginType(UserTypeEnum.CUSTOMER.code);
        paramDto.setCustomerId(customer.getId());
        paramDto.setCustomerName(customer.getName());
        paramDto.setCustomerType(customer.getType());
        paramDto.setState(OrderStateEnum.SUBMITTED.code);// 下单状态

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
        log.info("===>99车圈-查询订单详情,请求参数：{}", JSON.toJSONString(dto));
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
        log.info("<===99车圈-查询订单详情,返回参数：{}", JSON.toJSONString(resultVo));
        return resultVo;
    }

    @Override
    public ResultVo<String> cancelOrder(CancelOrderReqDto reqDto) {
        String account = LoginAccountUtil.getLoginAccount();
        if (StringUtils.isEmpty(account)) {
            return BaseResultUtil.fail("登录账号信息有误，请检查!");
        }
        //查询订单信息是否存在
        Order order = super.getOne(new QueryWrapper<Order>().lambda()
                .eq(Order::getNo, reqDto.getOrderNo()));
        if (null == order) {
            return BaseResultUtil.fail("订单编号有误，请检查!");
        }
        Customer customer = csCustomerService.getByPhone(account, true);
        if (null == customer) {
            return BaseResultUtil.fail("用户信息有误，请检查!");
        }
        if (order.getCustomerId() != null && customer.getId() != null &&
                !order.getCustomerId().equals(customer.getId())) {
            return BaseResultUtil.fail("订单不属于该用户，请检查!");
        }
        //请求信息封装
        CancelOrderDto dto = new CancelOrderDto();
        dto.setOrderId(order.getId());
        dto.setLoginId(customer.getId());
        dto.setLoginPhone(customer.getContactPhone());
        dto.setLoginName(customer.getName());
        dto.setLoginType(UserTypeEnum.CUSTOMER);
        ResultVo cancelVo = csOrderService.cancel(dto);
        if (isResultSuccess(cancelVo)) {
            return BaseResultUtil.getVo(cancelVo.getCode(), cancelVo.getMsg(),
                    reqDto.getOrderNo());
        }else {
            return cancelVo;
        }
    }

    /**
     * 检查返回结果是否成功
     *
     * @param resultVo
     * @return
     */
    private boolean isResultSuccess(ResultVo resultVo) {
        if (null == resultVo) {
            return false;
        }
        return ResultEnum.SUCCESS.getCode().equals(resultVo.getCode());
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

    @Override
    public ResultVo<String> allowReleaseCar(ReleaseCarReqDto reqDto) {
        if(AccountConstant.ACCOUNT_99CC.equals(reqDto.getCustomerPhone())){
            //九九车圈
            Set<String> orderNoSet = Sets.newHashSet();
            Set<String> orderCarNoSet = Sets.newHashSet();
            for (String no : reqDto.getNoList()) {
                if(no.contains("-")){
                    orderCarNoSet.add(no);
                }else{
                    orderNoSet.add(no);
                }
            }
            //处理订单
            if(!CollectionUtils.isEmpty(orderNoSet)){
                for (String no : orderNoSet) {
                    Order order = orderDao.findByNo(no);
                    if(order == null || !reqDto.getCustomerPhone().equals(order.getCustomerPhone())){
                        return BaseResultUtil.fail("订单号错误，请检查");
                    }
                }
                //更新订单车辆标识
                orderCarDao.updateReleaseFlagByOrderNos(orderNoSet, reqDto.getType());
                //记录车辆日志
                List<OrderCar> list = orderCarDao.findListbyOrderNos(orderNoSet);
            }
            //处理车辆
            if(!CollectionUtils.isEmpty(orderCarNoSet)){
                //验证订单是否是当前客户的
                List<Order> oList = orderDao.findListByCarNos(orderCarNoSet);
                if(CollectionUtils.isEmpty(oList)){
                    return BaseResultUtil.fail("车辆编号错误，请检查");
                }
                for (Order order : oList) {
                    if(!reqDto.getCustomerPhone().equals(order.getCustomerPhone())){
                        return BaseResultUtil.fail("车辆编号错误，请检查");
                    }
                }
                //更新车辆标识
                orderCarDao.updateReleaseFlagByNos(orderCarNoSet, reqDto.getType());
                //记录车辆日志

                csOrderCarLogService.asyncSaveBatchForReleaseCar(orderCarNoSet, reqDto.getType());
            }


        }

        return BaseResultUtil.success();
    }
}
