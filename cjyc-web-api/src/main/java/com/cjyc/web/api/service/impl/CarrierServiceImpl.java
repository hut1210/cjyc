package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.web.carrier.CarrierDto;
import com.cjyc.common.model.dto.web.carrier.SeleCarrierDto;
import com.cjyc.common.model.dto.web.carrier.SeleVehicleDto;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.SysEnum;
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
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
public class CarrierServiceImpl extends ServiceImpl<ICarrierDao, com.cjyc.common.model.entity.Carrier> implements ICarrierService {

    @Resource
    private ICarrierDao iCarrierDao;

    @Resource
    private IAdminDao iAdminDao;

    @Resource
    private IDriverDao iDriverDao;

    @Resource
    private IVehicleDao iVehicleDao;

    @Resource
    private ICarrierDriverConDao iCarrierDriverConDao;

    @Resource
    private IBankCardBindDao iBankCardBindDao;

    @Resource
    private ICarrierCarCountDao iCarrierCarCountDao;

    @Resource
    private ICarrierCityConDao iCarrierCityConDao;

    @Resource
    private ICarrierCityConService iCarrierCityConService;

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
            carrier.setState(SysEnum.ZERO.getValue());
            carrier.setType(SysEnum.TWO.getValue());
            carrier = encapCarrier(carrier,dto);
            i = iCarrierDao.insert(carrier);
            Admin admin = null;
            if(i > 0){
                //添加承运商主管理员
                admin = new Admin();
                admin.setName(dto.getLinkman());
                admin.setPhone(dto.getLinkmanPhone());
                admin.setState(SysEnum.TWO.getValue());
                k = iAdminDao.insert(admin);
            }
            if(k > 0){
                //添加承运商司机管理员
                driver = new Driver();
                driver.setPhone(dto.getLinkmanPhone());
                driver.setIdentity(SysEnum.ONE.getValue());
                driver.setState(SysEnum.TWO.getValue());
                driver.setCreateUserId(dto.getUserId());
                m = iDriverDao.insert(driver);
            }
            if(m > 0){
                //承运商与司机绑定关系
                CarrierDriverCon cdc = new CarrierDriverCon();
                cdc.setDriverId(driver.getId());
                cdc.setCarrierId(carrier.getId());
                cdc.setRole(SysEnum.ONE.getValue());
                j = iCarrierDriverConDao.insert(cdc);
            }
            if(j > 0){
                //保存银行卡信息
                BankCardBind bcb = new BankCardBind();
                bcb.setUserId(admin.getUserId());
                bcb.setCardType(dto.getCardType());
                bcb.setCardNo(dto.getCardNo());
                bcb.setBankName(dto.getBankName());
                bcb.setIdCard(dto.getLegaIdCard());
                bcb.setCardPhone(dto.getLinkmanPhone());
                bcb.setState(SysEnum.ONE.getValue());
                n = iBankCardBindDao.insert(bcb);
            }
            //业务范围
            if(n > 0){
                CarrierCityCon ccc = iCarrierCityConService.encapCarrCityCon(dto);
                ccc.setCarrierId(carrier.getId());
                return iCarrierCityConDao.insert(ccc) > 0 ? true : false;
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
            Carrier carrier = iCarrierDao.selectById(dto.getId());
            carrier = encapCarrier(carrier,dto);
            i = iCarrierDao.updateById(carrier);
            if(i > 0){
                //更新承运商主管理员
                admin = iAdminDao.getByCarrierId(dto.getId());
                if(admin != null){
                    admin.setName(dto.getLinkman());
                    admin.setPhone(dto.getLinkmanPhone());
                    j = iAdminDao.updateById(admin);
                }
            }
            if(j > 0){
                //更新承运商司机管理员
                Driver driver = iDriverDao.getDriverByDriverId(dto.getId());
                if(driver != null){
                    driver.setPhone(dto.getLinkmanPhone());
                    k = iDriverDao.updateById(driver);
                }
            }
            if(k > 0){
                //更新银行卡信息
                BankCardBind bcb = iBankCardBindDao.getBankCardBindByUserId(admin.getUserId());
                if(bcb != null){
                    bcb.setCardType(dto.getCardType());
                    bcb.setCardNo(dto.getCardNo());
                    bcb.setBankName(dto.getBankName());
                    bcb.setIdCard(dto.getLegaIdCard());
                    bcb.setCardPhone(dto.getLinkmanPhone());
                    m = iBankCardBindDao.updateById(bcb);
                }
            }
            if(m > 0){
                //承运商业务范围
                iCarrierCityConService.updateCarrCityCon(dto.getId(),dto);
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
            List<CarrierVo> carrierVos = iCarrierDao.getCarrierByTerm(dto);
            if(carrierVos != null && carrierVos.size() > 0){
                for(CarrierVo vo : carrierVos){
                    Map<String,String> map = iCarrierCarCountDao.getCarIncomeByCarrId(vo.getId());
                    if(!map.isEmpty()){
                        if(StringUtils.isNotBlank(map.get("carNum"))){
                            vo.setCarNum(Integer.parseInt(map.get("carNum")));
                        }
                        if(StringUtils.isNotBlank(map.get("totalIncome"))){
                           vo.setTotalIncome(new BigDecimal(map.get("totalIncome")).divide(new BigDecimal(100)));
                        }
                    }
                }
            }
            PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
            pageInfo = new PageInfo<>(carrierVos);
        }catch (Exception e){
            log.info("根据条件查询承运商信息出现异常");
        }
        return pageInfo;
    }

