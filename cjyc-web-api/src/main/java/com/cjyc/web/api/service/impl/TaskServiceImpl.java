package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjkj.common.redis.lock.RedisDistributedLock;
import com.cjkj.common.redis.template.StringRedisUtil;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.web.task.*;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.SendNoTypeEnum;
import com.cjyc.common.model.enums.order.OrderCarStateEnum;
import com.cjyc.common.model.enums.order.OrderStateEnum;
import com.cjyc.common.model.enums.task.TaskStateEnum;
import com.cjyc.common.model.enums.waybill.WaybillCarStateEnum;
import com.cjyc.common.model.enums.waybill.WaybillStateEnum;
import com.cjyc.common.model.enums.waybill.WaybillTypeEnum;
import com.cjyc.common.model.exception.ServerException;
import com.cjyc.common.model.keys.RedisKeys;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.OrderCarVo;
import com.cjyc.common.model.vo.web.task.ListByWaybillTaskVo;
import com.cjyc.common.model.vo.web.task.TaskVo;
import com.cjyc.common.model.vo.web.waybill.WaybillCarVo;
import com.cjyc.web.api.service.ISendNoService;
import com.cjyc.web.api.service.ITaskService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.*;

/**
 * <p>
 * 任务表(子运单) 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-26
 */
@Service
public class TaskServiceImpl extends ServiceImpl<ITaskDao, Task> implements ITaskService {

    @Resource
    private IOrderDao orderDao;
    @Resource
    private IOrderCarDao orderCarDao;
    @Resource
    private IWaybillDao waybillDao;
    @Resource
    private IWaybillCarDao waybillCarDao;
    @Resource
    private IDriverDao driverDao;
    @Resource
    private RedisDistributedLock redisLock;
    @Resource
    private ISendNoService sendNoService;
    @Resource
    private ITaskDao taskDao;
    @Resource
    private StringRedisUtil redisUtil;
    @Resource
    private ITaskCarDao taskCarDao;
    @Resource
    private IStoreDao storeDao;

    @Override
    public ResultVo allot(AllotTaskDto paramsDto) {
        Long waybillId = paramsDto.getWaybillId();
        Long driverId = paramsDto.getDriverId();
        List<Long> waybillCarIdList = paramsDto.getWaybillCarIdList();
        Set<String> lockKeySet = new HashSet<>();
        try {
            //验证运单状态
            Waybill waybill = waybillDao.selectById(waybillId);
            if (waybill == null
                    || waybill.getState() < WaybillStateEnum.WAIT_ALLOT.code
                    || waybill.getState() > WaybillStateEnum.FINISHED.code) {

                return BaseResultUtil.fail("当前运单的状态，无法分配任务");
            }
            List<WaybillCar> list = waybillCarDao.findVoByIds(waybillCarIdList);
            if (list == null || list.isEmpty()) {
                return BaseResultUtil.fail("内容不能为空");
            }
            //验证司机信息
            Driver driver = driverDao.selectById(driverId);
            if (driver == null || driver.getState() != 1) {
                return BaseResultUtil.fail("司机不在运营中");
            }

            if(StringUtils.isNotBlank(waybill.getGuideLine())){
                if(waybill.getCarNum() != list.size()){
                    return BaseResultUtil.fail("此单不允许拆单分派");
                }
            }

            Task task = new Task();
            //计算任务编号
            task.setNo("");
            task.setWaybillId(waybill.getId());
            task.setWaybillNo(waybill.getNo());
            task.setCarNum(paramsDto.getWaybillCarIdList().size());
            task.setState(TaskStateEnum.WAIT_ALLOT_CONFIRM.code);
            task.setDriverId(driver.getId());
            task.setDriverPhone(driver.getPhone());
            task.setDriverName(driver.getName());
            task.setCreateTime(System.currentTimeMillis());
            task.setCreateUser(paramsDto.getUserName());
            task.setCreateUserId(paramsDto.getUserId());
            taskDao.insert(task);

            List<TaskCar> saveTaskCarList = new ArrayList<>();
            int noCount = 0;
            for (WaybillCar waybillCar : list) {
                if (waybillCar == null) {
                    continue;
                }
                //加锁
                String lockKey = RedisKeys.getAllotTaskKey(waybillCar.getOrderCarNo());
                if (!redisLock.lock(lockKey, 20000, 100, 300)) {
                    throw new ServerException("当前运单的状态，无法分配任务");
                }
                lockKeySet.add(lockKey);

                //验证运单车辆状态
                if (waybillCar.getState() > WaybillCarStateEnum.WAIT_ALLOT.code) {
                    throw new ServerException("当前运单车辆的状态，无法分配任务");
                }

                TaskCar taskCar = new TaskCar();
                taskCar.setTaskId(task.getId());
                taskCar.setWaybillCarId(waybillCar.getId());
                taskCarDao.insert(taskCar);
                noCount++;
            }

            if (noCount != task.getCarNum()) {
                task.setCarNum(noCount);
                taskDao.updateById(task);
            }

        } finally {
            for (String key : lockKeySet) {
                redisLock.releaseLock(key);
            }
        }
        return null;
    }

