package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjkj.common.redis.lock.RedisDistributedLock;
import com.cjkj.common.redis.template.StringRedisUtil;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.web.waybill.*;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.*;
import com.cjyc.common.model.enums.order.OrderCarLocalStateEnum;
import com.cjyc.common.model.enums.order.OrderCarStateEnum;
import com.cjyc.common.model.enums.order.OrderCarTrunkStateEnum;
import com.cjyc.common.model.enums.order.OrderStateEnum;
import com.cjyc.common.model.enums.task.TaskStateEnum;
import com.cjyc.common.model.enums.waybill.WaybillSourceEnum;
import com.cjyc.common.model.enums.waybill.WaybillCarStateEnum;
import com.cjyc.common.model.enums.waybill.WaybillStateEnum;
import com.cjyc.common.model.enums.waybill.WaybillTypeEnum;
import com.cjyc.common.model.keys.RedisKeys;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.waybill.HistoryListWaybillVo;
import com.cjyc.web.api.exception.ParameterException;
import com.cjyc.web.api.exception.ServerException;
import com.cjyc.web.api.service.ISendNoService;
import com.cjyc.web.api.service.IWaybillService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
     * 提送车调度
     *
     * @param paramsDto 参数
     */
    @Override
    public ResultVo localDispatch(LocalDispatchListWaybillDto paramsDto) {
        Long currentMillisTime = System.currentTimeMillis();
        Set<String> lockSet = new HashSet<>();
        try {
            List<LocalDispatchWaybillDto> list = paramsDto.getList();
            for (int i = 0; i < list.size(); i++) {
                int idx = i + 1;
                LocalDispatchWaybillDto dto = list.get(i);
                if (dto == null) {
                    continue;
                }
                String orderCarNo = dto.getOrderCarNo();
                Long orderCarId = dto.getOrderCarId();
                Long carrierId = dto.getCarrierId();
                //加锁
                String lockKey = RedisKeys.getDispatchLock(orderCarNo);

                if (!redisLock.lock(lockKey)) {
                    throw new ParameterException("序号为{0}运单，编号为{1}的车辆，其他人正在调度", idx, orderCarNo);
                }
                lockSet.add(lockKey);
                //验证订单车辆状态
                OrderCar orderCar = orderCarDao.selectById(orderCarId);
                if (orderCar == null) {
                    throw new ParameterException("序号为{0}运单,编号为{1}的车辆，车辆所属订单车辆不存在", idx, orderCarNo);
                }
                if (orderCar.getState() == null
                        || (paramsDto.getType() == WaybillTypeEnum.PICK.code && orderCar.getPickState() <= OrderCarLocalStateEnum.WAIT_DISPATCH.code)
                        || (paramsDto.getType() == WaybillTypeEnum.BACK.code && orderCar.getBackState() <= OrderCarLocalStateEnum.WAIT_DISPATCH.code)) {
                    throw new ParameterException("序号为{0}运单,编号为{1}的车辆，当前车辆状态不能提车/配送调度", idx, orderCarNo);
                }

                //验证订单状态
                Order order = orderDao.selectById(orderCar.getId());
                if (order == null) {
                    throw new ParameterException("序号为{0}运单,编号为{1}的车辆，当前车辆不存在", idx, orderCarNo);
                }
                if (order.getState() == null
                        || order.getState() < OrderStateEnum.CHECKED.code
                        || order.getState() > OrderStateEnum.FINISHED.code) {
                    throw new ParameterException("序号为{0}运单,编号为{1}的车辆，车辆所属订单状态不能提车/配送调度", idx, orderCarNo);
                }

                //验证承运商是否可以运营
                Carrier carrier = carrierDao.selectById(carrierId);
                if (carrier.getBusinessState() == null || carrier.getBusinessState() != 0) {
                    throw new ParameterException("序号为{0}运单,编号为{1}的车辆，所选承运商停运中", idx, orderCarNo);
                }

                //验证司机信息
                Driver driver = null;
                if (carrier.getDriverNum() <= 1) {
                    driver = driverDao.findTopByCarrierId(carrierId);
                    if (driver == null) {
                        throw new ParameterException("序号为{0}运单,编号为{1}的车辆，所选承运商没有运营中的司机", idx, orderCarNo);
                    }
                }
                /**1、添加运单信息*/
                Waybill waybill = new Waybill();
                waybill.setNo(sendNoService.getNo(SendNoTypeEnum.ORDER));
                waybill.setType(WaybillTypeEnum.PICK.code);
                if (paramsDto.getUserId().equals(carrierId)) {
                    waybill.setSource(WaybillSourceEnum.SELF.code);
                } else {
                    waybill.setSource(WaybillSourceEnum.MANUAL.code);
                }
                waybill.setCarrierId(carrierId);
                waybill.setCarNum(1);
                waybill.setState(WaybillStateEnum.WAIT_ALLOT_CONFIRM.code);
                //提送车费用逻辑，调度时不允许修改提送车费用，需要到订单中修改提送车费用，多则返还，少则后补
                waybill.setFreightFee(orderCar.getPickFee());
                waybill.setRemark(dto.getRemark());
                waybill.setCreateTime(currentMillisTime);
                waybill.setCreateUser(paramsDto.getUserName());
                waybill.setCreateUserId(paramsDto.getUserId());
                waybillDao.insert(waybill);

                /**2、添加运单车辆信息*/
                WaybillCar waybillCar = new WaybillCar();
                waybillCar.setWaybillId(waybill.getId());
                waybillCar.setWaybillNo(waybill.getNo());
                waybillCar.setOrderCarId(orderCar.getId());
                if (paramsDto.getType() == WaybillTypeEnum.PICK.code) {
                    waybillCar.setFreightFee(orderCar.getPickFee());
                } else if (paramsDto.getType() == WaybillTypeEnum.BACK.code) {
                    waybillCar.setFreightFee(orderCar.getPickFee());
                } else {
                    throw new ParameterException("序号为{0}运单,编号为{1}的车辆，取车方式（上门/自送取）不能识别", idx, orderCarNo);
                }
                //地址赋值
                copyBeanAddressField(order, waybillCar);
                waybillCar.setState(WaybillCarStateEnum.WAIT_ALLOT.code);
                waybillCar.setExpectStartTime(dto.getExpectPickTime());
                //TODO 计算预计到达时间，计算线路是否存在
                waybillCar.setTakeType(dto.getTakeType());
                waybillCar.setLoadLinkName(dto.getLoadLinkName());
                waybillCar.setLoadLinkUserId(dto.getUnloadLinkUserId());
                waybillCar.setLoadLinkPhone(dto.getLoadLinkPhone());
                waybillCar.setUnloadLinkName(dto.getUnloadLinkName());
                waybillCar.setUnloadLinkUserId(dto.getLoadLinkUserId());
                waybillCar.setUnloadLinkPhone(dto.getUnloadLinkPhone());
                waybillCar.setCreateTime(currentMillisTime);
                waybillCarDao.insert(waybillCar);

                //3、承运商有且仅有一个司机
                if (carrier.getDriverNum() == 1) {
                    //只有一个司机添加任务信息
                    Task task = new Task();
                    task.setNo(sendNoService.getNo(SendNoTypeEnum.TASK));
                    task.setWaybillId(waybill.getId());
                    task.setCarNum(1);
                    task.setState(TaskStateEnum.WAIT_ALLOT_CONFIRM.code);
                    task.setDriverId(driver.getId());
                    task.setDriverName(driver.getName());
                    task.setCreateTime(currentMillisTime);
                    task.setCreateUser(paramsDto.getUserName());
                    task.setCreateUserId(paramsDto.getUserId());
                    taskDao.insert(task);

                    //4、插入任务车辆关联表
                    TaskCar taskCar = new TaskCar();
                    taskCar.setTaskId(task.getId());
                    taskCar.setWaybillCarId(waybillCar.getId());
                    taskCarDao.insert(taskCar);

                    //5、更新订单车辆状态
                    if (paramsDto.getType() == WaybillTypeEnum.PICK.code) {
                        orderCarDao.updatePickStateById(OrderCarLocalStateEnum.WAIT_LOAD.code, orderCarId);
                    }
                    if (paramsDto.getType() == WaybillTypeEnum.BACK.code) {
                        orderCarDao.updateBackStateById(OrderCarLocalStateEnum.WAIT_LOAD.code, orderCarId);
                    }

                }

            }
        } finally {
            for (String key : lockSet) {
                redisLock.releaseLock(key);
            }
        }
        return BaseResultUtil.success();
    }

    private void copyBeanAddressField(Order order, WaybillCar waybillCar) {
        waybillCar.setStartProvince(order.getStartProvince());
        waybillCar.setStartProvinceCode(order.getStartProvinceCode());
        waybillCar.setStartCity(order.getStartCity());
        waybillCar.setStartCityCode(order.getStartCityCode());
        waybillCar.setStartArea(order.getStartArea());
        waybillCar.setStartAreaCode(order.getStartAreaCode());
        waybillCar.setStartAddress(order.getStartAddress());
        waybillCar.setEndProvince(order.getEndProvince());
        waybillCar.setEndProvinceCode(order.getEndProvinceCode());
        waybillCar.setEndCity(order.getEndCity());
        waybillCar.setEndCityCode(order.getEndCityCode());
        waybillCar.setEndArea(order.getEndArea());
        waybillCar.setEndAreaCode(order.getEndAreaCode());
        waybillCar.setEndAddress(order.getEndAddress());
    }

    /**
     * 干线调度
     *
     * @param paramsDto 参数
     * @author JPG
     * @since 2019/10/17 9:16
     */
    @Override
    public ResultVo trunkDispatch(TrunkDispatchListShellWaybillDto paramsDto) {

        Long currentMillisTime = System.currentTimeMillis();
        Long userId = paramsDto.getUserId();
        Set<String> lockSet = new HashSet<>();
        //验证用户
        Admin admin = adminDao.findByUserId(userId);
        if (admin == null || admin.getState() != AdminStateEnum.CHECKED.code) {
            return BaseResultUtil.fail("当前业务员，不在职");
        }
        // TODO 验证用户角色
        try {
            List<TrunkDispatchListWaybillDto> list = paramsDto.getList();
            //多运单循环
            for (int i = 0; i < list.size(); i++) {
                TrunkDispatchListWaybillDto dtoList = list.get(i);
                if (dtoList == null) {
                    continue;
                }
                List<TrunkDispatchWaybillDto> subList = dtoList.getList();
                if (subList == null || subList.isEmpty()) {
                    continue;
                }
                Long carrierId = dtoList.getCarrierId();

                //标记位置用
                int idx = i + 1;
                //验证承运商是否可以运营
                Carrier carrier = carrierDao.selectById(carrierId);
                if (carrier.getBusinessState() == null || carrier.getBusinessState() != 0) {
                    throw new ParameterException("序号为{0}运单，所选承运商，停运中", idx);
                }
                //验证司机信息
                Driver driver = null;
                if (carrier.getDriverNum() <= 1) {
                    driver = driverDao.findTopByCarrierId(carrierId);
                    if (driver == null) {
                        throw new ParameterException("序号为{0}运单，所选承运商没有运营中的司机", idx);
                    }
                }
                //验证司机承载信息
                /**1、组装运单信息*/
                Waybill waybill = new Waybill();
                waybill.setNo(sendNoService.getNo(SendNoTypeEnum.ORDER));
                waybill.setType(WaybillTypeEnum.PICK.code);
                if (userId.equals(carrierId)) {
                    waybill.setSource(WaybillSourceEnum.SELF.code);
                } else {
                    waybill.setSource(WaybillSourceEnum.MANUAL.code);
                }
                waybill.setGuideLine(dtoList.getGuideLine());
                waybill.setRecommendLine(dtoList.getRecommendLine());
                waybill.setCarrierId(carrierId);
                waybill.setCarNum(1);
                waybill.setState(WaybillStateEnum.WAIT_ALLOT_CONFIRM.code);
                //TODO 提送车费用逻辑，调度时不允许修改提送车费用，需要到订单中修改提送车费用，多则返还，少则后补
                waybill.setFreightFee(dtoList.getFreightFee());
                waybill.setCreateTime(currentMillisTime);
                waybill.setCreateUser(admin.getName());
                waybill.setCreateUserId(admin.getId());
                waybillDao.insert(waybill);

                //承运商有且仅有一个司机
                Task task = null;
                if (carrier.getDriverNum() == 1) {
                    /**1+、写入任务表*/
                    task = new Task();
                    task.setNo(sendNoService.getNo(SendNoTypeEnum.TASK));
                    task.setWaybillId(waybill.getId());
                    task.setCarNum(1);
                    task.setState(TaskStateEnum.WAIT_ALLOT_CONFIRM.code);
                    task.setDriverId(driver.getId());
                    task.setDriverName(driver.getName());
                    task.setCreateTime(currentMillisTime);
                    task.setCreateUser(admin.getName());
                    task.setCreateUserId(admin.getCreateUserId());
                    taskDao.insert(task);
                }

                /**2、运单，车辆循环*/
                List<WaybillCar> waybillCarList = new ArrayList<>();
                List<TaskCar> taskCarList = new ArrayList<>();
                List<Long> orderCarIdList = new ArrayList<>();
                //合计运费
                BigDecimal sumFreightFee = BigDecimal.ZERO;
                for (TrunkDispatchWaybillDto dto : subList) {
                    if (dto == null) {
                        continue;
                    }
                    String orderCarNo = dto.getOrderCarNo();
                    Long orderCarId = dto.getOrderCarId();

                    //加锁
                    String lockKey = RedisKeys.getDispatchLock(orderCarNo);
                    if (!redisLock.lock(lockKey, 20000, 100, 300L)) {
                        throw new ParameterException("序号为{0}运单，编号为{1}的车辆，其他人正在调度", idx, orderCarNo);
                    }
                    lockSet.add(lockKey);

                    //验证订单车辆状态
                    OrderCar orderCar = orderCarDao.selectById(orderCarId);
                    if (orderCar == null) {
                        throw new ParameterException("序号为{0}运单，编号为{1}的车辆，不存在", idx, orderCarNo);
                    }
                    if (orderCar.getState() == null) {
                        throw new ParameterException("序号为{0}运单，编号为{1}的车辆，无法提车调度", idx, orderCarNo);
                    }
                    //验证订单状态
                    Order order = orderDao.selectById(orderCar.getId());
                    if (order == null) {
                        throw new ParameterException("序号为{0}运单，编号为{1}的车辆，所属订单车辆不存在", idx, orderCarNo);
                    }

                    if (order.getState() == null
                            || order.getState() < OrderStateEnum.CHECKED.code
                            || order.getState() > OrderStateEnum.FINISHED.code) {
                        throw new ParameterException("序号为{0}运单，编号为{1}的车辆，所属订单状态无法提车调度", idx, orderCarNo);
                    }

                    WaybillCar waybillCar = new WaybillCar();
                    waybillCar.setWaybillId(waybill.getId());
                    waybillCar.setWaybillNo(waybill.getNo());
                    waybillCar.setOrderCarId(orderCar.getId());
                    waybillCar.setFreightFee(dto.getFreightFee());
                    //地址赋值
                    copyBeanAddressField(order, waybillCar);
                    waybillCar.setStartAreaCode(dto.getStartAreaCode());
                    waybillCar.setEndAreaCode(dto.getEndAreaCode());
                    waybillCar.setState(WaybillCarStateEnum.WAIT_ALLOT.code);
                    waybillCar.setExpectStartTime(dto.getExpectPickTime());
                    waybillCar.setTakeType(2);
                    waybillCar.setLoadLinkName(dto.getLoadLinkName());
                    waybillCar.setLoadLinkUserId(dto.getUnloadLinkUserId());
                    waybillCar.setLoadLinkPhone(dto.getLoadLinkPhone());
                    waybillCar.setUnloadLinkName(dto.getUnloadLinkName());
                    waybillCar.setUnloadLinkUserId(dto.getLoadLinkUserId());
                    waybillCar.setUnloadLinkPhone(dto.getUnloadLinkPhone());
                    waybillCar.setCreateTime(currentMillisTime);

                    waybillCarList.add(waybillCar);
                    orderCarIdList.add(orderCar.getId());

                    sumFreightFee = sumFreightFee.add(dto.getFreightFee());
                    //承运商有且仅有一个司机
                    if (carrier.getDriverNum() == 1) {
                        /**2+、组装任务车辆表数据*/
                        TaskCar taskCar = new TaskCar();
                        taskCar.setTaskId(task.getId());
                        taskCar.setWaybillCarId(waybillCar.getId());
                        taskCarList.add(taskCar);
                    }

                }
                //写入waybillcar
                int row = waybillCarDao.saveBatch(waybillCarList);

                //写入taskCar
                int row2 = taskCarDao.saveBatch(taskCarList);

                //更新订单车辆状态
                orderCarDao.updateTrunkStateBatchByIds(OrderCarTrunkStateEnum.WAIT_LOAD.code, orderCarIdList);
            }
        } finally {
            for (String key : lockSet) {
                redisLock.releaseLock(key);
            }
        }
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo cancelDispatch(CancelDispatchDto paramsDto) {
        Long userId = paramsDto.getUserId();
        //验证用户
        Admin admin = adminDao.findByUserId(userId);
        if (admin == null || admin.getState() != AdminStateEnum.CHECKED.code) {
            return BaseResultUtil.fail("当前业务员，不在职");
        }
        // TODO 验证用户角色
        //取消运单
        boolean isAllowCancel = true;
        List<Long> cancelWaybillIdList = new ArrayList<>();
        List<String> waybillNoList = paramsDto.getWaybillNoList();
        List<Waybill> waybillList = waybillDao.findListByNos(waybillNoList);
        StringBuilder nos = new StringBuilder();
        for (Waybill waybill : waybillList) {
            if (waybill == null) {
                continue;
            }

            //取消运单
            cancelWaybill(waybill);

        }
        if (!isAllowCancel) {
            return BaseResultUtil.fail("运单{}已被承接，无法取消", nos.toString());
        }
        //更新运单主单状态
        waybillDao.updateStateBatchByNos(WaybillStateEnum.F_CANCEL.code, waybillNoList);
        //是否改变任务状态
        taskDao.updateListByWaybillIds(TaskStateEnum.F_CANCEL.code, cancelWaybillIdList);
        return BaseResultUtil.success();
    }

    private void cancelWaybill(Waybill waybill) {

        //状态不大于待承接
        if (waybill.getState() > WaybillStateEnum.WAIT_ALLOT_CONFIRM.code) {
            throw new ServerException("运单{0},当前状态不能取消", waybill);
        }
        //修改运单主单状态
        waybill.setState(WaybillStateEnum.F_CANCEL.code);
        waybillDao.updateById(waybill);
        //修改任务主单状态
        taskDao.cancelBywaybillId(TaskStateEnum.F_CANCEL.code, waybill.getId());

        //修改车辆状态
        List<OrderCar> list = orderCarDao.findListByWaybillId(waybill.getId());
        if (list == null || list.isEmpty()) {
            return;
        }
        for (OrderCar orderCar : list) {
            if (orderCar == null) {
                continue;
            }
            updateOrderCarDispatchState(waybill, orderCar);
        }
        //更新车辆下一环节调度单状态
        List<WaybillCar> waybillCarlist = waybillCarDao.findListByWaybillId(waybill.getId());
        for (WaybillCar waybillCar : waybillCarlist) {
            if(waybillCar == null){
                continue;
            }
            updateNextForCancelDispatch(waybillCar.getOrderCarId(), waybillCar.getEndStoreId());


        }
    }

    /**
     * 取消调度-修改后续调度调度车辆状态
     * @param orderCarId
     * @param endStoreId
     */
    private void updateNextForCancelDispatch(Long orderCarId, Long endStoreId) {
        List<WaybillCar> nextWaybillCarList = waybillCarDao.findNextDispatch(orderCarId, endStoreId);
        if(nextWaybillCarList != null || !nextWaybillCarList.isEmpty()){
            waybillCarDao.updateForCancelDispatch(orderCarId, endStoreId, WaybillCarStateEnum.CANCEL_DISPATCH.code);
            //TODO 修改运单车辆数
            for (WaybillCar waybillCar : nextWaybillCarList) {
                if(waybillCar == null){
                    continue;
                }
                updateNextForCancelDispatch(orderCarId, waybillCar.getEndStoreId());
            }
            //TODO 给调度人和承运商发送变更消息
        }


        waybillCarDao.updateForCancelDispatch(orderCarId, endStoreId, WaybillCarStateEnum.CANCEL_DISPATCH.code);
    }

    /**
     * 修改订单车辆调度状态
     * @author JPG
     * @since 2019/10/29 9:57
     * @param waybill
     * @param orderCar
     */
    private void updateOrderCarDispatchState(Waybill waybill, OrderCar orderCar) {
        //查询订单是否已经完结
        Order order = orderDao.selectById(orderCar.getOrderId());
        if (order == null || order.getState() >= OrderStateEnum.FINISHED.code) {
            return;
        }
        //修改车辆状态
        if (waybill.getType() == WaybillTypeEnum.PICK.code) {
            //订单车辆状态
            if (orderCar.getState() == OrderCarStateEnum.WAIT_PICK.code) {
                orderCar.setState(OrderCarStateEnum.WAIT_PICK_DISPATCH.code);
            }
            //订单车辆提车状态
            orderCar.setPickState(OrderCarLocalStateEnum.WAIT_DISPATCH.code);
        }
        if (waybill.getType() == WaybillTypeEnum.BACK.code) {
            //订单车辆状态
            if (orderCar.getState() == OrderCarStateEnum.WAIT_BACK.code) {
                orderCar.setState(OrderCarStateEnum.WAIT_BACK_DISPATCH.code);
            }
            //订单车辆配送状态
            orderCar.setBackState(OrderCarLocalStateEnum.WAIT_DISPATCH.code);
        }
        if (waybill.getType() == WaybillTypeEnum.BACK.code) {
            int trunkState = OrderCarLocalStateEnum.WAIT_DISPATCH.code;
            //根据订单车辆所有运单状态判断订单车辆干线状态
            List<Integer> waybillStateList = waybillDao.findTrunkStateListByOrderCarId(orderCar.getId());
            if (waybillStateList != null || !waybillStateList.isEmpty()) {
                if (waybillStateList.contains(WaybillStateEnum.FINISHED.code)) {
                    trunkState = OrderCarTrunkStateEnum.NODE_FINISHED.code;
                    if (orderCar.getNowStoreId().equals(order.getEndStoreId())
                            || orderCar.getNowAreaCode().equals(order.getEndAreaCode())) {
                        trunkState = OrderCarTrunkStateEnum.FINISHED.code;
                    }
                }
                if (waybillStateList.contains(WaybillStateEnum.WAIT_ALLOT_CONFIRM.code)
                        || waybillStateList.contains(WaybillStateEnum.ALLOT_CONFIRM.code)) {
                    trunkState = OrderCarTrunkStateEnum.WAIT_LOAD.code;
                }
                if (waybillStateList.contains(WaybillStateEnum.TRANSPORTING.code)) {
                    trunkState = OrderCarTrunkStateEnum.WAIT_UNLOAD.code;
                }
            }

            //订单车辆状态
            if (orderCar.getState() == OrderCarStateEnum.WAIT_TRUNK.code) {
                orderCar.setState(OrderCarStateEnum.WAIT_TRUNK_DISPATCH.code);
            }
            //订单车辆干线状态
            orderCar.setTrunkState(trunkState);

        }
        orderCarDao.updateById(orderCar);
    }

    @Override
    public ResultVo<List<HistoryListWaybillVo>> historyList(HistoryListWaybillDto paramsDto) {
        List<HistoryListWaybillVo> list = waybillDao.findHistoryList(paramsDto);
        return BaseResultUtil.success(list);
    }

}
