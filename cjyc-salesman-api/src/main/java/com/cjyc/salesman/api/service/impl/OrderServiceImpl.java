package com.cjyc.salesman.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.ICarSeriesDao;
import com.cjyc.common.model.dao.ICustomerDao;
import com.cjyc.common.model.dao.IOrderCarDao;
import com.cjyc.common.model.dao.IOrderDao;
import com.cjyc.common.model.dto.salesman.order.SalesOrderDetailDto;
import com.cjyc.common.model.dto.salesman.order.SalesOrderQueryDto;
import com.cjyc.common.model.dto.web.order.CommitOrderDto;
import com.cjyc.common.model.dto.web.order.SimpleCommitOrderDto;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.common.model.entity.defined.BizScope;
import com.cjyc.common.model.enums.BizScopeEnum;
import com.cjyc.common.model.enums.customer.CustomerTypeEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.TimeStampUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.salesman.order.SalesOrderCarVo;
import com.cjyc.common.model.vo.salesman.order.SalesOrderDetailVo;
import com.cjyc.common.model.vo.salesman.order.SalesOrderVo;
import com.cjyc.common.system.config.LogoImgProperty;
import com.cjyc.common.system.service.ICsStoreService;
import com.cjyc.common.system.service.sys.ICsSysService;
import com.cjyc.salesman.api.service.IOrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class OrderServiceImpl extends ServiceImpl<IOrderDao, Order> implements IOrderService {

    @Resource
    private IOrderDao orderDao;
    @Resource
    private IOrderCarDao orderCarDao;
    @Resource
    private ICarSeriesDao carSeriesDao;
    @Resource
    private ICustomerDao customerDao;
    @Resource
    private ICsSysService csSysService;
    @Resource
    private ICsStoreService csStoreService;

    @Override
    public ResultVo<PageVo<SalesOrderVo>> findOrder(SalesOrderQueryDto dto) {
        // 根据登录ID查询当前业务员所在业务中心ID
        BizScope bizScope = csSysService.getBizScopeByLoginId(dto.getLoginId(), true);
        // 判断当前登录人是否有权限访问
        if (BizScopeEnum.NONE.code == bizScope.getCode()) {
            return BaseResultUtil.fail("您没有访问权限!");
        }
        // 获取业务中心ID
        dto.setStoreIds(csStoreService.getStoreIds(bizScope));
        if(dto.getCreateEndTime() != null){
            dto.setCreateEndTime(TimeStampUtil.addDays(dto.getCreateEndTime(),1));
        }
        if(dto.getExpectEndTime() != null){
            dto.setExpectEndTime(TimeStampUtil.addDays(dto.getExpectEndTime(),1));
        }
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<SalesOrderVo> orderVos = orderDao.findOrder(dto);
        PageInfo<SalesOrderVo> pageInfo = new PageInfo(orderVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<SalesOrderDetailVo> findOrderDetail(SalesOrderDetailDto dto) {
        SalesOrderDetailVo detailVo = new SalesOrderDetailVo();
        List<SalesOrderCarVo> carVoList = Lists.newArrayList();
        Order order = orderDao.selectById(dto.getOrderId());
        if(order == null){
            return BaseResultUtil.success(detailVo);
        }
        if(order.getCustomerType() == CustomerTypeEnum.ENTERPRISE.code){
            //大客户
            Customer customer = customerDao.selectById(order.getCustomerId());
            BeanUtils.copyProperties(customer,detailVo);
        }
        detailVo.setOrderId(order.getId());
        detailVo.setOrderNo(order.getNo());
        BeanUtils.copyProperties(order,detailVo);
        List<OrderCar> orderCars = orderCarDao.selectList(new QueryWrapper<OrderCar>().lambda().eq(OrderCar::getOrderId, order.getId()));
        String logoImg = LogoImgProperty.logoImg;
        if(!CollectionUtils.isEmpty(orderCars)){
            for(OrderCar orderCar : orderCars){
                logoImg += carSeriesDao.getLogoImgByBraMod(orderCar.getBrand(), orderCar.getModel());
                SalesOrderCarVo carVo = new SalesOrderCarVo();
                carVo.setOrderCarId(orderCar.getId());
                carVo.setLogoImg(logoImg);
                BeanUtils.copyProperties(orderCar,carVo);
                carVoList.add(carVo);
            }
        }
        detailVo.setCarVoList(carVoList);
        return BaseResultUtil.success(detailVo);
    }

}