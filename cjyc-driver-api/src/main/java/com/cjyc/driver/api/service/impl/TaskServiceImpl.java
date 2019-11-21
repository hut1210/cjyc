package com.cjyc.driver.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.driver.*;
import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.common.model.entity.Task;
import com.cjyc.common.model.entity.Waybill;
import com.cjyc.common.model.entity.WaybillCar;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.driver.task.CarDetailVo;
import com.cjyc.common.model.vo.driver.task.TaskDetailVo;
import com.cjyc.common.model.vo.driver.task.TaskDriverVo;
import com.cjyc.common.model.vo.driver.task.WaybillTaskVo;
import com.cjyc.driver.api.service.ITaskService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 任务表(子运单) 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-11-19
 */
@Service
public class TaskServiceImpl extends ServiceImpl<ITaskDao, Task> implements ITaskService {
    @Autowired
    private IWaybillDao waybillDao;
    @Autowired
    private ITaskDao taskDao;
    @Autowired
    private IWaybillCarDao waybillCarDao;
    @Autowired
    private IOrderCarDao orderCarDao;
    @Autowired
    private IDriverDao driverDao;

    @Override
    public ResultVo<PageVo<WaybillTaskVo>> getWaitHandleTaskPage(BaseDriverDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<WaybillTaskVo> taskList = waybillDao.selectWaitHandleTaskPage(dto);
        PageInfo pageInfo = new PageInfo(taskList);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<PageVo<WaybillTaskVo>> getNoFinishTaskPage(NoFinishTaskQueryDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<WaybillTaskVo> taskList = taskDao.selectNoFinishTaskPage(dto);
        PageInfo pageInfo = new PageInfo(taskList);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<PageVo<TaskDriverVo>> getDriverPage(DriverQueryDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<TaskDriverVo> taskList = driverDao.selectDriverList(dto);
        PageInfo<TaskDriverVo> pageInfo = new PageInfo(taskList);
        List<TaskDriverVo> pageInfoList = pageInfo.getList();
        List<TaskDriverVo> returnList = new ArrayList<>(10);
        // 将当前操作人放在列表的第一个
        if (!CollectionUtils.isEmpty(pageInfoList)) {
            for (TaskDriverVo driverVo : pageInfoList) {
                if (dto.getLoginId().equals(driverVo.getLoginId())) {
                    returnList.add(driverVo);
                    break;
                }
            }
            for (TaskDriverVo driverVo : pageInfoList) {
                if (!dto.getLoginId().equals(driverVo.getLoginId())) {
                    returnList.add(driverVo);
                }
            }
        }
        pageInfo.setList(returnList);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<PageVo<WaybillTaskVo>> getFinishTaskPage(FinishTaskQueryDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<WaybillTaskVo> taskList = taskDao.selectFinishTaskPage(dto);
        PageInfo pageInfo = new PageInfo(taskList);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<TaskDetailVo> getDetail(DetailQueryDto dto) {
        TaskDetailVo taskDetailVo = new TaskDetailVo();
        // 查询运单信息
        Waybill waybill = waybillDao.selectById(dto.getWaybillId());
        BeanUtils.copyProperties(waybill,taskDetailVo);
        // 查询车辆信息
        List<WaybillCar> waybillCarList = waybillCarDao.selectList(new QueryWrapper<WaybillCar>().lambda()
                .eq(WaybillCar::getWaybillId, dto.getWaybillId()));
        CarDetailVo carDetailVo = null;
        List<CarDetailVo> carDetailVoList = new ArrayList<>(10);
        for (WaybillCar waybillCar : waybillCarList) {
            carDetailVo = new CarDetailVo();
            BeanUtils.copyProperties(waybillCar,carDetailVo);
            // 查询品牌车系信息
            OrderCar orderCar = orderCarDao.selectById(waybillCar.getOrderCarId());
            BeanUtils.copyProperties(orderCar,carDetailVo);
            carDetailVo.setCompleteTime(waybill.getCompleteTime());
            carDetailVoList.add(carDetailVo);
        }
        taskDetailVo.setCarDetailVoList(carDetailVoList);
        return BaseResultUtil.success(taskDetailVo);
    }
}
