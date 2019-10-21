package com.cjyc.customer.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjyc.common.model.constant.NoConstant;
import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.dao.ICarSeriesDao;
import com.cjyc.common.model.dao.IIncrementerDao;
import com.cjyc.common.model.dao.IOrderCarDao;
import com.cjyc.common.model.dao.IOrderDao;
import com.cjyc.common.model.dto.BasePageDto;
import com.cjyc.common.model.dto.customer.OrderConditionDto;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.customer.OrderCarCenterVo;
import com.cjyc.common.model.vo.customer.OrderCenterVo;
import com.cjyc.common.model.vo.customer.OrderDetailVo;
import com.cjyc.customer.api.dto.OrderCarDto;
import com.cjyc.customer.api.dto.OrderDto;
import com.cjyc.customer.api.service.IOrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @auther litan
 * @description: com.cjyc.customer.api.service.impl
 * @date:2019/10/8
 */
@Service
@Slf4j
public class OrderServiceImpl implements IOrderService{

    @Resource
    private IOrderDao orderDao;

    @Resource
    private IIncrementerDao incrementerDao;

    @Resource
    private IOrderCarDao iOrderCarDao;

    @Resource
    private ICarSeriesDao iCarSeriesDao;

    @Override
    public boolean commitOrder(OrderDto orderDto) {

        int isSimple = orderDto.getIsSimple();
        int saveType = orderDto.getSaveType();

        Order order = new Order();
        BeanUtils.copyProperties(orderDto,order);

        //获取订单业务编号
        String orderNo = incrementerDao.getIncrementer(NoConstant.ORDER_PREFIX);
        order.setNo(orderNo);
        //简单
        if(isSimple == 1){

            //详单
        }else if(isSimple == 0){
            //草稿
            if(saveType==0){
                order.setState(0);//待提交
                //正式下单
            }else if(saveType==1){
                order.setState(1);//待分配
            }
        }

        order.setSource(1);
        order.setCarNum(orderDto.getOrderCarDtoList().size());
        order.setCreateTime(System.currentTimeMillis());
        order.setCreateUserName(orderDto.getCustomerName());
        //order.setCreateUserType(0);//创建人类型：0客户，1业务员
        order.setCreateUserId(0L);
        int count = orderDao.addOrder(order);

        //保存车辆信息
        List<OrderCarDto> carDtoList =  orderDto.getOrderCarDtoList();
        if(count > 0){
            for(OrderCarDto orderCarDto : carDtoList){
                OrderCar orderCar = new OrderCar();
                BeanUtils.copyProperties(orderCarDto,orderCar);
                String carNo = incrementerDao.getIncrementer(NoConstant.CAR_PREFIX);

                orderCar.setOrderNo(orderNo);
                orderCar.setOrderId(order.getId());
                orderCar.setNo(carNo);

                iOrderCarDao.insert(orderCar);
            }
        }

        return count > 0 ? true : false;
    }

    @Override
    public boolean modify(OrderDto orderDto) {
        Order order = orderDao.selectById(orderDto.getOrderId());
        BeanUtils.copyProperties(orderDto,order);

        //删除之前的
        QueryWrapper<OrderCar> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id",orderDto.getOrderId());
        int delCount = iOrderCarDao.delete(wrapper);

        //保存车辆信息
        List<OrderCarDto> carDtoList =  orderDto.getOrderCarDtoList();
        if(delCount > 0){
            for(OrderCarDto orderCarDto : carDtoList){
                OrderCar orderCar = new OrderCar();
                BeanUtils.copyProperties(orderCarDto,orderCar);
                String carNo = incrementerDao.getIncrementer(NoConstant.CAR_PREFIX);
                orderCar.setWlPayState(0);
                orderCar.setOrderNo(order.getNo());
                orderCar.setOrderId(order.getId());
                orderCar.setNo(carNo);

                iOrderCarDao.insert(orderCar);
            }
        }

        return false;
    }

    @Override
    public PageInfo<OrderCenterVo> getWaitConFirmOrders(BasePageDto basePageDto) {
        try{
            //查询所有待确认订单信息
            List<OrderCenterVo> ordCenVos = orderDao.getWaitConFirmOrders();
            return encapOrderList(ordCenVos,basePageDto);
        }catch (Exception e){
            log.info("获取待确认订单出现异常");
        }
        return null;
    }

    @Override
    public PageInfo<OrderCenterVo> getTransOrders(BasePageDto basePageDto) {
        try{
            //查询所有运输中订单信息
            List<OrderCenterVo> ordCenVos = orderDao.getTransOrders();
            return encapOrderList(ordCenVos,basePageDto);
        }catch (Exception e){
            log.info("获取运输中订单出现异常");
        }
        return null;
    }

    @Override
    public PageInfo<OrderCenterVo> getPaidOrders(BasePageDto basePageDto) {
        try{
            //查询所有已支付订单信息
            List<OrderCenterVo> ordCenVos = orderDao.getPaidOrders();
            return encapOrderList(ordCenVos,basePageDto);
        }catch (Exception e){
            log.info("获取已支付订单出现异常");
        }
        return null;
    }

    @Override
    public PageInfo<OrderCenterVo> getAllOrders(BasePageDto basePageDto) {
        try{
            //查询全部订单信息
            List<OrderCenterVo> ordCenVos = orderDao.getAllOrders();
            return encapOrderList(ordCenVos,basePageDto);
        }catch (Exception e){
            log.info("获取全部订单出现异常");
        }
        return null;
    }

