package com.cjyc.common.system.service.impl;

import com.cjyc.common.model.dao.IOrderCarDao;
import com.cjyc.common.model.dao.IOrderDao;
import com.cjyc.common.model.dto.web.order.CommitOrderCarDto;
import com.cjyc.common.model.dto.web.order.CommitOrderDto;
import com.cjyc.common.model.dto.web.order.SaveOrderCarDto;
import com.cjyc.common.model.dto.web.order.SaveOrderDto;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.common.model.entity.Store;
import com.cjyc.common.model.enums.PayModeEnum;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.enums.SendNoTypeEnum;
import com.cjyc.common.model.enums.city.CityLevelEnum;
import com.cjyc.common.model.enums.customer.CustomerTypeEnum;
import com.cjyc.common.model.enums.order.OrderCarStateEnum;
import com.cjyc.common.model.enums.order.OrderStateEnum;
import com.cjyc.common.model.exception.ParameterException;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.city.FullCity;
import com.cjyc.common.system.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * 订单公共业务
 * @author JPG
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class CsOrderServiceImpl implements ICsOrderService {

    @Resource
    private IOrderDao orderDao;
    @Resource
    private IOrderCarDao orderCarDao;
    @Resource
    private ICsCityService csCityService;
    @Resource
    private ICsSendNoService csSendNoService;
    @Resource
    private ICsCustomerService csCustomerService;
    @Resource
    private ICsStoreService csStoreService;


    @Override
    public ResultVo save(SaveOrderDto paramsDto, OrderStateEnum stateEnum) {
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
        FullCity startFullCity = csCityService.findFullCity(paramsDto.getStartAreaCode(), CityLevelEnum.PROVINCE);
        copyOrderStartCity(startFullCity, order);
        FullCity endFullCity = csCityService.findFullCity(paramsDto.getEndAreaCode(), CityLevelEnum.PROVINCE);
        copyOrderEndCity(endFullCity, order);

        /**1、组装订单数据
         */
        if (newOrderFlag) {
            order.setNo(csSendNoService.getNo(SendNoTypeEnum.ORDER));
        }
        order.setState(stateEnum.code);
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
        FullCity startFullCity = csCityService.findFullCity(paramsDto.getStartAreaCode(), CityLevelEnum.PROVINCE);
        copyOrderStartCity(startFullCity, order);
        FullCity endFullCity = csCityService.findFullCity(paramsDto.getEndAreaCode(), CityLevelEnum.PROVINCE);
        copyOrderEndCity(endFullCity, order);

        //验证用户
        Customer customer = new Customer();
        if (paramsDto.getCustomerId() != null) {
            customer = csCustomerService.getByUserId(paramsDto.getCustomerId(),true);
            if(customer == null){
                customer = csCustomerService.getByPhone(paramsDto.getCustomerPhone(), true);
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
                    csCustomerService.save(customer);
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
        if (newOrderFlag) {
            order.setNo(csSendNoService.getNo(SendNoTypeEnum.ORDER));
        }
        //计算所属业务中心ID
        if(paramsDto.getStartStoreId() > 0){
            order.setStartBelongStoreId(paramsDto.getStartStoreId());
        }else{
            //查询地址所属业务中心
            Store startBelongStore = csStoreService.findOneBelongByAreaCode(order.getStartAreaCode());
            if(startBelongStore != null){
                order.setStartBelongStoreId(startBelongStore.getId());
            }
        }
        if(paramsDto.getEndStoreId() > 0){
            order.setStartBelongStoreId(paramsDto.getEndStoreId());
        }else{
            //查询地址所属业务中心
            Store endBelongStore = csStoreService.findOneBelongByAreaCode(order.getEndAreaCode());
            if(endBelongStore != null){
                order.setStartBelongStoreId(endBelongStore.getId());
            }
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

    /**
     * 拷贝订单开始城市
     * @author JPG
     * @since 2019/11/5 9:06
     * @param vo
     * @param order
     */
    private void copyOrderStartCity(FullCity vo, Order order) {
        if(vo == null){
            return;
        }
        order.setStartProvince(vo.getProvince());
        order.setStartProvinceCode(vo.getProvinceCode());
        order.setStartCity(vo.getCity());
        order.setStartCityCode(vo.getCityCode());
        order.setStartArea(vo.getArea());
        order.setStartAreaCode(vo.getAreaCode());
    }
    /**
     * 拷贝订单结束城市
     * @author JPG
     * @since 2019/11/5 9:06
     * @param vo
     * @param order
     */
    private void copyOrderEndCity(FullCity vo, Order order) {
        if(vo == null){
            return;
        }
        order.setEndProvince(vo.getProvince());
        order.setEndProvinceCode(vo.getProvinceCode());
        order.setEndCity(vo.getCity());
        order.setEndCityCode(vo.getCityCode());
        order.setEndArea(vo.getArea());
        order.setEndAreaCode(vo.getAreaCode());
    }



}
