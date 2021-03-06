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
import com.cjyc.common.model.enums.waybill.WaybillCarrierTypeEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.JsonUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        log.info("====>业务员端-分页查询提送车；提送车历史记录列表,请求json数据 :: "+ JsonUtils.objectToJson(dto));
        convertTime(dto);// 转换时间
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<TaskWaybillVo> list = taskDao.selectCarryList(dto);
        PageInfo<TaskWaybillVo> pageInfo = new PageInfo<>(list);
        return BaseResultUtil.success(pageInfo);
    }

    private void convertTime(TaskWaybillQueryDto dto) {
        if (dto.getCompleteTimeE() != null && dto.getCompleteTimeE() != 0) {
            dto.setCompleteTimeE(TimeStampUtil.convertEndTime(dto.getCompleteTimeE()));
        }
        Long creatTime = dto.getCreatTime();
        if (creatTime != null && creatTime != 0) {
            dto.setCreatTimeS(creatTime);
            dto.setCreatTimeE(TimeStampUtil.convertEndTime(creatTime));
        }
    }

    @Override
    public ResultVo<TaskDetailVo> getCarryDetail(DetailQueryDto dto) {
        log.info("====>业务员端-查询提送车,提送车历史记录任务详情；查询出入库,出入库历史记录任务详情,请求json数据 :: "+JsonUtils.objectToJson(dto));
        TaskDetailVo taskDetailVo = new TaskDetailVo();
        // 查询运单信息
        Waybill waybill = waybillDao.selectById(dto.getWaybillId());
        if (waybill == null) {
            log.error("===>查询运单为空...");
            return BaseResultUtil.fail("查询运单为空");
        }
        taskDetailVo.setType(waybill.getType());
        taskDetailVo.setCarrierType(waybill.getCarrierType());

        // 查询任务单信息
        Task task = taskDao.selectById(dto.getTaskId());
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
        LambdaQueryWrapper<TaskCar> queryWrapper = new QueryWrapper<TaskCar>().lambda().eq(TaskCar::getTaskId,dto.getTaskId());
        List<TaskCar> taskCarList = taskCarDao.selectList(queryWrapper);

        // 查询车辆信息
        List<CarDetailVo> carDetailVoList = new ArrayList<>(10);
        BigDecimal freightFee = BigDecimal.ZERO;
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
                dto.setWaybillCarId(taskCar.getWaybillCarId());
                WaybillCar waybillCar = waybillCarDao.selectWaybillCar(dto);
                if (waybillCar != null) {
                    carDetailVo = new CarDetailVo();
                    BeanUtils.copyProperties(waybillCar,carDetailVo);
                    freightFee = freightFee.add(waybillCar.getFreightFee());

                    // 给详细地址拼接市区
                    carDetailVo.setStartAddress(waybillCar.getStartCity()+waybillCar.getStartArea()+waybillCar.getStartAddress());
                    carDetailVo.setEndAddress(waybillCar.getEndCity()+waybillCar.getEndArea()+waybillCar.getEndAddress());


                    // 指导路线,运单是提车或者送车，将始发成和结束城市用“-”拼接
                    carDetailVo.setGuideLine(waybillCar.getStartCity() + "-" + waybillCar.getEndCity());

                    // 获取车辆运输图片
                    getCarPhotoImg(carDetailVo, dto.getDetailState(), waybillCar,waybill);

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
                }
            }
        }
        taskDetailVo.setFreightFee(freightFee);
        taskDetailVo.setCarDetailVoList(carDetailVoList);
        return BaseResultUtil.success(taskDetailVo);
    }

    private void getCarPhotoImg(CarDetailVo carDetailVo, String detailState, WaybillCar waybillCar,Waybill waybill) {
        // 查询车辆历史图片
        StringBuilder sb = getCarHistoryPhotoImg(waybillCar,waybill);

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

    private StringBuilder getCarHistoryPhotoImg(WaybillCar waybillCar,Waybill waybill) {
        Map<String,Object> map = new HashMap<>(3);
        map.put("orderCarId",waybillCar.getOrderCarId());
        map.put("waybillCarId",waybillCar.getId());
        map.put("waybillType",waybill.getType());
        List<WaybillCar> waybillCarList = waybillCarDao.selectWaybillCarList(map);
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

    @Override
    public ResultVo<PageVo<TaskWaybillVo>> getOutAndInStoragePage(OutAndInStorageQueryDto dto) {
        log.info("====>业务员端-分页查询出入库，出入库历史记录列表,请求json数据 :: "+JsonUtils.objectToJson(dto));
        // 转换日期
        convertTime(dto);

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

    private void convertTime(OutAndInStorageQueryDto dto) {
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
    }

}