    @Override
    public OrderDetailVo getOrderDetailByNo(String orderNo) {
        OrderDetailVo detailVo = null;
        try{
            //根据订单编号查询订单详情
            detailVo = orderDao.getOrderDetailByNo(orderNo);
            detailVo.setCreateTime(LocalDateTimeUtil.convertToString(Long.valueOf(detailVo.getCreateTime()), TimePatternConstant.DATE));
            detailVo.setExpectStartDate(LocalDateTimeUtil.convertToString(Long.valueOf(detailVo.getExpectStartDate()), TimePatternConstant.DATE));
            //根据订单编号获取车辆信息
            List<OrderCarCenterVo> ordCarCenVos = encapOrderCarList(orderNo);
            detailVo.setOrderCarCenterVos(ordCarCenVos);
        }catch (Exception e){
            log.info("获取订单详情出现异常");
        }
        return detailVo;
    }

    @Override
    public PageInfo<OrderCenterVo> getConFirmOrdsByTerm(OrderConditionDto dto) {
        try{
            List<OrderCenterVo> orderCenterVos = orderDao.getConFirmOrdsByTerm(dto);
            return encapOrderByTermList(orderCenterVos,dto);
        }catch (Exception e){
            log.info("根据条件筛选待确认订单出现异常");
        }
        return null;
    }

    @Override
    public PageInfo<OrderCenterVo> getTransOrdsByTerm(OrderConditionDto dto) {
        try{
            List<OrderCenterVo> orderCenterVos = orderDao.getTransOrdsByTerm(dto);
            return encapOrderByTermList(orderCenterVos,dto);
        }catch (Exception e){
            log.info("根据条件筛选待确认订单出现异常");
        }
        return null;
    }

    @Override
    public PageInfo<OrderCenterVo> getPaidOrdsByTerm(OrderConditionDto dto) {
        try{
            List<OrderCenterVo> orderCenterVos = orderDao.getPaidOrdsByTerm(dto);
            return encapOrderByTermList(orderCenterVos,dto);
        }catch (Exception e){
            log.info("根据条件筛选待确认订单出现异常");
        }
        return null;
    }

    @Override
    public PageInfo<OrderCenterVo> getAllOrdsByTerm(OrderConditionDto dto) {
        try{
            List<OrderCenterVo> orderCenterVos = orderDao.getAllOrdsByTerm(dto);
            return encapOrderByTermList(orderCenterVos,dto);
        }catch (Exception e){
            log.info("根据条件筛选待确认订单出现异常");
        }
        return null;
    }

    /**
     * 通过条件封装筛选后的订单列表
     * @return
     */
    private PageInfo<OrderCenterVo> encapOrderByTermList(List<OrderCenterVo> orderCenterVos,OrderConditionDto dto){
        PageInfo<OrderCenterVo> pageInfo = new PageInfo<>();
        List<OrderCarCenterVo> carCenterVos = null;
        if(orderCenterVos.get(0) != null && orderCenterVos.size() >= 1){
            for(OrderCenterVo order : orderCenterVos){
                carCenterVos = encapOrderCarList(order.getNo(),dto.getStoreId(),dto.getBrand(),dto.getModel());
                order.setOrderCarCenterVos(carCenterVos);
            }
            PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
            pageInfo = new PageInfo<>(orderCenterVos);
        }
        return pageInfo;
    }

    /**
     * 封装待确认/运输中/待支付/全部订单列表
     * @param ordCenVos
     * @param basePageDto
     * @return
     */
    private PageInfo<OrderCenterVo> encapOrderList(List<OrderCenterVo> ordCenVos,BasePageDto basePageDto){
        PageInfo<OrderCenterVo> pageInfo = new PageInfo<>();
        try{
            if(ordCenVos.get(0) != null && ordCenVos.size() >= 1){
                for(OrderCenterVo order : ordCenVos){
                    List<OrderCarCenterVo> ordCarCenVos = encapOrderCarList(order.getNo());
                    order.setOrderCarCenterVos(ordCarCenVos);
                }
                PageHelper.startPage(basePageDto.getCurrentPage(), basePageDto.getPageSize());
                pageInfo = new PageInfo<>(ordCenVos);
            }
        }catch (Exception e){
            log.info("获取订单列表出现异常");
        }
        return pageInfo;
    }

    /**
     * 通过订单编号封装车辆信息
     * @param strs
     * @return
     */
    private List<OrderCarCenterVo> encapOrderCarList(String... strs){
        List<OrderCarCenterVo> ordCarCenVos = new ArrayList<>();
        if(strs.length == 1){
            //通过订单编号查询车辆信息
            ordCarCenVos = iOrderCarDao.getOrderCarByNo(strs[0]);
        }else if(strs.length == 4){
            ordCarCenVos = iOrderCarDao.getOrderCarInfoByTerm(strs[0],strs[1],strs[2],strs[3]);
        }
        if(ordCarCenVos != null && ordCarCenVos.size() > 0){
            //根据车牌和型号查logo
            for(OrderCarCenterVo carCenterVo : ordCarCenVos){
                String logoImg = iCarSeriesDao.getLogoImgByBraMod(carCenterVo.getBrand(),carCenterVo.getModel());
                carCenterVo.setLogoImg(logoImg);
            }
        }
        return ordCarCenVos;
    }
}
