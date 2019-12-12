package com.cjyc.salesman.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.IOrderDao;
import com.cjyc.common.model.dto.salesman.order.SalesOrderDetailDto;
import com.cjyc.common.model.dto.salesman.order.SalesOrderQueryDto;
import com.cjyc.common.model.dto.web.order.CommitOrderDto;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.salesman.order.SalesOrderDetailVo;
import com.cjyc.common.model.vo.salesman.order.SalesOrderVo;
import com.cjyc.common.system.service.ICsOrderService;
import com.cjyc.salesman.api.service.IOrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OrderServiceImpl extends ServiceImpl<IOrderDao, Order> implements IOrderService {

    @Resource
    private IOrderDao orderDao;
    @Resource
    private ICsOrderService csOrderService;

    @Override
    public ResultVo<PageVo<SalesOrderVo>> findOrder(SalesOrderQueryDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<SalesOrderVo> orderVos = orderDao.findOrder(dto);
        PageInfo<SalesOrderVo> pageInfo = new PageInfo(orderVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<SalesOrderDetailVo> findOrderDetail(SalesOrderDetailDto dto) {

        return null;
    }

}