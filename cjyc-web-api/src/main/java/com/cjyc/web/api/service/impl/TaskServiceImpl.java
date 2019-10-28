package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjkj.common.redis.lock.RedisDistributedLock;
import com.cjkj.common.redis.template.StringRedisUtil;
import com.cjyc.common.model.dao.IDriverDao;
import com.cjyc.common.model.dao.ITaskDao;
import com.cjyc.common.model.dao.IWaybillCarDao;
import com.cjyc.common.model.dao.IWaybillDao;
import com.cjyc.common.model.dto.web.task.AllotTaskDto;
import com.cjyc.common.model.entity.Driver;
import com.cjyc.common.model.entity.Task;
import com.cjyc.common.model.entity.Waybill;
import com.cjyc.common.model.entity.WaybillCar;
import com.cjyc.common.model.enums.waybill.WaybillCarStateEnum;
import com.cjyc.common.model.enums.waybill.WaybillStateEnum;
import com.cjyc.common.model.keys.RedisKeys;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.service.ITaskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    private StringRedisUtil redisUtil;

    @Override
    public ResultVo allot(AllotTaskDto reqDto) {
        Long waybillId = reqDto.getWaybillId();
        Long driverId = reqDto.getDriverId();
        List<Long> waybillCarIdList = reqDto.getWaybillCarIdList();
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

            for (WaybillCar waybillCar : list) {
                if(waybillCar == null){
                    continue;
                }
                //加锁
                String lockKey = RedisKeys.getAllotTaskKey(waybillCar.getOrderCarNo());
                if(!redisLock.lock(lockKey, 20000, 100, 300)){
                    return BaseResultUtil.fail("当前运单的状态，无法分配任务");
                }
                lockKeySet.add(lockKey);

                //验证运单车辆状态
                if(waybillCar.getState() > WaybillCarStateEnum.WAIT_ALLOT.code){
                    return BaseResultUtil.fail("当前运单车辆的状态，无法分配任务");
                }

            }

            Task task = new Task();
            //task.set

        } finally {
            for (String key : lockKeySet) {
                redisLock.releaseLock(key);
            }
        }
        return null;
    }
}
