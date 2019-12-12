package com.cjyc.salesman.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.ICarSeriesDao;
import com.cjyc.common.model.dao.IOrderCarDao;
import com.cjyc.common.model.dao.IOrderDao;
import com.cjyc.common.model.dto.salesman.order.SalesOrderDetailDto;
import com.cjyc.common.model.dto.salesman.order.SalesOrderQueryDto;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.salesman.order.SalesOrderCarVo;
import com.cjyc.common.model.vo.salesman.order.SalesOrderDetailVo;
import com.cjyc.common.model.vo.salesman.order.SalesOrderVo;
import com.cjyc.common.system.config.LogoImgProperty;
import com.cjyc.salesman.api.service.IOrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl extends ServiceImpl<IOrderDao, Order> implements IOrderService {

    @Resource
    private IOrderDao orderDao;
    @Resource
    private IOrderCarDao orderCarDao;
    @Resource
    private ICarSeriesDao carSeriesDao;

    @Override
    public ResultVo<PageVo<SalesOrderVo>> findOrder(SalesOrderQueryDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<SalesOrderVo> orderVos = orderDao.findOrder(dto);
        PageInfo<SalesOrderVo> pageInfo = new PageInfo(orderVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<SalesOrderDetailVo> findOrderDetail(SalesOrderDetailDto dto) {
        String logoImg = LogoImgProperty.logoImg;
        SalesOrderDetailVo detailVo = new SalesOrderDetailVo();
        List<SalesOrderCarVo> carVoList = new ArrayList<>();
        Order order = orderDao.selectById(dto.getOrderId());
        detailVo.setOrderNo(order.getNo());
        BeanUtils.copyProperties(order,detailVo);
        List<OrderCar> orderCars = orderCarDao.selectList(new QueryWrapper<OrderCar>().lambda().eq(OrderCar::getOrderId, order.getId()));
        if(!CollectionUtils.isEmpty(orderCars)){
            for(OrderCar orderCar : orderCars){
                logoImg += carSeriesDao.getLogoImgByBraMod(orderCar.getBrand(), orderCar.getModel());
                SalesOrderCarVo carVo = new SalesOrderCarVo();
                carVo.setLogoImg(logoImg);
                BeanUtils.copyProperties(orderCar,carVo);
                carVoList.add(carVo);
            }
        }
        detailVo.setCarVoList(carVoList);
        return BaseResultUtil.success(detailVo);
    }
}