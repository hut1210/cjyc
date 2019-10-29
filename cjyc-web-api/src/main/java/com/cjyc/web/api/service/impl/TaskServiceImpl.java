package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjkj.common.redis.lock.RedisDistributedLock;
import com.cjkj.common.redis.template.StringRedisUtil;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.web.task.AllotTaskDto;
import com.cjyc.common.model.dto.web.task.LoadTaskDto;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.SendNoTypeEnum;
import com.cjyc.common.model.enums.task.TaskStateEnum;
import com.cjyc.common.model.enums.waybill.WaybillCarStateEnum;
import com.cjyc.common.model.enums.waybill.WaybillStateEnum;
import com.cjyc.common.model.keys.RedisKeys;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.exception.ServerException;
import com.cjyc.web.api.service.ISendNoService;
import com.cjyc.web.api.service.ITaskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            if(list == null || list.isEmpty()){
                return BaseResultUtil.fail("内容不能为空");
            }
            //验证司机信息
            Driver driver = driverDao.selectById(driverId);
            if(driver == null || driver.getState() != 1 ){
                return BaseResultUtil.fail("司机不在运营中");
            }

            Task task = new Task();
            task.setNo(sendNoService.getNo(SendNoTypeEnum.TASK));
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
                if(waybillCar == null){
                    continue;
                }
                //加锁
                String lockKey = RedisKeys.getAllotTaskKey(waybillCar.getOrderCarNo());
                if(!redisLock.lock(lockKey, 20000, 100, 300)){
                    throw new ServerException("当前运单的状态，无法分配任务");
                }
                lockKeySet.add(lockKey);

                //验证运单车辆状态
                if(waybillCar.getState() > WaybillCarStateEnum.WAIT_ALLOT.code){
                    throw new ServerException("当前运单车辆的状态，无法分配任务");
                }

                TaskCar taskCar = new TaskCar();
                taskCar.setTaskId(task.getId());
                taskCar.setWaybillCarId(waybillCar.getId());
                taskCarDao.insert(taskCar);
                noCount++;
            }

            if(noCount != task.getCarNum()){
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
        if(waybillCar == null){
            return BaseResultUtil.fail("运单车辆不存在");
        }
        if(waybillCar.getState() > WaybillCarStateEnum.LOADED.code){
            return BaseResultUtil.fail("车辆已经装过车");
        }
        waybillCar.setState(WaybillCarStateEnum.LOADED.code);
        waybillCar.setLoadPhotoImg(paramsDto.getLoadPhotoImg());
        waybillCar.setLoadTime(System.currentTimeMillis());
        waybillCarDao.updateById(waybillCar);
        return BaseResultUtil.success();
    }
}
