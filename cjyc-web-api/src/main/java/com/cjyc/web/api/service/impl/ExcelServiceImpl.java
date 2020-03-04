package com.cjyc.web.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.cjyc.common.model.dao.IOrderChangeLogDao;
import com.cjyc.common.model.dto.web.excel.ChangePriceExportDto;
import com.cjyc.common.model.entity.OrderChangeLog;
import com.cjyc.common.model.entity.defined.FullOrder;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.excel.ImportOrderChangePriceVo;
import com.cjyc.web.api.service.IExcelService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service
public class ExcelServiceImpl implements IExcelService {

    @Resource
    private IOrderChangeLogDao orderChangeLogDao;
    @Override
    public ResultVo<List<ImportOrderChangePriceVo>> listOrderChangePriceSimple(ChangePriceExportDto reqDto) {
        List<ImportOrderChangePriceVo> resList = Lists.newArrayList();
        List<OrderChangeLog> list = orderChangeLogDao.findAll(reqDto);
        for (OrderChangeLog orderChangeLog : list) {
            FullOrder[] fos = getFullOrderFromChangeLog(orderChangeLog);
            ImportOrderChangePriceVo iocp = getByFullOrder(fos);
            
        }
        return null;
    }

    private ImportOrderChangePriceVo getByFullOrder(FullOrder[] fos) {
        ImportOrderChangePriceVo vo = new ImportOrderChangePriceVo();
        vo.setNo(fos[1].getNo());
        vo.setCustomerName(fos[1].getCustomerName());
        vo.setCustomerTypeStr(String.valueOf(fos[1].getCustomerType()));
        vo.setOldTotalFee(fos[0].getTotalFee() == null ? BigDecimal.ZERO : fos[0].getTotalFee() );
/*        vo.setOldWlTotalFee();
        vo.setOldAgencyFee(vo.getOldTotalFee());
        vo.setNewTotalFee();
        vo.setNewWlTotalFee();
        vo.setNewAgencyFee();*/
        return null;

    }

    private FullOrder[] getFullOrderFromChangeLog(OrderChangeLog orderChangeLog) {
        FullOrder oFullOrder;
        if(StringUtils.isNotBlank(orderChangeLog.getOldContent())){
            oFullOrder = JSON.parseObject(orderChangeLog.getOldContent(), FullOrder.class);
        }else{
            oFullOrder = new FullOrder();
        }

        FullOrder nFullOrder;
        if(StringUtils.isNotBlank(orderChangeLog.getOldContent())){
            nFullOrder = JSON.parseObject(orderChangeLog.getOldContent(), FullOrder.class);
        }else{
            nFullOrder = new FullOrder();
        }

        return new FullOrder[]{oFullOrder, nFullOrder};
    }
}
