package com.cjyc.salesman.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjyc.common.model.constant.FieldConstant;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.driver.task.DetailQueryDto;
import com.cjyc.common.model.dto.salesman.task.OutAndInStorageQueryDto;
import com.cjyc.common.model.dto.salesman.task.TaskWaybillQueryDto;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.entity.defined.BizScope;
import com.cjyc.common.model.enums.BizScopeEnum;
import com.cjyc.common.model.enums.waybill.WaybillCarStateEnum;
import com.cjyc.common.model.enums.waybill.WaybillCarrierTypeEnum;
import com.cjyc.common.model.enums.waybill.WaybillTypeEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.TimeStampUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.driver.task.CarDetailVo;
import com.cjyc.common.model.vo.driver.task.TaskDetailVo;
import com.cjyc.common.model.vo.salesman.task.TaskWaybillVo;
import com.cjyc.common.system.config.LogoImgProperty;
import com.cjyc.common.system.service.sys.ICsSysService;
import com.cjyc.salesman.api.service.ITaskService;
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
 * @Description 任务业务接口实现类
 * @Author Liu Xing Xiang
 * @Date 2019/12/9 11:32
 **/
@Slf4j
@Service
public class TaskServiceImpl implements ITaskService {
    @Autowired
    private ITaskDao taskDao;
    @Autowired
    private ITaskCarDao taskCarDao;
    @Autowired
    private IWaybillDao waybillDao;
    @Autowired
    private IWaybillCarDao waybillCarDao;
    @Autowired
    private IOrderCarDao orderCarDao;
    @Autowired
    private IOrderDao orderDao;
    @Autowired
    private ICsSysService csSysService;
    @Autowired
    private ICarSeriesDao carSeriesDao;

