package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjkj.common.redis.lock.RedisDistributedLock;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.web.task.*;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.CarStorageTypeEnum;
import com.cjyc.common.model.enums.order.OrderCarStateEnum;
import com.cjyc.common.model.enums.order.OrderStateEnum;
import com.cjyc.common.model.enums.task.TaskStateEnum;
import com.cjyc.common.model.enums.waybill.WaybillCarStateEnum;
import com.cjyc.common.model.enums.waybill.WaybillStateEnum;
import com.cjyc.common.model.enums.waybill.WaybillTypeEnum;
import com.cjyc.common.model.exception.ServerException;
import com.cjyc.common.model.keys.RedisKeys;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.OrderCarVo;
import com.cjyc.common.model.vo.web.task.CrTaskVo;
import com.cjyc.common.model.vo.web.task.ListByWaybillTaskVo;
import com.cjyc.common.model.vo.web.task.TaskVo;
import com.cjyc.common.model.vo.web.waybill.WaybillCarVo;
import com.cjyc.common.system.service.ICsTaskService;
import com.cjyc.web.api.service.ICarStorageLogService;
import com.cjyc.web.api.service.ITaskService;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
@Transactional(rollbackFor = Exception.class)
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
    private ITaskDao taskDao;
    @Resource
    private ITaskCarDao taskCarDao;
    @Resource
    private IStoreDao storeDao;
    @Resource
    private ICsTaskService csTaskService;

    @Override
    public ResultVo allot(AllotTaskDto paramsDto) {
        return csTaskService.allot(paramsDto);
    }

    @Override
    public ResultVo load(LoadTaskDto paramsDto) {
        return csTaskService.load(paramsDto);
    }

    @Override
    public ResultVo unload(UnLoadTaskDto paramsDto) {
        return csTaskService.unload(paramsDto);
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
           // waybillCarDao.updateStateForLoad(WaybillCarStateEnum.LOADED.code, waybillCar.getId());


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

    @Override
    public ResultVo<PageVo<CrTaskVo>> crAllottedList(CrTaskDto paramsDto) {
        PageHelper.startPage(paramsDto.getCurrentPage(), paramsDto.getPageSize(), true);
        List<CrTaskVo> list = taskDao.findListForMineCarrier(paramsDto);

        return null;
    }

}
