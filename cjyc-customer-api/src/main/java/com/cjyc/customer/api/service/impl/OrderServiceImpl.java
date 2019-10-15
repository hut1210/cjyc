package com.cjyc.customer.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjkj.common.utils.ExcelUtil;
import com.cjyc.common.model.constant.PatternConstant;
import com.cjyc.common.model.dao.ICarSeriesDao;
import com.cjyc.common.model.dao.IOrderCarDao;
import com.cjyc.common.model.dao.IOrderDao;
import com.cjyc.common.model.dto.BasePageDto;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.enums.SysEnum;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.customer.OrderCarCenterVo;
import com.cjyc.common.model.vo.customer.OrderCenterVo;
import com.cjyc.common.model.vo.customer.OrderDetailVo;
import com.cjyc.customer.api.dto.OrderDto;
import com.cjyc.customer.api.service.IOrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
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

    @Autowired
    IOrderDao orderDao;

    @Resource
    IOrderCarDao iOrderCarDao;

    @Resource
    ICarSeriesDao iCarSeriesDao;

    @Override
    public boolean commitOrder(OrderDto orderDto) {

        int isSimple = orderDto.getIsSimple();
        int saveType = orderDto.getSaveType();


        Order order = new Order();
        BeanUtils.copyProperties(orderDto,order);
        order.setId(001121212335653L);
        order.setNo("555666");
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
        int id = orderDao.add(order);

        return id > 0 ? true : false;
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
            detailVo.setCreateTime(LocalDateTimeUtil.convertToString(Long.valueOf(detailVo.getCreateTime()), PatternConstant.DATE));
            detailVo.setExpectStartDate(LocalDateTimeUtil.convertToString(Long.valueOf(detailVo.getExpectStartDate()), PatternConstant.DATE));
            //根据订单编号获取车辆信息
            List<OrderCarCenterVo> ordCarCenVos = encapOrderCarList(orderNo);
            detailVo.setOrderCarCenterVos(ordCarCenVos);
        }catch (Exception e){
            log.info("获取订单详情出现异常");
        }
        return detailVo;
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
                    if(StringUtils.isNotBlank(order.getTotalFee())){
                        order.setTotalFee(new BigDecimal(order.getTotalFee()).divide(new BigDecimal(100)).toString());
                    }else{
                        order.setTotalFee(SysEnum.ZERO.toString());
                    }
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
     * @param orderNo
     * @return
     */
    private List<OrderCarCenterVo> encapOrderCarList(String orderNo){
        //通过订单编号查询车辆信息
        List<OrderCarCenterVo> ordCarCenVos = iOrderCarDao.getOrderCarByNo(orderNo);
        if(ordCarCenVos != null && ordCarCenVos.size() > 0){
            //根据车牌和型号查logo
            for(OrderCarCenterVo carCenterVo : ordCarCenVos){
                String logoImg = iCarSeriesDao.getLogoImgByBraMod(carCenterVo.getBrand(),carCenterVo.getModel());
                carCenterVo.setLogoImg(logoImg);
            }
        }else{
            ordCarCenVos = new ArrayList<OrderCarCenterVo>();
        }
        return ordCarCenVos;
    }
}
