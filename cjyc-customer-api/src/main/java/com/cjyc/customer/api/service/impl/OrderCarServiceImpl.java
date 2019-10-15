package com.cjyc.customer.api.service.impl;

import com.cjyc.common.model.dao.IOrderCarDao;
import com.cjyc.common.model.vo.customer.OrderCarCenterVo;
import com.cjyc.customer.api.service.IOrderCarService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 *  @author: zj
 *  @Date: 2019/10/14 9:35
 *  @Description:订单明细（车辆信息）
 */
@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
public class OrderCarServiceImpl implements IOrderCarService {

    @Resource
    private IOrderCarDao iOrderCarDao;

    @Override
    public OrderCarCenterVo getOrderCarInfoById(Long orderCarId) {
        OrderCarCenterVo vo = new OrderCarCenterVo();
        try{
            vo = iOrderCarDao.getOrderCarInfoById(orderCarId);
            if(vo != null){
                if(StringUtils.isBlank(vo.getInsuredAmount())){
                    vo.setInsuredAmount("");
                    vo.setInsuranceFee("");
                }
                if(StringUtils.isBlank(vo.getVin())){
                    vo.setVin("");
                }
                if(StringUtils.isBlank(vo.getPlateNo())){
                    vo.setPlateNo("");
                }
                return vo;
            }
        }catch (Exception e){
            log.info("查看车辆信息出现异常");
        }
        return vo;
    }
}