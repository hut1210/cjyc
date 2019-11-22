package com.cjyc.driver.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.driver.*;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.driver.DriverIdentityEnum;
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
    private ITaskCarDao taskCarDao;
    @Autowired
    private IOrderCarDao orderCarDao;
    @Autowired
    private IDriverDao driverDao;

    @Override
    public ResultVo<PageVo<WaybillTaskVo>> getWaitHandleTaskPage(BaseDriverDto dto) {
        // 根据司机登录ID查询司机信息ID
        Driver driver = driverDao.selectOne(new QueryWrapper<Driver>().lambda()
                .eq(Driver::getUserId, dto.getLoginId()).eq(Driver::getIdentity, DriverIdentityEnum.CARRIER_MANAGER.code).select(Driver::getId));
        if (driver == null) {
            return BaseResultUtil.fail("司机账号不正确或该司机不是管理员,请检查!");
        }
        // 分页查询待分配的运动信息
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<WaybillTaskVo> taskList = waybillDao.selectWaitHandleTaskPage(driver.getId());
        PageInfo pageInfo = new PageInfo(taskList);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<PageVo<WaybillTaskVo>> getNoFinishTaskPage(NoFinishTaskQueryDto dto) {
        // 根据司机登录ID查询司机信息ID
        Driver driver = driverDao.selectOne(new QueryWrapper<Driver>().lambda().eq(Driver::getUserId, dto.getLoginId()).select(Driver::getId));
        if (driver == null) {
            return BaseResultUtil.fail("司机账号不正确,请检查!");
        }
        dto.setDriverId(driver.getId());
        // 分页查询提车，交车任务信息
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
    public ResultVo<PageVo<WaybillTaskVo>> getHistoryTaskPage(TaskQueryDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<WaybillTaskVo> taskList = taskDao.selectHistoryTaskPage(dto);
        PageInfo pageInfo = new PageInfo(taskList);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<PageVo<WaybillTaskVo>> getFinishTaskPage(TaskQueryDto dto) {
        // 根据司机登录ID查询司机信息ID
        Driver driver = driverDao.selectOne(new QueryWrapper<Driver>().lambda().eq(Driver::getUserId, dto.getLoginId()).select(Driver::getId));
        if (driver == null) {
            return BaseResultUtil.fail("司机账号不正确,请检查!");
        }
        dto.setDriverId(driver.getId());

        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<WaybillTaskVo> taskList = taskDao.selectFinishTaskPage(dto);
        PageInfo pageInfo = new PageInfo(taskList);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<TaskDetailVo> getDetail(DetailQueryDto dto) {
        TaskDetailVo taskDetailVo = new TaskDetailVo();
        // 查询运单信息
        Long waybillId = dto.getWaybillId();
        Waybill waybill = waybillDao.selectById(waybillId);
        BeanUtils.copyProperties(waybill,taskDetailVo);

        // 查询车辆信息
        List<CarDetailVo> carDetailVoList = new ArrayList<>(10);
        Long taskId = dto.getTaskId();
        if (taskId != null && taskId != 0) {// 已分配的任务
            // 查询司机信息
            Task task = taskDao.selectById(taskId);
            taskDetailVo.setDriverName(task.getDriverName());
            taskDetailVo.setDriverPhone(task.getDriverPhone());
            taskDetailVo.setVehiclePlateNo(task.getVehiclePlateNo());
            taskDetailVo.setGuideLine(task.getGuideLine());

            LambdaQueryWrapper<TaskCar> queryWrapper = new QueryWrapper<TaskCar>().lambda().eq(TaskCar::getTaskId,taskId);
            List<TaskCar> taskCarList = taskCarDao.selectList(queryWrapper);
            if (!CollectionUtils.isEmpty(taskCarList)) {
                CarDetailVo carDetailVo = null;
                for (TaskCar taskCar : taskCarList) {
                    // 查询任务单车辆信息
                    WaybillCar waybillCar = waybillCarDao.selectById(taskCar.getWaybillCarId());
                    carDetailVo = new CarDetailVo();
                    BeanUtils.copyProperties(waybillCar,carDetailVo);
                    // 查询品牌车系信息
                    OrderCar orderCar = orderCarDao.selectById(waybillCar.getOrderCarId());
                    BeanUtils.copyProperties(orderCar,carDetailVo);
                    carDetailVo.setCompleteTime(task.getCompleteTime());
                    carDetailVoList.add(carDetailVo);
                }
            }

        } else {// 待分配的任务
            // 查询运单车辆信息
            LambdaQueryWrapper<WaybillCar> queryWrapper = new QueryWrapper<WaybillCar>().lambda().eq(WaybillCar::getWaybillId, waybillId);
            List<WaybillCar> waybillCarList = waybillCarDao.selectList(queryWrapper);

            // 查询品牌车系信息
            CarDetailVo carDetailVo = null;
            if (!CollectionUtils.isEmpty(waybillCarList)) {
                for (WaybillCar waybillCar : waybillCarList) {
                    carDetailVo = new CarDetailVo();
                    BeanUtils.copyProperties(waybillCar,carDetailVo);
                    OrderCar orderCar = orderCarDao.selectById(waybillCar.getOrderCarId());
                    BeanUtils.copyProperties(orderCar,carDetailVo);
                    carDetailVoList.add(carDetailVo);
                }
            }
        }

        taskDetailVo.setCarDetailVoList(carDetailVoList);
        return BaseResultUtil.success(taskDetailVo);
    }
}