    @Override
    public ResultVo<PageVo<TaskWaybillVo>> getCarryPage(TaskWaybillQueryDto dto) {
        if (dto.getCompleteTimeE() != null && dto.getCompleteTimeE() != 0) {
            dto.setCompleteTimeE(TimeStampUtil.convertEndTime(dto.getCompleteTimeE()));
        }
        Long creatTime = dto.getCreatTime();
        if (creatTime != null && creatTime != 0) {
            dto.setCreatTimeS(creatTime);
            dto.setCreatTimeE(TimeStampUtil.convertEndTime(creatTime));
        }
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<TaskWaybillVo> list = taskDao.selectCarryList(dto);
        PageInfo<TaskWaybillVo> pageInfo = new PageInfo<>(list);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<TaskDetailVo> getCarryDetail(DetailQueryDto dto) {
        TaskDetailVo taskDetailVo = new TaskDetailVo();
        // 查询运单信息
        Long waybillId = dto.getWaybillId();
        Waybill waybill = waybillDao.selectById(waybillId);
        if (waybill == null) {
            log.error("===>查询运单为空...");
            return BaseResultUtil.fail("查询运单为空");
        }
        taskDetailVo.setType(waybill.getType());
        taskDetailVo.setCarrierType(waybill.getCarrierType());

        // 查询任务单信息
        Long taskId = dto.getTaskId();
        Task task = taskDao.selectById(taskId);
        if (task == null) {
            log.error("===>查询任务单为空...");
            return BaseResultUtil.fail("查询任务单为空");
        }
        BeanUtils.copyProperties(task,taskDetailVo);
        // 承运商类型不是企业或者不是干线运输时，运单号显示运单号，否则显示任务单号
        if (WaybillCarrierTypeEnum.TRUNK_ENTERPRISE.code != waybill.getCarrierType()) {
            taskDetailVo.setNo(waybill.getNo());
        }

        // 任务单车辆
        LambdaQueryWrapper<TaskCar> queryWrapper = new QueryWrapper<TaskCar>().lambda().eq(TaskCar::getTaskId,taskId);
        List<TaskCar> taskCarList = taskCarDao.selectList(queryWrapper);

        // 查询车辆信息
        List<CarDetailVo> carDetailVoList = new ArrayList<>(10);
        BigDecimal freightFee = new BigDecimal(0);
        if (!CollectionUtils.isEmpty(taskCarList)) {
            // 根据登录ID查询当前业务员所在业务中心ID
            BizScope bizScope = csSysService.getBizScopeByLoginIdNew(dto.getLoginId(), true);

            // 判断当前登录人是否有权限访问
            if (BizScopeEnum.NONE.code == bizScope.getCode()) {
                return BaseResultUtil.fail("您没有访问权限!");
            }

            dto.setStoreIds(bizScope.getStoreIds());

            CarDetailVo carDetailVo = null;
            for (TaskCar taskCar : taskCarList) {
                // 查询任务单车辆信息
                //String detailState = dto.getDetailState();
                //WaybillCar waybillCar = getWaybillCar(detailState, taskCar);

                dto.setWaybillCarId(taskCar.getWaybillCarId());
                WaybillCar waybillCar = waybillCarDao.selectWaybillCar(dto);
                if (waybillCar != null) {
                    carDetailVo = new CarDetailVo();
                    BeanUtils.copyProperties(waybillCar,carDetailVo);
                    freightFee = freightFee.add(waybillCar.getFreightFee());

                    // 指导路线,运单是提车或者送车，将始发成和结束城市用“-”拼接
                    carDetailVo.setGuideLine(waybillCar.getStartCity() + "-" + waybillCar.getEndCity());

                    // 获取车辆运输图片
                    getCarPhotoImg(carDetailVo, detailState, waybillCar);

                    // 查询品牌车系信息
                    OrderCar orderCar = orderCarDao.selectById(waybillCar.getOrderCarId());
                    BeanUtils.copyProperties(orderCar,carDetailVo);

                    // 查询车辆logo图片
                    String logoImg = carSeriesDao.getLogoImgByBraMod(carDetailVo.getBrand(),carDetailVo.getModel());
                    carDetailVo.setLogoPhotoImg(LogoImgProperty.logoImg+logoImg);

                    // 查询支付类型
                    Order order = orderDao.selectById(orderCar.getOrderId());
                    carDetailVo.setPayType(order.getPayType());

                    // 设置任务单车辆ID和运单车辆状态
                    carDetailVo.setId(taskCar.getId());
                    carDetailVo.setWaybillCarState(waybillCar.getState());
                    carDetailVoList.add(carDetailVo);

                    // todo 测试车辆图片
                    String[] split = carDetailVo.getHistoryLoadPhotoImg().split(",");
                    log.info("===>历史图片数量："+split.length);
                    String[] string1 = StringUtils.isEmpty(carDetailVo.getLoadPhotoImg()) ? new String[]{}
                            : carDetailVo.getLoadPhotoImg().split(",");
                    log.info("===>提车图片数量："+string1.length);
                    String[] string2 = StringUtils.isEmpty(carDetailVo.getUnloadPhotoImg()) ? new String[]{}
                            : carDetailVo.getUnloadPhotoImg().split(",");
                    log.info("===>收车图片数量："+string2.length);
                }
            }
        }
        taskDetailVo.setFreightFee(freightFee);
        taskDetailVo.setCarDetailVoList(carDetailVoList);
        return BaseResultUtil.success(taskDetailVo);
    }

    private void getCarPhotoImg(CarDetailVo carDetailVo, String detailState, WaybillCar waybillCar) {
        // 查询车辆历史图片
        StringBuilder sb = getCarHistoryPhotoImg(waybillCar);

        // 封装当前车辆图片
        fillCarPhotoImg(detailState, waybillCar, sb);

        // 封装历史图片
        carDetailVo.setHistoryLoadPhotoImg(sb.toString());
    }


    private void fillCarPhotoImg(String detailState, WaybillCar waybillCar, StringBuilder sb) {
        if (FieldConstant.WAIT_TO_CAR.equals(detailState)
                || FieldConstant.FINISH_PUT_OUT.equals(detailState)
                || FieldConstant.FINISH_CAR.equals(detailState)
                || FieldConstant.WAIT_PUT_IN.equals(detailState)
                || FieldConstant.FINISH_PUT_IN.equals(detailState)) {
            // 待交车 待入库 已出库 已交付 已入库
            String loadPhotoImg = waybillCar.getLoadPhotoImg();
            if (sb.length() > 0 && !StringUtils.isEmpty(loadPhotoImg)) {
                sb.append(",");
            }
            if (!StringUtils.isEmpty(loadPhotoImg)) {
                sb.append(loadPhotoImg);
            }
        }

        if (FieldConstant.FINISH_CAR.equals(detailState)
                || FieldConstant.FINISH_PUT_IN.equals(detailState)) {
            // 已交付 已入库
            String unloadPhotoImg = waybillCar.getUnloadPhotoImg();
            if (sb.length() > 0 && !StringUtils.isEmpty(unloadPhotoImg)) {
                sb.append(",");
            }
            if (!StringUtils.isEmpty(unloadPhotoImg)) {
                sb.append(unloadPhotoImg);
            }
        }
    }

    private StringBuilder getCarHistoryPhotoImg(WaybillCar waybillCar) {
        LambdaQueryWrapper<WaybillCar> query = new QueryWrapper<WaybillCar>().lambda()
                .eq(WaybillCar::getOrderCarId, waybillCar.getOrderCarId())
                .lt(WaybillCar::getId, waybillCar.getId());
        List<WaybillCar> waybillCarList = waybillCarDao.selectList(query);
        StringBuilder sb = new StringBuilder();
        if (!CollectionUtils.isEmpty(waybillCarList)) {
            for (WaybillCar car : waybillCarList) {
                String loadPhotoImg = car.getLoadPhotoImg();
                String unloadPhotoImg = car.getUnloadPhotoImg();
                if (sb.length() > 0 && !StringUtils.isEmpty(loadPhotoImg)) {
                    sb.append(",");
                }
                if (!StringUtils.isEmpty(loadPhotoImg)) {
                    sb.append(loadPhotoImg);
                }
                if (sb.length() > 0 && !StringUtils.isEmpty(unloadPhotoImg)) {
                    sb.append(",");
                }
                if (!StringUtils.isEmpty(unloadPhotoImg)) {
                    sb.append(unloadPhotoImg);
                }
            }
        }
        return sb;
    }

    public WaybillCar getWaybillCar(String detailState, TaskCar taskCar) {
        LambdaQueryWrapper<WaybillCar> query = new QueryWrapper<WaybillCar>().lambda().eq(WaybillCar::getId, taskCar.getWaybillCarId());
        if (FieldConstant.WAIT_GET_CAR.equals(detailState)) {
            // 待提车
            query = query.in(WaybillCar::getState, WaybillCarStateEnum.WAIT_LOAD.code,WaybillCarStateEnum.WAIT_LOAD_CONFIRM.code);
        } else if (FieldConstant.WAIT_TO_CAR.equals(detailState)) {
            // 待交车
            query = query.in(WaybillCar::getState, WaybillCarStateEnum.LOADED.code,WaybillCarStateEnum.WAIT_UNLOAD_CONFIRM.code);
        } else if (FieldConstant.FINISH_CAR.equals(detailState) || FieldConstant.FINISH_PUT_IN.equals(detailState)) {
            // 已交付 已入库
            query = query.eq(WaybillCar::getState, WaybillCarStateEnum.UNLOADED.code);
        } else if (FieldConstant.WAIT_PUT_IN.equals(detailState)) {
            // 待入库
            query = query.eq(WaybillCar::getState, WaybillCarStateEnum.WAIT_UNLOAD_CONFIRM.code);
        } else if (FieldConstant.WAIT_PUT_OUT.equals(detailState)) {
            // 待出库
            query = query.eq(WaybillCar::getState, WaybillCarStateEnum.WAIT_LOAD_CONFIRM.code);
        } else if (FieldConstant.FINISH_PUT_OUT.equals(detailState)) {
            // 已出库
            query = query.gt(WaybillCar::getState, WaybillCarStateEnum.WAIT_LOAD_CONFIRM.code);
        }

        return waybillCarDao.selectOne(query);
    }

    @Override
    public ResultVo<PageVo<TaskWaybillVo>> getOutAndInStoragePage(OutAndInStorageQueryDto dto) {
        if (dto.getInStorageTimeE() != null && dto.getInStorageTimeE() != 0) {
            dto.setInStorageTimeE(TimeStampUtil.convertEndTime(dto.getInStorageTimeE()));
        }
        if (dto.getOutStorageTimeE() != null && dto.getOutStorageTimeE() != 0) {
            dto.setOutStorageTimeE(TimeStampUtil.convertEndTime(dto.getOutStorageTimeE()));
        }
        Long creatTime = dto.getCreatTime();
        if (creatTime != null && creatTime != 0) {
            dto.setCreatTimeS(creatTime);
            dto.setCreatTimeE(TimeStampUtil.convertEndTime(creatTime));
        }

        // 根据登录ID查询当前业务员所在业务中心ID
        BizScope bizScope = csSysService.getBizScopeByLoginIdNew(dto.getLoginId(), true);

        // 判断当前登录人是否有权限访问
        if (BizScopeEnum.NONE.code == bizScope.getCode()) {
            return BaseResultUtil.fail("您没有访问权限!");
        }

        dto.setStoreIds(bizScope.getStoreIds());
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<TaskWaybillVo> list = taskDao.selectOutAndInStorageList(dto);
        PageInfo<TaskWaybillVo> pageInfo = new PageInfo<>(list);
        return BaseResultUtil.success(pageInfo);
    }

}
