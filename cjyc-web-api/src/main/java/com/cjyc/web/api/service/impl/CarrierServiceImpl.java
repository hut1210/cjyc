package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.ReturnMsg;
import com.cjkj.common.utils.ExcelUtil;
import com.cjkj.usercenter.dto.common.*;
import com.cjkj.usercenter.dto.yc.AddDeptAndUserResp;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.carrier.*;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.*;
import com.cjyc.common.model.enums.driver.DriverIdentityEnum;
import com.cjyc.common.model.enums.task.TaskStateEnum;
import com.cjyc.common.model.enums.transport.*;
import com.cjyc.common.model.util.*;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.carrier.*;
import com.cjyc.common.system.feign.ISysRoleService;
import com.cjyc.common.system.feign.ISysUserService;
import com.cjyc.common.system.service.ICsBankInfoService;
import com.cjyc.common.system.service.ICsCarrierService;
import com.cjyc.common.system.service.ICsRoleService;
import com.cjyc.common.system.util.ResultDataUtil;
import com.cjyc.web.api.service.ICarrierCityConService;
import com.cjyc.web.api.service.ICarrierService;
import com.cjyc.web.api.service.IPublicPayBankService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    private ITaskDao taskDao;
    @Resource
    private ICarrierCityConService carrierCityConService;
    @Autowired
    private ISysUserService sysUserService;
    @Resource
    private ICsCarrierService csCarrierService;
    @Resource
    private ICsRoleService csRoleService;
    @Resource
    private ISysRoleService sysRoleService;
    @Resource
    private IUserRoleDeptDao userRoleDeptDao;
    @Resource
    private ICsBankInfoService bankInfoService;
    @Resource
    private IPublicPayBankService payBankService;

    /**
     * 承运商超级管理员角色名称
     */
    private static final String CARRIER_SUPER_ROLE_NAME =
            YmlProperty.get("cjkj.carrier_super_role_name");

    /**
     * 承运商管理员角色名称
     */
    private static final String CARRIER_COMMON_ROLE_NAME =
            YmlProperty.get("cjkj.carrier_common_role_name");

    /**
     * 社会车辆事业部机构ID
     */
    private static final Long BIZ_TOP_DEPT_ID = Long.parseLong(YmlProperty.get("cjkj.dept_admin_id"));

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
            carrier.setCreateTime(System.currentTimeMillis());
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
            driver.setCreateTime(System.currentTimeMillis());
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

            saveBankCardBand(dto,null,carrier.getId());
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
        origCarrier.setCheckTime(System.currentTimeMillis());
        //审核通过的更新到物流平台
        ResultData<Long> rd = csCarrierService.updateCarrierToPlatform(origCarrier, dto);
        if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
            log.error(rd.getMsg());
            return BaseResultUtil.fail(rd.getMsg());
        }
        super.updateById(origCarrier);

        //更新银行卡信息(先删除后添加)
        bankCardBindDao.removeBandCarBind(dto.getCarrierId());
        saveBankCardBand(dto,null,origCarrier.getId());

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
        driver.setCheckTime(System.currentTimeMillis());
        carrier.setCheckUserId(dto.getLoginId());
        carrier.setCheckTime(System.currentTimeMillis());
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
        List<CarrierVo> carrierVos = encapCarrierNew(dto);
        //if (!CollectionUtils.isEmpty(carrierVos)) {
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
            //if(!CollectionUtils.isEmpty(exportExcelList)){
            ExcelUtil.exportExcel(exportExcelList, title, sheetName, CarrierExportExcel.class, fileName, response);
            //}
        } catch (IOException e) {
            log.error("导出承运商管理信息异常:{}",e);
        }
        //}
    }

    /*********************************韵车集成改版 st*****************************/
    /**
     * 添加承运商_改版
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo saveOrModifyCarrierNew(CarrierDto dto) {
        if (dto.getCarrierId() == null) {
            //新增
            List<Driver> existDriverList = driverDao.selectList(new QueryWrapper<Driver>().lambda()
                    .eq(Driver::getPhone, dto.getLinkmanPhone())
                    .or()
                    .eq(Driver::getIdCard, dto.getLegalIdCard()));
            if (!CollectionUtils.isEmpty(existDriverList)) {
                return BaseResultUtil.fail("该承运商账号已存在");
            }
            //将承运商超级管理员账号、分配角色信息同步到物流平台
            Role role = csRoleService
                    .getByName(CARRIER_SUPER_ROLE_NAME, 2);
            ResultData<Long> addUserRd = saveCarrierSuperAdminToPlatform(dto.getLinkman(),
                    dto.getLinkmanPhone(), role);
            if (!ResultDataUtil.isSuccess(addUserRd)) {
                return BaseResultUtil.fail("保存承运商信息错误，原因：" + addUserRd.getMsg());
            }
            Long userId = addUserRd.getData();
            //添加承运商
            Carrier carrier = new Carrier();
            BeanUtils.copyProperties(dto,carrier);
            carrier.setState(CommonStateEnum.WAIT_CHECK.code);
            carrier.setType(CarrierTypeEnum.ENTERPRISE.code);
            carrier.setBusinessState(BusinessStateEnum.BUSINESS.code);
            carrier.setCreateUserId(dto.getLoginId());
            carrier.setCreateTime(System.currentTimeMillis());
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
            driver.setCreateTime(System.currentTimeMillis());
            driver.setUserId(userId);
            driverDao.insert(driver);

            //承运商、用户、角色关系维护
            UserRoleDept userRoleDept = new UserRoleDept();
            userRoleDept.setUserId(driver.getId());
            userRoleDept.setRoleId(role.getId());
            userRoleDept.setDeptId(carrier.getId()+"");
            userRoleDept.setDeptType(2);
            userRoleDept.setUserType(UserTypeEnum.DRIVER.code);
            userRoleDept.setState(CommonStateEnum.WAIT_CHECK.code);
            userRoleDept.setMode(dto.getMode());
            userRoleDept.setCreateTime(System.currentTimeMillis());
            userRoleDept.setCreateUserId(dto.getLoginId());
            userRoleDeptDao.insert(userRoleDept);

            saveBankCardBand(dto,null,carrier.getId());
            //添加承运商业务范围
            carrierCityConService.batchSave(carrier.getId(),dto.getCodes());
        }else {
            //修改
            Integer count = carrierDao.existBusinessDriverNew(dto);
            if(count != 1){
                return BaseResultUtil.fail("该手机号不是承运商下司机，请先添加");
            }
            return modifyCarrierNew(dto);
        }
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo findCarrierNew(SeleCarrierDto dto) {
        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        List<CarrierVo> carrierVos = encapCarrierNew(dto);
        PageInfo<CarrierVo> pageInfo =  new PageInfo<>(carrierVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo verifyCarrierNew(OperateDto dto) {
       /* Role role = csRoleService.getByName(CARRIER_SUPER_ROLE_NAME, 2);
        if (role == null) {
            return BaseResultUtil.fail("根据角色名称：" + CARRIER_SUPER_ROLE_NAME +
                    ",未查询到角色信息");
        }*/
        Carrier carrier = carrierDao.selectById(dto.getId());
        Driver driver = driverDao.selectOne(new QueryWrapper<Driver>().lambda()
                .eq(Driver::getPhone, carrier.getLinkmanPhone()));
        //Driver driver = findDriverNew(carrier.getId(), role);
        UserRoleDept userRoleDept = userRoleDeptDao.selectOne(new QueryWrapper<UserRoleDept>().lambda()
                .eq(UserRoleDept::getDeptId, carrier.getId())
                .eq(UserRoleDept::getUserId, driver.getId()));
        if (null == carrier || null == driver || null == userRoleDept) {
            return BaseResultUtil.fail("承运商信息有误，请检查");
        }
        if (FlagEnum.AUDIT_PASS.code == dto.getFlag()) {
            //审核通过
            userRoleDept.setState(CommonStateEnum.CHECKED.code);
            carrier.setState(CommonStateEnum.CHECKED.code);
        }else if (FlagEnum.AUDIT_REJECT.code == dto.getFlag()) {
            //审核拒绝
            userRoleDept.setState(CommonStateEnum.REJECT.code);
            carrier.setState(CommonStateEnum.REJECT.code);
        }else if (FlagEnum.FROZEN.code == dto.getFlag()) {
            //冻结
            userRoleDept.setState(CommonStateEnum.FROZEN.code);
            carrier.setState(CommonStateEnum.FROZEN.code);
        }else if (FlagEnum.THAW.code == dto.getFlag()) {
            //解冻
            userRoleDept.setState(CommonStateEnum.CHECKED.code);
            carrier.setState(CommonStateEnum.CHECKED.code);
        }
        driver.setCheckUserId(dto.getLoginId());
        driver.setCheckTime(System.currentTimeMillis());
        carrier.setCheckUserId(dto.getLoginId());
        carrier.setCheckTime(System.currentTimeMillis());
        userRoleDeptDao.updateById(userRoleDept);
        driverDao.updateById(driver);
        super.updateById(carrier);
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo showBaseCarrierNew(Long carrierId) {
        BaseCarrierVo vo = carrierDao.showCarrierByIdNew(carrierId);
        if(vo != null){
            vo.setMapCodes(carrierCityConService.getMapCodes(carrierId));
        }
        return BaseResultUtil.success(vo);
    }

    @Override
    public ResultVo resetPwdNew(Long id) {
        Role role = csRoleService.getByName(CARRIER_SUPER_ROLE_NAME, 2);
        if (role == null) {
            return BaseResultUtil.fail("根据角色名称：" + CARRIER_SUPER_ROLE_NAME +
                    ",未查询到角色信息");
        }
        Role roleC = csRoleService.getByName(CARRIER_COMMON_ROLE_NAME, 2);
        if (roleC == null) {
            return BaseResultUtil.fail("根据角色名称：" + CARRIER_COMMON_ROLE_NAME +
                    ",未查询到角色信息");
        }
        UserRoleDept userRoleDept = userRoleDeptDao.selectOne(new QueryWrapper<UserRoleDept>().lambda()
                .eq(UserRoleDept::getDeptId, id)
                .eq(UserRoleDept::getRoleId, role.getId()));
        if(userRoleDept == null){
            userRoleDept = userRoleDeptDao.selectOne(new QueryWrapper<UserRoleDept>().lambda()
                    .eq(UserRoleDept::getDeptId, id)
                    .eq(UserRoleDept::getRoleId, roleC.getId()));
        }
        if (userRoleDept != null) {
            Driver driver = driverDao.selectById(userRoleDept.getUserId());
            if (driver != null) {
                ResultData rd =
                        sysUserService.resetPwd(driver.getUserId(), YmlProperty.get("cjkj.salesman.password"));
                if (!ResultDataUtil.isSuccess(rd)) {
                    return BaseResultUtil.fail("重置密码错误，原因：" + rd.getMsg());
                }else {
                    return BaseResultUtil.success();
                }
            }
        }
        return BaseResultUtil.fail("用户信息有误，请检查");
    }
    /*********************************韵车集成改版 ed*****************************/
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
    private void saveBankCardBand(CarrierDto dto,CarrierImportExcel excel,Long carrierId){
        //保存银行卡信息
        BankCardBind bcb = new BankCardBind();
        if(dto != null){
            BeanUtils.copyProperties(dto,bcb);
            bcb.setIdCard(dto.getLegalIdCard());
            bcb.setCardPhone(dto.getLinkmanPhone());
        }else{
            BeanUtils.copyProperties(excel,bcb);
            if("对公".equals(excel.getCardType())){
                bcb.setCardType(1);
            }else{
                bcb.setCardType(2);
            }
            bcb.setIdCard(excel.getLegalIdCard());
            bcb.setCardPhone(excel.getLinkmanPhone());
        }
        //承运商id
        bcb.setUserId(carrierId);
        //承运商超级管理员
        bcb.setUserType(UserTypeEnum.DRIVER.code);
        bcb.setState(UseStateEnum.USABLE.code);
        bcb.setCardColour(RandomUtil.getIntRandom());
        bcb.setCreateTime(System.currentTimeMillis());
        //获取银行编码
        if(dto.getCardType().equals(CardTypeEnum.PRIVATE.code)){
            BankInfo bankInfo = bankInfoService.findBankCode(bcb.getBankName());
            if(bankInfo != null){
                bcb.setBankCode(bankInfo.getBankCode());
            }
        }else if(dto.getCardType().equals(CardTypeEnum.PUBLIC.code)){
            PublicPayBank payBank = payBankService.findPayBank(bcb.getBankName());
            if(payBank != null){
                bcb.setBankCode(payBank.getBankCode());
            }
            bcb.setProvinceName(dto.getProvinceName());
            bcb.setAreaName(dto.getAreaName());
        }
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

    /**
     * 将承运商管理员账号及超级管理员角色关系添加到物流平台
     * @return
     */
    private ResultData<Long> saveCarrierSuperAdminToPlatform(String name, String phone, Role role) {
        ResultData<UserResp> existUser = sysUserService.getByAccount(phone);
        if (!ResultDataUtil.isSuccess(existUser)) {
            return ResultData.failed("查询账号信息异常，原因：" + existUser.getMsg());
        }
        if (role == null) {
            return ResultData.failed("根据角色名称：" + CARRIER_SUPER_ROLE_NAME +
                    ",未查询到角色信息");
        }
        Long existUserId = existUser.getData() == null?null: existUser.getData().getUserId();
        if (existUserId == null) {
            //用户不存在， 需要新增
            AddUserReq addUserReq = new AddUserReq();
            addUserReq.setName(name);
            addUserReq.setMobile(phone);
            addUserReq.setAccount(phone);
            addUserReq.setDeptId(BIZ_TOP_DEPT_ID);
            addUserReq.setPassword(YmlProperty.get("cjkj.driver.password"));
            addUserReq.setRoleIdList(Arrays.asList(role.getRoleId()));
            ResultData<AddUserResp> rd = sysUserService.save(addUserReq);
            if (!ResultDataUtil.isSuccess(rd)) {
                return ResultData.failed("司机信息保存失败，原因：" + rd.getMsg());
            }
            return ResultData.ok(rd.getData().getUserId());
        }else {
            //用户已存在，需要更新
            ResultData<List<SelectRoleResp>> rolesRd =
                    sysRoleService.getListByUserId(existUser.getData().getUserId());
            if (!ResultDataUtil.isSuccess(rolesRd)) {
                return ResultData.failed("角色信息查询失败，原因：" + rolesRd.getMsg());
            }
            ResultData updateRd = null;
            if (CollectionUtils.isEmpty(rolesRd.getData())) {
                //角色信息为空，直接更新用户
                UpdateUserReq req = new UpdateUserReq();
                req.setUserId(existUserId);
                req.setRoleIdList(Arrays.asList(role.getRoleId()));
                updateRd = sysUserService.update(req);
            }else {
                //角色信息不为空，需新增当前角色
                List<Long> roleIdList = rolesRd.getData().stream()
                        .map(r -> r.getRoleId()).collect(Collectors.toList());
                if (roleIdList.contains(role.getRoleId())) {
                    return ResultData.ok(existUserId);
                }else {
                    roleIdList.add(role.getRoleId());
                    UpdateUserReq req = new UpdateUserReq();
                    req.setUserId(existUserId);
                    req.setRoleIdList(roleIdList);
                    updateRd = sysUserService.update(req);
                }
            }
            if (!ResultDataUtil.isSuccess(updateRd)) {
                return ResultData.failed("用户信息保存失败，原因：" + updateRd.getMsg());
            }
            return ResultData.ok(existUserId);
        }
    }

    /**
     * 修改承运商
     * @param dto
     * @return
     */
    private ResultVo modifyCarrierNew(CarrierDto dto) {
        Carrier origCarrier = carrierDao.selectById(dto.getCarrierId());
        Role role = csRoleService.getByName(CARRIER_SUPER_ROLE_NAME, 2);
        if (role == null) {
            return BaseResultUtil.fail("根据角色名称：" + CARRIER_SUPER_ROLE_NAME +
                    ",未查询到角色信息");
        }
        Driver driver = driverDao.selectOne(new QueryWrapper<Driver>().lambda().eq(Driver::getPhone,dto.getLinkmanPhone()));
        UserRoleDept urd = userRoleDeptDao.selectOne(new QueryWrapper<UserRoleDept>().lambda()
                .eq(UserRoleDept::getDeptId, origCarrier.getId())
                .eq(UserRoleDept::getUserId, driver.getId()));
        if(origCarrier == null || urd == null || driver == null){
            return BaseResultUtil.fail("数据信息有误");
        }
//        if (CommonStateEnum.WAIT_CHECK.code == urd.getState()) {
        //待审核
        if (!dto.getLinkmanPhone().equals(origCarrier.getLinkmanPhone())) {
            //更换手机号
            Role commonRole = csRoleService.getByName(CARRIER_COMMON_ROLE_NAME, 2);
            if (commonRole == null) {
                return BaseResultUtil.fail("根据角色名称：" + CARRIER_COMMON_ROLE_NAME +
                        ",未查询到角色信息");
            }
            //原承运商超级管理员司机
            Driver origSuperDriver = driverDao.selectById(urd.getUserId());
            if (origSuperDriver == null) {
                return BaseResultUtil.fail("承运商超级管理员信息错误，请检查");
            }
            //用户信息同步到物流平台
            ResultData rd = updateRoleForCarrierToPlatform(origSuperDriver, driver, role, commonRole);
            if (!ResultDataUtil.isSuccess(rd)) {
                return BaseResultUtil.fail("更新司机角色信息错误，原因：" + rd.getMsg());
            }
            urd.setRoleId(commonRole.getId());
            userRoleDeptDao.updateById(urd);
            UserRoleDept updateUrd = userRoleDeptDao.selectOne(new QueryWrapper<UserRoleDept>().lambda()
                    .eq(UserRoleDept::getUserId, driver.getId())
                    .eq(UserRoleDept::getDeptId, origCarrier.getId()));
            updateUrd.setMode(dto.getMode());
            updateUrd.setState(CommonStateEnum.WAIT_CHECK.code);
            updateUrd.setRoleId(role.getId());
            userRoleDeptDao.updateById(updateUrd);
        }else {
            //无需更换手机号
            urd.setMode(dto.getMode());
            urd.setState(CommonStateEnum.WAIT_CHECK.code);
            userRoleDeptDao.updateById(urd);
            //没换手机号
            driver.setName(dto.getLinkman());
            driver.setRealName(dto.getLinkman());
            driver.setPhone(dto.getLinkmanPhone());
            driverDao.updateById(driver);
        }
//        }
        //更新承运商
        BeanUtils.copyProperties(dto,origCarrier);
        origCarrier.setState(CommonStateEnum.WAIT_CHECK.code);
        origCarrier.setId(dto.getCarrierId());
        origCarrier.setCheckUserId(dto.getLoginId());
        origCarrier.setCheckTime(System.currentTimeMillis());
        super.updateById(origCarrier);

        //更新银行卡信息(先删除后添加)
        bankCardBindDao.removeBandCarBind(dto.getCarrierId());
        saveBankCardBand(dto,null, origCarrier.getId());

        //承运商业务范围,先批量删除，再添加
        carrierCityConService.batchDelete(origCarrier.getId());
        carrierCityConService.batchSave(origCarrier.getId(),dto.getCodes());
        return BaseResultUtil.success();
    }


    /**
     * 封装承运商
     * @param dto
     * @return
     */
    private List<CarrierVo> encapCarrierNew(SeleCarrierDto dto){
        List<CarrierVo> carrierVos = carrierDao.getCarrierByTermNew(dto);
        if(!CollectionUtils.isEmpty(carrierVos)){
            for(CarrierVo vo : carrierVos){
                CarrierCarCount count = carrierCarCountDao.countNew(vo.getCarrierId());
                if(count != null){
                    vo.setCarNum(count.getCarNum());
                    vo.setTotalIncome(count.getIncome());
                }
            }
        }
        return carrierVos;
    }

    /**
     * 保存司机及承运商-司机关联关系_改版
     * @param carrierId
     */
    private Driver findDriverNew(Long carrierId, Role role) {
        List<UserRoleDept> conList = userRoleDeptDao.selectList(new QueryWrapper<UserRoleDept>().lambda()
                .eq(UserRoleDept::getDeptId, carrierId)
                .eq(UserRoleDept::getRoleId, role.getId()));
        Long driverId = null;
        if (!CollectionUtils.isEmpty(conList)) {
            driverId = conList.get(0).getUserId();
        }
        if (driverId != null) {
            Driver driver = driverDao.selectById(driverId);
            if (driver != null) {
                return driver;
            }
        }
        return null;
    }

    @Override
    public ResultVo<PageVo<TransportDriverVo>> transportDriverNew(TransportDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<TransportDriverVo> transportDriverVos =  driverDao.findTransportDriverNew(dto);
        if(!CollectionUtils.isEmpty(transportDriverVos)){
            for(TransportDriverVo vo : transportDriverVos){
                CarrierCarCount count = carrierCarCountDao.driverCount(vo.getDriverId());
                if(count != null){
                    vo.setCarNum(count.getCarNum());
                }
                //处理该司机当前营运状态
                Integer taskCount = taskDao.selectCount(new QueryWrapper<Task>().lambda()
                        .eq(Task::getDriverId, vo.getDriverId())
                        .lt(Task::getState, TaskStateEnum.FINISHED.code));
                if(taskCount > 0){
                    vo.setBusinessState(BusinessStateEnum.OUTAGE.code);
                }else{
                    vo.setBusinessState(BusinessStateEnum.BUSINESS.code);
                }
            }
        }
        PageInfo<TransportDriverVo> pageInfo = new PageInfo<>(transportDriverVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<PageVo<TransportVehicleVo>> transportVehicleNew(TransportDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<TransportVehicleVo> transportVehicleVos = vehicleDao.findTransportVehicleNew(dto);
        PageInfo<TransportVehicleVo> pageInfo = new PageInfo<>(transportVehicleVos);
        return BaseResultUtil.success(pageInfo);
    }

    /**
     * 承运商变更修改角色信息到物流平台
     * @return
     */
    private ResultData updateRoleForCarrierToPlatform(Driver origDriver, Driver newDriver,
                                                      Role superRole, Role commonRole) {
        ResultData<List<SelectRoleResp>> rolesRd =
                sysRoleService.getListByUserId(origDriver.getUserId());
        if (!ResultDataUtil.isSuccess(rolesRd)) {
            return ResultData.failed("角色信息查询失败，原因：" + rolesRd.getMsg());
        }
        ResultData<List<SelectRoleResp>> newRolesRd = sysRoleService.getListByUserId(newDriver.getUserId());
        if (!ResultDataUtil.isSuccess(newRolesRd)) {
            return ResultData.failed("角色信息查询失败，原因：" + newRolesRd.getMsg());
        }
        if (!CollectionUtils.isEmpty(rolesRd.getData()) && !CollectionUtils.isEmpty(newRolesRd.getData())) {
            List<Long> origIdList = rolesRd.getData().stream()
                    .map(r -> r.getRoleId()).collect(Collectors.toList());
            origIdList.remove(superRole.getRoleId());
            if (!origIdList.contains(commonRole.getRoleId())) {
                origIdList.add(commonRole.getRoleId());
            }
            UpdateUserReq origReq = new UpdateUserReq();
            origReq.setUserId(origDriver.getUserId());
            origReq.setRoleIdList(origIdList);
            ResultData updateRd = sysUserService.update(origReq);
            if (!ResultDataUtil.isSuccess(updateRd)) {
                return ResultData.failed("用户角色信息更新失败，原因：" + updateRd.getMsg());
            }
            List<Long> newIdList = newRolesRd.getData().stream()
                    .map(r -> r.getRoleId()).collect(Collectors.toList());
            newIdList.remove(commonRole.getRoleId());
            if (!newIdList.contains(superRole.getRoleId())) {
                newIdList.add(superRole.getRoleId());
            }
            UpdateUserReq newReq = new UpdateUserReq();
            newReq.setUserId(newDriver.getUserId());
            newReq.setRoleIdList(newIdList);
            ResultData newUpdateRd = sysUserService.update(newReq);
            if (!ResultDataUtil.isSuccess(newUpdateRd)) {
                return ResultData.failed("用户角色信息更新失败，原因：" + newUpdateRd.getMsg());
            }
            return ResultData.ok("成功");
        }else {
            return ResultData.failed("角色信息数据错误，请检查");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean importCarrierExcel(MultipartFile file, Long loginId) {
        boolean result;
        try {
            List<CarrierImportExcel> carrierImportExcelList = ExcelUtil.importExcel(file, 0, 1, CarrierImportExcel.class);
            if (!CollectionUtils.isEmpty(carrierImportExcelList)) {
                for (CarrierImportExcel carrierImportExcel : carrierImportExcelList) {
                    //新增
                    List<Driver> existDriverList = driverDao.selectList(new QueryWrapper<Driver>().lambda()
                            .eq(Driver::getPhone, carrierImportExcel.getLinkmanPhone())
                            .or()
                            .eq(Driver::getIdCard, carrierImportExcel.getLegalIdCard()));
                    if (!CollectionUtils.isEmpty(existDriverList)) {
                        continue;
                    }
                    //将承运商超级管理员账号、分配角色信息同步到物流平台
                    Role role = csRoleService
                            .getByName(CARRIER_SUPER_ROLE_NAME, 2);
                    ResultData<Long> addUserRd = saveCarrierSuperAdminToPlatform(carrierImportExcel.getLinkman(),
                            carrierImportExcel.getLinkmanPhone(), role);
                    if (!ResultDataUtil.isSuccess(addUserRd)) {
                        continue;
                    }
                    Long userId = addUserRd.getData();
                    if(userId == null){
                        continue;
                    }
                    //添加承运商
                    Carrier carrier = new Carrier();
                    BeanUtils.copyProperties(carrierImportExcel,carrier);
                    if("是".equals(carrierImportExcel.getIsInvoice())){
                        carrier.setIsInvoice(1);
                    }else{
                        carrier.setIsInvoice(0);
                    }
                    if("账期".equals(carrierImportExcel.getSettleType())){
                        carrier.setSettleType(1);
                    }else{
                        carrier.setSettleType(0);
                    }
                    carrier.setState(CommonStateEnum.WAIT_CHECK.code);
                    carrier.setType(CarrierTypeEnum.ENTERPRISE.code);
                    carrier.setBusinessState(BusinessStateEnum.BUSINESS.code);
                    carrier.setCreateUserId(loginId);
                    carrier.setCreateTime(System.currentTimeMillis());
                    super.save(carrier);

                    //添加承运商司机管理员
                    Driver driver = new Driver();
                    driver.setName(carrierImportExcel.getName());
                    driver.setPhone(carrierImportExcel.getLinkmanPhone());
                    driver.setType(DriverTypeEnum.SOCIETY.code);
                    driver.setIdentity(DriverIdentityEnum.CARRIER_MANAGER.code);
                    driver.setBusinessState(BusinessStateEnum.BUSINESS.code);
                    driver.setSource(DriverSourceEnum.SALEMAN_WEB.code);
                    driver.setIdCard(carrierImportExcel.getLegalIdCard());
                    driver.setRealName(carrierImportExcel.getLinkman());
                    driver.setCreateUserId(loginId);
                    driver.setCreateTime(System.currentTimeMillis());
                    driver.setUserId(userId);
                    driverDao.insert(driver);

                    //承运商、用户、角色关系维护
                    UserRoleDept userRoleDept = new UserRoleDept();
                    userRoleDept.setUserId(driver.getId());
                    userRoleDept.setRoleId(role.getId());
                    userRoleDept.setDeptId(String.valueOf(carrier.getId()));
                    userRoleDept.setDeptType(2);
                    userRoleDept.setUserType(UserTypeEnum.DRIVER.code);
                    userRoleDept.setState(CommonStateEnum.WAIT_CHECK.code);
                    userRoleDept.setCreateTime(System.currentTimeMillis());
                    userRoleDept.setCreateUserId(loginId);
                    userRoleDeptDao.insert(userRoleDept);
                    //保存银行卡信息
                    saveBankCardBand(null,carrierImportExcel,carrier.getId());
                }
                result = true;
            } else {
                result = false;
            }
        } catch (Exception e) {
            log.error("导入社会司机失败异常:{}", e);
            result = false;
        }
        return result;
    }
}