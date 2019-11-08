package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.web.waybill.CysWaybillDto;
import com.cjyc.common.model.dto.web.waybill.*;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.BaseTipVo;
import com.cjyc.common.model.vo.ListVo;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.waybill.*;
import com.cjyc.common.system.service.ICsWaybillService;
import com.cjyc.web.api.service.IWaybillService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 运单表(业务员调度单) 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-17
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class WaybillServiceImpl extends ServiceImpl<IWaybillDao, Waybill> implements IWaybillService {

    @Resource
    private IWaybillDao waybillDao;
    @Resource
    private IWaybillCarDao waybillCarDao;
    @Resource
    private ICsWaybillService csWaybillService;

    /**
     * 提送车调度
     *
     * @param paramsDto 参数
     */
    @Override
    public ResultVo localDispatch(LocalDispatchListWaybillDto paramsDto) {
        return csWaybillService.localDisoatch(paramsDto);
    }

    /**
     * 干线调度
     *
     * @param paramsDto 参数
     * @author JPG
     * @since 2019/10/17 9:16
     */
    @Override
    public ResultVo trunkDispatch(TrunkDispatchListShellWaybillDto paramsDto) {
        return csWaybillService.trunkDispatch(paramsDto);
    }


    @Override
    public ResultVo<ListVo<BaseTipVo>> cancelDispatch(CancelDispatchDto paramsDto) {
        return csWaybillService.cancelDispatch(paramsDto);
    }


    @Override
    public ResultVo<List<HistoryListWaybillVo>> historyList(HistoryListWaybillDto paramsDto) {
        List<HistoryListWaybillVo> list = waybillDao.findHistoryList(paramsDto);
        return BaseResultUtil.success(list);
    }

    @Override
    public ResultVo<List<CysWaybillVo>> cysList(CysWaybillDto reqDto) {

        return null;
    }

    @Override
    public ResultVo<PageVo<LocalListWaybillCarVo>> locallist(LocalListWaybillCarDto paramsDto) {
        PageHelper.startPage(paramsDto.getCurrentPage(), paramsDto.getPageSize(), true);
        List<LocalListWaybillCarVo> list = waybillCarDao.findListLocal(paramsDto);
        PageInfo<LocalListWaybillCarVo> pageInfo = new PageInfo<>(list);
        if(paramsDto.getCurrentPage() > pageInfo.getPages()){
            pageInfo.setList(null);
        }
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<PageVo<TrunkMainListWaybillVo>> getTrunkMainList(TrunkMainListWaybillDto paramsDto) {
        PageHelper.startPage(paramsDto.getCurrentPage(), paramsDto.getPageSize(), true);
        List<TrunkMainListWaybillVo> list = waybillDao.findMainListTrunk(paramsDto);
        PageInfo<TrunkMainListWaybillVo> pageInfo = new PageInfo<>(list);
        if(paramsDto.getCurrentPage() > pageInfo.getPages()){
            pageInfo.setList(null);
        }
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<PageVo<TrunkSubListWaybillVo>> getTrunkSubList(TrunkSubListWaybillDto paramsDto) {
        PageHelper.startPage(paramsDto.getCurrentPage(), paramsDto.getPageSize(), true);
        List<TrunkSubListWaybillVo> list = waybillDao.findSubListTrunk(paramsDto);
        PageInfo<TrunkSubListWaybillVo> pageInfo = new PageInfo<>(list);
        if(paramsDto.getCurrentPage() > pageInfo.getPages()){
            pageInfo.setList(null);
        }
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<PageVo<TrunkListWaybillVo>> trunklist(TrunkListWaybillDto paramsDto) {

        PageHelper.startPage(paramsDto.getCurrentPage(), paramsDto.getPageSize(), true);
        List<TrunkListWaybillVo> list = waybillDao.findListTrunk(paramsDto);
        PageInfo<TrunkListWaybillVo> pageInfo = new PageInfo<>(list);
        if(paramsDto.getCurrentPage() > pageInfo.getPages()){
            pageInfo.setList(null);
        }
        return BaseResultUtil.success(pageInfo);

  /*      Integer currentPage = paramsDto.getCurrentPage();
        Integer pageSize = paramsDto.getPageSize();
        //统计
        Map<String, Object> countInfo = waybillDao.countListTrunkWithTask(paramsDto);
        Integer totalCount = (Integer) countInfo.get("totalCount");
        if(totalCount < 0){
            return BaseResultUtil.success(PageVo.<TrunkListWaybillVo>builder().build());
        }
        int pages = new BigDecimal(totalCount).divide(new BigDecimal(pageSize), BigDecimal.ROUND_CEILING).intValue();
        //分页查询

        List<TrunkListWaybillVo> list = waybillDao.findListTrunk(paramsDto);
        PageVo<TrunkListWaybillVo> pageVo = PageVo.<TrunkListWaybillVo>builder().totalPages(pages)
                .currentPage(currentPage)
                .pageSize(pageSize)
                .totalRecords(totalCount)
                .list(currentPage > pages || currentPage < 1 ? null : list)
                .build();

        return BaseResultUtil.success(pageVo);*/
    }


    @Override
    public ResultVo<PageVo<TrunkCarListWaybillCarVo>> trunkCarlist(TrunkListWaybillCarDto paramsDto) {
        List<TrunkCarListWaybillVo> list = waybillDao.findListTrunkWithCar(paramsDto);
        List<TrunkCarListWaybillCarVo> list1 = waybillCarDao.findCarListTrunk(paramsDto);
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo<WaybillVo> getForTrunkDetail(Long waybillId) {
        WaybillVo waybillVo = waybillDao.findVoById(waybillId);
        List<TrunkDetailWaybillCarVo> waybillCarVo = waybillCarDao.findForTrunkDetail(waybillId);
        waybillVo.setList(waybillCarVo);
        return BaseResultUtil.success(waybillVo);
    }

    @Override
    public ResultVo<List<TrunkDetailWaybillCarVo> > getCarByType(Long orderCarId, Integer waybillType) {
       List<TrunkDetailWaybillCarVo> list =waybillCarDao.findVoByType(orderCarId, waybillType);

        return BaseResultUtil.success(list);
    }



}
