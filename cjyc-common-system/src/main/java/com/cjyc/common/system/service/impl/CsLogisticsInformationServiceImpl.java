package com.cjyc.common.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjkj.common.model.ResultData;
import com.cjyc.common.model.dao.IOrderCarDao;
import com.cjyc.common.model.dao.ITaskDao;
import com.cjyc.common.model.dao.IVehicleDao;
import com.cjyc.common.model.dao.IWaybillCarDao;
import com.cjyc.common.model.dto.LocationInfoDto;
import com.cjyc.common.model.dto.LogisticsInformationDto;
import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.common.model.entity.Task;
import com.cjyc.common.model.entity.Vehicle;
import com.cjyc.common.model.enums.order.OrderCarStateEnum;
import com.cjyc.common.model.enums.task.TaskStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.LogisticsInformationVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.order.OutterLogVo;
import com.cjyc.common.model.vo.customer.order.OutterOrderCarLogVo;
import com.cjyc.common.system.entity.UploadUserLocationReq;
import com.cjyc.common.system.feign.ISysLocationService;
import com.cjyc.common.system.service.ICsLogisticsInformationService;
import com.cjyc.common.system.service.ICsOrderCarLogService;
import com.cjyc.common.system.util.ResultDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Resource
    private ISysLocationService sysLocationService;

    @Override
    public ResultVo<LogisticsInformationVo> getLogisticsInformation(LogisticsInformationDto reqDto) {
        LogisticsInformationVo logisticsInfoVo = new LogisticsInformationVo();
        // 查询车辆运输日志
        ResultVo<OutterLogVo> orderCarLog = csOrderCarLogService.getOrderCarLog(reqDto.getOrderCarNo());
        OutterLogVo data = orderCarLog.getData();
        if (data != null) {
            BeanUtils.copyProperties(data,logisticsInfoVo);
        }

        // 查询车辆实时位置
        getLocation(reqDto, logisticsInfoVo);
        return BaseResultUtil.success(logisticsInfoVo);
    }

    @Override
    public ResultVo uploadUserLocation(LocationInfoDto reqDto) {
        // 判断任务单状态是否为“运输中”状态，状态为“运输中”时才保存位置信息
        List<Task> taskList = taskDao.selectList(new QueryWrapper<Task>().lambda()
                .eq(Task::getDriverId, reqDto.getLoginId())
                .eq(Task::getState, TaskStateEnum.TRANSPORTING.code));
        if (!CollectionUtils.isEmpty(taskList)) {
            // 调用位置服务位置平台同步接口
            UploadUserLocationReq uploadUserLocationReq = getUploadUserLocationReq(reqDto);

            log.info("===>调用位置服务平台接口-用户位置信息上传开始,请求参数：{}", JSON.toJSONString(uploadUserLocationReq));
            ResultData resultData = sysLocationService.uploadUserLocation(uploadUserLocationReq);
            log.info("<===调用位置服务平台接口-用户位置信息上传结束,响应参数：{}", JSON.toJSONString(resultData));

            if (ResultDataUtil.isSuccess(resultData)) {
                log.info("===用户位置信息上传成功===");
                return BaseResultUtil.success();
            }

            log.info("===用户位置信息上传失败===");
            return BaseResultUtil.fail("用户位置信息上传失败");
        }

        log.info("===当前用户没有运输任务---未调用位置服务平台接口===");
        return BaseResultUtil.success("当前用户没有运输任务");
    }

    private UploadUserLocationReq getUploadUserLocationReq(LocationInfoDto reqDto) {
        UploadUserLocationReq uploadUserLocationReq = new UploadUserLocationReq();
        uploadUserLocationReq.setUserId(String.valueOf(reqDto.getLoginId()));
        uploadUserLocationReq.setCoordinateType(reqDto.getCoordinateType());
        uploadUserLocationReq.setLat(reqDto.getLat());
        uploadUserLocationReq.setLng(reqDto.getLng());
        uploadUserLocationReq.setGpsTime(LocalDateTimeUtil.formatLong(reqDto.getGpsTime(),"yyyy-MM-dd HH:mm:ss"));
        uploadUserLocationReq.setSendTime(LocalDateTimeUtil.formatLong(reqDto.getSendTime(),"yyyy-MM-dd HH:mm:ss"));
        return uploadUserLocationReq;
    }

    private void getLocation(LogisticsInformationDto reqDto, LogisticsInformationVo logisticsInfoVo) {
        // 判断车辆状态
        OrderCar orderCar = orderCarDao.selectOne(new QueryWrapper<OrderCar>().lambda()
                .eq(OrderCar::getNo, reqDto.getOrderCarNo()));
        if (orderCar != null) {
            // 车辆状态为 “运输中” 时，查询位置信息
            boolean isTransport = orderCar.getState() > OrderCarStateEnum.WAIT_PICK.code && orderCar.getState() < OrderCarStateEnum.SIGNED.code;
            if (isTransport) {
                // 查询运输车车牌号与司机ID
                Map<String, Object> map = waybillCarDao.selectPlateNoByOrderCarNo(reqDto.getOrderCarNo());
                String plateNo = map == null ? "" : String.valueOf(map.get("plateNo"));

                // 查询运输车信息
                Vehicle vehicle = vehicleDao.selectOne(new QueryWrapper<Vehicle>().lambda()
                        .eq(Vehicle::getPlateNo, plateNo).select(Vehicle::getOwnershipType));

                // 判断运输车车牌号是否为自营车辆，是自营车则根据车牌号车讯位置，否则根据司机信息查询
                OutterOrderCarLogVo locationVo = null;
                //boolean isOwnerCar = vehicle != null && VehicleOwnerEnum.SELFBUSINESS.code == vehicle.getOwnershipType();
                boolean isOwnerCar = false;
                if (isOwnerCar) {
                    // 是自营车，根据车牌号查询位置信息
                    log.info("===>调用位置服务平台接口-根据车牌号查询位置信息开始,请求参数：{}", JSON.toJSONString(plateNo));
                    ResultData resultData = sysLocationService.getLocationByPlateNo(plateNo);
                    log.info("<===调用位置服务平台接口-根据车牌号查询位置信息结束,响应参数：{}", JSON.toJSONString(resultData));

                    if (ResultDataUtil.isSuccess(resultData)) {
                        log.info("===根据车牌号查询位置信息成功===");
                        locationVo = getOutterOrderCarLogVo(locationVo, resultData, "sendTime");

                    } else {
                        log.info("===根据车牌号查询位置信息失败===");
                    }
                }

                if (!isOwnerCar || locationVo == null) {
                    // 非自营车，根据司机信息查询APP定位信息
                    String userId = map == null ? "" : String.valueOf(map.get("driverId"));
                    log.info("===>调用位置服务平台接口-查询用户实时位置开始,请求参数：{}", JSON.toJSONString(userId));
                    //ResultData resultData = sysLocationService.getUserLocation(userId);

                    Map map1 = new HashMap(10);
                    map1.put("location","北京市 朝阳区 石各庄110号 长久大厦");
                    map1.put("gpsTime","2020-04-09 17:25:24");
                    ResultData resultData = new ResultData("00000","",map1);
                    log.info("<===调用位置服务平台接口-查询用户实时位置结束,响应参数：{}", JSON.toJSONString(resultData));

                    if (ResultDataUtil.isSuccess(resultData)) {
                        log.info("===查询用户实时位置成功===");
                        locationVo = getOutterOrderCarLogVo(locationVo, resultData, "gpsTime");
                    } else {
                        log.info("===查询用户实时位置失败===");
                    }

                }

                if (locationVo != null) {
                    List<OutterOrderCarLogVo> list = logisticsInfoVo.getList();
                    list.add(0,locationVo);
                }

            }
        }
    }

    private OutterOrderCarLogVo getOutterOrderCarLogVo(OutterOrderCarLogVo locationVo, ResultData resultData, String gpsTime) {
        Map resMap = JSON.parseObject(JSON.toJSONString(resultData.getData()), Map.class);
        String location = resMap == null ? "" : String.valueOf(resMap.get("location"));
        String sendTime = resMap == null ? null : String.valueOf(resMap.get(gpsTime));

        if (!StringUtils.isEmpty(sendTime) && !StringUtils.isEmpty(location)) {
            locationVo = new OutterOrderCarLogVo();
            locationVo.setTypeStr("实时位置");
            locationVo.setCreateTime(LocalDateTimeUtil.convertDateStrToLong(sendTime, "yyyy-MM-dd HH:mm:ss"));
            locationVo.setOuterLog(location);
            locationVo.setTag(1);
        }
        return locationVo;
    }

}
