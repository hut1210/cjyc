package com.cjyc.common.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjyc.common.model.dao.IOrderCarDao;
import com.cjyc.common.model.dao.ITaskDao;
import com.cjyc.common.model.dao.IVehicleDao;
import com.cjyc.common.model.dao.IWaybillCarDao;
import com.cjyc.common.model.dto.LogisticsInformationDto;
import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.common.model.entity.Vehicle;
import com.cjyc.common.model.enums.order.OrderCarStateEnum;
import com.cjyc.common.model.enums.transport.VehicleOwnerEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.LogisticsInformationVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.order.OutterLogVo;
import com.cjyc.common.system.service.ICsLogisticsInformationService;
import com.cjyc.common.system.service.ICsOrderCarLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * @Description 物流信息接口实现类
 * @Author Liu Xing Xiang
 * @Date 2020/4/3 11:27
 **/
@Slf4j
@Service
public class CsLogisticsInformationServiceImpl implements ICsLogisticsInformationService {
    @Resource
    private ICsOrderCarLogService csOrderCarLogService;
    @Resource
    private IOrderCarDao orderCarDao;
    @Resource
    private ITaskDao taskDao;
    @Resource
    private IVehicleDao vehicleDao;
    @Resource
    private IWaybillCarDao waybillCarDao;

    @Override
    public ResultVo<LogisticsInformationVo> getLogisticsInformation(LogisticsInformationDto reqDto) {
        LogisticsInformationVo logisticsInfoVo = new LogisticsInformationVo();
        // 查询车辆运输日志
        ResultVo<OutterLogVo> orderCarLog = csOrderCarLogService.getOrderCarLog(reqDto.getOrderCarNo());
        BeanUtils.copyProperties(orderCarLog.getData(),logisticsInfoVo);
        // 查询车辆实时位置
        getLocation(reqDto, logisticsInfoVo);
        return BaseResultUtil.success(logisticsInfoVo);
    }

    private void getLocation(LogisticsInformationDto reqDto, LogisticsInformationVo logisticsInfoVo) {
        // 判断车辆状态
        OrderCar orderCar = orderCarDao.selectOne(new QueryWrapper<OrderCar>().lambda()
                .eq(OrderCar::getNo, reqDto.getOrderCarNo()));
        if (orderCar != null) {
            // 车辆状态为 “运输中” 时，查询位置信息
            boolean isTransport = orderCar.getState() > OrderCarStateEnum.WAIT_PICK.code && orderCar.getState() < OrderCarStateEnum.SIGNED.code;
            if (isTransport) {
                // 查询运输车车牌号
                String plateNo = waybillCarDao.selectPlateNoByOrderCarNo(reqDto.getOrderCarNo());

                // 查询运输车信息
                Vehicle vehicle = vehicleDao.selectOne(new QueryWrapper<Vehicle>().lambda()
                        .eq(Vehicle::getPlateNo, plateNo).select(Vehicle::getOwnershipType));

                // 判断运输车车牌号是否为自营车辆，是自营车则根据车牌号车讯位置，否则根据司机信息查询
                boolean isOwnerCar = vehicle != null && VehicleOwnerEnum.SELFBUSINESS.code == vehicle.getOwnershipType();
                String location = null;
                if (isOwnerCar) {
                    // 是自营车，根据车牌号查询位置信息


                    location = "";
                }

                if (!isOwnerCar || StringUtils.isEmpty(location)) {
                    // 非自营车，根据司机信息查询APP定位信息

                    location = "";
                }
                logisticsInfoVo.setLocation(location);
            }
        }
    }
}
