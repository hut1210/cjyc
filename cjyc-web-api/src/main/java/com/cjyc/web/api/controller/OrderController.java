package com.cjyc.web.api.controller;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.alibaba.fastjson.JSONObject;
import com.cjkj.log.monitor.LogUtil;
import com.cjyc.common.model.dto.web.order.*;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.enums.UserTypeEnum;
import com.cjyc.common.model.enums.order.OrderPickTypeEnum;
import com.cjyc.common.model.enums.order.OrderStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.OrderCarVo;
import com.cjyc.common.model.vo.web.order.*;
import com.cjyc.common.system.service.ICsAdminService;
import com.cjyc.common.system.service.ICsCustomerService;
import com.cjyc.common.system.service.ICsOrderService;
import com.cjyc.web.api.service.IOrderService;
import com.cjyc.web.api.util.CustomerOrderExcelVerifyHandler;
import com.cjyc.web.api.util.ExcelUtil;
import com.cjyc.web.api.util.KeyCustomerOrderExcelVerifyHandler;
import com.cjyc.web.api.util.PatCustomerOrderExcelVerifyHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 订单
 * @author JPG
 */
@RestController
@Api(tags = "订单")
@RequestMapping(value = "/order",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class OrderController {

    @Resource
    private IOrderService orderService;
    @Resource
    private ICsOrderService csOrderService;
    @Resource
    private ICsAdminService csAdminService;
    @Resource
    private ICsCustomerService csCustomerService;
    @Autowired
    private CustomerOrderExcelVerifyHandler customerOrderVerifyHandler;
    @Autowired
    private KeyCustomerOrderExcelVerifyHandler keyCustomerOrderVerifyHandler;
    @Autowired
    private PatCustomerOrderExcelVerifyHandler patCustomerOrderExcelVerifyHandler;
    /**
     * 保存,只保存无验证
     * @author JPG
     */
    @ApiOperation(value = "订单保存")
    @PostMapping(value = "/save")
    public ResultVo save(@Validated @RequestBody SaveOrderDto reqDto) {

        if(reqDto.getLoginType() != null && UserTypeEnum.CUSTOMER.code == reqDto.getLoginType()){
            //验证用户存不存在
            ResultVo<Customer> vo = csCustomerService.validateAndGetActive(reqDto.getLoginId());
            if(vo.getCode() != ResultEnum.SUCCESS.getCode()){
                return BaseResultUtil.fail(vo.getMsg());
            }
            Customer customer = vo.getData();
            reqDto.setLoginName(customer.getName());
            reqDto.setLoginPhone(customer.getContactPhone());
            reqDto.setLoginType(UserTypeEnum.CUSTOMER.code);
            reqDto.setCreateUserId(customer.getId());
            reqDto.setCreateUserName(customer.getName());
        }else{
            //验证用户存不存在
            Admin admin = csAdminService.validate(reqDto.getLoginId());
            reqDto.setLoginName(admin.getName());
            reqDto.setLoginPhone(admin.getPhone());
            reqDto.setLoginType(UserTypeEnum.ADMIN.code);
            reqDto.setCreateUserId(admin.getId());
            reqDto.setCreateUserName(admin.getName());
        }
        reqDto.setState(OrderStateEnum.WAIT_SUBMIT.code);
        ResultVo resultVo = csOrderService.save(reqDto);
        //发送推送信息
        return resultVo;
    }


    /**
     * 提交
     * @author JPG
     */
    @ApiOperation(value = "订单提交")
    @PostMapping(value = "/commit")
    public ResultVo commit(@Validated @RequestBody CommitOrderDto reqDto) {
        if(reqDto.getLoginType() != null && UserTypeEnum.CUSTOMER.code == reqDto.getLoginType()){
            //验证用户存不存在
            ResultVo<Customer> vo = csCustomerService.validateAndGetActive(reqDto.getLoginId());
            if(vo.getCode() != ResultEnum.SUCCESS.getCode()){
                return BaseResultUtil.fail(vo.getMsg());
            }
            Customer customer = vo.getData();
            reqDto.setLoginName(customer.getName());
            reqDto.setLoginPhone(customer.getContactPhone());
            reqDto.setLoginType(UserTypeEnum.CUSTOMER.code);
            reqDto.setCreateUserId(customer.getId());
            reqDto.setCreateUserName(customer.getName());
        }else{
            //验证用户存不存在
            Admin admin = csAdminService.validate(reqDto.getLoginId());
            reqDto.setLoginName(admin.getName());
            reqDto.setLoginPhone(admin.getPhone());
            reqDto.setLoginType(UserTypeEnum.ADMIN.code);
            reqDto.setCreateUserId(admin.getId());
            reqDto.setCreateUserName(admin.getName());
        }
        //发送短信
        return csOrderService.commit(reqDto);
    }

    /**
     * 完善订单信息
     * @author JPG
     */
    @ApiOperation(value = "完善订单信息")
    @PostMapping(value = "/replenish/info/update")
    public ResultVo replenishInfo(@RequestBody ReplenishOrderDto reqDto) {
        return csOrderService.replenishInfo(reqDto);
    }

    /**
     * 审核订单
     * @author JPG
     */
    @ApiOperation(value = "审核订单")
    @PostMapping(value = "/check")
    public ResultVo check(@RequestBody CheckOrderDto reqDto) {
        //验证用户存不存在
        Admin admin = csAdminService.validate(reqDto.getLoginId());
        reqDto.setLoginName(admin.getName());
        reqDto.setLoginPhone(admin.getPhone());
        return csOrderService.check(reqDto);
    }

    /**
     * 驳回订单
     * @author JPG
     */
    @ApiOperation(value = "驳回订单")
    @PostMapping(value = "/reject")
    public ResultVo reject(@RequestBody RejectOrderDto reqDto) {
        //验证用户存不存在
        Admin admin = csAdminService.validate(reqDto.getLoginId());
        reqDto.setLoginName(admin.getName());
        reqDto.setLoginPhone(admin.getPhone());
        reqDto.setLoginType(UserTypeEnum.ADMIN);
        return csOrderService.reject(reqDto);
    }

    /**
     * 分配订单
     * @author JPG
     */
    @ApiOperation(value = "分配订单")
    @PostMapping(value = "/allot")
    public ResultVo allot(@Validated @RequestBody AllotOrderDto reqDto) {
        //验证用户存不存在
        Admin admin = csAdminService.validate(reqDto.getLoginId());
        reqDto.setLoginName(admin.getName());
        Admin toAdmin = csAdminService.validate(reqDto.getToAdminId());
        reqDto.setToAdminName(toAdmin.getName());
        return csOrderService.allot(reqDto);
    }


    /**
     * 订单改价
     * @author JPG
     */
    @ApiOperation(value = "订单改价")
    @PostMapping(value = "/change/price")
    public ResultVo changePrice(@RequestBody ChangePriceOrderDto reqDto) {
        Admin admin = csAdminService.validate(reqDto.getLoginId());
        reqDto.setLoginName(admin.getName());
        reqDto.setLoginPhone(admin.getPhone());
        return csOrderService.changePrice(reqDto);
    }


    /**
     * 取消订单
     * @author JPG
     */
    @ApiOperation(value = "取消订单")
    @PostMapping(value = "/cancel")
    public ResultVo cancel(@RequestBody CancelOrderDto reqDto) {
        Admin admin = csAdminService.validate(reqDto.getLoginId());
        reqDto.setLoginName(admin.getName());
        reqDto.setLoginPhone(admin.getPhone());
        reqDto.setLoginType(UserTypeEnum.ADMIN);
        return csOrderService.cancel(reqDto);
    }


    /**
     * 作废订单
     * @author JPG
     */
    @ApiOperation(value = "作废订单")
    @PostMapping(value = "/obsolete")
    public ResultVo obsolete(@RequestBody CancelOrderDto reqDto) {

        Admin admin = csAdminService.validate(reqDto.getLoginId());
        reqDto.setLoginName(admin.getName());

        return csOrderService.obsolete(reqDto);
    }

    /**
     * 查询订单-根据ID
     * @author JPG
     */
    @ApiOperation(value = "查询订单-根据ID")
    @PostMapping(value = "/get/{orderId}")
    public ResultVo<OrderVo> get(@PathVariable Long orderId) {
        OrderVo orderVo = orderService.getVoById(orderId);
        return BaseResultUtil.success(orderVo);
    }


    /**
     * 查询订单-根据车辆ID
     * @author JPG
     */
    @ApiOperation(value = "查询订单-根据ID")
    @PostMapping(value = "/car/vo/get/{orderCarId}")
    public ResultVo<OrderCarVo> getCarVo(@PathVariable Long orderCarId) {
        OrderCarVo vo = orderService.getCarVoById(orderCarId);
        return BaseResultUtil.success(vo);
    }

    /**
     * 查询订单取消记录-根据ID
     * @author JPG
     */
    @ApiOperation(value = "查询订单取消记录-根据ID")
    @PostMapping(value = "/change/log/list")
    public ResultVo<List<ListOrderChangeLogVo>> get(@RequestBody ListOrderChangeLogDto reqDto) {
        List<ListOrderChangeLogVo> list = orderService.getChangeLogVoById(reqDto);
        return BaseResultUtil.success(list);
    }

    /**
     * 查询订单列表
     * @author JPG
     */
    @ApiOperation(value = "查询订单列表")
    @PostMapping(value = "/list")
    public ResultVo<PageVo<ListOrderVo>> list(@RequestBody ListOrderDto reqDto) {
        return orderService.list(reqDto);
    }

    @ApiOperation(value = "分页导出订单列表")
    @GetMapping(value = "/exportPageList")
    public ResultVo exportPageList(ListOrderDto reqDto, HttpServletResponse response){
        ResultVo<PageVo<ListOrderVo>> resultVo = orderService.list(reqDto);
        if (!isResultSuccess(resultVo)) {
            return BaseResultUtil.fail("导出数据异常");
        }
        PageVo<ListOrderVo> data = resultVo.getData();
        if (data == null || CollectionUtils.isEmpty(data.getList())) {
            return BaseResultUtil.success("未查询到数据");
        }
        try{
            ExcelUtil.exportExcel(data.getList(), "订单信息", "订单信息",
                    ListOrderVo.class, System.currentTimeMillis()+"订单信息.xls", response);
            return null;
        }catch (Exception e) {
            LogUtil.error("导出订单信息异常", e);
            return BaseResultUtil.fail("导出订单信息异常: " + e.getMessage());
        }
    }

    @ApiOperation(value = "导出全部订单列表")
    @GetMapping(value = "/exportAllList")
    public void exportAllList(ListOrderDto reqDto, HttpServletResponse response) throws IOException, InvalidFormatException {
        ResultVo<List<ListOrderVo>> orderRs = orderService.listAll(reqDto);
        if (!isResultSuccess(orderRs)) {
            ExcelUtil.printExcelResult(ExcelUtil.getWorkBookForShowMsg("提示信息", orderRs.getMsg()),
                    "导出异常.xls", response);
            return;
        }
        List<ListOrderVo> orderList = orderRs.getData();
        if (CollectionUtils.isEmpty(orderList)) {
            ExcelUtil.printExcelResult(ExcelUtil.getWorkBookForShowMsg("提示信息", "未查询到结果信息"),
                    "结果为空.xls", response);
            return;
        }
        orderList = orderList.stream().filter(o -> o != null).collect(Collectors.toList());
        try{
            ExcelUtil.exportExcel(orderList, "订单信息", "订单信息",
                    ListOrderVo.class, System.currentTimeMillis()+"订单信息.xls", response);
            return ;
        }catch (Exception e) {
            LogUtil.error("导出订单信息异常", e);
            ExcelUtil.printExcelResult(ExcelUtil.getWorkBookForShowMsg("提示信息", "导出订单信息异常: " + e.getMessage()),
                    "导出异常.xls", response);
        }
    }

    /**
     * 查询订单车辆列表
     * @author JPG
     */
    @ApiOperation(value = "订单车辆查询")
    @PostMapping(value = "/car/list")
    public ResultVo<PageVo<ListOrderCarVo>> carlist(@RequestBody ListOrderCarDto reqDto) {
        return orderService.carlist(reqDto);
    }


    @ApiOperation(value = "分页导出车辆信息列表")
    @GetMapping(value = "/car/exportPageList")
    public ResultVo exportPageCarList(ListOrderCarDto reqDto, HttpServletResponse response) {
        ResultVo<PageVo<ListOrderCarVo>> resultVo = orderService.carlist(reqDto);
        if (!isResultSuccess(resultVo)) {
            return BaseResultUtil.fail("导出数据异常");
        }
        PageVo<ListOrderCarVo> data = resultVo.getData();
        if (data == null || CollectionUtils.isEmpty(data.getList())) {
            return BaseResultUtil.success("未查询到数据");
        }
        try{
            ExcelUtil.exportExcel(data.getList(), "车辆信息", "车辆信息",
                    ListOrderCarVo.class, System.currentTimeMillis()+"车辆信息.xls", response);
            return null;
        }catch (Exception e) {
            LogUtil.error("导出车辆信息异常", e);
            return BaseResultUtil.fail("导出车辆信息异常: " + e.getMessage());
        }
    }

    @ApiOperation(value = "导出全部车辆信息列表")
    @GetMapping(value = "/car/exportAllList")
    public ResultVo exportAllCarList(ListOrderCarDto reqDto, HttpServletResponse response) {
        List<ListOrderCarVo> carList = orderService.carListAll(reqDto);
        if (CollectionUtils.isEmpty(carList)) {
            return BaseResultUtil.success("未查询到结果");
        }
        carList = carList.stream().filter(Objects::nonNull).collect(Collectors.toList());
        try {
            ExcelUtil.exportExcel(carList, "车辆信息", "车辆信息",
                    ListOrderCarVo.class, System.currentTimeMillis()+"车辆信息.xls", response);
            return null;
        }catch (Exception e) {
            LogUtil.error("导出车辆信息异常", e);
            return BaseResultUtil.fail("导出车辆信息异常: " + e.getMessage());
        }
    }
    /**
     * 查询订单车辆运输信息-根据ID
     * @author JPG
     */
    @ApiOperation(value = "查询订单车辆运输状态-根据ID")
    @PostMapping(value = "/detail/info/{orderId}")
    public ResultVo<List<TransportInfoOrderCarVo>> detailInfo(@PathVariable Long orderId) {
        List<TransportInfoOrderCarVo> list = orderService.getTransportInfoVoById(orderId);
        return BaseResultUtil.success(list);
    }



    @ApiOperation(value = "c端客户订单导入", notes = "验证失败返回失败Excel文件流，其它情况返回json结果信息")
    @PostMapping(value = "/importCustomerOrder")
    public void importCustomerOrder(MultipartFile file, Long loginId, HttpServletResponse response){
        if (file != null && !file.isEmpty() && loginId != null && loginId > 0L) {
            ImportParams orderParams = new ImportParams();
            orderParams.setSheetNum(1);
            orderParams.setHeadRows(2);
            orderParams.setNeedVerfiy(true);
            orderParams.setVerifyHandler(customerOrderVerifyHandler);

            ImportParams carParams = new ImportParams();
            carParams.setStartSheetIndex(1);
            carParams.setSheetNum(1);
            carParams.setHeadRows(2);
            carParams.setNeedVerfiy(true);

            List<ImportCustomerOrderDto> orderList = null;
            List<ImportCustomerOrderCarDto> carList = null;
            try {
                ExcelImportResult<ImportCustomerOrderDto> orderResult = ExcelImportUtil.importExcelMore(file.getInputStream(), ImportCustomerOrderDto.class, orderParams);
                if (orderResult.isVerfiyFail()) {
                    String fileName = "验证失败.xlsx";
                    printExcelResult(orderResult.getFailWorkbook(), fileName, response);
                } else {
                    orderList = orderResult.getList();
                    ExcelImportResult<ImportCustomerOrderCarDto> carResult =
                            ExcelImportUtil.importExcelMore(file.getInputStream(),
                                    ImportCustomerOrderCarDto.class, carParams);
                    if (carResult.isVerfiyFail()) {
                        String fileName = "验证失败.xlsx";
                        printExcelResult(carResult.getFailWorkbook(), fileName, response);
                    } else {
                        carList = carResult.getList();
                        //信息保存
                        Admin admin = csAdminService.validate(loginId);
                        orderService.importCustomerOrder(orderList, carList, admin);
                        printJsonResult(BaseResultUtil.success("成功"), response);
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
                printJsonResult(BaseResultUtil.fail("异常: " + e.getMessage()), response);
            }
        }else {
            printJsonResult(BaseResultUtil.fail("参数错误，请检查"), response);
        }
    }

    @ApiOperation(value = "大客户订单导入", notes = "验证失败返回失败Excel文件流，其它情况返回json结果信息")
    @PostMapping(value = "/importEnterpriseOrder")
    public void importKeyCustomerOrder(MultipartFile file, Long loginId, HttpServletResponse response) {
        if (file != null && !file.isEmpty() && loginId != null && loginId > 0L) {
            ImportParams orderParams = new ImportParams();
            orderParams.setSheetNum(1);
            orderParams.setHeadRows(2);
            orderParams.setNeedVerfiy(true);
            orderParams.setVerifyHandler(keyCustomerOrderVerifyHandler);

            ImportParams carParams = new ImportParams();
            carParams.setSheetNum(1);
            carParams.setStartSheetIndex(1);
            carParams.setHeadRows(2);
            carParams.setNeedVerfiy(true);

            List<ImportKeyCustomerOrderDto> orderList = null;
            List<ImportKeyCustomerOrderCarDto> carList = null;
            try {
                ExcelImportResult<ImportKeyCustomerOrderDto> orderResult = ExcelImportUtil.importExcelMore(file.getInputStream(),
                        ImportKeyCustomerOrderDto.class, orderParams);
                if (orderResult.isVerfiyFail()) {
                    String fileName = "验证失败.xlsx";
                    printExcelResult(orderResult.getFailWorkbook(), fileName, response);
                }else {
                    orderList = orderResult.getList();
                    ExcelImportResult<ImportKeyCustomerOrderCarDto> carResult = ExcelImportUtil.importExcelMore(file.getInputStream(),
                            ImportKeyCustomerOrderCarDto.class, carParams);
                    if (carResult.isVerfiyFail()) {
                        String fileName = "验证失败.xlsx";
                        printExcelResult(orderResult.getFailWorkbook(), fileName, response);
                    }else {
                        carList = carResult.getList();
                        //导入操作
                        Admin admin = csAdminService.validate(loginId);
                        orderService.importKeyCustomerOrder(orderList, carList, admin);
                        printJsonResult(BaseResultUtil.success("成功"), response);
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
                LogUtil.error("导入大客户订单异常：", e);
                printJsonResult(BaseResultUtil.fail("异常: " + e.getMessage()), response);
            }
        }
    }

    @ApiOperation(value = "合伙人订单导入", notes = "验证失败返回失败Excel文件流，其它情况返回json结果信息")
    @PostMapping(value = "importCooperatorOrder")
    public void importPatCustomerOrder(MultipartFile file, Long loginId, HttpServletResponse response) {
        if (file != null && !file.isEmpty() && loginId != null && loginId > 0L) {
            ImportParams orderParams = new ImportParams();
            orderParams.setSheetNum(1);
            orderParams.setHeadRows(2);
            orderParams.setNeedVerfiy(true);
            orderParams.setVerifyHandler(patCustomerOrderExcelVerifyHandler);

            ImportParams carParams = new ImportParams();
            carParams.setSheetNum(1);
            carParams.setStartSheetIndex(1);
            carParams.setHeadRows(2);
            carParams.setNeedVerfiy(true);

            List<ImportPatCustomerOrderDto> orderList = null;
            List<ImportPatCustomerOrderCarDto> carList = null;
            try {
                ExcelImportResult<ImportPatCustomerOrderDto> orderResult = ExcelImportUtil.importExcelMore(file.getInputStream(),
                        ImportPatCustomerOrderDto.class, orderParams);
                if (orderResult.isVerfiyFail()) {
                    String fileName = "验证失败.xlsx";
                    printExcelResult(orderResult.getFailWorkbook(), fileName, response);
                }else {
                    orderList = orderResult.getList();
                    ExcelImportResult<ImportPatCustomerOrderCarDto> carResult = ExcelImportUtil.importExcelMore(file.getInputStream(),
                            ImportPatCustomerOrderCarDto.class, carParams);
                    if (carResult.isVerfiyFail()) {
                        String fileName = "验证失败.xlsx";
                        printExcelResult(orderResult.getFailWorkbook(), fileName, response);
                    }else {
                        carList = carResult.getList();
                        //TODO 导入操作
                        Admin admin = csAdminService.validate(loginId);
                        orderService.importPatCustomerOrder(orderList, carList, admin);
                        printJsonResult(BaseResultUtil.success("成功"), response);
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
                LogUtil.error("导入大客户订单异常：", e);
                printJsonResult(BaseResultUtil.fail("异常: " + e.getMessage()), response);
            }
        }
    }
    /**
     * 检查返回结果是否成功
     * @param resultVo
     * @return
     */
    private boolean isResultSuccess(ResultVo resultVo) {
        if (null == resultVo) {
            return false;
        }
        return resultVo.getCode() == ResultEnum.SUCCESS.getCode();
    }

    /**
     * 响应json格式信息
     * @param resultObj
     * @param response
     */
    private void printJsonResult(Object resultObj, HttpServletResponse response) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getOutputStream().write(JSONObject.toJSONBytes(resultObj));
        }catch (Exception e) {
            LogUtil.error("响应信息异常", e);
        }
    }

    /**
     * 响应返回Excel文件信息
     * @param workbook
     * @param fileName
     * @param response
     */
    private void printExcelResult(Workbook workbook, String fileName, HttpServletResponse response) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-disposition", "attachment; filename="
                    + new String(fileName.getBytes("UTF-8"), "ISO8859-1"));
            workbook.write(response.getOutputStream());
        }catch (Exception e) {
            LogUtil.error("响应信息异常", e);
        }
    }
}
