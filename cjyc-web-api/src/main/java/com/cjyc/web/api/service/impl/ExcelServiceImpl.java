package com.cjyc.web.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.cjyc.common.model.constant.TimeConstant;
import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.dao.IOrderChangeLogDao;
import com.cjyc.common.model.dao.IWaybillCarDao;
import com.cjyc.common.model.dto.web.excel.DriverLoginCountExportDto;
import com.cjyc.common.model.dto.web.excel.OrderChangePriceExportDto;
import com.cjyc.common.model.dto.web.excel.WaybillPriceCompareExportDto;
import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.common.model.entity.OrderChangeLog;
import com.cjyc.common.model.entity.defined.FullOrder;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.util.MoneyUtil;
import com.cjyc.common.model.util.TimeStampUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.excel.DriverLoginCountExportVo;
import com.cjyc.common.model.vo.web.excel.ImportOrderChangePriceVo;
import com.cjyc.common.model.vo.web.excel.WaybillPriceCompareExportVo;
import com.cjyc.web.api.service.IExcelService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExcelServiceImpl implements IExcelService {

    @Resource
    private IOrderChangeLogDao orderChangeLogDao;
    @Resource
    private IWaybillCarDao waybillCarDao;

    @Override
    public ResultVo<List<ImportOrderChangePriceVo>> listOrderChangePriceSimple(OrderChangePriceExportDto reqDto) {
        List<ImportOrderChangePriceVo> resList = Lists.newArrayList();
        List<OrderChangeLog> list = orderChangeLogDao.findAll(reqDto);
        for (OrderChangeLog orderChangeLog : list) {
            FullOrder[] fos = getFullOrderFromChangeLog(orderChangeLog);
            ImportOrderChangePriceVo iocp = getByFullOrder(fos, orderChangeLog);
            resList.add(iocp);
        }
        return BaseResultUtil.success(resList);
    }

    @Override
    public ResultVo<List<WaybillPriceCompareExportVo>> listWaybillPriceCompare(WaybillPriceCompareExportDto reqDto) {
        List<WaybillPriceCompareExportVo> list = waybillCarDao.listWaybillPriceCompare(reqDto);
        return BaseResultUtil.success(list);
    }

    @Override
    public ResultVo<List<DriverLoginCountExportVo>> ListDriverLoginCount(DriverLoginCountExportDto paramsDto) {
        //未设定日期
        if (paramsDto.getStartDate() == null && paramsDto.getEndDate() == null) {
            //默认90天内
            paramsDto.setEndDate(LocalDateTimeUtil.getMillisByLDT(LocalDateTimeUtil.getDayEndByLDT(LocalDateTime.now())));
            paramsDto.setStartDate(TimeStampUtil.addDays(paramsDto.getEndDate(), -90));
        }
        //未设定开始日期
        if (paramsDto.getStartDate() == null && paramsDto.getEndDate() != null) {
            paramsDto.setStartDate(TimeStampUtil.addDays(paramsDto.getEndDate(), -90));
        }
        //未设定结束日期
        if (paramsDto.getStartDate() != null && paramsDto.getEndDate() == null) {
            paramsDto.setEndDate(TimeStampUtil.addDays(paramsDto.getStartDate(), 90));
        }
        //设定日期范围过大
        if (paramsDto.getStartDate() != null && paramsDto.getEndDate() != null) {
            boolean f = (paramsDto.getEndDate() - paramsDto.getStartDate()) > (90 * TimeConstant.MILLS_OF_ONE_DAY);
            if (f) {
                paramsDto.setStartDate(TimeStampUtil.addDays(paramsDto.getEndDate(), -90));
            }
        }

        paramsDto.setStartTime(LocalDateTimeUtil.formatLong(paramsDto.getStartDate(), TimePatternConstant.DATETIME));
        paramsDto.setEndTime(LocalDateTimeUtil.formatLong(paramsDto.getEndDate(), TimePatternConstant.DATETIME));

        if (paramsDto.getQueryType() == 0) {
            if (StringUtils.isBlank(paramsDto.getInterfaceName())) {
                return BaseResultUtil.fail("请输入接口名称");
            }
        } else {
            String interfaceName;
            switch (paramsDto.getQueryType()) {
                case 1:
                    interfaceName = "/api/api-oauth/oauth2/token";
                    break;
                case 2:
                    interfaceName = "/cjyc-salesman/cjyc/salesman/login/pwd";
                    break;
                case 3:
                    interfaceName = "/cjyc-driver/cjyc/driver/login/login";
                    break;
                case 4:
                    interfaceName = "/cjyc-customer/cjyc/customer/login/login";
                    break;
                default:
                    interfaceName = paramsDto.getInterfaceName();
            }
            paramsDto.setInterfaceName(interfaceName);
        }


        return null;
    }

    private ImportOrderChangePriceVo getByFullOrder(FullOrder[] fos, OrderChangeLog ocl) {
        ImportOrderChangePriceVo vo = new ImportOrderChangePriceVo();
        vo.setCreateUserName(ocl.getCreateUser());
        vo.setOrderCreateUserName(fos[0].getCreateUserName());
        vo.setNo(ocl.getOrderNo());
        vo.setCustomerName(fos[1].getCustomerName());
        vo.setCustomerTypeStr(getCustomerTypeStr(fos[1].getCustomerType()));
        vo.setCreateTimeStr(LocalDateTimeUtil.formatLong(ocl.getCreateTime(), "yyyy/MM/dd HH:mm:ss"));

        BigDecimal oldTotalFee = fos[0].getTotalFee();
        BigDecimal oldWlFee = getWlFee(fos[0].getList());
        vo.setOldState(getOrderState(fos[0].getState()));
        vo.setOldCarNum(fos[0].getCarNum());
        vo.setOldTotalFee(MoneyUtil.fenToYuan(oldTotalFee, MoneyUtil.PATTERN_TWO));
        vo.setOldWlTotalFee(MoneyUtil.fenToYuan(oldWlFee, MoneyUtil.PATTERN_TWO));
        vo.setOldAgencyFee(MoneyUtil.fenToYuan(oldTotalFee.subtract(oldWlFee), MoneyUtil.PATTERN_TWO));

        BigDecimal newTotalFee = fos[1].getTotalFee();
        BigDecimal newWlFee = getWlFee(fos[1].getList());
        vo.setNewState(getOrderState(fos[1].getState()));
        vo.setNewCarNum(fos[1].getCarNum());
        vo.setNewTotalFee(MoneyUtil.fenToYuan(newTotalFee, MoneyUtil.PATTERN_TWO));
        vo.setNewWlTotalFee(MoneyUtil.fenToYuan(newWlFee, MoneyUtil.PATTERN_TWO));
        vo.setNewAgencyFee(MoneyUtil.fenToYuan(newTotalFee.subtract(newWlFee), MoneyUtil.PATTERN_TWO));
        vo.setRemark(ocl.getReason());
        return vo;
    }

    private String getOrderState(Integer state) {
        switch (state) {
            case 0:
                return "预订单";
            case 2:
                return "待确认(客户提交)";
            case 5:
                return "待确认(业务员提交)";
            case 15:
                return "待付款";
            case 25:
                return "待调度";
            case 55:
                return "运输中";
            case 100:
                return "已交付";
            case 113:
                return "已取消";
            default:
                return "";
        }
    }

    private String getCustomerTypeStr(Integer customerType) {
        switch (customerType) {
            case 1:
                return "个人";
            case 2:
                return "企业";
            case 3:
                return "合伙人";
            default:
                return "";
        }
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

        FullOrder oFullOrder = StringUtils.isBlank(orderChangeLog.getOldContent()) ? new FullOrder() : JSON.parseObject(orderChangeLog.getOldContent(), FullOrder.class);

        FullOrder nFullOrder = StringUtils.isBlank(orderChangeLog.getOldContent()) ? new FullOrder() : JSON.parseObject(orderChangeLog.getNewContent(), FullOrder.class);

        return new FullOrder[]{oFullOrder, nFullOrder};
    }
}
