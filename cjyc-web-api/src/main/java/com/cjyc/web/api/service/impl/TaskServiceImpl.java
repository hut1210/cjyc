package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjkj.common.utils.ExcelUtil;
import com.cjyc.common.model.dao.ICarrierDao;
import com.cjyc.common.model.dao.ITaskDao;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.web.task.*;
import com.cjyc.common.model.dto.web.waybill.CrWaybillDto;
import com.cjyc.common.model.entity.Carrier;
import com.cjyc.common.model.entity.Task;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultReasonVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.mineCarrier.MyCarrierVo;
import com.cjyc.common.model.vo.web.task.*;
import com.cjyc.common.model.vo.web.waybill.CrWaybillVo;
import com.cjyc.common.model.vo.web.waybill.ExportCrWaybillVo;
import com.cjyc.common.model.vo.web.waybill.WaybillCarVo;
import com.cjyc.common.system.service.ICsTaskService;
import com.cjyc.common.system.service.sys.ICsSysService;
import com.cjyc.web.api.service.ITaskService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 任务表(子运单) 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-26
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TaskServiceImpl extends ServiceImpl<ITaskDao, Task> implements ITaskService {
    @Resource
    private IWaybillCarDao waybillCarDao;
    @Resource
    private ITaskDao taskDao;
    @Resource
    private ICsTaskService csTaskService;
    @Resource
    private ICsSysService csSysService;
    @Resource
    private ICarrierDao carrierDao;

    @Override
    public ResultVo allot(AllotTaskDto paramsDto) {
        return csTaskService.allot(paramsDto);
    }

    @Override
    public ResultVo<ResultReasonVo> load(LoadTaskDto paramsDto) {
        return csTaskService.load(paramsDto);
    }

    @Override
    public ResultVo<ResultReasonVo> unload(UnLoadTaskDto paramsDto) {
        return csTaskService.unload(paramsDto);
    }

    @Override
    public ResultVo<ResultReasonVo> inStore(InStoreTaskDto paramsDto) {
        return csTaskService.inStore(paramsDto);
    }

    @Override
    public ResultVo<ResultReasonVo> outStore(OutStoreTaskDto paramsDto) {
        return csTaskService.outStore(paramsDto);
    }

    @Override
    public ResultVo receipt(ReceiptTaskDto reqDto) {
        return csTaskService.receipt(reqDto);
    }

    @Override
    public ResultVo<List<ListByWaybillTaskVo>> getlistByWaybillId(Long waybillId) {
        List<ListByWaybillTaskVo> list = taskDao.findListByWaybillId(waybillId);
        return BaseResultUtil.success(list);
    }

    @Override
    public ResultVo<TaskVo> get(Long taskId) {
        TaskVo taskVo = taskDao.findVoById(taskId);
        List<WaybillCarVo> list = waybillCarDao.findVoByTaskId(taskId);
        taskVo.setList(list);
        return BaseResultUtil.success(taskVo);
    }

    @Override
    public ResultVo<PageVo<CrTaskVo>> crTaskList(CrTaskDto paramsDto) {
        //根据角色查询承运商ID
        paramsDto.setCarrierId(paramsDto.getCarrierId());

        PageHelper.startPage(paramsDto.getCurrentPage(), paramsDto.getPageSize(), true);
        List<CrTaskVo> list = taskDao.findListForMineCarrier(paramsDto);
        PageInfo<CrTaskVo> pageInfo = new PageInfo<>(list);
        if(paramsDto.getCurrentPage() > pageInfo.getPages()){
            pageInfo.setList(list);
        }
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<PageVo<TaskPageVo>> getTaskPage(TaskPageDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<TaskPageVo> list = taskDao.selectMyTaskList(dto);
        PageInfo<TaskPageVo> pageInfo = new PageInfo<>(list);
        return BaseResultUtil.success(pageInfo);
    }




    /************************************韵车集成改版 st***********************************/

    @Override
    public ResultVo<PageVo<CrTaskVo>> crTaskListNew(CrTaskDto paramsDto) {

        //根据角色查询承运商ID
        Carrier carrier = carrierDao.selectById(paramsDto.getCarrierId());
        if(carrier == null){
            return BaseResultUtil.fail("承运商信息不存在");
        }
        paramsDto.setCarrierId(carrier.getId());

        PageHelper.startPage(paramsDto.getCurrentPage(), paramsDto.getPageSize(), true);
        List<CrTaskVo> list = taskDao.findListForMineCarrier(paramsDto);
        PageInfo<CrTaskVo> pageInfo = new PageInfo<>(list);
        if(paramsDto.getCurrentPage() > pageInfo.getPages()){
            pageInfo.setList(list);
        }
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public void exportCrAllottedListExcel(HttpServletRequest request, HttpServletResponse response) {
        CrTaskDto dto = findCrTaskDto(request);
        List<CrTaskVo> crTaskVoList = taskDao.findListForMineCarrier(dto);
        List<ExportCrTaskVo> exportExcelList = Lists.newArrayList();
        for (CrTaskVo vo : crTaskVoList) {
            ExportCrTaskVo exportCrTaskVo = new ExportCrTaskVo();
            BeanUtils.copyProperties(vo, exportCrTaskVo);
            exportExcelList.add(exportCrTaskVo);
        }
        String title = "已指派";
        String sheetName = "已指派";
        String fileName = "已指派.xls";
        try {
            ExcelUtil.exportExcel(exportExcelList, title, sheetName, ExportCrTaskVo.class, fileName, response);
        } catch (IOException e) {
            log.error("导出已指派信息异常:{}",e);
        }
    }

    /**
     * 封装运单excel请求
     * @param request
     * @return
     */
    private CrTaskDto findCrTaskDto(HttpServletRequest request){
        CrTaskDto dto = new CrTaskDto();
        dto.setWaybillNo(request.getParameter("waybillNo"));
        dto.setTaskNo(request.getParameter("taskNo"));
        dto.setDriverName(request.getParameter("driverName"));
        dto.setDriverPhone(request.getParameter("driverPhone"));
        dto.setVehiclePlateNo(request.getParameter("vehiclePlateNo"));
        dto.setCarrierId(StringUtils.isBlank(request.getParameter("carrierId")) ? null:Long.valueOf(request.getParameter("carrierId")));
        return dto;
    }

}
