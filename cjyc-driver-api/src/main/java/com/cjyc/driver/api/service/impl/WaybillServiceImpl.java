package com.cjyc.driver.api.service.impl;

import com.cjyc.common.model.dao.IOrderCarDao;
import com.cjyc.common.model.dao.IWaybillCarDao;
import com.cjyc.common.model.dao.IWaybillDao;
import com.cjyc.common.model.dto.driver.task.ReplenishInfoDto;
import com.cjyc.common.model.dto.driver.waybill.WaitAllotDto;
import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.common.model.entity.WaybillCar;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.driver.waybill.WaitAllotVo;
import com.cjyc.common.system.service.ICsCarrierService;
import com.cjyc.common.model.constant.Constant;
import com.cjyc.driver.api.service.IWaybillService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class WaybillServiceImpl implements IWaybillService {
    @Resource
    private IWaybillDao waybillDao;
    @Resource
    private IWaybillCarDao waybillCarDao;
    @Resource
    private IOrderCarDao orderCarDao;
    @Resource
    private ICsCarrierService csCarrierService;

    @Override
    public ResultVo<PageVo<WaitAllotVo>> getWaitAllotPage(WaitAllotDto dto) {
        //查询所属承运商ID
        List<Long> carrierIds =  csCarrierService.getBelongIdsListByDriver(dto.getDriverId());
        if(CollectionUtils.isEmpty(carrierIds)){
            return null;
        }
        //查询所属承运商待分配运单
        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize(), true);
        List<WaitAllotVo> list = waybillDao.findWaitAllotListByCarrierIds(carrierIds);
        PageInfo<WaitAllotVo> pageInfo = new PageInfo<>(list);
        if(pageInfo.getPages() < dto.getCurrentPage()){
            pageInfo.setList(null);
        }
        return null;
    }
}
