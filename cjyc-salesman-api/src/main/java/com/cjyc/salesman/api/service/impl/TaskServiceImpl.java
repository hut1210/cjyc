package com.cjyc.salesman.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.driver.task.DetailQueryDto;
import com.cjyc.common.model.dto.salesman.task.TaskQueryConditionDto;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.driver.task.CarDetailVo;
import com.cjyc.common.model.vo.driver.task.TaskDetailVo;
import com.cjyc.common.model.vo.salesman.task.TaskWaybillVo;
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

    @Override
    public ResultVo<PageVo<TaskWaybillVo>> getCarryPage(TaskQueryConditionDto dto) {
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
        if (waybillId == 0) {
            log.error("运单ID参数错误");
            return BaseResultUtil.fail("运单ID参数错误");
        }
        Waybill waybill = waybillDao.selectById(waybillId);
        if (waybill == null) {
            log.error("未查询到运单信息");
            return BaseResultUtil.fail("未查询到运单信息");
        }
        taskDetailVo.setType(waybill.getType());

        // 查询车辆信息
        List<CarDetailVo> carDetailVoList = new ArrayList<>(10);
        // 查询任务单信息信息
        Long taskId = dto.getTaskId();
        if (taskId == 0) {
            log.error("任务单ID参数错误");
            return BaseResultUtil.fail("任务单ID参数错误");
        }
        Task task = taskDao.selectById(taskId);
        if (task == null) {
            log.error("未查询到任务单信息");
            return BaseResultUtil.fail("未查询到任务单信息");
        }
        taskDetailVo.setGuideLine(task.getGuideLine());
        taskDetailVo.setCreateTime(task.getCreateTime());
        taskDetailVo.setNo(task.getNo());

        LambdaQueryWrapper<TaskCar> queryWrapper = new QueryWrapper<TaskCar>().lambda().eq(TaskCar::getTaskId,taskId);
        List<TaskCar> taskCarList = taskCarDao.selectList(queryWrapper);
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
                carDetailVo.setCompleteTime(task.getCompleteTime());
                // 查询支付方式
                Order order = orderDao.selectById(orderCar.getOrderId());
                carDetailVo.setPayType(order.getPayType());

                carDetailVo.setId(waybillCar.getId());
                carDetailVoList.add(carDetailVo);
            }
        }
        taskDetailVo.setFreightFee(freightFee);
        taskDetailVo.setCarDetailVoList(carDetailVoList);
        return BaseResultUtil.success(taskDetailVo);
    }
}
