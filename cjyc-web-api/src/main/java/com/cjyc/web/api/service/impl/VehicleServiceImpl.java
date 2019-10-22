package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.constant.PatternConstant;
import com.cjyc.common.model.dao.IDriverVehicleConDao;
import com.cjyc.common.model.dao.IVehicleDao;
import com.cjyc.common.model.dao.IVehicleRunningDao;
import com.cjyc.common.model.dto.web.vehicle.SelectVehicleDto;
import com.cjyc.common.model.dto.web.vehicle.VehicleDto;
import com.cjyc.common.model.entity.DriverVehicleCon;
import com.cjyc.common.model.entity.Vehicle;
import com.cjyc.common.model.entity.VehicleRunning;
import com.cjyc.common.model.enums.SysEnum;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.web.vehicle.VehicleVo;
import com.cjyc.web.api.exception.CommonException;
import com.cjyc.web.api.service.IVehicleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
public class VehicleServiceImpl extends ServiceImpl<IVehicleDao, Vehicle> implements IVehicleService {

    @Resource
    private IVehicleDao iVehicleDao;

    @Resource
    private IVehicleRunningDao iVehicleRunningDao;

    @Resource
    private IDriverVehicleConDao iDriverVehicleConDao;

    @Override
    public boolean saveVehicle(VehicleDto dto) {
        try{
            //保存运输车辆信息
            Vehicle vehicle = new Vehicle();
            vehicle = encapVehicle(vehicle,dto);
            int i = iVehicleDao.insert(vehicle);
            if(i > 0){
                //保存运行运力池
                VehicleRunning vr = new VehicleRunning();
                vr = encapVehicleRunning(vr,dto,vehicle);
                int num = iVehicleRunningDao.insert(vr);
                if(num > 0){
                    //保存司机与运力关系
                    DriverVehicleCon dvc = new DriverVehicleCon();
                    dvc.setDriverId(Long.valueOf(dto.getDriverId()));
                    dvc.setVehicleId(vr.getVehicleId());
                   return iDriverVehicleConDao.insert(dvc) > 0 ? true:false;
                }
            }
        }catch (Exception e){
            log.info("新增车辆信息出现异常");
            throw new CommonException(e.getMessage());
        }
        return false;
    }

    @Override
    public VehicleVo showVehicle(Long vehicleId) {
        try{
            VehicleVo vehicleVo = iVehicleDao.getVehicleById(vehicleId);
            if(vehicleVo != null){
                return vehicleVo;
            }
        }catch (Exception e){
            log.info("根据id查看车辆信息出现异常");
        }
        return null;
    }

    @Override
    public boolean updateVehicle(VehicleDto dto) {
        try{
            //更新车辆信息
            Vehicle vehicle = iVehicleDao.selectById(dto.getId());
            vehicle = encapVehicle(vehicle,dto);
            int i = iVehicleDao.updateById(vehicle);
            if(i > 0){
                //更新运行运力池
                VehicleRunning vr = iVehicleRunningDao.getVehiRunByVehiId(dto.getId());
                vr = encapVehicleRunning(vr,dto,vehicle);
                int num = iVehicleRunningDao.updateById(vr);
                if(num > 0){
                    //更新司机与运力关系
                    DriverVehicleCon dvc = iDriverVehicleConDao.getDriverVehicleCon(dto.getId());
                    dvc.setDriverId(Long.valueOf(dto.getDriverId()));
                    dvc.setVehicleId(Long.valueOf(dto.getId()));
                    return iDriverVehicleConDao.updateById(dvc) > 0 ? true:false;
                }
            }
        }catch (Exception e){
            log.info("根据车辆id更新车辆信息出现异常");
            throw new CommonException(e.getMessage());
        }
        return false;
    }

    @Override
    public PageInfo<VehicleVo> getVehicleByTerm(SelectVehicleDto dto) {
        PageInfo<VehicleVo> pageInfo = null;
        try{
            List<VehicleVo> vehicleVos = iVehicleDao.getVehicleByTerm(dto);
            if(vehicleVos != null && vehicleVos.size() > 0){
                for(VehicleVo vo : vehicleVos){
                    vo.setCreateName(LocalDateTimeUtil.convertToString(Long.valueOf(vo.getCreateTime()), PatternConstant.COMPLEX_TIME_FORMAT));
                }
                PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
                pageInfo = new PageInfo<>(vehicleVos);
            }
        }catch (Exception e){
            log.info("根据条件查询车辆信息出现异常");
        }
        return pageInfo;
    }

    @Override
    public boolean delVehicleByIds(List<Long> ids) {
        int i = 0;
        int j = 0;
        try{
            //删除车辆与绑定信息
            for(Long id : ids){
                i = iVehicleRunningDao.delVehicleRunByVehId(id);
                j = iDriverVehicleConDao.delDriverVehConByVehId(id);
            }
            //删除车辆信息
            if(i == j && i == ids.size() && j == ids.size()){
               return iVehicleDao.deleteBatchIds(ids) == ids.size() ? true : false;
            }
        }catch (Exception e){
            log.info("根据车辆ids删除车辆信息出现异常");
            throw new CommonException(e.getMessage());
        }
        return false;
    }

    /**
     * 封装车辆信息
     * @param v
     * @param dto
     * @return
     */
    private Vehicle encapVehicle(Vehicle v,VehicleDto dto){
        try{
            v.setPlateNo(dto.getPlateNo());
            v.setDefaultCarryNum(Integer.valueOf(dto.getDefaultCarryNum()));
            v.setOwnershipType(Integer.valueOf(dto.getOwnershipType()));
            v.setState(Integer.valueOf(SysEnum.ZERO.getValue()));
            v.setCreateUserId(Long.valueOf(dto.getUserId()));
            v.setCreateTime(LocalDateTimeUtil.convertToLong(LocalDateTimeUtil.formatLDTNow(PatternConstant.COMPLEX_TIME_FORMAT),
                    PatternConstant.COMPLEX_TIME_FORMAT));
        }catch (Exception e){
            log.info("封装车辆信息出现异常");
        }
        return v;
    }

    private VehicleRunning encapVehicleRunning(VehicleRunning vr,VehicleDto dto,Vehicle vehicle){
        try{
            vr.setDriverId(Long.valueOf(dto.getDriverId()));
            vr.setVehicleId(vehicle.getId());
            vr.setPlateNo(dto.getPlateNo());
            vr.setCarryCarNum(Integer.valueOf(dto.getDefaultCarryNum()));
            vr.setState(Integer.valueOf(SysEnum.ONE.getValue()));
            vr.setRunningState(Integer.valueOf(SysEnum.ZERO.getValue()));
            vr.setCreateTime(LocalDateTimeUtil.convertToLong(LocalDateTimeUtil.formatLDTNow(PatternConstant.COMPLEX_TIME_FORMAT),
                    PatternConstant.COMPLEX_TIME_FORMAT));
        }catch (Exception e){
            log.info("封装运力信息出现异常");
        }
        return vr;
    }
}