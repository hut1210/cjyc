package com.cjyc.common.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjkj.common.model.ReturnMsg;
import com.cjyc.common.model.constant.FieldConstant;
import com.cjyc.common.model.dao.ITaskDao;
import com.cjyc.common.model.dao.IVehicleDao;
import com.cjyc.common.model.dao.IWaybillCarDao;
import com.cjyc.common.model.dto.LocationInfoDto;
import com.cjyc.common.model.dto.LogisticsInformationDto;
import com.cjyc.common.model.entity.Task;
import com.cjyc.common.model.entity.Vehicle;
import com.cjyc.common.model.enums.task.TaskStateEnum;
import com.cjyc.common.model.enums.transport.VehicleOwnerEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.LogisticsInformationVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.order.OutterLogVo;
import com.cjyc.common.model.vo.customer.order.OutterOrderCarLogVo;
import com.cjyc.common.model.vo.salesman.task.TaskInfo;
import com.cjyc.common.system.dto.location.ResultData;
import com.cjyc.common.system.dto.location.UploadUserLocationReq;
import com.cjyc.common.system.feign.ISysLocationService;
import com.cjyc.common.system.service.ICsLogisticsInformationService;
import com.cjyc.common.system.service.ICsOrderCarLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
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
        if (data != null && FieldConstant.IN_TRANSIT.equals(data.getOutterState())) {
            getLocation(reqDto, logisticsInfoVo);
        } else {
            log.info("===当前车辆不在运输中---未查询实时位置信息===,车辆编号：{}",reqDto.getOrderCarNo());
        }

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

            if (isSuccess(resultData)) {
                log.info("===用户位置信息上传成功===");
                return BaseResultUtil.success();
            }

            log.info("===用户位置信息上传失败===");
            return BaseResultUtil.fail("用户位置信息上传失败");
        }

        log.info("===当前用户没有运输任务---未上传用户位置信息===,用户ID：{}",reqDto.getLoginId());
        return BaseResultUtil.success("当前用户没有运输任务，未上传用户位置信息");
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
        // 查询运输车 车牌号 与 司机ID
        TaskInfo taskInfo = waybillCarDao.selectTaskInfoByOrderCarNo(reqDto.getOrderCarNo());
        if (taskInfo == null) {
            return;
        }

        // 查询运输车信息
        Vehicle vehicle = vehicleDao.selectOne(new QueryWrapper<Vehicle>().lambda()
                .eq(Vehicle::getPlateNo, taskInfo.getPlateNo()).select(Vehicle::getOwnershipType));

        // 判断运输车车牌号是否为自营车辆，是自营车则根据车牌号车讯位置，否则根据司机信息查询
        OutterOrderCarLogVo locationVo = null;
        boolean isOwnerCar = vehicle != null && VehicleOwnerEnum.SELFBUSINESS.code == vehicle.getOwnershipType();
        // 是自营车，根据车牌号查询位置信息
        if (isOwnerCar) {
            JSONObject obj = new JSONObject();
            obj.put("plateNo",taskInfo.getPlateNo());

            log.info("===>调用位置服务平台接口-根据车牌号-查询位置信息开始,请求参数：{}", JSON.toJSONString(obj));
            ResultData resultData = sysLocationService.getLocationByPlateNo(obj);
            log.info("<===调用位置服务平台接口-根据车牌号-查询位置信息结束,响应参数：{}", JSON.toJSONString(resultData));

            if (isSuccess(resultData)) {
                log.info("===根据车牌号-查询位置信息成功===");
                locationVo = getOutterOrderCarLogVo(locationVo, resultData, "sendTime");
            } else {
                log.info("===根据车牌号-查询位置信息失败===");
            }
        }

        // 非自营车，根据司机信息查询APP定位信息
        if (!isOwnerCar || locationVo == null) {
            JSONObject object = new JSONObject();
            object.put("userId",taskInfo.getDriverId());
            log.info("===>调用位置服务平台接口-根据用户ID-查询用户实时位置开始,请求参数：{}", JSON.toJSONString(object));
            ResultData resultData = sysLocationService.getUserLocation(object);
            log.info("<===调用位置服务平台接口-根据用户ID-查询用户实时位置结束,响应参数：{}", JSON.toJSONString(resultData));

            if (isSuccess(resultData)) {
                log.info("===根据用户ID-查询用户实时位置成功===");
                locationVo = getOutterOrderCarLogVo(locationVo, resultData, "gpsTime");
            } else {
                log.info("===根据用户ID-查询用户实时位置失败===");
            }
        }

        if (locationVo != null) {
            List<OutterOrderCarLogVo> list = logisticsInfoVo.getList();
            list.add(0,locationVo);
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

    /**
     * ResultData是否处理成功
     * @param resultData
     * @return
     */
    public boolean isSuccess(ResultData resultData) {
        if (resultData == null) {
            return false;
        }
        if (ReturnMsg.SUCCESS.getCode().equals(resultData.getCode())) {
            return true;
        }
        return false;
    }

}
