package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.constant.NoConstant;
import com.cjyc.common.model.dao.IIncrementerDao;
import com.cjyc.common.model.dao.IOrderCarDao;
import com.cjyc.common.model.dao.IOrderDao;
import com.cjyc.common.model.dto.web.order.OrderCarLineWaitDispatchCountListDto;
import com.cjyc.common.model.dto.web.order.OrderCarWaitDispatchListDto;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ListVo;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.order.OrderCarWaitDispatchVo;
import com.cjyc.web.api.dto.OrderCarDto;
import com.cjyc.web.api.dto.OrderDto;
import com.cjyc.web.api.service.IBizScopeService;
import com.cjyc.web.api.service.IOrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表(客户下单) 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-09
 */
@Service
public class OrderServiceImpl extends ServiceImpl<IOrderDao, Order> implements IOrderService {

    @Resource
    private IOrderDao orderDao;
    @Resource
    private IOrderCarDao orderCarDao;
    @Resource
    IIncrementerDao incrementerDao;
    @Resource
    IOrderCarDao iOrderCarDao;

    @Override
    public ResultVo<ListVo<Map<String, Object>>> waitDispatchCarCountList() {
        List<Map<String, Object>> list = orderCarDao.countListWaitDispatchCar();
        //查询统计
        Map<String, Object> countInfo = null;
        if(list != null || !list.isEmpty()){
            countInfo = orderCarDao.countTotalWaitDispatchCar();
        }
        return BaseResultUtil.success(list, countInfo);
    }


    @Override
    public ResultVo<PageVo<OrderCarWaitDispatchVo>> waitDispatchCarList(OrderCarWaitDispatchListDto paramsDto, List<Long> bizScope) {
        PageHelper.startPage(paramsDto.getCurrentPage(), paramsDto.getPageSize(), true);
        List<OrderCarWaitDispatchVo> list = orderCarDao.findWaitDispatchCarList(paramsDto, bizScope);
        PageInfo<OrderCarWaitDispatchVo> pageInfo = new PageInfo<>(list);
        if(paramsDto.getCurrentPage() > pageInfo.getPages()){
            pageInfo.setList(null);
        }
        return BaseResultUtil.success(pageInfo);
    }

    /**
     * 按线路统计待调度车辆（统计列表）
     *
     * @author JPG
     * @since 2019/10/16 10:04
     */
    @Override
    public ResultVo<ListVo<Map<String, Object>>> lineWaitDispatchCarCountList(OrderCarLineWaitDispatchCountListDto paramsDto, List<Long> bizScopeStoreIds) {
        //查询列表
        List<Map<String, Object>> list = orderCarDao.findlineWaitDispatchCarCountList(paramsDto, bizScopeStoreIds);

        //统计结果
        return null;
    }

    @Override
    public boolean commitOrder(OrderDto orderDto) {
        int saveType = orderDto.getSaveType(); //0-预订单 1-下单
        int customerType = orderDto.getCustomerType();//1-c端 2-大客户 3-合伙人

        Order order = new Order();
        BeanUtils.copyProperties(orderDto,order);

        //获取订单业务编号
        String orderNo = incrementerDao.getIncrementer(NoConstant.ORDER_PREFIX);
        order.setNo(orderNo);

        //草稿
        if(saveType==0){
            order.setState(0);//待提交
            //正式下单
        }else if(saveType==1){
            order.setState(1);//待分配
        }

        order.setSource(21);//订单来源：1用户app，2用户小程序，12业务员app，12业务员小程序，21韵车后台
        order.setCarNum(orderDto.getOrderCarDtoList().size());
        order.setCreateTime(System.currentTimeMillis());
        order.setCreateUserName(orderDto.getSalesmanName());
        order.setCreateUserId(1L);//创建人类型：0客户，1业务员
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
                orderCar.setWlPayState(0);//应收状态：0未支付，1已支付
                iOrderCarDao.insert(orderCar);
            }
        }

        return count > 0 ? true : false;
    }
}