    @Override
    public boolean verifyCarrierById(Long id, String state) {
        try{
            //审核状态 1:审核通过 2:审核拒绝 3：冻结 4:解除
            com.cjyc.common.model.entity.Carrier carrier = iCarrierDao.selectById(id);
            //审核通过
            if(state.equals(SysEnum.ONE.getValue())){
                carrier.setState(SysEnum.TWO.getValue());
            }else if(state.equals(SysEnum.TWO.getValue())){
                //审核拒绝
                carrier.setState(SysEnum.FOUR.getValue());
            }else if(state.equals(SysEnum.THREE.getValue())){
                //冻结
                carrier.setState(SysEnum.SEVEN.getValue());
            }else if(state.equals(SysEnum.FOUR.getValue())){
                //解冻
                carrier.setState(SysEnum.TWO.getValue());
            }
            return iCarrierDao.updateById(carrier) > 0 ? true : false;
        }catch (Exception e){
            log.info("审核承运商信息出现异常");
            throw new CommonException(e.getMessage());
        }
    }

    @Override
    public BaseCarrierVo getBaseCarrierById(Long id) {
        try{
            BaseCarrierVo vo = iCarrierDao.showCarrierById(id);
            if(vo != null){
                return vo;
            }
        }catch (Exception e){
            log.info("根据承运商id查看承运商信息出现异常");
        }
        return null;
    }

    @Override
    public BusinessCityCode getCarrierBusiById(Long id) {
        BusinessCityCode bcc = null;
        try{
           CarrierCityCon ccc =  iCarrierCityConDao.getCarrierCodeByCarrierId(id);
           if(ccc != null){
               bcc = iCarrierCityConService.showCarrCityCon(ccc);
           }
        }catch (Exception e){
            log.info("根据承运商id查看承运商业务范围出现异常");
        }
        return bcc;
    }

    @Override
    public PageInfo<BaseVehicleVo> getBaseVehicleByTerm(SeleVehicleDto dto) {
        PageInfo<BaseVehicleVo> pageInfo = null;
        try{
            List<BaseVehicleVo> baseVehicleVos = iVehicleDao.getBaseVehicleByTerm(dto);
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
        return carrier;
    }
}