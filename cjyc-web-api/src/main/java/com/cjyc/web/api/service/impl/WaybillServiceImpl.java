package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjkj.common.redis.lock.RedisDistributedLock;
import com.cjkj.common.redis.template.StringRedisUtil;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.web.waybill.*;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.*;
import com.cjyc.common.model.enums.order.OrderCarStateEnum;
import com.cjyc.common.model.enums.order.OrderStateEnum;
import com.cjyc.common.model.enums.task.TaskCarStateEnum;
import com.cjyc.common.model.enums.task.TaskStateEnum;
import com.cjyc.common.model.enums.waybill.DispatchTypeEnum;
import com.cjyc.common.model.enums.waybill.WaybillCarStateEnum;
import com.cjyc.common.model.enums.waybill.WaybillStateEnum;
import com.cjyc.common.model.enums.waybill.WaybillTypeEnum;
import com.cjyc.common.model.keys.RedisKeys;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.BaseListTipVo;
import com.cjyc.common.model.vo.BaseTipVo;
import com.cjyc.common.model.vo.ListVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.service.ISendNoService;
import com.cjyc.web.api.service.IWaybillService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 运单表(业务员调度单) 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-17
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class WaybillServiceImpl extends ServiceImpl<IWaybillDao, Waybill> implements IWaybillService {

    @Resource
    private IWaybillDao waybillDao;
    @Resource
    private IOrderCarDao orderCarDao;
    @Resource
    private IOrderDao orderDao;
    @Resource
    private StringRedisUtil redisUtil;
    @Resource
    private RedisDistributedLock redisLock;
    @Resource
    private ISendNoService sendNoService;
    @Resource
    private ICarrierDao carrierDao;
    @Resource
    private IAdminDao adminDao;
    @Resource
    private IWaybillCarDao waybillCarDao;
    @Resource
    private IDriverDao driverDao;
    @Resource
    private ITaskDao taskDao;
    @Resource
    private ITaskCarDao taskCarDao;

    /**
     * 提车调度
     * @param paramsDto
     * @return
     */
    @Override
    public ResultVo<ListVo<BaseTipVo>> pickDispatch(WaybillPickDispatchListDto paramsDto) {
        long failNum = 0L;
        List<BaseTipVo> failList = new ArrayList<>();
        Long currentTime = System.currentTimeMillis();

        Long userId = paramsDto.getUserId();
        //验证用户
        Admin admin = adminDao.findByUserId(userId);
        if(admin == null || admin.getState() != AdminStateEnum.CHECKED.code){
            return BaseResultUtil.fail("当前业务员，没有操作权限");
        }
        // TODO 验证用户角色

        List<WaybillPickDispatchDto> list = paramsDto.getList();
        for (WaybillPickDispatchDto dto : list) {

            String orderCarNo = dto.getOrderCarNo();
            if (dto == null) {
                continue;
            }

            Long orderCarId = dto.getOrderCarId();
            Long carrierId = dto.getCarrierId();
            //加锁
            String lockKey = RedisKeys.getDispatchLock(orderCarId);
            try {
                if(!redisLock.lock(lockKey)){
                    failNum++;
                    failList.add(new BaseTipVo(orderCarNo, "当前车辆，其他人正在调度"));
                    continue;
                }
                //验证订单车辆状态
                OrderCar orderCar = orderCarDao.selectById(orderCarId);
                if (orderCar == null) {
                    failNum++;
                    failList.add(new BaseTipVo(orderCarNo, "订单车辆不存在"));
                    continue;
                }
                if (orderCar.getState() == null
                        || orderCar.getState() <= OrderCarStateEnum.WAIT_PICK_DISPATCH.code) {
                    failNum++;
                    failList.add(new BaseTipVo(orderCarNo, "当前车辆状态，无法提车调度"));
                    continue;
                }
                //验证订单状态
                Order order = orderDao.selectById(orderCar.getId());
                if (order == null) {
                    failNum++;
                    failList.add(new BaseTipVo(orderCarNo, "订单车辆不存在"));
                    continue;
                }

                if (order.getState() == null
                        || order.getState() < OrderStateEnum.CHECKED.code
                        || order.getState() > OrderStateEnum.FINISHED.code) {
                    failNum++;
                    failList.add(new BaseTipVo(orderCarNo, "当前车辆订单状态，无法提车调度"));
                    continue;
                }

                //验证承运商是否可以运营
                Carrier carrier = carrierDao.selectById(carrierId);
                if(carrier.getBusinessState() == null || carrier.getBusinessState() != 0){
                    failNum++;
                    failList.add(new BaseTipVo(orderCarNo, "当前司机，停运中"));
                    continue;
                }


                //验证司机信息
                Driver driver = null;
                if(carrier.getDriverNum() <= 1){
                    driver = driverDao.findTopByCarrierId(carrierId);
                    if(driver == null){
                        failNum++;
                        failList.add(new BaseTipVo(orderCarNo, "当前承运商，没有运营中的司机"));
                        continue;
                    }
                }
                //1、添加运单信息
                Waybill waybill = new Waybill();
                waybill.setNo(sendNoService.getNo(SendNoTypeEnum.ORDER));
                waybill.setType(WaybillTypeEnum.PICK.code);
                if(userId.equals(carrierId)){
                    waybill.setDispatchType(DispatchTypeEnum.SELF.code);
                }else{
                    waybill.setDispatchType(DispatchTypeEnum.MANUAL.code);
                }
                //waybill.setGuideLine(null);
                //waybill.setRecommendLine(null);
                waybill.setCarrierId(carrierId);
                waybill.setCarNum(1);
                waybill.setState(WaybillStateEnum.WAIT_ALLOT_CONFIRM.code);
                //TODO 提送车费用逻辑，调度时不允许修改提送车费用，需要到订单中修改提送车费用，多则返还，少则后补
                waybill.setFreightFee(orderCar.getPickFee());
                waybill.setCreateTime(currentTime);
                waybill.setCreateUser(admin.getName());
                waybill.setCreateUserId(admin.getId());
                int row = waybillDao.insert(waybill);

                //2、添加运单车辆信息
                WaybillCar waybillCar = new WaybillCar();
                waybillCar.setWaybillId(waybill.getId());
                waybillCar.setOrderCarId(orderCar.getId());
                //waybillCar.setFreightFee()
                waybillCar.setStartAddress(dto.getStartAddress());
                waybillCar.setEndAddress(dto.getEndAddress());
                //TODO 如何知道车辆当前所在城市
                waybillCar.setState(WaybillCarStateEnum.APPOINTED.code);
                waybillCar.setExpectPickTime(LocalDateTimeUtil.getMillisByLDT(LocalDateTimeUtil.getDayStartByLDT(LocalDateTime.now())));
                waybillCar.setPickType(dto.getPickType());
                waybillCar.setPickContactName(dto.getPickContactName());
                waybillCar.setPickContactPhone(dto.getPickContactPhone());
                int row2 = waybillCarDao.insert(waybillCar);

                //3、承运商有且仅有一个司机
                if(carrier.getDriverNum() == 1){
                    //只有一个司机添加任务信息
                    Task task = new Task();
                    task.setNo(sendNoService.getNo(SendNoTypeEnum.TASK));
                    task.setWaybillId(waybill.getId());
                    task.setCarNum(1);
                    task.setState(TaskStateEnum.WAIT_ALLOT_CONFIRM.code);
                    task.setDriverId(driver.getId());
                    task.setDriverName(driver.getName());
                    task.setLoadCarNum(1);
                    task.setCreateTime(currentTime);
                    task.setCreateUser(admin.getName());
                    task.setCreateUserId(admin.getCreateUserId());
                    taskDao.insert(task);

                    //4、插入任务车辆表
                    TaskCar taskCar = new TaskCar();
                    taskCar.setTaskId(task.getId());
                    taskCar.setWaybillId(waybill.getId());
                    taskCar.setWaybillCarId(waybillCar.getId());
                    taskCar.setOrderCarId(orderCar.getId());
                    taskCar.setState(TaskCarStateEnum.WAIT_LOAD.code);
                    taskCar.setFreightFee(orderCar.getPickFee());
                    taskCar.setCreateTime(currentTime);

                    taskCarDao.insert(taskCar);
                }

            } finally {
                redisUtil.del(lockKey);
            }

        }
        return BaseResultUtil.success(failList, failNum);
    }

    /**
     * 干线调度
     *
     * @param paramsDto
     * @author JPG
     * @since 2019/10/17 9:16
     */
    @Override
    public ResultVo<ListVo<BaseListTipVo>> trunkDispatch(WaybillTrunkDispatchListListDto paramsDto) {

        //返回内容
        long failNum = 0L;
        List<BaseListTipVo> failList = new ArrayList<>();
        Long currentTime = System.currentTimeMillis();
        Long userId = paramsDto.getUserId();
        //验证用户
        Admin admin = adminDao.findByUserId(userId);
        if(admin == null || admin.getState() != AdminStateEnum.CHECKED.code){
            return BaseResultUtil.fail("当前业务员，没有操作权限");
        }
        // TODO 验证用户角色


        List<WaybillTrunkDispatchListDto> list = paramsDto.getList();
        //运单循环
        for (WaybillTrunkDispatchListDto ListDto : list) {

            if(ListDto == null){
                continue;
            }
            List<WaybillTrunkDispatchDto> subList = ListDto.getList();
            if(subList == null || subList.isEmpty()){
                continue;
            }
            //子返回内容
            long carFailNum = 0L;
            BaseListTipVo baseListTipVo = new BaseListTipVo();
/*

            //车辆循环
            for (WaybillTrunkDispatchDto dto : subList) {
                String orderCarNo = dto.getOrderCarNo();
                if (dto == null) {
                    failNum++;
                    failList.add(new BaseTipVo(orderCarNo, "空信息"));
                    continue;
                }

                Long orderCarId = dto.getOrderCarId();
                Long carrierId = dto.getCarrierId();
                //加锁
                String lockKey = RedisKeys.getDispatchLock(orderCarId);
                try {
                    if(!redisLock.lock(lockKey)){
                        failNum++;
                        failList.add(new BaseTipVo(orderCarNo, "当前车辆，其他人正在调度"));
                        continue;
                    }
                    //验证订单车辆状态
                    OrderCar orderCar = orderCarDao.selectById(orderCarId);
                    if (orderCar == null) {
                        failNum++;
                        failList.add(new BaseTipVo(orderCarNo, "订单车辆不存在"));
                        continue;
                    }
                    if (orderCar.getState() == null
                            || orderCar.getState() <= OrderCarStateEnum.WAIT_PICK_DISPATCH.code) {
                        failNum++;
                        failList.add(new BaseTipVo(orderCarNo, "当前车辆状态，无法提车调度"));
                        continue;
                    }
                    //验证订单状态
                    Order order = orderDao.selectById(orderCar.getId());
                    if (order == null) {
                        failNum++;
                        failList.add(new BaseTipVo(orderCarNo, "订单车辆不存在"));
                        continue;
                    }

                    if (order.getState() == null
                            || order.getState() < OrderStateEnum.CHECKED.code
                            || order.getState() > OrderStateEnum.FINISHED.code) {
                        failNum++;
                        failList.add(new BaseTipVo(orderCarNo, "当前车辆订单状态，无法提车调度"));
                        continue;
                    }

                    //验证承运商是否可以运营
                    Carrier carrier = carrierDao.selectById(carrierId);
                    if(carrier.getBusinessState() == null || carrier.getBusinessState() != 0){
                        failNum++;
                        failList.add(new BaseTipVo(orderCarNo, "当前司机，停运中"));
                        continue;
                    }


                    //验证司机信息
                    Driver driver = null;
                    if(carrier.getDriverNum() <= 1){
                        driver = driverDao.findTopByCarrierId(carrierId);
                        if(driver == null){
                            failNum++;
                            failList.add(new BaseTipVo(orderCarNo, "当前承运商，没有运营中的司机"));
                            continue;
                        }
                    }
                    //1、添加运单信息
                    Waybill waybill = new Waybill();
                    waybill.setNo(sendNoService.getNo(SendNoTypeEnum.ORDER));
                    waybill.setType(WaybillTypeEnum.PICK.code);
                    if(userId.equals(carrierId)){
                        waybill.setDispatchType(DispatchTypeEnum.SELF.code);
                    }else{
                        waybill.setDispatchType(DispatchTypeEnum.MANUAL.code);
                    }
                    //waybill.setGuideLine(null);
                    //waybill.setRecommendLine(null);
                    waybill.setCarrierId(carrierId);
                    waybill.setCarNum(1);
                    waybill.setState(WaybillStateEnum.WAIT_ALLOT_CONFIRM.code);
                    //TODO 提送车费用逻辑，调度时不允许修改提送车费用，需要到订单中修改提送车费用，多则返还，少则后补
                    waybill.setFreightFee(orderCar.getPickFee());
                    waybill.setCreateTime(currentTime);
                    waybill.setCreateUser(admin.getName());
                    waybill.setCreateUserId(admin.getId());
                    int row = waybillDao.insert(waybill);

                    //2、添加运单车辆信息
                    WaybillCar waybillCar = new WaybillCar();
                    waybillCar.setWaybillId(waybill.getId());
                    waybillCar.setOrderCarId(orderCar.getId());
                    //waybillCar.setFreightFee()
                    waybillCar.setStartAddress(dto.getStartAddress());
                    waybillCar.setEndAddress(dto.getEndAddress());
                    //TODO 如何知道车辆当前所在城市
                    waybillCar.setState(WaybillCarStateEnum.APPOINTED.code);
                    waybillCar.setExpectPickTime(LocalDateTimeUtil.getMillisByLDT(LocalDateTimeUtil.getDayStartByLDT(LocalDateTime.now())));
                    waybillCar.setPickType(dto.getPickType());
                    waybillCar.setPickContactName(dto.getPickContactName());
                    waybillCar.setPickContactPhone(dto.getPickContactPhone());
                    int row2 = waybillCarDao.insert(waybillCar);

                    //3、承运商有且仅有一个司机
                    if(carrier.getDriverNum() == 1){
                        //只有一个司机添加任务信息
                        Task task = new Task();
                        task.setNo(sendNoService.getNo(SendNoTypeEnum.TASK));
                        task.setWaybillId(waybill.getId());
                        task.setCarNum(1);
                        task.setState(TaskStateEnum.WAIT_ALLOT_CONFIRM.code);
                        task.setDriverId(driver.getId());
                        task.setDriverName(driver.getName());
                        task.setLoadCarNum(1);
                        task.setCreateTime(currentTime);
                        task.setCreateUser(admin.getName());
                        task.setCreateUserId(admin.getCreateUserId());
                        taskDao.insert(task);

                        //4、插入任务车辆表
                        TaskCar taskCar = new TaskCar();
                        taskCar.setTaskId(task.getId());
                        taskCar.setWaybillId(waybill.getId());
                        taskCar.setWaybillCarId(waybillCar.getId());
                        taskCar.setOrderCarId(orderCar.getId());
                        taskCar.setState(TaskCarStateEnum.WAIT_LOAD.code);
                        taskCar.setFreightFee(orderCar.getPickFee());
                        taskCar.setCreateTime(currentTime);

                        taskCarDao.insert(taskCar);
                    }

                } finally {
                    redisUtil.del(lockKey);
                }
            }
*/


        }
        return BaseResultUtil.success(failList, failNum);
    }

}
