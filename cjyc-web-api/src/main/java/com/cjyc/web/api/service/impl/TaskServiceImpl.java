package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.ITaskDao;
import com.cjyc.common.model.dao.IWaybillCarDao;
import com.cjyc.common.model.dto.web.task.*;
import com.cjyc.common.model.entity.Carrier;
import com.cjyc.common.model.entity.Task;
import com.cjyc.common.model.entity.defined.BizScope;
import com.cjyc.common.model.enums.BizScopeEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultReasonVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.task.CrTaskVo;
import com.cjyc.common.model.vo.web.task.ListByWaybillTaskVo;
import com.cjyc.common.model.vo.web.task.TaskPageVo;
import com.cjyc.common.model.vo.web.task.TaskVo;
import com.cjyc.common.model.vo.web.waybill.WaybillCarVo;
import com.cjyc.common.system.service.ICsTaskService;
import com.cjyc.common.system.service.sys.ICsSysService;
import com.cjyc.web.api.service.ITaskService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
        Carrier carrier = csSysService.getCarrierByRoleId(paramsDto.getRoleId());
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
    public ResultVo getTaskPage(TaskPageDto dto) {
        // 根据登录ID查询业务中心ID
        BizScope bizScope = csSysService.getBizScopeByLoginId(dto.getLoginId(), true);

        // 判断当前登录人是否有权限访问
        int code = bizScope.getCode();
        if (BizScopeEnum.NONE.code == code) {
            return BaseResultUtil.fail("您没有访问权限!");
        }
        // 设置业务中心ID
        String storeId = getStoreId(bizScope);
        dto.setStoreId(storeId);

        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<TaskPageVo> list = taskDao.selectMyTaskList(dto);
        PageInfo<TaskPageVo> pageInfo = new PageInfo<>(list);
        return BaseResultUtil.success(pageInfo);
    }

    private String getStoreId(BizScope bizScope) {
        StringBuilder storeId = null;
        if (bizScope.getCode() == BizScopeEnum.CHINA.code) {
            return null;
        } else {
            storeId = new StringBuilder();
            for (Long id : bizScope.getStoreIds()) {
                if (storeId.length() > 0) {
                    storeId.append(",");
                }
                storeId.append(id);
            }
            return storeId.toString();
        }
    }

}
