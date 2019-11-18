package com.cjyc.common.system.service.impl;

import com.cjkj.common.redis.lock.RedisDistributedLock;
import com.cjkj.common.redis.template.StringRedisUtil;
import com.cjyc.common.model.dao.ITaskDao;
import com.cjyc.common.model.keys.RedisKeys;
import com.cjyc.common.system.service.ICsTaskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 * @author JPG
 */
@Service
public class CsTaskServiceImpl implements ICsTaskService {


    @Resource
    private ITaskDao taskDao;
    @Resource
    private RedisDistributedLock redisLock;
    @Resource
    private StringRedisUtil redisUtil;

    @Override
    public String getTaskNo(String waybillNo) {
        String taskNo = null;
        String lockKey = RedisKeys.getNewTaskNoKey(waybillNo);
        if(!redisLock.lock(lockKey, 30000, 100, 300)){
            return null;
        }
        String maxNo = taskDao.findMaxNo(waybillNo);
        if(maxNo == null){
            taskNo = waybillNo + "-" + "1";
        }else{
            String[] split = maxNo.split("-");
            taskNo = split[0] + "-" + (Integer.valueOf(split[1]) + 1);
        }
        return taskNo;
    }
}
