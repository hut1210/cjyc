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
import com.cjyc.common.model.enums.waybill.WaybillCarrierTypeEnum;
import com.cjyc.common.model.enums.waybill.WaybillTypeEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.TimeStampUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.driver.task.CarDetailVo;
import com.cjyc.common.model.vo.driver.task.TaskBillVo;
import com.cjyc.common.model.vo.driver.task.TaskDetailVo;
import com.cjyc.common.model.vo.driver.task.TaskDriverVo;
import com.cjyc.common.system.config.LogoImgProperty;
import com.cjyc.driver.api.service.ITaskService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
    @Autowired
    private ICarSeriesDao carSeriesDao;

    @Override
    public ResultVo<PageVo<TaskBillVo>> getWaitHandleTaskPage(BaseDriverDto dto) {
        // 分页查询待分配的运单信息
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<TaskBillVo> taskList = waybillDao.selectWaitHandleTaskPage(dto);
        PageInfo pageInfo = new PageInfo(taskList);
        // 填充指导线路
        fillGuideLine(pageInfo);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<PageVo<TaskBillVo>> getNoFinishTaskPage(NoFinishTaskQueryDto dto) {
        // 分页查询提车，交车任务信息
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<TaskBillVo> taskList = taskDao.selectNoFinishTaskPage(dto);
        PageInfo pageInfo = new PageInfo(taskList);
        // 填充指导线路
        fillGuideLine(pageInfo);
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
        PageInfo<TaskBillVo> pageInfo = new PageInfo(taskList);
        // 填充指导线路
        fillGuideLine(pageInfo);
        return BaseResultUtil.success(pageInfo);
    }

    private void fillGuideLine(PageInfo<TaskBillVo> pageInfo) {
        List<TaskBillVo> list = pageInfo.getList();
        if (!CollectionUtils.isEmpty(list)) {
            for (TaskBillVo taskBillVo : list) {
                Integer type = taskBillVo.getType();
                boolean b = WaybillTypeEnum.PICK.code == type || WaybillTypeEnum.BACK.code == type;
                if (StringUtils.isEmpty(taskBillVo.getGuideLine()) && b) {
                    taskBillVo.setGuideLine(taskBillVo.getStartCity() +"-"+ taskBillVo.getEndCity());
                }
            }
        }
        pageInfo.setList(list);
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
        PageInfo<TaskBillVo> pageInfo = new PageInfo(taskList);
        // 填充指导线路
        fillGuideLine(pageInfo);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<TaskDetailVo> getNoHandleDetail(DetailQueryDto dto) {
        TaskDetailVo taskDetailVo = new TaskDetailVo();
        // 查询运单信息
        Long waybillId = dto.getWaybillId();
        Waybill waybill = waybillDao.selectById(waybillId);
        if (waybill == null) {
            log.error("查询运单为空");
            return BaseResultUtil.fail("查询运单为空");
        }
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
                carDetailVo.setGuideLine(taskDetailVo.getGuideLine());

                // 查询除了当前车辆运单的历史车辆运单图片
                getHistoryWaybillCarImg(carDetailVo, waybillCar,dto.getDetailType());

                // 查询品牌车系信息
                OrderCar orderCar = orderCarDao.selectById(waybillCar.getOrderCarId());
                BeanUtils.copyProperties(orderCar,carDetailVo);

                // 查询车辆logo图片
                String logoImg = carSeriesDao.getLogoImgByBraMod(carDetailVo.getBrand(),carDetailVo.getModel());
                carDetailVo.setLogoPhotoImg(LogoImgProperty.logoImg+logoImg);

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
        Waybill waybill = waybillDao.selectById(waybillId);
        if (waybill == null) {
            log.error("===> 查询运单为空...");
            return BaseResultUtil.fail("查询运单为空");
        }
        taskDetailVo.setType(waybill.getType());

        // 查询任务单信息信息
        Task task = taskDao.selectById(dto.getTaskId());
        if (task == null) {
            log.error("===> 查询任务单为空...");
            return BaseResultUtil.fail("查询任务单为空");
        }
        BeanUtils.copyProperties(task,taskDetailVo);
        // 承运商类型不是企业或者不是干线运输时，运单号显示运单号，否则显示任务单号
        if (WaybillCarrierTypeEnum.TRUNK_ENTERPRISE.code != waybill.getCarrierType()) {
            taskDetailVo.setNo(waybill.getNo());
        }

        // 查询车辆信息
        LambdaQueryWrapper<TaskCar> queryWrapper = new QueryWrapper<TaskCar>().lambda().eq(TaskCar::getTaskId, dto.getTaskId());
        List<TaskCar> taskCarList = taskCarDao.selectList(queryWrapper);
        List<CarDetailVo> carDetailVoList = new ArrayList<>(10);
        BigDecimal freightFee = new BigDecimal(0);
        if (!CollectionUtils.isEmpty(taskCarList)) {
            CarDetailVo carDetailVo = null;
            String detailType = dto.getDetailType();
            for (TaskCar taskCar : taskCarList) {
                carDetailVo = new CarDetailVo();
                // 查询任务单车辆信息
                WaybillCar waybillCar = getWaybillCar(detailType, taskCar);
                if (waybillCar != null) {
                    carDetailVo.setWaybillCarState(waybillCar.getState());
                    BeanUtils.copyProperties(waybillCar,carDetailVo);

                    // 查询除了当前车辆运单的历史车辆运单图片
                    getHistoryWaybillCarImg(carDetailVo, waybillCar,detailType);

                    // 运费
                    freightFee = freightFee.add(waybillCar.getFreightFee()==null?new BigDecimal(0):waybillCar.getFreightFee());

                    // 如果指导路线为空，且运单是提车或者送车，将始发成和结束城市用“-”拼接
                    fillGuideLine(taskDetailVo,waybillCar);
                    carDetailVo.setGuideLine(taskDetailVo.getGuideLine());

                    // 查询品牌车系信息
                    OrderCar orderCar = orderCarDao.selectById(waybillCar.getOrderCarId());
                    BeanUtils.copyProperties(orderCar,carDetailVo);

                    // 查询车辆logo图片
                    String logoImg = carSeriesDao.getLogoImgByBraMod(carDetailVo.getBrand(),carDetailVo.getModel());
                    carDetailVo.setLogoPhotoImg(LogoImgProperty.logoImg+logoImg);

                    // 查询支付方式
                    Order order = orderDao.selectById(orderCar.getOrderId());
                    carDetailVo.setPayType(order.getPayType());

                    carDetailVo.setId(taskCar.getId());
                    carDetailVo.setWaybillCarState(waybillCar.getState());
                    carDetailVoList.add(carDetailVo);
                }
            }
        }

        taskDetailVo.setFreightFee(freightFee);
        taskDetailVo.setCarDetailVoList(carDetailVoList);
        return BaseResultUtil.success(taskDetailVo);
    }

    private WaybillCar getWaybillCar(String detailType, TaskCar taskCar) {
        LambdaQueryWrapper<WaybillCar> query = new QueryWrapper<WaybillCar>().lambda()
                .eq(WaybillCar::getId, taskCar.getWaybillCarId());
        if (FieldConstant.WAIT_PICK_CAR.equals(detailType)) {
            // 待提车详情
            query = query.in(WaybillCar::getState, WaybillCarStateEnum.WAIT_LOAD.code,WaybillCarStateEnum.WAIT_LOAD_CONFIRM.code);
        } else if(FieldConstant.WAIT_GIVE_CAR.equals(detailType)){
            // 待交车详情
            query = query.in(WaybillCar::getState,WaybillCarStateEnum.LOADED.code,WaybillCarStateEnum.WAIT_UNLOAD_CONFIRM.code);
        } else if (FieldConstant.FINISH.equals(detailType)){
            // 已交付
            query = query.eq(WaybillCar::getState,WaybillCarStateEnum.UNLOADED.code);
        }
        return waybillCarDao.selectOne(query);
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
        // 待交车 和已交付 车辆
        if (!FieldConstant.WAIT_PICK_CAR.equals(detailType)) {
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
        if (FieldConstant.ALL_TASK.equals(detailType) || FieldConstant.FINISH.equals(detailType)) {
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
