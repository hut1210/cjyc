package com.cjyc.foreign.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.IOrderCarDao;
import com.cjyc.common.model.dao.IOrderDao;
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
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<IOrderDao, Order> implements IOrderService {
    @Resource
    private IOrderCarDao orderCarDao;
    @Resource
    private ICsCustomerService csCustomerService;
    @Resource
    private ICsOrderService csOrderService;
    @Resource
    private ICsLineService csLineService;

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
        BigDecimal totalParamInsuranceFee = BigDecimal.ZERO;// 总保险费-参数值
        List<OrderCarSubmitReqDto> orderCarList = reqDto.getOrderCarList();
        for (OrderCarSubmitReqDto carSubmitReqDto : orderCarList) {
            int valuation = carSubmitReqDto.getValuation();// 车值
            BigDecimal paramInsuranceFee = carSubmitReqDto.getAddInsuranceFee();// 保险费-参数值
            BigDecimal addInsuranceFee = BigDecimal.ZERO;// 每辆车保险费-计算值
            // 计算每辆车保险费
            if (valuation > 10) {
                addInsuranceFee = addInsuranceFee.add(BigDecimal.valueOf(50));
                valuation = valuation - 10;
            }
            while (valuation - 10 >= 0) {
                addInsuranceFee = addInsuranceFee.add(BigDecimal.valueOf(50));
                valuation = valuation - 10;
            }
            // 验证每辆车保险费
            if (!addInsuranceFee.equals(paramInsuranceFee)) {
                log.info("车牌号【" + carSubmitReqDto.getPlateNo() + "】保险费金额不正确!");
                log.info("车牌号【" + carSubmitReqDto.getPlateNo() + "】保险费金额为 "+ addInsuranceFee);
                return BaseResultUtil.fail("车牌号【" + carSubmitReqDto.getPlateNo() + "】保险费金额不正确!");
            }

            carSubmitReqDto.setAddInsuranceFee(addInsuranceFee);// 设置正确的保险费
            totalInsuranceFee = totalInsuranceFee.add(addInsuranceFee);
            totalParamInsuranceFee = totalParamInsuranceFee.add(paramInsuranceFee);
        }

        // 验证线路费
        Line line = csLineService.getlineByArea(reqDto.getStartAreaCode(), reqDto.getEndAreaCode());
        if (line == null) {
            log.info("===>线路不存在，请重新选择城市");
            return BaseResultUtil.fail("线路不存在，请重新选择城市");
        }
        BigDecimal lineFee = line.getDefaultWlFee() == null ? BigDecimal.ZERO : line.getDefaultWlFee().divide(new BigDecimal(100));
        if (!lineFee.equals(reqDto.getLineWlFreightFee())) {
            log.info("===>线路费金额不正确");
            return BaseResultUtil.fail("线路费金额不正确!");
        }

        // 验证订单总价 订单总价 = (线路费用 * 车辆数)+总保险费
        BigDecimal totalFee = reqDto.getLineWlFreightFee().multiply(BigDecimal.valueOf(reqDto.getOrderCarList().size())).add(totalParamInsuranceFee);
        if (!reqDto.getTotalFee().equals(totalFee)) {
            log.info("===>订单金额不正确");
            return BaseResultUtil.fail("订单金额不正确!");
        }

        // 设置线路费与订单总价
        //reqDto.setLineWlFreightFee(lineFee);
        //reqDto.setTotalFee(lineFee.multiply(BigDecimal.valueOf(carNum)).add(totalInsuranceFee));

        // 设置车辆干线费用
        List<OrderCarSubmitReqDto> carList = reqDto.getOrderCarList();
        if(!CollectionUtils.isEmpty(carList) && reqDto.getLineWlFreightFee() != null){
            carList.forEach(dto -> dto.setTrunkFee(reqDto.getLineWlFreightFee()));
        }

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
