package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.IAdminDao;
import com.cjyc.common.model.dao.IBankCardBindDao;
import com.cjyc.common.model.dao.ICarrierDao;
import com.cjyc.common.model.dao.IDriverDao;
import com.cjyc.common.model.dto.web.carrier.CarrierDto;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.entity.BankCardBind;
import com.cjyc.common.model.entity.Carrier;
import com.cjyc.common.model.entity.Driver;
import com.cjyc.common.model.enums.SysEnum;
import com.cjyc.web.api.exception.CommonException;
import com.cjyc.web.api.service.ICarrierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
public class CarrierServiceImpl extends ServiceImpl<ICarrierDao, Carrier> implements ICarrierService {

    @Resource
    private ICarrierDao iCarrierDao;

    @Resource
    private IAdminDao iAdminDao;

    @Resource
    private IBankCardBindDao iBankCardBindDao;

    @Override
    public boolean saveCarrier(CarrierDto dto) {
        int i ;
        int j = 0;
        try{
            //添加承运商
            Carrier carrier = new Carrier();
            carrier.setName(dto.getName());
            carrier.setType(dto.getType());
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
            i = iCarrierDao.insert(carrier);
            Admin admin = null;
            if(i > 0){
                //添加承运商主管理员
                admin = new Admin();
                admin.setName("admin");
                admin.setPhone(dto.getLinkmanPhone());
                j = iAdminDao.insert(admin);
            }
            if(j > 0){
                //保存银行卡信息
                BankCardBind bcb = new BankCardBind();
                bcb.setUserId(admin.getUserId());
                bcb.setCardType(dto.getCardType());
                bcb.setCardNo(dto.getCardNo());
                bcb.setBankName(dto.getBankName());
                bcb.setIdCard(dto.getIdCard());
                bcb.setCardPhone(dto.getLinkmanPhone());
                bcb.setState(Integer.parseInt(SysEnum.ONE.getValue()));
                return iBankCardBindDao.insert(bcb) > 0 ? true : false;
            }
        }catch (Exception e){
            log.info("新增承运商信息出现异常");
            throw new CommonException(e.getMessage());
        }
        return false;
    }
}