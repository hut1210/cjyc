package com.cjyc.web.api.util;

import com.cjkj.log.monitor.LogUtil;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.vo.ResultVo;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ReturnExcel {

    public <T> void printExcel(ResultVo<List<T>> vo, Class clazz, String tip, HttpServletResponse response) {
        if (!isResultSuccess(vo)) {
            ExcelUtil.printExcelResult(ExcelUtil.getWorkBookForShowMsg("提示信息", vo.getMsg()), "导出异常.xls", response);
            return;
        }
        List<T> list = vo.getData();
        if (CollectionUtils.isEmpty(list)) {
            ExcelUtil.printExcelResult(ExcelUtil.getWorkBookForShowMsg("提示信息", "未查询到结果信息"),"结果为空.xls", response);
            return;
        }
        list = list.stream().filter(Objects::nonNull).collect(Collectors.toList());
        try {
            ExcelUtil.exportExcel(list, "订单信息", "订单信息", clazz, System.currentTimeMillis() + "订单信息.xls", response);
        } catch (Exception e) {
            LogUtil.error("导出订单信息异常", e);
            ExcelUtil.printExcelResult(ExcelUtil.getWorkBookForShowMsg("提示信息", "导出订单信息异常: " + e.getMessage()),
                    "导出异常.xls", response);
        }
    }


    /**
     * 检查返回结果是否成功
     *
     * @param resultVo
     * @return
     */
    private boolean isResultSuccess(ResultVo resultVo) {
        if (null == resultVo) {
            return false;
        }
        return resultVo.getCode() == ResultEnum.SUCCESS.getCode();
    }
}
