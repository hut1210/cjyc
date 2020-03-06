package com.cjyc.web.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.cjyc.common.model.dao.IOrderChangeLogDao;
import com.cjyc.common.model.dto.web.excel.ChangePriceExportDto;
import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.common.model.entity.OrderChangeLog;
import com.cjyc.common.model.entity.defined.FullOrder;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.util.MoneyUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.excel.ImportOrderChangePriceVo;
import com.cjyc.web.api.service.IExcelService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
            ImportOrderChangePriceVo iocp = getByFullOrder(fos, orderChangeLog);

        }
        return null;
    }

    private ImportOrderChangePriceVo getByFullOrder(FullOrder[] fos, OrderChangeLog ocl) {
        ImportOrderChangePriceVo vo = new ImportOrderChangePriceVo();
        vo.setCreateUserName(ocl.getCreateUser());
        vo.setNo(ocl.getOrderNo());
        vo.setCustomerName(fos[1].getCustomerName());
        vo.setCustomerType(fos[1].getCustomerType());
        vo.setCreateTimeStr(LocalDateTimeUtil.formatLong(ocl.getCreateTime(), "yyyy/MM/dd"));
        vo.setOldCarNum(fos[0].getCarNum());

        BigDecimal totalFee = fos[0].getTotalFee();
        BigDecimal wlFee = getWlFee(fos[0].getList());
        vo.setOldTotalFee(MoneyUtil.fenToYuan(wlFee, MoneyUtil.PATTERN_TWO));
        vo.setOldWlTotalFee(MoneyUtil.fenToYuan(totalFee, MoneyUtil.PATTERN_TWO));
        vo.setOldAgencyFee(MoneyUtil.fenToYuan(totalFee.subtract(wlFee), MoneyUtil.PATTERN_TWO));

        BigDecimal totalFee2 = fos[1].getTotalFee();
        BigDecimal wlFee2 = getWlFee(fos[1].getList());
        vo.setNewCarNum(fos[1].getCarNum());
        vo.setNewAgencyFee(MoneyUtil.fenToYuan(totalFee2, MoneyUtil.PATTERN_TWO));
        vo.setNewWlTotalFee(MoneyUtil.fenToYuan(wlFee2, MoneyUtil.PATTERN_TWO));
        vo.setNewTotalFee(MoneyUtil.fenToYuan(totalFee2.subtract(wlFee2), MoneyUtil.PATTERN_TWO));
        return vo;
    }

    private BigDecimal getWlFee(List<OrderCar> list) {
        if (CollectionUtils.isEmpty(list)) {
            return BigDecimal.ZERO;
        }

        BigDecimal wlFee = BigDecimal.ZERO;
        for (OrderCar orderCar : list) {
            if (orderCar == null) {
                continue;
            }
            wlFee = wlFee.add(MoneyUtil.nullToZero(orderCar.getPickFee()))
                    .add(MoneyUtil.nullToZero(orderCar.getTrunkFee()))
                    .add(MoneyUtil.nullToZero(orderCar.getBackFee()))
                    .add(MoneyUtil.nullToZero(orderCar.getAddInsuranceFee()));

        }
        return wlFee;
    }

    private FullOrder[] getFullOrderFromChangeLog(OrderChangeLog orderChangeLog) {
        FullOrder oFullOrder;
        if (StringUtils.isNotBlank(orderChangeLog.getOldContent())) {
            oFullOrder = JSON.parseObject(orderChangeLog.getOldContent(), FullOrder.class);
        } else {
            oFullOrder = new FullOrder();
        }

        FullOrder nFullOrder;
        if (StringUtils.isNotBlank(orderChangeLog.getOldContent())) {
            nFullOrder = JSON.parseObject(orderChangeLog.getOldContent(), FullOrder.class);
        } else {
            nFullOrder = new FullOrder();
        }

        return new FullOrder[]{oFullOrder, nFullOrder};
    }
}
