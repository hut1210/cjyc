package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.web.carrier.CarrierDto;
import com.cjyc.common.model.dto.web.carrier.SeleCarrierDto;
import com.cjyc.common.model.dto.web.carrier.SeleVehicleDto;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.AdminStateEnum;
import com.cjyc.common.model.enums.FlagEnum;
import com.cjyc.common.model.enums.UseStateEnum;
import com.cjyc.common.model.enums.transport.*;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.web.carrier.BaseVehicleVo;
import com.cjyc.common.model.vo.web.carrier.CarrierVo;
import com.cjyc.common.model.vo.web.carrier.BaseCarrierVo;
import com.cjyc.web.api.exception.CommonException;
import com.cjyc.web.api.service.ICarrierCityConService;
import com.cjyc.web.api.service.ICarrierService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
public class CarrierServiceImpl extends ServiceImpl<ICarrierDao, com.cjyc.common.model.entity.Carrier> implements ICarrierService {

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
    private ICarrierCityConDao carrierCityConDao;

    @Resource
    private ICarrierCityConService carrierCityConService;

    @Override
    public boolean saveCarrier(CarrierDto dto) {
        int i ;
        int j = 0;
        int k = 0;
        int m = 0;
        int n = 0;
        Driver driver = null;
        try{
            //添加承运商
            Carrier carrier = new Carrier();
            carrier.setState(VerifyStateEnum.BE_AUDITED.code);
            carrier.setType(CarrierTypeEnum.ENTERPRISE.code);
            carrier = encapCarrier(carrier,dto);
            i = carrierDao.insert(carrier);
            if(k > 0){
                //添加承运商司机管理员
                driver = new Driver();
                driver.setPhone(dto.getLinkmanPhone());
                driver.setIdentity(DriverIdentityEnum.ADMIN.code);
                driver.setState(VerifyStateEnum.AUDIT_PASS.code);
                driver.setBusinessState(BusinessStateEnum.BUSINESS.code);
                driver.setSource(DriverSourceEnum.SALEMAN_WEB.code);
                driver.setCreateUserId(dto.getUserId());
                m = driverDao.insert(driver);
            }
            if(m > 0){
                //承运商与司机绑定关系
                CarrierDriverCon cdc = new CarrierDriverCon();
                cdc.setDriverId(driver.getId());
                cdc.setCarrierId(carrier.getId());
                cdc.setRole(DriverIdentityEnum.ADMIN.code);
                j = carrierDriverConDao.insert(cdc);
            }
            if(j > 0){
                //保存银行卡信息
                BankCardBind bcb = new BankCardBind();
                bcb.setUserId(driver.getUserId());
                bcb.setCardType(dto.getCardType());
                bcb.setCardNo(dto.getCardNo());
                bcb.setBankName(dto.getBankName());
                bcb.setIdCard(dto.getLegaIdCard());
                bcb.setCardPhone(dto.getLinkmanPhone());
                bcb.setState(UseStateEnum.USABLE.code);
                bcb.setCreateTime(LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now()));
                n = bankCardBindDao.insert(bcb);
            }
            //业务范围
            if(n > 0){
                //添加承运商业务范围
                return carrierCityConService.batchSave(carrier.getId(),dto.getCodes());
            }
        }catch (Exception e){
            log.info("新增承运商信息出现异常");
            throw new CommonException(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean updateCarrier(CarrierDto dto) {
        int i ;
        int j = 0;
        int k = 0;
        int m = 0;
        Admin admin = null;
        try{
            //更新承运商
            Carrier carrier = carrierDao.selectById(dto.getId());
            carrier = encapCarrier(carrier,dto);
            i = carrierDao.updateById(carrier);
            if(j > 0){
                //更新承运商司机管理员
                Driver driver = driverDao.getDriverByDriverId(dto.getId());
                if(driver != null){
                    driver.setPhone(dto.getLinkmanPhone());
                    k = driverDao.updateById(driver);
                }
            }
            if(k > 0){
                //更新银行卡信息
                BankCardBind bcb = bankCardBindDao.getBankCardBindByUserId(admin.getUserId());
                if(bcb != null){
                    bcb.setCardType(dto.getCardType());
                    bcb.setCardNo(dto.getCardNo());
                    bcb.setBankName(dto.getBankName());
                    bcb.setIdCard(dto.getLegaIdCard());
                    bcb.setCardPhone(dto.getLinkmanPhone());
                    m = bankCardBindDao.updateById(bcb);
                }
            }
            if(m > 0){
                //承运商业务范围,先批量删除，再添加
                carrierCityConService.batchDelete(carrier.getId());
                carrierCityConService.batchSave(carrier.getId(),dto.getCodes());
            }
        }catch (Exception e){
            log.info("更新承运商信息出现异常");
            throw new CommonException(e.getMessage());
        }
        return false;
    }

    @Override
    public PageInfo<CarrierVo> getCarrierByTerm(SeleCarrierDto dto) {
        PageInfo<CarrierVo> pageInfo = null;
        try{
            List<CarrierVo> carrierVos = carrierDao.getCarrierByTerm(dto);
            if(carrierVos != null && carrierVos.size() > 0){
                for(CarrierVo vo : carrierVos){
                    Map<String,String> map = carrierCarCountDao.getCarIncomeByCarrId(vo.getId());
                    if(!map.isEmpty()){
                        if(StringUtils.isNotBlank(map.get("carNum"))){
                            vo.setCarNum(Integer.parseInt(map.get("carNum")));
                        }
                        if(StringUtils.isNotBlank(map.get("totalIncome"))){
                           vo.setTotalIncome(new BigDecimal(map.get("totalIncome")).divide(new BigDecimal(100)));
                        }
                    }
                }
                PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
                pageInfo = new PageInfo<>(carrierVos);
            }
        }catch (Exception e){
            log.info("根据条件查询承运商信息出现异常");
        }
        return pageInfo;
    }

    @Override
    public boolean verifyCarrierById(Long id, Integer state) {
        try{
            //审核状态 1:审核通过 2:审核拒绝 3：冻结 4:解除
            Carrier carrier = carrierDao.selectById(id);
            //审核通过
            if(FlagEnum.AUDIT_PASS.code == state){
                carrier.setState(VerifyStateEnum.AUDIT_PASS.code);
            }else if(FlagEnum.AUDIT_REJECT.code == state){
                //审核拒绝
                carrier.setState(VerifyStateEnum.AUDIT_REJECT.code);
            }else if(FlagEnum.FROZEN.code == state){
                //冻结
                carrier.setState(VerifyStateEnum.FROZEN.code);
            }else if(FlagEnum.THAW.code == state){
                //解冻
                carrier.setState(VerifyStateEnum.AUDIT_PASS.code);
            }
            return carrierDao.updateById(carrier) > 0 ? true : false;
        }catch (Exception e){
            log.info("审核承运商信息出现异常");
            throw new CommonException(e.getMessage());
        }
    }

    @Override
    public BaseCarrierVo getBaseCarrierById(Long id) {
        try{
            BaseCarrierVo vo = carrierDao.showCarrierById(id);
            if(vo != null){
                return vo;
            }
        }catch (Exception e){
            log.info("根据承运商id查看承运商信息出现异常");
        }
        return null;
    }

    @Override
    public PageInfo<BaseVehicleVo> getBaseVehicleByTerm(SeleVehicleDto dto) {
        PageInfo<BaseVehicleVo> pageInfo = null;
        try{
            List<BaseVehicleVo> baseVehicleVos = vehicleDao.getBaseVehicleByTerm(dto);
            PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
            pageInfo = new PageInfo<>(baseVehicleVos);
        }catch (Exception e){
            log.info("根据条件查看车辆信息出现异常");
        }
        return pageInfo;
    }

    /**
     * 封装承运商
     * @param carrier
     * @return
     */
    private Carrier encapCarrier(Carrier carrier,CarrierDto dto){
        carrier.setName(dto.getName());
        carrier.setLegalIdCard(dto.getLegaIdCard());
        carrier.setLegalName(dto.getLegalName());
        carrier.setLinkman(dto.getLinkman());
        carrier.setLinkmanPhone(dto.getLinkmanPhone());
        carrier.setSettleType(dto.getSettleType());
        carrier.setSettlePeriod(dto.getSettlePeriod());
        carrier.setIsInvoice(dto.getIsInvoice());
        carrier.setBusLicenseFrontImg(dto.getBusLicenseFrontImg());
        carrier.setBusLicenseBackImg(dto.getBusLicenseBackImg());
        carrier.setTransportLicenseFrontImg(dto.getTransportLicenseFrontImg());
        carrier.setTransportLicenseBackImg(dto.getTransportLicenseBackImg());
        carrier.setBankOpenFrontImg(dto.getBankOpenFrontImg());
        carrier.setBankOpenBackImg(dto.getBankOpenBackImg());
        carrier.setCreateTime(LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now()));
        carrier.setCreateUserId(dto.getUserId());
        return carrier;
    }
}