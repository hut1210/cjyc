package com.cjyc.salesman.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.IOrderDao;
import com.cjyc.common.model.dto.salesman.PageSalesDto;
import com.cjyc.common.model.dto.salesman.order.AppOrderQueryDto;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.salesman.order.AppOrderVo;
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

    @Override
    public ResultVo<PageVo<AppOrderVo>> findDraftOrder(PageSalesDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<AppOrderVo> draftOrderVos = orderDao.findDraftOrder(dto);
        PageInfo<AppOrderVo> pageInfo = new PageInfo(draftOrderVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<PageVo<AppOrderVo>> findOrder(AppOrderQueryDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<AppOrderVo> orderVos = orderDao.findOrder(dto);
        PageInfo<AppOrderVo> pageInfo = new PageInfo(orderVos);
        return BaseResultUtil.success(pageInfo);
    }
}