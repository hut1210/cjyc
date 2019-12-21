package com.cjyc.driver.api.service.impl;

import cn.hutool.core.text.StrBuilder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.constant.FieldConstant;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.driver.BaseDriverDto;
import com.cjyc.common.model.dto.driver.task.DetailQueryDto;
import com.cjyc.common.model.dto.driver.task.DriverQueryDto;
import com.cjyc.common.model.dto.driver.task.NoFinishTaskQueryDto;
import com.cjyc.common.model.dto.driver.task.TaskQueryDto;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.waybill.WaybillCarStateEnum;
import com.cjyc.common.model.enums.waybill.WaybillTypeEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.TimeStampUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.driver.task.CarDetailVo;
import com.cjyc.common.model.vo.driver.task.TaskBillVo;
import com.cjyc.common.model.vo.driver.task.TaskDetailVo;
import com.cjyc.common.model.vo.driver.task.TaskDriverVo;
import com.cjyc.driver.api.service.ITaskService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
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
    private IOrderDao orderDao;
    @Autowired
    private IDriverDao driverDao;

    @Override
    public ResultVo<PageVo<TaskBillVo>> getWaitHandleTaskPage(BaseDriverDto dto) {
        // 分页查询待分配的运单信息
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<TaskBillVo> taskList = waybillDao.selectWaitHandleTaskPage(dto);
        PageInfo pageInfo = new PageInfo(taskList);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<PageVo<TaskBillVo>> getNoFinishTaskPage(NoFinishTaskQueryDto dto) {
        // 分页查询提车，交车任务信息
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<TaskBillVo> taskList = taskDao.selectNoFinishTaskPage(dto);
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
    public ResultVo<PageVo<TaskBillVo>> getHistoryTaskPage(TaskQueryDto dto) {
        if (dto.getCompleteTimeE() != null && dto.getCompleteTimeE() != 0) {
            dto.setCompleteTimeE(TimeStampUtil.convertEndTime(dto.getCompleteTimeE()));
        }
        if (dto.getExpectStartDateE() != null && dto.getExpectStartDateE() != 0) {
            dto.setExpectStartDateE(TimeStampUtil.convertEndTime(dto.getExpectStartDateE()));
        }
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<TaskBillVo> taskList = taskDao.selectHistoryTaskPage(dto);
        PageInfo pageInfo = new PageInfo(taskList);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<PageVo<TaskBillVo>> getFinishTaskPage(TaskQueryDto dto) {
        if (dto.getCompleteTimeE() != null && dto.getCompleteTimeE() != 0) {
            dto.setCompleteTimeE(TimeStampUtil.convertEndTime(dto.getCompleteTimeE()));
        }
        if (dto.getExpectStartDateE() != null && dto.getExpectStartDateE() != 0) {
            dto.setExpectStartDateE(TimeStampUtil.convertEndTime(dto.getExpectStartDateE()));
        }

        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<TaskBillVo> taskList = taskDao.selectFinishTaskPage(dto);
        PageInfo pageInfo = new PageInfo(taskList);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<TaskDetailVo> getNoHandleDetail(DetailQueryDto dto) {
        TaskDetailVo taskDetailVo = new TaskDetailVo();
        // 查询运单信息
        Long waybillId = dto.getWaybillId();
        if (waybillId == 0) {
            log.error("运单ID参数错误");
            return BaseResultUtil.fail("运单ID参数错误");
        }
        Waybill waybill = waybillDao.selectById(waybillId);
        BeanUtils.copyProperties(waybill,taskDetailVo);

        // 查询运单车辆信息
        LambdaQueryWrapper<WaybillCar> queryWrapper = new QueryWrapper<WaybillCar>().lambda()
                .eq(WaybillCar::getWaybillId, waybillId).eq(WaybillCar::getState, WaybillCarStateEnum.WAIT_ALLOT.code);
        List<WaybillCar> waybillCarList = waybillCarDao.selectList(queryWrapper);
        List<CarDetailVo> carDetailVoList = new ArrayList<>(10);
        BigDecimal freightFee = new BigDecimal(0);
        CarDetailVo carDetailVo = null;
        if (!CollectionUtils.isEmpty(waybillCarList)) {
            for (WaybillCar waybillCar : waybillCarList) {
                carDetailVo = new CarDetailVo();
                BeanUtils.copyProperties(waybillCar,carDetailVo);

                // 获取运单车辆费用
                freightFee = freightFee.add(waybillCar.getFreightFee()==null?new BigDecimal(0):waybillCar.getFreightFee());

                // 如果指导路线为空，且运单是提车或者送车，将始发成和结束城市用“-”拼接
                fillGuideLine(taskDetailVo,waybillCar);

                // 查询品牌车系信息
                OrderCar orderCar = orderCarDao.selectById(waybillCar.getOrderCarId());
                BeanUtils.copyProperties(orderCar,carDetailVo);

                carDetailVo.setId(waybillCar.getId());
                carDetailVo.setWaybillCarState(waybillCar.getState());
                carDetailVoList.add(carDetailVo);
            }
        }

        taskDetailVo.setFreightFee(freightFee);
        taskDetailVo.setCarDetailVoList(carDetailVoList);
        return BaseResultUtil.success(taskDetailVo);
    }

    private void fillGuideLine(TaskDetailVo taskDetailVo,WaybillCar waybillCar) {
        boolean b = WaybillTypeEnum.PICK.code == taskDetailVo.getType() || WaybillTypeEnum.BACK.code == taskDetailVo.getType();
        if (b && StringUtils.isEmpty(taskDetailVo.getGuideLine())) {
            taskDetailVo.setGuideLine(waybillCar.getStartCity() + "-" + waybillCar.getEndCity());
        }
    }

    @Override
    public ResultVo<TaskDetailVo> getHistoryDetail(DetailQueryDto dto) {
        TaskDetailVo taskDetailVo = new TaskDetailVo();
        // 查询运单信息
        Long waybillId = dto.getWaybillId();
        if (waybillId == 0) {
            log.error("运单ID参数错误");
            return BaseResultUtil.fail("运单ID参数错误");
        }
        Waybill waybill = waybillDao.selectById(waybillId);
        taskDetailVo.setType(waybill.getType());

        // 查询任务单信息信息
        Long taskId = dto.getTaskId();
        if (taskId == 0) {
            log.error("任务单ID参数错误");
            return BaseResultUtil.fail("任务单ID参数错误");
        }
        Task task = taskDao.selectById(taskId);
        BeanUtils.copyProperties(task,taskDetailVo);

        // 查询车辆信息
        LambdaQueryWrapper<TaskCar> queryWrapper = new QueryWrapper<TaskCar>().lambda().eq(TaskCar::getTaskId, taskId);
        List<TaskCar> taskCarList = taskCarDao.selectList(queryWrapper);
        List<CarDetailVo> carDetailVoList = new ArrayList<>(10);
        BigDecimal freightFee = new BigDecimal(0);
        if (!CollectionUtils.isEmpty(taskCarList)) {
            CarDetailVo carDetailVo = null;
            for (TaskCar taskCar : taskCarList) {
                // 查询任务单车辆信息
                WaybillCar waybillCar = waybillCarDao.selectById(taskCar.getWaybillCarId());
                carDetailVo = new CarDetailVo();
                BeanUtils.copyProperties(waybillCar,carDetailVo);

                // 查询除了当前车辆运单的历史车辆运单图片
                getHistoryWaybillCarImg(carDetailVo, waybillCar,dto.getDetailType());

                // 运费
                freightFee = freightFee.add(waybillCar.getFreightFee()==null?new BigDecimal(0):waybillCar.getFreightFee());

                // 如果指导路线为空，且运单是提车或者送车，将始发成和结束城市用“-”拼接
                fillGuideLine(taskDetailVo,waybillCar);

                // 查询品牌车系信息
                OrderCar orderCar = orderCarDao.selectById(waybillCar.getOrderCarId());
                BeanUtils.copyProperties(orderCar,carDetailVo);

                // 查询支付方式
                Order order = orderDao.selectById(orderCar.getOrderId());
                carDetailVo.setPayType(order.getPayType());

                carDetailVo.setId(taskCar.getId());
                carDetailVo.setWaybillCarState(waybillCar.getState());
                carDetailVoList.add(carDetailVo);
            }
        }

        taskDetailVo.setFreightFee(freightFee);
        taskDetailVo.setCarDetailVoList(carDetailVoList);
        return BaseResultUtil.success(taskDetailVo);
    }

    private void getHistoryWaybillCarImg(CarDetailVo carDetailVo, WaybillCar waybillCar,String detailType) {
        List<WaybillCar> waybillCarList = waybillCarDao.selectList(new QueryWrapper<WaybillCar>().lambda()
                .eq(WaybillCar::getOrderCarNo, waybillCar.getOrderCarNo())
                .lt(WaybillCar::getId, waybillCar.getId()));
        StrBuilder sb = new StrBuilder();
        // 封装历史运单车辆图片
        fillHistoryWaybillCarImg(waybillCarList, sb);
        // 封装当前运单车辆图片
        fillThisWaybillCarImg(waybillCar, detailType, sb);
        // 历史图片
        carDetailVo.setHistoryLoadPhotoImg(sb.toString());
    }

    private void fillHistoryWaybillCarImg(List<WaybillCar> waybillCarList, StrBuilder sb) {
        if (!CollectionUtils.isEmpty(waybillCarList)) {
            for (WaybillCar car : waybillCarList) {
                String loadPhotoImg = car.getLoadPhotoImg();
                if (sb.length() > 0 && !StringUtils.isEmpty(loadPhotoImg)) {
                    sb.append(",");
                }
                if (!StringUtils.isEmpty(loadPhotoImg)) {
                    sb.append(loadPhotoImg);
                }

                String unloadPhotoImg = car.getUnloadPhotoImg();
                if (sb.length() > 0 && !StringUtils.isEmpty(unloadPhotoImg)) {
                    sb.append(",");
                }
                if (!StringUtils.isEmpty(unloadPhotoImg)) {
                    sb.append(unloadPhotoImg);
                }
            }
        }
    }

    private void fillThisWaybillCarImg(WaybillCar waybillCar, String detailType, StrBuilder sb) {
        // 待交车车辆
        if (FieldConstant.WAIT_GIVE_CAR.equals(detailType) || FieldConstant.ALL_TASK.equals(detailType)) {
            // 当前车辆装车图片
            String loadPhotoImg1 = waybillCar.getLoadPhotoImg();
            if (sb.length() > 0 && !StringUtils.isEmpty(loadPhotoImg1)) {
                sb.append(",");
            }
            if (!StringUtils.isEmpty(loadPhotoImg1)) {
                sb.append(loadPhotoImg1);
            }
        }
        // 已交付车辆
        if (FieldConstant.ALL_TASK.equals(detailType)) {
            // 当前车辆卸车图片
            String unloadPhotoImg1 = waybillCar.getUnloadPhotoImg();
            if (sb.length() > 0 && !StringUtils.isEmpty(unloadPhotoImg1)) {
                sb.append(",");
            }
            if (!StringUtils.isEmpty(unloadPhotoImg1)) {
                sb.append(unloadPhotoImg1);
            }
        }
    }

}
