package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjkj.common.utils.ExcelUtil;
import com.cjyc.common.model.constant.FieldConstant;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.customer.invoice.OrderAmountDto;
import com.cjyc.common.model.dto.web.invoice.InvoiceDetailAndConfirmDto;
import com.cjyc.common.model.dto.web.invoice.InvoiceQueryDto;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.JsonUtils;
import com.cjyc.common.model.util.TimeStampUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.invoice.InvoiceApplyVo;
import com.cjyc.common.model.vo.web.invoice.InvoiceApplyExportExcel;
import com.cjyc.common.model.vo.web.invoice.InvoiceDetailVo;
import com.cjyc.web.api.service.IInvoiceApplyService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 发票申请信息表 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-11-02
 */
@Slf4j
@Service
public class InvoiceApplyServiceImpl extends ServiceImpl<IInvoiceApplyDao, InvoiceApply> implements IInvoiceApplyService {
    @Autowired
    private ICustomerInvoiceDao customerInvoiceDao;
    @Autowired
    private IInvoiceOrderConDao invoiceOrderConDao;
    @Autowired
    private IOrderDao orderDao;
    @Autowired
    private IAdminDao adminDao;

    @Override
    public ResultVo getInvoiceApplyPage(InvoiceQueryDto dto) {
        log.info("====>web端-分页查询发票申请信息列表,请求json数据 :: "+ JsonUtils.objectToJson(dto));
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<InvoiceApply> list = getInvoiceApplyList(dto);
        PageInfo pageInfo = new PageInfo(list);
        List<InvoiceApply> pageInfoList = pageInfo.getList();
        List<InvoiceApplyVo> returnList = new ArrayList<>(10);
        for (InvoiceApply invoiceApply : pageInfoList) {
            InvoiceApplyVo vo = new InvoiceApplyVo();
            BeanUtils.copyProperties(invoiceApply,vo);
            returnList.add(vo);
        }
        pageInfo.setList(returnList);
        return BaseResultUtil.success(pageInfo);
    }

    private List<InvoiceApply> getInvoiceApplyList(InvoiceQueryDto dto) {
        if (dto.getApplyTimeEnd() != null && dto.getApplyTimeEnd() != 0) {
            dto.setApplyTimeEnd(TimeStampUtil.convertEndTime(dto.getApplyTimeEnd()));
        }
        if (dto.getInvoiceTimeEnd() != null && dto.getInvoiceTimeEnd() != 0) {
            dto.setInvoiceTimeEnd(TimeStampUtil.convertEndTime(dto.getInvoiceTimeEnd()));
        }

        LambdaQueryWrapper<InvoiceApply> queryWrapper = getInvoiceApplyLambdaQueryWrapper(dto);
        return super.list(queryWrapper);
    }

    private LambdaQueryWrapper<InvoiceApply> getInvoiceApplyLambdaQueryWrapper(InvoiceQueryDto dto) {
        LambdaQueryWrapper<InvoiceApply> queryWrapper = new QueryWrapper<InvoiceApply>().lambda()
                .like(!StringUtils.isEmpty(dto.getCustomerName()),InvoiceApply::getCustomerName,dto.getCustomerName())
                .like(!StringUtils.isEmpty(dto.getInvoiceNo()),InvoiceApply::getInvoiceNo,dto.getInvoiceNo())
                .like(!StringUtils.isEmpty(dto.getOperationName()),InvoiceApply::getOperationName,dto.getOperationName())
                .eq(!StringUtils.isEmpty(dto.getState()),InvoiceApply::getState,dto.getState());
        if (!Objects.isNull(dto.getApplyTimeStart())) {
            queryWrapper = queryWrapper.ge(InvoiceApply::getApplyTime,dto.getApplyTimeStart());
        }
        if (!Objects.isNull(dto.getApplyTimeEnd())) {
            queryWrapper = queryWrapper.le(InvoiceApply::getApplyTime,dto.getApplyTimeEnd());
        }
        if (!Objects.isNull(dto.getInvoiceTimeStart())) {
            queryWrapper = queryWrapper.ge(InvoiceApply::getInvoiceTime,dto.getInvoiceTimeStart());
        }
        if (!Objects.isNull(dto.getInvoiceTimeEnd())) {
            queryWrapper = queryWrapper.le(InvoiceApply::getInvoiceTime,dto.getInvoiceTimeEnd());
        }
        return queryWrapper;
    }

