package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.IOrderCarDao;
import com.cjyc.common.model.dao.IOrderDao;
import com.cjyc.common.model.dao.IStoreDao;
import com.cjyc.common.model.dto.web.order.OrderCarLineWaitDispatchCountListDto;
import com.cjyc.common.model.dto.web.order.OrderCarWaitDispatchListDto;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.enums.SendNoTypeEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ListVo;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.order.OrderCarWaitDispatchVo;
import com.cjyc.web.api.dto.OrderCommitDto;
import com.cjyc.web.api.service.IOrderService;
import com.cjyc.web.api.service.ISendNoService;
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
    private IOrderCarDao iOrderCarDao;
    @Resource
    private ISendNoService sendNoService;
    @Resource
    private IStoreDao storeDao;

    @Override
    public ResultVo<ListVo<Map<String, Object>>> waitDispatchCarCountList() {
        List<Map<String, Object>> list = orderCarDao.countListWaitDispatchCar();
        //查询统计
        Map<String, Object> countInfo = null;
        if (list != null || !list.isEmpty()) {
            countInfo = orderCarDao.countTotalWaitDispatchCar();
        }
        return BaseResultUtil.success(list, countInfo);
    }

    @Override
    public ResultVo<PageVo<OrderCarWaitDispatchVo>> waitDispatchCarList(OrderCarWaitDispatchListDto paramsDto, List<Long> bizScope) {
        PageHelper.startPage(paramsDto.getCurrentPage(), paramsDto.getPageSize(), true);
        List<OrderCarWaitDispatchVo> list = orderCarDao.findWaitDispatchCarList(paramsDto, bizScope);
        PageInfo<OrderCarWaitDispatchVo> pageInfo = new PageInfo<>(list);
        if (paramsDto.getCurrentPage() > pageInfo.getPages()) {
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
    public ResultVo save(OrderCommitDto paramsDto) {

        Order order = new Order();
        BeanUtils.copyProperties(paramsDto, order);
        //发号
        order.setNo(sendNoService.getNo(SendNoTypeEnum.ORDER));
        order.setState(paramsDto.getState());
        order.setSource(paramsDto.getClientId());
        order.setCreateTime(System.currentTimeMillis());
<<<<<<< HEAD
        orderDao.insert(order);

        for(){

=======
        order.setCreateUserName(orderDto.getSalesmanName());
        order.setCreateUserId(orderDto.getSalesmanId());
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
>>>>>>> 6ed3d18cad2d9f5ccc05abc3121de7266d9974f7
        }
/*        order.setCarNum();
        order.setPickFee();
        order.setTrunkFee();
        order.setBackFee();
        order.setInsuranceFee();
        order.setDepositFee();
        order.setAgencyFee();*/


        return null;

    }

    @Override
    public ResultVo update(OrderCommitDto orderCommitDto) {
        return null;
    }

}
