package com.cjyc.common.system.service.impl;

import com.cjyc.common.model.dao.ICustomerDao;
import com.cjyc.common.model.dao.ICustomerLineDao;
import com.cjyc.common.model.dto.CommonDto;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.customerLine.CustomerLineVo;
import com.cjyc.common.system.service.ICsCustomerLineService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CsCustomerLineServiceImpl implements ICsCustomerLineService {

    @Resource
    private ICustomerLineDao customerLineDao;

    @Override
    public ResultVo<PageVo<CustomerLineVo>> queryLinePage(CommonDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<CustomerLineVo> lineVos = customerLineDao.findCustomerLine(dto.getLoginId());
        PageInfo<CustomerLineVo> pageInfo =  new PageInfo<>(lineVos);
        return BaseResultUtil.success(pageInfo);
    }

   /* @Async
    @Override
    public void asyncSave(Order order) {
        try {
            if(order.getLineId() == null){
                return;
            }
            CustomerLine customerLine = new CustomerLine();
            customerLine.setCustomerId(order.getCustomerId());
            customerLine.setOperateId(order.getCreateUserId());
            customerLine.setLineCode(order.getStartCityCode() + order.getEndCityCode());
            customerLine.setStartAdress(getOrderStartAddress(order));
            customerLine.setStartContact(order.getPickContactName());
            customerLine.setStartContactPhone(order.getPickContactPhone());
            customerLine.setEndAdress(getOrderEndAddress(order));
            customerLine.setEndContact(order.getBackContactName());
            customerLine.setEndContactPhone(order.getBackContactPhone());
            customerLine.setCreateTime(System.currentTimeMillis());
            customerLineDao.insert(customerLine);
        } catch (Exception e) {
            LogUtil.error(MessageFormat.format("订单{0}下单保存历史线路失败",order.getNo()), e);
        }

    }

    private String getOrderStartAddress(Order order) {
        return order.getStartProvince() == null ? "" : order.getStartProvince()
                + order.getStartCity() == null ? "" : order.getStartCity()
                + order.getStartArea() == null ? "" : order.getStartArea()
                + order.getStartAddress() == null ? "" : order.getStartAddress();
    }

    private String getOrderEndAddress(Order order) {
        return order.getEndProvince() == null ? "" : order.getEndProvince()
                + order.getEndCity() == null ? "" : order.getEndCity()
                + order.getEndArea() == null ? "" : order.getEndArea()
                + order.getEndAddress() == null ? "" : order.getEndAddress();
    }*/

}