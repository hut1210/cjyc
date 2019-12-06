package com.cjyc.customer.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjyc.common.model.dao.ICustomerDao;
import com.cjyc.common.model.dao.ILineDao;
import com.cjyc.common.model.dao.IStoreDao;
import com.cjyc.common.model.dto.customer.freightBill.FindStoreDto;
import com.cjyc.common.model.dto.customer.freightBill.TransportDto;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.entity.Line;
import com.cjyc.common.model.entity.Store;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.customerLine.BusinessStoreVo;
import com.cjyc.common.model.vo.customer.customerLine.StoreListVo;
import com.cjyc.customer.api.service.IInquiryService;
import com.cjyc.customer.api.service.ITransportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TransportServiceImpl implements ITransportService {

    @Resource
    private ICustomerDao customerDao;
    @Resource
    private ILineDao lineDao;
    @Resource
    private IInquiryService inquiryService;
    @Resource
    private IStoreDao storeDao;

    @Override
    public ResultVo<Map<String,Object>> linePriceByCode(TransportDto dto) {
        Customer customer = customerDao.selectOne(new QueryWrapper<Customer>().lambda()
                .eq(Customer::getId, dto.getLgoinId()));
        if(customer == null){
            return BaseResultUtil.fail("该客户不存在,请检查");
        }
        Line line = lineDao.getLinePriceByCode(dto.getFromCode(), dto.getToCode());
        if(line == null){
            return BaseResultUtil.fail("该班线不存在");
        }
        Map<String,Object> map = new HashMap<>(10);
        map.put("defaultWlFee",line.getDefaultWlFee() == null ? BigDecimal.ZERO : line.getDefaultWlFee().divide(new BigDecimal(100)));
        inquiryService.saveInquiry(dto,line.getDefaultWlFee());
        return BaseResultUtil.success(map);
    }

    @Override
    public ResultVo<StoreListVo> findStore(FindStoreDto dto) {
        List<BusinessStoreVo> storeVos = new ArrayList<>(10);
        List<Store> stores = storeDao.selectList(new QueryWrapper<Store>().lambda()
                                                .eq(Store::getCityCode,dto.getCityCode()));
        if(!CollectionUtils.isEmpty(stores)){
            for(Store store : stores){
                BusinessStoreVo bsv = new BusinessStoreVo();
                bsv.setStoreId(store.getId());
                bsv.setName(store.getName());
                bsv.setDetailAddr(store.getDetailAddr());
                storeVos.add(bsv);
            }
        }
        StoreListVo storeVoList = new StoreListVo();
        storeVoList.setStoreVoList(storeVos);
        return BaseResultUtil.success(storeVoList);
    }
}