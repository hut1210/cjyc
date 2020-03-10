package com.cjyc.web.api.util;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.cjkj.log.monitor.LogUtil;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.order.OrderCarWaitDispatchVo;
import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

/**
 * excel工具类
 */

public class ExcelUtil {
    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass,
                                   String fileName, boolean isCreateHeader, HttpServletResponse response) {
        ExportParams exportParams = new ExportParams(title, sheetName);
        exportParams.setCreateHeadRows(isCreateHeader);
        defaultExport(list, pojoClass, fileName, response, exportParams);
    }

    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName,
                                   HttpServletResponse response) {
        defaultExport(list, pojoClass, fileName, response, new ExportParams(title, sheetName));
    }

    public static void exportExcel(List<Map<String, Object>> list, String fileName, HttpServletResponse response) {
        defaultExport(list, fileName, response);
    }

    private static void defaultExport(List<?> list, Class<?> pojoClass, String fileName,
                                      HttpServletResponse response, ExportParams exportParams) {
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, pojoClass, list);
        if (workbook != null) ;
        downLoadExcel(fileName, response, workbook);
    }

    private static void downLoadExcel(String fileName, HttpServletResponse response, Workbook workbook) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("multipart/form-data");
            //response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setHeader("Content-disposition", "attachment; filename="
                    + new String(fileName.getBytes("UTF-8"), "ISO8859-1"));
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            //throw new NormalException(e.getMessage());
        }
    }

    private static void defaultExport(List<Map<String, Object>> list, String fileName, HttpServletResponse response) {
        Workbook workbook = ExcelExportUtil.exportExcel(list, ExcelType.HSSF);
        if (workbook != null) ;
        downLoadExcel(fileName, response, workbook);
    }

    public static <T> List<T> importExcel(String filePath, Integer titleRows, Integer headerRows, Class<T> pojoClass) {
        if (StringUtils.isBlank(filePath)) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        List<T> list = null;
        try {
            list = ExcelImportUtil.importExcel(new File(filePath), pojoClass, params);
        } catch (NoSuchElementException e) {
            //throw new NormalException("模板不能为空");
        } catch (Exception e) {
            e.printStackTrace();
            //throw new NormalException(e.getMessage());
        }
        return list;
    }

    public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass) {
        if (file == null) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        List<T> list = null;
        try {
            list = ExcelImportUtil.importExcel(file.getInputStream(), pojoClass, params);
        } catch (NoSuchElementException e) {
            // throw new NormalException("excel文件不能为空");
        } catch (Exception e) {
            //throw new NormalException(e.getMessage());
            System.out.println(e.getMessage());
        }
        return list;
    }

    /**
     * 导出错误或没有结果时使用此方法封装提示工作簿
     * @param title
     * @param msg
     * @return
     */
    public static Workbook getWorkBookForShowMsg(String title, String msg) {
        if (StringUtils.isBlank(title)) {
            title = "提示信息";
        }
        if (StringUtils.isBlank(msg)) {
            msg = "";
        }
        ShowInfo info = new ShowInfo();
        info.setShowMsg(msg);
        Workbook wk = ExcelExportUtil.exportExcel(new ExportParams(title, title), ShowInfo.class,
                Lists.newArrayList(info));
        return wk;
    }

    /**
     * 输出Excel文件
     * @param workbook
     * @param fileName
     * @param response
     */
    public static void printExcelResult(Workbook workbook, String fileName, HttpServletResponse response) {
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

    public static <T> void print(ResultVo<List<T>> dispatchRs, String fileName, HttpServletResponse response) {

        if (ResultEnum.SUCCESS.getCode() == dispatchRs.getCode()) {
            ExcelUtil.printExcelResult(ExcelUtil.getWorkBookForShowMsg("提示信息", dispatchRs.getMsg()),
                    "导出异常.xls", response);
            return;
        }
        List<T> dispatchList = dispatchRs.getData();
        if (CollectionUtils.isEmpty(dispatchList)) {
            ExcelUtil.printExcelResult(ExcelUtil.getWorkBookForShowMsg("提示信息", "未查询到结果信息"),
                    "结果为空.xls", response);
            return;
        }
        dispatchList = dispatchList.stream().filter(Objects::nonNull).collect(Collectors.toList());
        try{
            ExcelUtil.exportExcel(dispatchList, fileName, fileName,
                    OrderCarWaitDispatchVo.class, System.currentTimeMillis() + fileName + ".xls", response);
            return;
        }catch (Exception e) {
            LogUtil.error("导出信息" + fileName + "异常", e);
            ExcelUtil.printExcelResult(ExcelUtil.getWorkBookForShowMsg("提示信息", "导出" + fileName + "异常: " + e.getMessage()),
                    "导出异常.xls", response);
        }
    }
}
@Data
class ShowInfo {
    @Excel(name = "提示信息", orderNum = "0")
    private String showMsg;
}
