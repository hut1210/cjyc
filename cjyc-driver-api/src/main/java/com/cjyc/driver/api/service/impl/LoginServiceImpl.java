package com.cjyc.driver.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.ReturnMsg;
import com.cjkj.common.service.impl.SuperServiceImpl;
import com.cjkj.usercenter.dto.yc.AddDeptAndUserResp;
import com.cjyc.common.model.dao.ICarSeriesDao;
import com.cjyc.common.model.dao.ICarrierDao;
import com.cjyc.common.model.dao.ICarrierDriverConDao;
import com.cjyc.common.model.dao.IDriverDao;
import com.cjyc.common.model.dto.LoginDto;
import com.cjyc.common.model.entity.Carrier;
import com.cjyc.common.model.entity.CarrierDriverCon;
import com.cjyc.common.model.entity.Driver;
import com.cjyc.common.model.enums.CardTypeEnum;
import com.cjyc.common.model.enums.CommonStateEnum;
import com.cjyc.common.model.enums.SendNoTypeEnum;
import com.cjyc.common.model.enums.transport.*;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.driver.login.DriverLoginVo;
import com.cjyc.common.system.service.ICsCarrierService;
import com.cjyc.common.system.service.ICsDriverService;
import com.cjyc.common.system.service.ISendNoService;
import com.cjyc.driver.api.service.ILoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
@Slf4j
public class LoginServiceImpl extends SuperServiceImpl<IDriverDao, Driver> implements ILoginService {

    @Resource
    private ICarrierDao carrierDao;
    @Resource
    private IDriverDao driverDao;
    @Resource
    private ICarrierDriverConDao carrierDriverConDao;
    @Resource
    private ISendNoService sendNoService;
    @Resource
    private ICsDriverService csDriverService;
    @Resource
    private ICsCarrierService csCarrierService;

    private static final Long NOW = LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now());

    @Override
    public ResultVo<DriverLoginVo> login(LoginDto dto) {
        //查询该手机号是否在司机表中
        Driver driver = driverDao.selectOne(new QueryWrapper<Driver>().lambda()
                .eq(Driver::getPhone, dto.getPhone()));
        if(driver == null){


        }
        return null;
    }

    /**
     * 将新注册司机信息注册到物流平台和韵车
     */
    private void addToPlatform(String phone){
        //架构组和韵车添加数据
        Driver driver = new Driver();
        String no = sendNoService.getNo(SendNoTypeEnum.DRIVER, 6);
        driver.setName(no);
        driver.setRealName(no);
        driver.setPhone(phone);
        driver.setType(DriverTypeEnum.SOCIETY.code);
        driver.setIdentity(DriverIdentityEnum.PERSONAL_DRIVER.code);
        driver.setSource(DriverSourceEnum.APP_DRIVER.code);
        driver.setCreateTime(NOW);
        //新增数据到物流平台
        ResultData<Long> rd = csDriverService.saveDriverToPlatform(driver);
        driver.setUserId(rd.getData());
        driverDao.insert(driver);

        //保存承运商信息
        Carrier carrier = new Carrier();
        carrier.setName(no);
        carrier.setType(CarrierTypeEnum.PERSONAL.code);
        carrier.setLinkman(no);
        carrier.setLinkmanPhone(phone);
        carrier.setSettleType(ModeTypeEnum.TIME.code);
        carrier.setState(CommonStateEnum.WAIT_CHECK.code);
        carrier.setCreateTime(NOW);
        /*ResultData<AddDeptAndUserResp> carrierRd = csCarrierService.saveCarrierToPlatform(carrier);
        if (!ReturnMsg.SUCCESS.getCode().equals(carrierRd.getCode())) {
            //return BaseResultUtil.fail("承运商机构添加失败");
        }*/

        //承运商与司机关系
        CarrierDriverCon cdc = new CarrierDriverCon();
        cdc.setDriverId(driver.getId());
        cdc.setCarrierId(carrier.getId());
        cdc.setState(CommonStateEnum.WAIT_CHECK.code);
        cdc.setRole(DriverIdentityEnum.PERSONAL_DRIVER.code);
        carrierDriverConDao.insert(cdc);
    }
}