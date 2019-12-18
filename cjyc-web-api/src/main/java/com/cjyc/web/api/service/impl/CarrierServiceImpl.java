package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.ReturnMsg;
import com.cjkj.common.utils.ExcelUtil;
import com.cjkj.usercenter.dto.yc.AddDeptAndUserResp;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.carrier.*;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.*;
import com.cjyc.common.model.enums.driver.DriverIdentityEnum;
import com.cjyc.common.model.enums.transport.*;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.util.RandomUtil;
import com.cjyc.common.model.util.YmlProperty;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.carrier.*;
import com.cjyc.common.system.feign.ISysUserService;
import com.cjyc.common.system.service.ICsCarrierService;
import com.cjyc.web.api.service.ICarrierCityConService;
import com.cjyc.web.api.service.ICarrierService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CarrierServiceImpl extends ServiceImpl<ICarrierDao, Carrier> implements ICarrierService {

    @Resource
    private ICarrierDao carrierDao;
    @Resource
    private IDriverDao driverDao;
    @Resource
    private ICarrierDriverConDao carrierDriverConDao;
    @Resource
    private IBankCardBindDao bankCardBindDao;
    @Resource
    private ICarrierCarCountDao carrierCarCountDao;
    @Resource
    private IVehicleDao vehicleDao;
    @Resource
    private ICarrierCityConService carrierCityConService;
    @Autowired
    private ISysUserService sysUserService;
    @Resource
    private ICsCarrierService csCarrierService;

    private static final Long NOW = LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now());

    @Override
    public ResultVo saveOrModifyCarrier(CarrierDto dto) {
        if(dto.getCarrierId() == null){
            Carrier carr = carrierDao.selectOne(new QueryWrapper<Carrier>().lambda().eq(Carrier::getName, dto.getName()));
            if(carr != null){
                return BaseResultUtil.fail("该企业名称已存在,不可添加");
            }
            List<Driver> driverList = driverDao.selectList(new QueryWrapper<Driver>().lambda()
                    .eq(Driver::getPhone,dto.getLinkmanPhone())
                    .or()
                    .eq(Driver::getIdCard,dto.getLegalIdCard()));
            if(!CollectionUtils.isEmpty(driverList)){
                return BaseResultUtil.fail("该承运商账号已存在");
            }
            //添加承运商
            Carrier carrier = new Carrier();
            BeanUtils.copyProperties(dto,carrier);
            carrier.setState(CommonStateEnum.WAIT_CHECK.code);
            carrier.setType(CarrierTypeEnum.ENTERPRISE.code);
            carrier.setBusinessState(BusinessStateEnum.BUSINESS.code);
            carrier.setCreateUserId(dto.getLoginId());
            carrier.setCreateTime(NOW);
            //审核通过将承运商信息同步到物流平台
            ResultData<AddDeptAndUserResp> rd = csCarrierService.saveCarrierToPlatform(carrier);
            if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
                return BaseResultUtil.fail(rd.getMsg());
            }
            carrier.setDeptId(rd.getData().getDeptId());
            super.save(carrier);

            //添加承运商司机管理员
            Driver driver = new Driver();
            driver.setName(dto.getName());
            driver.setPhone(dto.getLinkmanPhone());
            driver.setType(DriverTypeEnum.SOCIETY.code);
            driver.setIdentity(DriverIdentityEnum.CARRIER_MANAGER.code);
            driver.setBusinessState(BusinessStateEnum.BUSINESS.code);
            driver.setSource(DriverSourceEnum.SALEMAN_WEB.code);
            driver.setIdCard(dto.getLegalIdCard());
            driver.setRealName(dto.getLinkman());
            driver.setCreateUserId(dto.getLoginId());
            driver.setCreateTime(NOW);
            driverDao.insert(driver);
            driver.setUserId(rd.getData().getUserId());
            driver.setId(driver.getId());
            driverDao.updateById(driver);

            //承运商与司机绑定关系
            CarrierDriverCon cdc = new CarrierDriverCon();
            cdc.setDriverId(driver.getId());
            cdc.setCarrierId(carrier.getId());
            cdc.setMode(dto.getMode());
            cdc.setState(CommonStateEnum.WAIT_CHECK.code);
            cdc.setRole(DriverRoleEnum.SUPERADMIN.code);
            carrierDriverConDao.insert(cdc);

            saveBankCardBand(dto,carrier.getId());
            //添加承运商业务范围
            carrierCityConService.batchSave(carrier.getId(),dto.getCodes());
        }else{
            Integer count = carrierDao.existBusinessDriver(dto);
            if(count != 1){
                return BaseResultUtil.fail("该手机号不是承运商下司机，请先添加");
            }
            return modifyCarrier(dto);
        }
        return BaseResultUtil.success();
    }

    /**
     * 修改承运商
     * @param dto
     * @return
     */
    private ResultVo modifyCarrier(CarrierDto dto) {
        Carrier origCarrier = carrierDao.selectById(dto.getCarrierId());
        CarrierDriverCon cdc = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>().lambda()
                .eq(CarrierDriverCon::getCarrierId, dto.getCarrierId())
                .eq(CarrierDriverCon::getRole, DriverRoleEnum.SUPERADMIN.code));
        Driver driver = driverDao.selectOne(new QueryWrapper<Driver>().lambda().eq(Driver::getPhone,dto.getLinkmanPhone()));
        if(origCarrier == null || cdc == null || driver == null){
            return BaseResultUtil.fail("数据信息有误");
        }
        if(cdc.getState() == CommonStateEnum.WAIT_CHECK.code){
            //待审核
            //更换手机号
            if(!dto.getLinkmanPhone().equals(origCarrier.getLinkmanPhone())){
                //更新手机号,查出之前的
                cdc.setRole(DriverRoleEnum.SUB_DRIVER.code);
                carrierDriverConDao.updateById(cdc);
                //查询现在的
                CarrierDriverCon cdCon = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>().lambda()
                        .eq(CarrierDriverCon::getCarrierId, origCarrier.getId())
                        .eq(CarrierDriverCon::getDriverId, driver.getId()));
                cdCon.setMode(dto.getMode());
                cdCon.setState(CommonStateEnum.WAIT_CHECK.code);
                cdCon.setRole(DriverRoleEnum.SUPERADMIN.code);
                carrierDriverConDao.updateById(cdc);
            }else{
                //没换手机号
                cdc.setMode(dto.getMode());
                cdc.setState(CommonStateEnum.WAIT_CHECK.code);
                carrierDriverConDao.updateById(cdc);
                //没换手机号时
                driver.setName(dto.getLinkman());
                driver.setRealName(dto.getLinkman());
                driver.setPhone(dto.getLinkmanPhone());
                driverDao.updateById(driver);
            }
        }
        //更新承运商
        BeanUtils.copyProperties(dto,origCarrier);
        origCarrier.setState(CommonStateEnum.WAIT_CHECK.code);
        origCarrier.setId(dto.getCarrierId());
        origCarrier.setCheckUserId(dto.getLoginId());
        origCarrier.setCheckTime(NOW);
        //审核通过的更新到物流平台
        ResultData<Long> rd = csCarrierService.updateCarrierToPlatform(origCarrier, dto);
        if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
            log.error(rd.getMsg());
            return BaseResultUtil.fail(rd.getMsg());
        }
        super.updateById(origCarrier);

        //更新银行卡信息(先删除后添加)
        bankCardBindDao.removeBandCarBind(dto.getCarrierId());
        saveBankCardBand(dto,origCarrier.getId());

        //承运商业务范围,先批量删除，再添加
        carrierCityConService.batchDelete(origCarrier.getId());
        carrierCityConService.batchSave(origCarrier.getId(),dto.getCodes());
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo findCarrier(SeleCarrierDto dto) {
        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        List<CarrierVo> carrierVos = encapCarrier(dto);
        PageInfo<CarrierVo> pageInfo =  new PageInfo<>(carrierVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo verifyCarrier(OperateDto dto) {
        Carrier carrier = carrierDao.selectById(dto.getId());
        Driver driver = findDriver(carrier.getId());
        CarrierDriverCon cdc = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>().lambda()
                                .eq(CarrierDriverCon::getCarrierId, carrier.getId())
                                .eq(CarrierDriverCon::getDriverId, driver.getId())
                                .ge(CarrierDriverCon::getRole, DriverRoleEnum.ADMIN.code));
        if (null == carrier || null == driver || cdc == null) {
            return BaseResultUtil.fail("承运商信息错误，请检查");
        }
        //审核通过
        if(FlagEnum.AUDIT_PASS.code == dto.getFlag()){
            //更新司机userID信息
            cdc.setState(CommonStateEnum.CHECKED.code);
            carrier.setState(CommonStateEnum.CHECKED.code);
        }else if(FlagEnum.AUDIT_REJECT.code == dto.getFlag()){
            //审核拒绝
            cdc.setState(CommonStateEnum.REJECT.code);
            carrier.setState(CommonStateEnum.REJECT.code);
        }else if(FlagEnum.FROZEN.code == dto.getFlag()){
            //冻结
            cdc.setState(CommonStateEnum.FROZEN.code);
            carrier.setState(CommonStateEnum.FROZEN.code);
        }else if(FlagEnum.THAW.code == dto.getFlag()){
            //解冻
            cdc.setState(CommonStateEnum.CHECKED.code);
            carrier.setState(CommonStateEnum.CHECKED.code);
        }
        driver.setCheckUserId(dto.getLoginId());
        driver.setCheckTime(NOW);
        carrier.setCheckUserId(dto.getLoginId());
        carrier.setCheckTime(NOW);
        carrierDriverConDao.updateById(cdc);
        driverDao.updateById(driver);
        super.updateById(carrier);
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo<BaseCarrierVo> showBaseCarrier(Long carrierId) {
        BaseCarrierVo vo = carrierDao.showCarrierById(carrierId);
        if(vo != null){
            vo.setMapCodes(carrierCityConService.getMapCodes(carrierId));
        }
        return BaseResultUtil.success(vo);
    }

    @Override
    public ResultVo resetPwd(Long id) {
        CarrierDriverCon driverCon = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>().lambda()
                .eq(CarrierDriverCon::getCarrierId, id)
                .eq(CarrierDriverCon::getRole, DriverRoleEnum.SUPERADMIN.code));
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

    @Override
    public ResultVo<PageVo<TransportDriverVo>> transportDriver(TransportDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<TransportDriverVo> transportDriverVos =  driverDao.findTransportDriver(dto);
        if(!CollectionUtils.isEmpty(transportDriverVos)){
            for(TransportDriverVo vo : transportDriverVos){
                CarrierCarCount count = carrierCarCountDao.driverCount(vo.getDriverId());
                if(count != null){
                    vo.setCarNum(count.getCarNum());
                }
            }
        }
        PageInfo<TransportDriverVo> pageInfo = new PageInfo<>(transportDriverVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<PageVo<TransportVehicleVo>> transportVehicle(TransportDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<TransportVehicleVo> transportVehicleVos = vehicleDao.findTransportVehicle(dto);
        PageInfo<TransportVehicleVo> pageInfo = new PageInfo<>(transportVehicleVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public void exportCarrierExcel(HttpServletRequest request, HttpServletResponse response) {
        SeleCarrierDto dto = getCarrierDto(request);
        List<CarrierVo> carrierVos = encapCarrier(dto);
        if (!CollectionUtils.isEmpty(carrierVos)) {
            // 生成导出数据
            List<CarrierExportExcel> exportExcelList = new ArrayList<>();
            for (CarrierVo vo : carrierVos) {
                CarrierExportExcel carrierExportExcel = new CarrierExportExcel();
                BeanUtils.copyProperties(vo, carrierExportExcel);
                exportExcelList.add(carrierExportExcel);
            }
            String title = "承运商管理";
            String sheetName = "承运商管理";
            String fileName = "承运商管理.xls";
            try {
                if(!CollectionUtils.isEmpty(exportExcelList)){
                    ExcelUtil.exportExcel(exportExcelList, title, sheetName, CarrierExportExcel.class, fileName, response);
                }
            } catch (IOException e) {
                log.error("导出承运商管理信息异常:{}",e);
            }
        }
    }

    /**
     * 封装承运商excel请求
     * @param request
     * @return
     */
    private SeleCarrierDto getCarrierDto(HttpServletRequest request){
        SeleCarrierDto dto = new SeleCarrierDto();
        dto.setName(request.getParameter("name"));
        dto.setLinkman(request.getParameter("linkman"));
        dto.setLinkmanPhone(request.getParameter("linkmanPhone"));
        dto.setCardNo(request.getParameter("cardNo"));
        dto.setLegalName(request.getParameter("legalName"));
        dto.setLegalIdCard(request.getParameter("legalIdCard"));
        dto.setIsInvoice(StringUtils.isBlank(request.getParameter("isInvoice")) ? null:Integer.valueOf(request.getParameter("isInvoice")));
        dto.setSettleType(StringUtils.isBlank(request.getParameter("settleType")) ? null:Integer.valueOf(request.getParameter("settleType")));
        dto.setState(StringUtils.isBlank(request.getParameter("state")) ? null:Integer.valueOf(request.getParameter("state")));
        return dto;
    }

    /**
     * 封装承运商
     * @param dto
     * @return
     */
    private List<CarrierVo> encapCarrier(SeleCarrierDto dto){
        List<CarrierVo> carrierVos = carrierDao.getCarrierByTerm(dto);
        if(!CollectionUtils.isEmpty(carrierVos)){
            for(CarrierVo vo : carrierVos){
                CarrierCarCount count = carrierCarCountDao.count(vo.getCarrierId());
                if(count != null){
                    vo.setCarNum(count.getCarNum());
                    vo.setTotalIncome(count.getIncome());
                }
            }
        }
        return carrierVos;
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
        bcb.setUserType(UserTypeEnum.DRIVER.code);
        bcb.setIdCard(dto.getLegalIdCard());
        bcb.setState(UseStateEnum.USABLE.code);
        bcb.setCardColour(RandomUtil.getIntRandom());
        bcb.setCreateTime(NOW);
        bcb.setCardPhone(dto.getLinkmanPhone());
        bankCardBindDao.insert(bcb);
    }

    /**
     * 保存司机及承运商-司机关联关系
     * @param carrierId
     */
    private Driver findDriver(Long carrierId) {
        List<CarrierDriverCon> conList = carrierDriverConDao.selectList(new QueryWrapper<CarrierDriverCon>().lambda()
                .eq(CarrierDriverCon::getCarrierId, carrierId)
                .eq(CarrierDriverCon::getRole, DriverRoleEnum.SUPERADMIN.code));
        Long driverId = null;
        if (!CollectionUtils.isEmpty(conList)) {
            driverId = conList.get(0).getDriverId();
        }
        if (driverId != null) {
            Driver driver = driverDao.selectById(driverId);
            if (driver != null) {
                return driver;
            }
        }
        return null;
    }

}