    @Override
    public ResultVo load(LoadTaskDto paramsDto) {
        Long taskCarId = paramsDto.getTaskCarId();

        WaybillCar waybillCar = waybillCarDao.findByTaskCarId(taskCarId);
        if (waybillCar == null) {
            return BaseResultUtil.fail("运单车辆不存在");
        }
        if (waybillCar.getState() > WaybillCarStateEnum.LOADED.code) {
            return BaseResultUtil.fail("车辆已经装过车");
        }
        //验证运单
        Waybill waybill = waybillDao.selectById(waybillCar.getId());
        if(waybill == null){
            return BaseResultUtil.fail("运单不存在");
        }
        if(waybill.getState() >= WaybillStateEnum.FINISHED.code || waybill.getState() <= WaybillStateEnum.WAIT_ALLOT_CONFIRM.code ){
            return BaseResultUtil.fail("运单已完结");
        }

        waybillCar.setLoadPhotoImg(paramsDto.getLoadPhotoImg());
        waybillCar.setLoadTime(System.currentTimeMillis());

        OrderCar orderCar = orderCarDao.selectById(waybillCar.getOrderCarId());


        int orderCarState = orderCar.getState();
        int waybillCarState = waybillCar.getState();
        if (waybill.getType() == WaybillTypeEnum.PICK.code) {
            //提车任务、自送到业务中心
            waybillCarState = WaybillCarStateEnum.LOADED.code;
            orderCarState = OrderCarStateEnum.PICKING.code;

        } else if (waybill.getType() == WaybillTypeEnum.TRUNK.code) {
            waybillCarState = WaybillCarStateEnum.WAIT_LOAD_TURN.code;
            orderCarState = OrderCarStateEnum.TRUNKING.code;
            //干线、提干、干送、提干送任务
            /*if(waybillCar.getLoadTurnType() == WaybillCarTurnType.HOME.code){
            }else if(waybillCar.getLoadTurnType() == WaybillCarTurnType.MIDWAY.code){
                //TODO 客户自送与干线交接,先默认干线不与自送直接对接
            }else {
            }*/
        } else if (waybill.getType() == WaybillTypeEnum.BACK.code) {
            //送车
            waybillCarState = WaybillCarStateEnum.WAIT_LOAD_TURN.code;
            orderCarState = OrderCarStateEnum.BACKING.code;
        } else {
            return BaseResultUtil.fail("未识别的任务类型");
        }
        // TODO 更新空车位数
        //更新订单状态
        orderDao.updateStateForLoad(OrderStateEnum.TRANSPORTING.code, waybillCar.getOrderCarId());
        //更新订单
        waybillCarDao.updateStateById(waybillCarState, waybillCar.getId());
        //更新车辆状态
        orderCarDao.updateStateForLoad(orderCarState, waybillCar.getOrderCarId());



        //TODO 给客户发送消息
        //TODO 写物流轨迹
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo unload(UnLoadTaskDto paramsDto) {
        Map<String, Object> failCarNoMap = new HashMap<>();
        Map<String, Object> successCarNoMap = new HashMap<>();

        List<Long> taskCarIdList = paramsDto.getTaskCarIdList();
        if (taskCarIdList == null || taskCarIdList.isEmpty()) {
            return BaseResultUtil.fail("车辆不能为空");
        }
        for (Long taskCarId : taskCarIdList) {
            if (taskCarId == null) {
                continue;
            }
            WaybillCar waybillCar = waybillCarDao.findByTaskCarId(taskCarId);
            if (waybillCar == null) {
                failCarNoMap.put(waybillCar.getOrderCarNo(), "运单车辆不存在");
                continue;
            }
            if (waybillCar.getState() > WaybillCarStateEnum.UNLOADED.code) {
                failCarNoMap.put(waybillCar.getOrderCarNo(), "车辆已经卸过车");
                continue;
            }
            waybillCar.setState(WaybillCarStateEnum.UNLOADED.code);
            waybillCar.setUnloadTime(System.currentTimeMillis());
            waybillCarDao.updateById(waybillCar);
            successCarNoMap.put(waybillCar.getOrderCarNo(), waybillCar.getId());

        }
        if (successCarNoMap.isEmpty()) {
            return BaseResultUtil.fail(failCarNoMap);
        }
        //TODO 发送收车推送信息

        return BaseResultUtil.success(failCarNoMap);
    }

    @Override
    public ResultVo inStore(InStoreTaskDto paramsDto) {
        Map<String, Object> failCarNoMap = new HashMap<>();
        long currentTimeMillis = System.currentTimeMillis();

        Long inStoreId = paramsDto.getStoreId();
        Store store = storeDao.selectById(inStoreId);
        if (store == null) {
            return BaseResultUtil.fail("业务中心不存在");
        }

        List<Long> taskCarIdList = paramsDto.getTaskCarIdList();
        if (taskCarIdList == null || taskCarIdList.isEmpty()) {
            return BaseResultUtil.fail("车辆不能为空");
        }
        for (Long taskCarId : taskCarIdList) {
            if (taskCarId == null) {
                continue;
            }
            WaybillCar waybillCar = waybillCarDao.findByTaskCarId(taskCarId);
            if (waybillCar == null) {
                failCarNoMap.put(waybillCar.getOrderCarNo(), "运单车辆不存在");
                continue;
            }
            if (waybillCar.getState() > WaybillCarStateEnum.UNLOADED.code) {
                failCarNoMap.put(waybillCar.getOrderCarNo(), "车辆已经卸过车");
                continue;
            }
            String endAddress = (waybillCar.getEndProvince() == null ? "" : waybillCar.getEndProvince())
                    + (waybillCar.getEndCity() == null ? "" : waybillCar.getEndCity())
                    + (waybillCar.getEndArea() == null ? "" : waybillCar.getEndArea())
                    + (waybillCar.getEndAddress() == null ? "" : waybillCar.getEndAddress());
            //验证卸车目的地
            if (waybillCar.getEndStoreId() == null) {
                failCarNoMap.put(waybillCar.getOrderCarNo(), MessageFormat.format("车辆只能卸在:{0}", endAddress));
                continue;
            }
            if (!waybillCar.getEndStoreId().equals(inStoreId)) {
                failCarNoMap.put(waybillCar.getOrderCarNo(), MessageFormat.format("车辆只能卸在:{0}业务员中心, 地址：{}", waybillCar.getEndStoreName(), endAddress));
                continue;
            }

            //验证车辆是否到达目的地
            Long orderCarId = waybillCar.getOrderCarId();
            OrderCarVo orderCarVo = orderCarDao.findExtraById(orderCarId);
            if (orderCarVo == null) {
                failCarNoMap.put(waybillCar.getOrderCarNo(), "车辆不存在");
                continue;
            }

            //当前状态
            int state = orderCarVo.getState();
            int newState = 0;
            //计算是否到达目的地业务中心
            int m = waybillCarDao.countByStartCityAndOrderCar(store.getCityCode(), inStoreId);
            if (store.getCityCode().equals(orderCarVo.getEndCityCode())) {
                //配送
                newState = OrderCarStateEnum.WAIT_BACK_DISPATCH.code;
                if (m > 0) {
                    //配送已调度
                    newState = OrderCarStateEnum.WAIT_BACK.code;
                }
            } else {
                //干线
                newState = OrderCarStateEnum.WAIT_TRUNK_DISPATCH.code;
                if (m > 0) {
                    //干线已调度
                    newState = OrderCarStateEnum.WAIT_TRUNK.code;
                }
            }

            //更新运单车辆状态
            waybillCar.setState(WaybillCarStateEnum.CONFIRMED.code);
            waybillCar.setUnloadTime(currentTimeMillis);
            waybillCarDao.updateById(waybillCar);

            //更新车辆状态和所在位置
            OrderCar orderCar = new OrderCar();
            orderCar.setId(orderCarId);
            orderCar.setState(newState);
            orderCar.setNowStoreId(inStoreId);
            orderCar.setNowAreaCode(store.getAreaCode());
            orderCarDao.updateById(orderCar);


            //TODO 添加物流日志
            //推送消息
        }
        return BaseResultUtil.success(failCarNoMap);
    }

    @Override
    public ResultVo outStore(OutStoreTaskDto paramsDto) {
        Map<String, Object> failCarNoMap = new HashMap<>();
        long currentTimeMillis = System.currentTimeMillis();

        List<Long> taskCarIdList = paramsDto.getTaskCarIdList();
        if (taskCarIdList == null || taskCarIdList.isEmpty()) {
            return BaseResultUtil.fail("车辆不能为空");
        }
        for (Long taskCarId : taskCarIdList) {
            if (taskCarId == null) {
                continue;
            }
            WaybillCar waybillCar = waybillCarDao.findByTaskCarId(taskCarId);
            if (waybillCar == null) {
                failCarNoMap.put(waybillCar.getOrderCarNo(), "运单车辆不存在");
                continue;
            }
            if (waybillCar.getState() != WaybillCarStateEnum.WAIT_LOAD_TURN.code) {
                failCarNoMap.put(waybillCar.getOrderCarNo(), "车辆尚未装车");
                continue;
            }
            waybillCarDao.updateStateById(WaybillCarStateEnum.LOADED.code, waybillCar.getId());


            //TODO 添加物流日志
            //推送消息
        }
        return BaseResultUtil.success(failCarNoMap);
    }

    @Override
    public ResultVo sign(SignTaskDto reqDto) {
        return null;
    }

    @Override
    public ResultVo<List<ListByWaybillTaskVo>> getlistByWaybillId(Long waybillId) {
        List<ListByWaybillTaskVo> list = taskDao.findListByWaybillId(waybillId);
        return BaseResultUtil.success(list);
    }

    @Override
    public ResultVo<TaskVo> get(Long taskId) {
        TaskVo taskVo = taskDao.findVoById(taskId);
        List<WaybillCarVo> list = waybillCarDao.findVoByTaskId(taskId);
        taskVo.setList(list);
        return BaseResultUtil.success(taskVo);
    }

}
