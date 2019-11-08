package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.ReturnMsg;
import com.cjkj.usercenter.dto.common.AddUserResp;
import com.cjkj.usercenter.dto.common.UpdateDeptReq;
import com.cjkj.usercenter.dto.common.UpdateUserReq;
import com.cjkj.usercenter.dto.yc.AddDeptAndUserReq;
import com.cjkj.usercenter.dto.yc.AddDeptAndUserResp;
import com.cjkj.usercenter.dto.yc.UpdateDeptManagerReq;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.carrier.CarrierDto;
import com.cjyc.common.model.dto.web.carrier.SeleCarrierDto;
import com.cjyc.common.model.dto.web.carrier.SeleVehicleDriverDto;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.*;
import com.cjyc.common.model.enums.transport.*;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.util.YmlProperty;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.carrier.BaseCarrierVo;
import com.cjyc.common.model.vo.web.carrier.BaseDriverVo;
import com.cjyc.common.model.vo.web.carrier.BaseVehicleVo;
import com.cjyc.common.model.vo.web.carrier.CarrierVo;
import com.cjyc.web.api.exception.CommonException;
import com.cjyc.common.system.feign.ISysDeptService;
import com.cjyc.common.system.feign.ISysUserService;
import com.cjyc.web.api.service.ICarrierCityConService;
import com.cjyc.web.api.service.ICarrierDriverConService;
import com.cjyc.web.api.service.ICarrierService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
public class CarrierServiceImpl extends ServiceImpl<ICarrierDao, Carrier> implements ICarrierService {

    @Resource
    private ICarrierDao carrierDao;

    @Resource
    private IDriverDao driverDao;

    @Resource
    private IVehicleDao vehicleDao;

    @Resource
    private ICarrierDriverConDao carrierDriverConDao;

    @Resource
    private IBankCardBindDao bankCardBindDao;

    @Resource
    private ICarrierCarCountDao carrierCarCountDao;

    @Resource
    private ICarrierCityConService carrierCityConService;

    @Resource
    private ICarrierDriverConService carrierDriverConService;

    @Autowired
    private ISysDeptService sysDeptService;

    @Autowired
    private ISysUserService sysUserService;

    @Value("${cjkj.carries_menu_ids}")
    private Long[] menuIds;

