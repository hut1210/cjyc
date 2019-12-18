package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.invoice.InvoiceDetailAndConfirmDto;
import com.cjyc.common.model.dto.web.invoice.InvoiceQueryDto;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.invoice.InvoiceApplyVo;
import com.cjyc.common.model.vo.web.invoice.InvoiceDetailVo;
import com.cjyc.web.api.service.IInvoiceApplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 发票申请信息表 前端控制器
 * </p>
 *
 * @author JPG
 * @since 2019-11-02
 */
@Api(tags = "基础数据-发票")
@RestController
@RequestMapping("/invoice")
public class InvoiceApplyController {
    @Autowired
    private IInvoiceApplyService invoiceApplyService;

    /**
     * 功能描述: 分页查询发票申请信息列表
     * @author liuxingxiang
     * @date 2019/12/18
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo<com.cjyc.common.model.vo.PageVo<java.util.List<com.cjyc.common.model.vo.customer.invoice.InvoiceApplyVo>>>
     */
    @ApiOperation(value = "分页查询发票申请信息列表")
    @PostMapping("/getInvoicePage")
    public ResultVo<PageVo<List<InvoiceApplyVo>>> getInvoiceApplyPage(@RequestBody InvoiceQueryDto dto){
        return invoiceApplyService.getInvoiceApplyPage(dto);
    }

    /**
     * 功能描述: 查看开票明细
     * @author liuxingxiang
     * @date 2019/12/18
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo<com.cjyc.common.model.vo.web.invoice.InvoiceDetailVo>
     */
    @ApiOperation(value = "查看开票明细")
    @PostMapping("/getDetail")
    public ResultVo<InvoiceDetailVo> getDetail(@RequestBody @Validated({InvoiceDetailAndConfirmDto.GetDetail.class}) InvoiceDetailAndConfirmDto dto){
        return invoiceApplyService.getDetail(dto);
    }

    /**
     * 功能描述: 确认开票
     * @author liuxingxiang
     * @date 2019/12/18
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo
     */
    @ApiOperation(value = "确认开票")
    @PostMapping("/confirmInvoice")
    public ResultVo confirmInvoice(@RequestBody @Validated({InvoiceDetailAndConfirmDto.ConfirmInvoice.class}) InvoiceDetailAndConfirmDto dto){
        return invoiceApplyService.confirmInvoice(dto);
    }

    /**
     * 功能描述: 导出Excel
     * @author liuxingxiang
     * @date 2019/12/18
     * @param request
     * @param response
     * @return void
     */
    @ApiOperation(value = "导出Excel")
    @GetMapping("/exportExcel")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response){
        invoiceApplyService.exportExcel(request,response);
    }

}
