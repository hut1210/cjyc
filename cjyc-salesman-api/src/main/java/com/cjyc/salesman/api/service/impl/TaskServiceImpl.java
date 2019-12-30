package com.cjyc.salesman.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.driver.task.DetailQueryDto;
import com.cjyc.common.model.dto.salesman.task.OutAndInStorageQueryDto;
import com.cjyc.common.model.dto.salesman.task.TaskWaybillQueryDto;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.entity.defined.BizScope;
import com.cjyc.common.model.enums.BizScopeEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.TimeStampUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.driver.task.CarDetailVo;
import com.cjyc.common.model.vo.driver.task.TaskDetailVo;
import com.cjyc.common.model.vo.salesman.task.TaskWaybillVo;
import com.cjyc.common.system.service.sys.ICsSysService;
import com.cjyc.salesman.api.service.ITaskService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    @Override
    public ResultVo<PageVo<TaskWaybillVo>> getCarryPage(TaskWaybillQueryDto dto) {
        if (dto.getCompleteTimeE() != null && dto.getCompleteTimeE() != 0) {
            dto.setCompleteTimeE(TimeStampUtil.convertEndTime(dto.getCompleteTimeE()));
        }
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<TaskWaybillVo> list = taskDao.selectCarryList(dto);
        PageInfo<TaskWaybillVo> pageInfo = new PageInfo<>(list);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<TaskDetailVo> getCarryDetail(DetailQueryDto dto) {
        TaskDetailVo taskDetailVo = new TaskDetailVo();
        // 查询运单类型
        Long waybillId = dto.getWaybillId();
        if (waybillId == 0) {
            log.error("运单ID参数错误");
            return BaseResultUtil.fail("运单ID参数错误");
        }
        Waybill waybill = waybillDao.selectById(waybillId);
        taskDetailVo.setType(waybill.getType());

        // 查询任务单信息
        Long taskId = dto.getTaskId();
        if (taskId == 0) {
            log.error("任务单ID参数错误");
            return BaseResultUtil.fail("任务单ID参数错误");
        }
        Task task = taskDao.selectById(taskId);
        BeanUtils.copyProperties(task,taskDetailVo);

        // 任务单车辆
        LambdaQueryWrapper<TaskCar> queryWrapper = new QueryWrapper<TaskCar>().lambda().eq(TaskCar::getTaskId,taskId);
        List<TaskCar> taskCarList = taskCarDao.selectList(queryWrapper);

        // 查询车辆信息
        List<CarDetailVo> carDetailVoList = new ArrayList<>(10);
        BigDecimal freightFee = new BigDecimal(0);
        if (!CollectionUtils.isEmpty(taskCarList)) {
            CarDetailVo carDetailVo = null;
            for (TaskCar taskCar : taskCarList) {
                // 查询任务单车辆信息
                WaybillCar waybillCar = waybillCarDao.selectById(taskCar.getWaybillCarId());
                carDetailVo = new CarDetailVo();
                BeanUtils.copyProperties(waybillCar,carDetailVo);
                freightFee = freightFee.add(waybillCar.getFreightFee());

                // 查询品牌车系信息
                OrderCar orderCar = orderCarDao.selectById(waybillCar.getOrderCarId());
                BeanUtils.copyProperties(orderCar,carDetailVo);

                // 查询支付方式
                Order order = orderDao.selectById(orderCar.getOrderId());
                carDetailVo.setPayType(order.getPayType());

                carDetailVo.setId(taskCar.getId());
                carDetailVoList.add(carDetailVo);
            }
        }
        taskDetailVo.setFreightFee(freightFee);
        taskDetailVo.setCarDetailVoList(carDetailVoList);
        return BaseResultUtil.success(taskDetailVo);
    }

    @Override
    public ResultVo<PageVo<TaskWaybillVo>> getOutAndInStoragePage(OutAndInStorageQueryDto dto) {
        if (dto.getInStorageTimeE() != null && dto.getInStorageTimeE() != 0) {
            dto.setInStorageTimeE(TimeStampUtil.convertEndTime(dto.getInStorageTimeE()));
        }
        if (dto.getOutStorageTimeE() != null && dto.getOutStorageTimeE() != 0) {
            dto.setOutStorageTimeE(TimeStampUtil.convertEndTime(dto.getOutStorageTimeE()));
        }

        // 根据登录ID查询当前业务员所在业务中心ID
        BizScope bizScope = csSysService.getBizScopeByLoginId(dto.getLoginId(), true);

        // 判断当前登录人是否有权限访问
        int code = bizScope.getCode();
        if (BizScopeEnum.NONE.code == code) {
            return BaseResultUtil.fail("您没有访问权限!");
        }

        dto.setStoreId(getStoreIds(bizScope));
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<TaskWaybillVo> list = taskDao.selectOutAndInStorageList(dto);
        PageInfo<TaskWaybillVo> pageInfo = new PageInfo<>(list);
        return BaseResultUtil.success(pageInfo);
    }

    private String getStoreIds(BizScope bizScope) {
        if (bizScope.getCode() == BizScopeEnum.CHINA.code) {
            return null;
        }
        Set<Long> storeIds = bizScope.getStoreIds();
        StringBuilder sb = new StringBuilder();
        for (Long storeId : storeIds) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(storeId);
        }
        return sb.toString();
    }
}