    private static final Long NOW = LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now());

    @Override
    public ResultVo saveCarrier(CarrierDto dto) {
        //判断是否已存在
        Carrier carr = carrierDao.selectOne(new QueryWrapper<Carrier>().lambda().eq(Carrier::getLinkmanPhone,dto.getLinkmanPhone()));
        if(carr != null){
            return BaseResultUtil.fail("该承运商已存在");
        }
        //添加承运商
        Carrier carrier = new Carrier();
        BeanUtils.copyProperties(dto,carrier);
        carrier.setState(CommonStateEnum.WAIT_CHECK.code);
        carrier.setType(CarrierTypeEnum.ENTERPRISE.code);
        carrier.setBusinessState(BusinessStateEnum.BUSINESS.code);
        carrier.setCreateUserId(dto.getLoginId());
        carrier.setCreateTime(NOW);
        super.save(carrier);

        //添加承运商司机管理员
        Driver driver = new Driver();
        driver.setName(dto.getName());
        driver.setPhone(dto.getLinkmanPhone());
        driver.setIdentity(DriverIdentityEnum.ADMIN.code);
        driver.setState(CommonStateEnum.CHECKED.code);
        driver.setBusinessState(BusinessStateEnum.BUSINESS.code);
        driver.setSource(DriverSourceEnum.SALEMAN_WEB.code);
        driver.setIdCard(dto.getLegalIdCard());
        driver.setRealName(dto.getLinkman());
        driver.setCreateUserId(dto.getLoginId());
        driver.setCreateTime(NOW);
        driverDao.insert(driver);

        //承运商与司机绑定关系
        CarrierDriverCon cdc = new CarrierDriverCon();
        cdc.setDriverId(driver.getId());
        cdc.setCarrierId(carrier.getId());
        cdc.setRole(DriverIdentityEnum.SUPERADMIN.code);
        carrierDriverConDao.insert(cdc);

        saveBankCardBand(dto,carrier.getId());
        //添加承运商业务范围
        carrierCityConService.batchSave(carrier.getId(),dto.getCodes());
        return BaseResultUtil.success();
    }

    @Override
    public boolean modifyCarrier(CarrierDto dto) {
        //更新承运商
        Carrier carrier = new Carrier();
        BeanUtils.copyProperties(dto,carrier);
        carrier.setId(dto.getCarrierId());
        //更新到物流平台
        Carrier origCarrier = carrierDao.selectById(dto.getCarrierId());
        //审核通过的
        ResultData<Long> rd = updateCarrierToPlatform(origCarrier, dto);
        if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
            log.error(rd.getMsg());
            return false;
        }
        super.updateById(carrier);
        //更新承运商司机管理员：司机更新用途？
        Driver driver = driverDao.getDriverByCarrierId(dto.getCarrierId());
        if(driver != null){
            driver.setPhone(dto.getLinkmanPhone());
            driver.setName(dto.getName());
            driver.setRealName(dto.getLinkman());
            driver.setIdCard(dto.getLegalIdCard());
            driverDao.updateById(driver);
        }
        //更新银行卡信息(先删除后添加)
        bankCardBindDao.removeBandCarBind(dto.getCarrierId());
        saveBankCardBand(dto,carrier.getId());

        //承运商业务范围,先批量删除，再添加
        carrierCityConService.batchDelete(carrier.getId());
        return carrierCityConService.batchSave(carrier.getId(),dto.getCodes());
    }

    @Override
    public ResultVo findCarrier(SeleCarrierDto dto) {
        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        List<CarrierVo> carrierVos = carrierDao.getCarrierByTerm(dto);
        if(!CollectionUtils.isEmpty(carrierVos)){
            for(CarrierVo vo : carrierVos){
                CarrierCarCount count = carrierCarCountDao.count(vo.getId());
                if(count != null){
                    vo.setCarNum(count.getCarNum() == null ? 0:count.getCarNum());
                    vo.setTotalIncome(count.getIncome() == null ? BigDecimal.ZERO:count.getIncome().divide(new BigDecimal(100)));
                }
            }
        }
        PageInfo<CarrierVo> pageInfo =  new PageInfo<>(carrierVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean verifyCarrier(OperateDto dto) {
        //审核状态 1:审核通过 2:审核拒绝 3：冻结 4:解除
        Carrier carrier = carrierDao.selectById(dto.getId());
        //审核通过
        if(FlagEnum.AUDIT_PASS.code == dto.getFlag()){
            //审核通过将承运商信息同步到物流平台
            ResultData<AddDeptAndUserResp> rd = saveCarrierToPlatform(carrier);
            if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
                throw new CommonException("承运商机构添加失败");
            }
            //更新司机userID信息
            updateDriver(rd.getData().getUserId(), carrier.getId());
            carrier.setDeptId(rd.getData().getDeptId());
            carrier.setState(CommonStateEnum.CHECKED.code);
            carrier.setCheckUserId(dto.getLoginId());
            carrier.setCheckTime(NOW);
        }else if(FlagEnum.AUDIT_REJECT.code == dto.getFlag()){
            //审核拒绝
            carrier.setState(CommonStateEnum.REJECT.code);
            carrier.setCheckUserId(dto.getLoginId());
            carrier.setCheckTime(NOW);
        }else if(FlagEnum.FROZEN.code == dto.getFlag()){
            //冻结
            carrier.setState(CommonStateEnum.FROZEN.code);
        }else if(FlagEnum.THAW.code == dto.getFlag()){
            //解冻
            carrier.setState(CommonStateEnum.CHECKED.code);
        }
        return super.updateById(carrier);
    }

    @Override
    public ResultVo showBaseCarrier(Long carrierId) {
        BaseCarrierVo vo = carrierDao.showCarrierById(carrierId);
        if(vo != null){
            vo.setMapCodes(carrierCityConService.getMapCodes(carrierId));
        }
        return BaseResultUtil.success(vo);
    }

    @Override
    public ResultVo findBaseVehicle(SeleVehicleDriverDto dto) {
        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        List<BaseVehicleVo> baseVehicleVos = vehicleDao.getBaseVehicleByTerm(dto);
        PageInfo<BaseVehicleVo> pageInfo = new PageInfo<>(baseVehicleVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo findBaseDriver(SeleVehicleDriverDto dto) {
        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        List<Long> idsList =  carrierDriverConService.getDriverIds(dto.getCarrierId());
        List<BaseDriverVo> driverVos = null;
        if(!CollectionUtils.isEmpty(idsList)){
            driverVos = driverDao.getDriversByIds(idsList);
            if(!CollectionUtils.isEmpty(driverVos)){
                for(BaseDriverVo vo : driverVos){
                    CarrierCarCount count = carrierCarCountDao.driverCount(vo.getDriverId());
                    if(count != null){
                        vo.setCarNum(count.getCarNum() == null ? 0:count.getCarNum());
                    }
                }
            }
        }
        PageInfo<BaseDriverVo> pageInfo = new PageInfo<>(driverVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo resetPwd(Long id) {
        CarrierDriverCon driverCon = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>()
                .eq("carrier_id", id)
                .eq("role", DriverIdentityEnum.SUPERADMIN.code));
        if (driverCon != null) {
            Driver driver = driverDao.selectById(driverCon.getDriverId());
            if (driver != null) {
                ResultData rd =
                        sysUserService.resetPwd(driver.getUserId(), YmlProperty.get("cjkj.salesman.password"));
                if (ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
                    return BaseResultUtil.success();
                }else {
                    return BaseResultUtil.fail(rd.getMsg());
                }
            }
        }
        return BaseResultUtil.fail("用户信息有误，请检查");
    }

    /**
     * 保存银行卡信息
     * @param dto
     * @param carrierId
     */
    private void saveBankCardBand(CarrierDto dto,Long carrierId){
        //保存银行卡信息
        BankCardBind bcb = new BankCardBind();
        BeanUtils.copyProperties(dto,bcb);
        //承运商id
        bcb.setUserId(carrierId);
        //承运商超级管理员
        bcb.setUserType(UserTypeEnum.ADMIN.code);
        bcb.setIdCard(dto.getLegalIdCard());
        bcb.setState(UseStateEnum.USABLE.code);
        bcb.setCreateTime(NOW);
        bcb.setCardPhone(dto.getLinkmanPhone());
        bankCardBindDao.insert(bcb);
    }

    /**
     * 将承运商相关信息保存到物流平台
     * @param carrier 承运商信息
     * @return
     */
    private ResultData<AddDeptAndUserResp> saveCarrierToPlatform(Carrier carrier) {
        if (null == carrier) {
            return ResultData.failed("承运商信息错误，请检查");
        }
        AddDeptAndUserReq deptReq = new AddDeptAndUserReq();
        deptReq.setName(carrier.getName());
        deptReq.setLegalPerson(carrier.getLegalName());
        deptReq.setDeptPerson(carrier.getLinkman());
        deptReq.setTelephone(carrier.getLinkmanPhone());
        deptReq.setPassword(YmlProperty.get("cjkj.salesman.password"));
        if (menuIds != null && menuIds.length > 0) {
            deptReq.setMenuIdList(Arrays.asList(menuIds));
        }
        ResultData<AddDeptAndUserResp> rd = sysDeptService.saveDeptAndUser(deptReq);
        return rd;
    }

    /**
     * 保存司机及承运商-司机关联关系
     * @param userId
     * @param carrierId
     */
    private void updateDriver(Long userId, Long carrierId) {
        List<CarrierDriverCon> conList = carrierDriverConDao.selectList(new QueryWrapper<CarrierDriverCon>()
                .eq("carrier_id", carrierId)
                .eq("role", DriverIdentityEnum.SUPERADMIN.code));
        Long driverId = null;
        if (!CollectionUtils.isEmpty(conList)) {
            driverId = conList.get(0).getDriverId();
        }
        if (driverId != null) {
            Driver driver = driverDao.selectById(driverId);
            if (driver != null) {
                driver.setUserId(userId);
                driverDao.updateById(driver);
            }
        }
    }

    /**
     * 更新承运商信息
     * @param carrier
     * @param dto
     * @return
     */
    private ResultData<Long> updateCarrierToPlatform(Carrier carrier, CarrierDto dto) {
        //1.判断 carrier不为空 且 deptId 不为空（物流平台存在此机构)
        Long userId = null;
        if (null != carrier) {
            if (carrier.getDeptId() != null && carrier.getDeptId() > 0L) {
                if (!carrier.getLinkmanPhone().equals(dto.getLinkmanPhone())) {
                    //需要变更联系人
                    //1.新联系人是否在物流平台
                    ResultData<AddUserResp> accountRd = sysUserService.getByAccount(dto.getLinkmanPhone());
                    if (!ReturnMsg.SUCCESS.getCode().equals(accountRd.getCode())) {
                        return ResultData.failed("查询用户信息失败，原因：" + accountRd.getMsg());
                    }
                    CarrierDriverCon origCdCon = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>()
                            .eq("carrier_id", carrier.getId())
                            .eq("role", DriverIdentityEnum.SUPERADMIN.code));
                    if (null == origCdCon) {
                        return ResultData.failed("数据异常，此承运商下无机构管理员");
                    }
                    if (accountRd.getData() != null) {
                        //2.如果存在：需要做用户角色关系管理及新用户校验
                        //2.1 新用户必须是此承运商司机
                        Driver driver = driverDao.selectOne(new QueryWrapper<Driver>()
                                .eq("user_id", accountRd.getData().getUserId()));
                        if (null == driver) {
                            return ResultData.failed("司机数据查询错误，根据user_id: " + accountRd.getData().getUserId() +
                                    "未查询到司机信息");
                        }
                        CarrierDriverCon cdCon = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>()
                                .eq("carrier_id", carrier.getId())
                                .eq("driver_id", driver.getId()));
                        if (null == cdCon) {
                            return ResultData.failed("新用户不属于此承运商");
                        }
                        //2.2 更新承运商信息及更换机构管理员
                        UpdateDeptManagerReq managerReq = new UpdateDeptManagerReq();
                        managerReq.setDeptId(carrier.getDeptId());
                        managerReq.setInitUserId(origCdCon.getDriverId());
                        managerReq.setNewUserId(accountRd.getData().getUserId());
                        ResultData rd = sysDeptService.updateDeptManager(managerReq);
                        if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
                            return ResultData.failed("变更机构管理员失败，原因：" + rd.getMsg());
                        }else {
                            //将原司机-承运商关系设置为普通角色
                            CarrierDriverCon carrierDriverCon = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>()
                                    .eq("carrier_id", dto.getCarrierId())
                                    .eq("role", DriverIdentityEnum.SUPERADMIN.code));
                            if (carrierDriverCon != null) {
                                carrierDriverCon.setRole(DriverIdentityEnum.SUB_DRIVER.code);
                                carrierDriverConDao.updateById(carrierDriverCon);
                            }
                            //新司机-承运商关系设置为管理员
                            cdCon.setRole(DriverIdentityEnum.SUPERADMIN.code);
                            carrierDriverConDao.updateById(cdCon);
                        }
                        userId = accountRd.getData().getUserId();
                    }else {
                        //3.如果不存在，直接将联系人手机号及姓名更新即可
                        UpdateUserReq userReq = new UpdateUserReq();
                        userReq.setUserId(origCdCon.getDriverId());
                        userReq.setAccount(dto.getLinkmanPhone());
                        userReq.setMobile(dto.getLinkmanPhone());
                        userReq.setName(dto.getLinkman());
                        ResultData updateRd = sysUserService.update(userReq);
                        if (!ReturnMsg.SUCCESS.getCode().equals(updateRd.getCode())) {
                            return ResultData.failed("更新管理员信息错误，原因：" + updateRd.getMsg());
                        }else {
                            CarrierDriverCon carrierDriverCon = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>()
                                    .eq("carrier_id", dto.getCarrierId())
                                    .eq("role", DriverIdentityEnum.SUPERADMIN.code));
                            Driver driver = driverDao.selectById(carrierDriverCon.getDriverId());
                            driver.setName(dto.getLinkman());
                            driver.setRealName(dto.getLinkman());
                            driver.setPhone(dto.getLinkmanPhone());
                            driverDao.updateById(driver);
                            userId = driver.getUserId();
                        }
                    }
                }

                //将企业名称、法人姓名信息更新即可
                if (!carrier.getName().equals(dto.getName())
                        || !carrier.getLegalName().equals(dto.getLegalName())) {
                    UpdateDeptReq deptReq = new UpdateDeptReq();
                    deptReq.setDeptId(carrier.getDeptId());
                    deptReq.setName(dto.getName());
                    deptReq.setLegalPerson(carrier.getLegalName());
                    return sysDeptService.update(deptReq);
                }
                return ResultData.ok(userId, "成功");
            }
            return ResultData.ok("数据未同步到物流平台，不需要变更");
        }
        return ResultData.ok("无变更：数据不存在");
    }
}