    @Override
    public ResultVo getDetail(InvoiceDetailAndConfirmDto dto) {
        log.info("====>web端-查看开票明细,请求json数据 :: "+ JsonUtils.objectToJson(dto));
        InvoiceDetailVo detailVo = new InvoiceDetailVo();
        // 根据客户ID，主键ID查询发票申请信息
        LambdaQueryWrapper<InvoiceApply> queryWrapper = new QueryWrapper<InvoiceApply>().lambda()
                .eq(InvoiceApply::getCustomerId, dto.getCustomerId())
                .eq(InvoiceApply::getId, dto.getInvoiceApplyId())
                .select(InvoiceApply::getAmount,InvoiceApply::getInvoiceId);
        InvoiceApply invoice = super.getOne(queryWrapper);
        detailVo.setAmount(Objects.isNull(invoice) ? new BigDecimal(0) : invoice.getAmount());

        // 根据发票信息ID查询客户发票信息
        if (!Objects.isNull(invoice)) {
            CustomerInvoice customerInvoice = customerInvoiceDao.selectById(invoice.getInvoiceId());
            detailVo.setCustomerInvoice(customerInvoice);
        }

        // 根据发票申请ID查询该发票下的订单号
        List<InvoiceOrderCon> invoiceOrderConList = invoiceOrderConDao.selectList(new QueryWrapper<InvoiceOrderCon>().lambda()
                .eq(InvoiceOrderCon::getInvoiceApplyId, dto.getInvoiceApplyId()));

        // 根据订单号查询订单金额
        List<OrderAmountDto> orderAmountList = new ArrayList<>(10);
        if (!CollectionUtils.isEmpty(invoiceOrderConList)) {
            for (InvoiceOrderCon invoiceOrderCon : invoiceOrderConList) {
                String orderNo = invoiceOrderCon.getOrderNo();
                Order order = orderDao.selectOne(new QueryWrapper<Order>().lambda().eq(Order::getNo, orderNo).select(Order::getTotalFee));
                OrderAmountDto amountDto = new OrderAmountDto();
                amountDto.setOrderNo(orderNo);
                amountDto.setAmount(Objects.isNull(order) ? new BigDecimal(0) : order.getTotalFee());
                orderAmountList.add(amountDto);
            }
        }
        detailVo.setOrderAmountList(orderAmountList);
        return BaseResultUtil.success(detailVo);
    }

    @Override
    public ResultVo confirmInvoice(InvoiceDetailAndConfirmDto dto) {
        log.info("====>web端-确认开票,请求json数据 :: "+JsonUtils.objectToJson(dto));
        // 验证是否已经开过票
        InvoiceApply invoiceApply = super.getById(dto.getInvoiceApplyId());
        if (!Objects.isNull(invoiceApply) && FieldConstant.INVOICE_FINISH == invoiceApply.getState()) {
            return BaseResultUtil.fail("该发票单已经开过票,不能重复开票!");
        }

        // 根据登录ID查询操作人名称
        Admin admin = adminDao.selectById(dto.getLoginId());

        // 更新开票信息
        LambdaUpdateWrapper<InvoiceApply> updateWrapper = new UpdateWrapper<InvoiceApply>().lambda()
                .set(InvoiceApply::getInvoiceNo, dto.getInvoiceNo())
                .set(InvoiceApply::getState, FieldConstant.INVOICE_FINISH)
                .set(InvoiceApply::getInvoiceTime, System.currentTimeMillis())
                .set(InvoiceApply::getOperationName, admin.getName())
                .eq(InvoiceApply::getId, dto.getInvoiceApplyId())
                .eq(InvoiceApply::getCustomerId, dto.getCustomerId());
        boolean result = super.update(updateWrapper);
        return result ? BaseResultUtil.success() : BaseResultUtil.fail();
    }

    @Override
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) {
        // 获取参数，封装参数
        InvoiceQueryDto dto = getInvoiceQueryDto(request);
        // 查询列表
        List<InvoiceApply> list = getInvoiceApplyList(dto);
        //if (!CollectionUtils.isEmpty(list)) {
            // 生成导出数据
            List<InvoiceApplyExportExcel> exportExcelList = new ArrayList<>(10);
            for (InvoiceApply invoiceApply : list) {
                InvoiceApplyExportExcel invoiceApplyExportExcel = new InvoiceApplyExportExcel();
                BeanUtils.copyProperties(invoiceApply, invoiceApplyExportExcel);
                exportExcelList.add(invoiceApplyExportExcel);
            }
            String title = "发票申请记录";
            String sheetName = "发票申请记录";
            String fileName = "发票申请记录表.xls";
            try {
                ExcelUtil.exportExcel(exportExcelList, title, sheetName, InvoiceApplyExportExcel.class, fileName, response);
            } catch (IOException e) {
                log.error("发票申请记录表异常:",e);
            }
        //}
    }

    private InvoiceQueryDto getInvoiceQueryDto(HttpServletRequest request) {
        InvoiceQueryDto dto = new InvoiceQueryDto();
        String state = request.getParameter("state");
        String applyTimeStart = request.getParameter("applyTimeStart");
        String applyTimeEnd = request.getParameter("applyTimeEnd");
        String invoiceTimeStart = request.getParameter("invoiceTimeStart");
        String invoiceTimeEnd = request.getParameter("invoiceTimeEnd");
        String currentPage = request.getParameter("currentPage");
        String pageSize = request.getParameter("pageSize");
        dto.setCustomerName(request.getParameter("customerName"));
        dto.setInvoiceNo(request.getParameter("invoiceNo"));
        dto.setOperationName(request.getParameter("operationName"));
        dto.setState(StringUtils.isEmpty(state) ? null : Integer.valueOf(state));
        dto.setApplyTimeStart(StringUtils.isEmpty(applyTimeStart) ? null : Long.valueOf(applyTimeStart));
        dto.setApplyTimeEnd(StringUtils.isEmpty(applyTimeEnd) ? null : Long.valueOf(applyTimeEnd));
        dto.setInvoiceTimeStart(StringUtils.isEmpty(invoiceTimeStart) ? null : Long.valueOf(invoiceTimeStart));
        dto.setInvoiceTimeEnd(StringUtils.isEmpty(invoiceTimeEnd) ? null : Long.valueOf(invoiceTimeEnd));
        dto.setCurrentPage(StringUtils.isEmpty(currentPage) ? null : Integer.valueOf(currentPage));
        dto.setPageSize(StringUtils.isEmpty(pageSize) ? null : Integer.valueOf(pageSize));
        return dto;
    }
}
