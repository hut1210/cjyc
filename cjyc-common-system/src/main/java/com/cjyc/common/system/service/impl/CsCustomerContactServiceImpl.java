package com.cjyc.common.system.service.impl;

import com.cjyc.common.model.dao.ICustomerContactDao;
import com.cjyc.common.model.entity.CustomerContact;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.system.service.ICsCustomerContactService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 客户联系人
 *
 * @author JPG
 */
@Service
public class CsCustomerContactServiceImpl implements ICsCustomerContactService {

    @Resource
    private ICustomerContactDao customerContactDao;

    @Async
    @Override
    public void asyncSaveByOrder(Order order) {

        if (order == null) {
            return;
        }

        if (order.getPickContactName() != null && order.getPickContactPhone() != null) {
            int num = customerContactDao.countByPhone(order.getPickContactPhone());
            if (num == 0) {
                CustomerContact startContact = new CustomerContact();
                startContact.setCustomerId(order.getCustomerId());
                startContact.setName(order.getPickContactName());
                startContact.setPhone(order.getPickContactPhone());
                startContact.setProvince(order.getStartProvince());
                startContact.setProvinceCode(order.getEndProvinceCode());
                startContact.setCity(order.getStartCity());
                startContact.setCityCode(order.getStartCityCode());
                startContact.setArea(order.getStartArea());
                startContact.setAreaCode(order.getStartAreaCode());
                startContact.setDetailAddr(order.getStartAddress());
                startContact.setRewark(order.getNo());
                customerContactDao.insert(startContact);
            }
        }

        if (order.getBackContactName() != null && order.getBackContactPhone() != null) {
            int num = customerContactDao.countByPhone(order.getBackContactPhone());
            if(num == 0){
                CustomerContact endContact = new CustomerContact();
                endContact.setCustomerId(order.getCustomerId());
                endContact.setName(order.getBackContactName());
                endContact.setPhone(order.getBackContactPhone());
                endContact.setProvince(order.getEndProvince());
                endContact.setProvinceCode(order.getEndProvinceCode());
                endContact.setCity(order.getEndCity());
                endContact.setCityCode(order.getEndCityCode());
                endContact.setArea(order.getEndArea());
                endContact.setAreaCode(order.getEndAreaCode());
                endContact.setDetailAddr(order.getEndAddress());
                endContact.setRewark(order.getNo());
                customerContactDao.insert(endContact);
            }
        }
    }
}
