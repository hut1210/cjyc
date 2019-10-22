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
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.exception.ParameterException;
import com.cjyc.web.api.service.ISendNoService;
import com.cjyc.web.api.service.IWaybillService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    public ResultVo pickAndBackDispatch(WaybillPickDispatchListDto paramsDto) {
        Long currentTime = System.currentTimeMillis();
        Long userId = paramsDto.getUserId();
        Set<String> lockSet = new HashSet<>();
        //验证用户
        Admin admin = adminDao.findByUserId(userId);
        if (admin == null || admin.getState() != AdminStateEnum.CHECKED.code) {
            return BaseResultUtil.fail("当前业务员，不在职");
        }
        // TODO 验证用户角色
        try {
            List<WaybillPickDispatchDto> list = paramsDto.getList();
            for (int i = 0; i < list.size(); i++) {
                int idx = i + 1;
                WaybillPickDispatchDto dto = list.get(i);
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
                        || orderCar.getState() <= OrderCarStateEnum.WAIT_PICK_DISPATCH.code) {
                    throw new ParameterException("序号为{0}运单,编号为{1}的车辆，当前车辆状态不能提车调度", idx, orderCarNo);
                }
                //验证订单状态
                Order order = orderDao.selectById(orderCar.getId());
                if (order == null) {
                    throw new ParameterException("序号为{0}运单,编号为{1}的车辆，当前车辆不存在", idx, orderCarNo);
                }

                if (order.getState() == null
                        || order.getState() < OrderStateEnum.CHECKED.code
                        || order.getState() > OrderStateEnum.FINISHED.code) {
                    throw new ParameterException("序号为{0}运单,编号为{1}的车辆，车辆所属订单状态不能提车调度", idx, orderCarNo);
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
                //1、添加运单信息
                Waybill waybill = new Waybill();
                waybill.setNo(sendNoService.getNo(SendNoTypeEnum.ORDER));
                waybill.setType(WaybillTypeEnum.PICK.code);
                if (userId.equals(carrierId)) {
                    waybill.setDispatchType(DispatchTypeEnum.SELF.code);
                } else {
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
                waybillDao.insert(waybill);

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

                    //5、更新订单车辆状态
                    orderCarDao.updateStateById(OrderCarStateEnum.WAIT_PICK.code, orderCarId);
                }

            }
        } finally {
            redisUtil.del(lockSet.toArray(new String[0]));
        }
        return BaseResultUtil.success();
    }

    /**
     * 干线调度
     *
     * @param paramsDto 参数
     * @author JPG
     * @since 2019/10/17 9:16
     */
    @Override
    public ResultVo trunkDispatch(WaybillTrunkDispatchListListDto paramsDto) {

        Long currentTime = System.currentTimeMillis();
        Long userId = paramsDto.getUserId();
        Set<String> lockSet = new HashSet<>();
        //验证用户
        Admin admin = adminDao.findByUserId(userId);
        if (admin == null || admin.getState() != AdminStateEnum.CHECKED.code) {
            return BaseResultUtil.fail("当前业务员，不在职");
        }
        // TODO 验证用户角色
        try {
            List<WaybillTrunkDispatchListDto> list = paramsDto.getList();
            //多运单循环
            for (int i = 0; i < list.size(); i++) {
                WaybillTrunkDispatchListDto dtoList = list.get(i);
                if (dtoList == null) {
                    continue;
                }
                List<WaybillTrunkDispatchDto> subList = dtoList.getList();
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
                    waybill.setDispatchType(DispatchTypeEnum.SELF.code);
                } else {
                    waybill.setDispatchType(DispatchTypeEnum.MANUAL.code);
                }
                waybill.setGuideLine(dtoList.getGuideLine());
                waybill.setRecommendLine(dtoList.getRecommendLine());
                waybill.setCarrierId(carrierId);
                waybill.setCarNum(1);
                waybill.setState(WaybillStateEnum.WAIT_ALLOT_CONFIRM.code);
                //TODO 提送车费用逻辑，调度时不允许修改提送车费用，需要到订单中修改提送车费用，多则返还，少则后补
                waybill.setFreightFee(dtoList.getFreightFee());
                waybill.setCreateTime(currentTime);
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
                    task.setLoadCarNum(1);
                    task.setCreateTime(currentTime);
                    task.setCreateUser(admin.getName());
                    task.setCreateUserId(admin.getCreateUserId());
                    taskDao.insert(task);
                }

                /**2、单运单，车辆循环*/
                List<WaybillCar> waybillCarList = new ArrayList<>();
                List<TaskCar> taskCarList = new ArrayList<>();
                List<Long> orderCarIdList = new ArrayList<>();
                //合计运费
                BigDecimal sumFreightFee = BigDecimal.ZERO;
                for (WaybillTrunkDispatchDto dto : subList) {
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

                    OrderCar orderCar = validateTrunkDispatchCar(idx, orderCarId, orderCarNo);

                    //2、添加运单车辆信息
                    WaybillCar waybillCar = new WaybillCar();
                    waybillCar.setWaybillId(waybill.getId());
                    waybillCar.setOrderCarId(orderCar.getId());
                    waybillCar.setFreightFee(dto.getFreightFee());
                    waybillCar.setStartAddress(dto.getStartDetailAddr());
                    waybillCar.setEndAddress(dto.getEndDetailAddr());
                    waybillCar.setStartAreaCode(dto.getStartAreaCode());
                    waybillCar.setEndAreaCode(dto.getEndAreaCode());
                    waybillCar.setState(WaybillCarStateEnum.APPOINTED.code);
                    waybillCar.setExpectPickTime(LocalDateTimeUtil.getMillisByLDT(LocalDateTimeUtil.getDayStartByLDT(LocalDateTime.now())));
                    waybillCar.setPickType(2);
                    waybillCar.setPickContactName(dto.getPickContactName());
                    waybillCar.setPickContactPhone(dto.getPickContactPhone());
                    waybillCar.setReceiveContactName(dto.getReceiveContactName());
                    waybillCar.setReceiveContactPhone(dto.getReceiveContactPhone());

                    waybillCarList.add(waybillCar);
                    orderCarIdList.add(orderCar.getId());

                    sumFreightFee = sumFreightFee.add(dto.getFreightFee());
                    //承运商有且仅有一个司机
                    if (carrier.getDriverNum() == 1) {
                        /**2+、组装任务车辆表数据*/
                        TaskCar taskCar = new TaskCar();
                        taskCar.setTaskId(task.getId());
                        taskCar.setWaybillId(waybill.getId());
                        taskCar.setWaybillCarId(waybillCar.getId());
                        taskCar.setOrderCarId(orderCar.getId());
                        taskCar.setState(TaskCarStateEnum.WAIT_LOAD.code);
                        taskCar.setFreightFee(orderCar.getPickFee());
                        taskCar.setCreateTime(currentTime);
                        taskCarList.add(taskCar);
                    }

                }
                //写入waybillcar
                int row = waybillCarDao.saveBatch(waybillCarList);

                //写入taskCar
                int row2 = taskCarDao.saveBatch(taskCarList);

                //更新订单车辆状态
                orderCarDao.updateStateBatchByIds(OrderCarStateEnum.WAIT_PICK.code, orderCarIdList);
            }
        } finally {
            redisUtil.del(lockSet.toArray(new String[0]));
        }
        return BaseResultUtil.success();
    }

    /**
     * 验证调度单内车辆是否可以调度
     *
     * @param idx        顺序位
     * @param orderCarId 车辆ID
     * @param orderCarNo 车辆编号
     * @author JPG
     * @since 2019/10/18 11:38
     */
    private OrderCar validateTrunkDispatchCar(int idx, Long orderCarId, String orderCarNo) {

        //验证订单车辆状态
        OrderCar orderCar = orderCarDao.selectById(orderCarId);
        if (orderCar == null) {
            throw new ParameterException("序号为{0}运单，编号为{1}的车辆，不存在", idx, orderCarNo);
        }
        if (orderCar.getState() == null
                || orderCar.getState() <= OrderCarStateEnum.WAIT_TRUNK_DISPATCH.code) {
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

        return orderCar;

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
        if(waybillList == null){
            return BaseResultUtil.fail("运单不存在");
        }
        StringBuilder nos = new StringBuilder();
        for (Waybill waybill : waybillList) {
            if(waybill == null){
                continue;
            }
            if(waybill.getState() <= WaybillStateEnum.WAIT_ALLOT_CONFIRM.code){
                //状态不大于待承接
                cancelWaybillIdList.add(waybill.getId());
            }else{
                //状态大于待承接
                nos.append(waybill.getNo());
                isAllowCancel = false;
            }

        }
        if(!isAllowCancel){
            return BaseResultUtil.fail("运单{}已被承接，无法取消", nos.toString());
        }
        //更新运单主单状态
        waybillDao.updateStateBatchByNos(WaybillStateEnum.F_CANCEL.code, waybillNoList);
        //是否改变任务状态
        taskDao.updateListByWaybillIds(TaskStateEnum.F_CANCEL.code, cancelWaybillIdList);
        return BaseResultUtil.success();
    }


}
