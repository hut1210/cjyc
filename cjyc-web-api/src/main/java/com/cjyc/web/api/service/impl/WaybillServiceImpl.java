package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjkj.common.model.ResultData;
import com.cjkj.usercenter.dto.common.SelectRoleResp;
import com.cjyc.common.model.dao.IWaybillCarDao;
import com.cjyc.common.model.dao.IWaybillDao;
import com.cjyc.common.model.dto.web.waybill.*;
import com.cjyc.common.model.entity.Carrier;
import com.cjyc.common.model.entity.Waybill;
import com.cjyc.common.model.entity.defined.BizScope;
import com.cjyc.common.model.enums.BizScopeEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.BaseTipVo;
import com.cjyc.common.model.vo.ListVo;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.waybill.*;
import com.cjyc.common.system.feign.ISysRoleService;
import com.cjyc.common.system.service.ICsWaybillService;
import com.cjyc.common.system.service.sys.ICsSysService;
import com.cjyc.web.api.service.IWaybillService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
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
    @Resource
    private ICsSysService csSysService;

    /**
     * 提送车调度
     *
     * @param paramsDto 参数
     */
    @Override
    public ResultVo saveLocal(SaveLocalDto paramsDto) {
        return csWaybillService.saveLocal(paramsDto);
    }

    /**
     * 干线调度
     *
     * @param paramsDto 参数
     * @author JPG
     * @since 2019/10/17 9:16
     */
    @Override
    public ResultVo saveTrunk(SaveTrunkWaybillDto paramsDto) {
        return csWaybillService.saveTrunk(paramsDto);
    }
    @Override
    public ResultVo updateLocal(UpdateLocalDto paramsDto) {
        return  csWaybillService.updateLocal(paramsDto);
    }

    @Override
    public ResultVo updateTrunk(UpdateTrunkWaybillDto paramsDto) {
        return csWaybillService.updateTrunk(paramsDto);
    }

    @Override
    public ResultVo updateTrunkMidwayFinish(UpdateTrunkMidwayFinishDto paramsDto) {
        return  csWaybillService.updateTrunkMidwayFinish(paramsDto);
    }

    @Override
    public ResultVo trunkMidwayUnload(TrunkMidwayUnload paramsDto) {
        return csWaybillService.trunkMidwayUnload(paramsDto);
    }

    @Override
    public ResultVo<PageVo<CrWaybillVo>> inStoreList(storeListDto reqDto) {


        return null;
    }

    @Override
    public ResultVo<ListVo<BaseTipVo>> cancel(CancelWaybillDto paramsDto) {
        return csWaybillService.cancelDispatch(paramsDto);
    }


    @Override
    public ResultVo<List<HistoryListWaybillVo>> historyList(HistoryListDto paramsDto) {
        List<HistoryListWaybillVo> list = waybillDao.findHistoryList(paramsDto);
        return BaseResultUtil.success(list);
    }

    @Override
    public ResultVo<PageVo<CrWaybillVo>> crListForMineCarrier(CrWaybillDto paramsDto) {
        //根据角色查询承运商ID
        Carrier carrier = csSysService.getCarrierByRoleId(paramsDto.getRoleId());
        if(carrier == null){
            return BaseResultUtil.fail("承运商信息不存在");
        }
        paramsDto.setCarrierId(carrier.getId());
        //查询承运商信息
        PageHelper.startPage(paramsDto.getCurrentPage(), paramsDto.getPageSize(), true);
        List<CrWaybillVo> list = waybillDao.findCrListForMineCarrier(paramsDto);
        PageInfo<CrWaybillVo> pageInfo = new PageInfo<>(list);
        if(paramsDto.getCurrentPage() > pageInfo.getPages()){
            pageInfo.setList(null);
        }
        return BaseResultUtil.success(pageInfo);
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
        //查询角色业务中心范围
        BizScope bizScope = csSysService.getBizScopeByRoleId(paramsDto.getRoleId(), true);
        if(bizScope == null || bizScope.getCode() == BizScopeEnum.NONE.code){
            return null;
        }
        paramsDto.setBizScope(bizScope.getCode() == 0 ? null : bizScope.getStoreIds());


        PageHelper.startPage(paramsDto.getCurrentPage(), paramsDto.getPageSize(), true);
        List<TrunkListWaybillVo> list = null;
        if(StringUtils.isBlank(paramsDto.getDriverName()) && StringUtils.isBlank(paramsDto.getDriverPhone()) && StringUtils.isBlank(paramsDto.getVehiclePlateNo())){
            list = waybillDao.findLeftListTrunk(paramsDto);
        }else{
            list = waybillDao.findListTrunk(paramsDto);
        }
        PageInfo<TrunkListWaybillVo> pageInfo = new PageInfo<>(list);
        if(paramsDto.getCurrentPage() > pageInfo.getPages()){
            pageInfo.setList(null);
        }
        return BaseResultUtil.success(pageInfo);
    }


    @Override
    public ResultVo<PageVo<TrunkCarListWaybillCarVo>> trunkCarlist(TrunkListWaybillCarDto paramsDto) {
        //查询角色业务中心范围
        BizScope bizScope = csSysService.getBizScopeByRoleId(paramsDto.getRoleId(), true);
        if(bizScope == null || bizScope.getCode() == BizScopeEnum.NONE.code){
            return null;
        }
        paramsDto.setBizScope(bizScope.getCode() == 0 ? null : bizScope.getStoreIds());


        PageHelper.startPage(paramsDto.getCurrentPage(), paramsDto.getPageSize(), true);
        List<TrunkCarListWaybillCarVo> list = waybillCarDao.findTrunkCarList(paramsDto);
        PageInfo<TrunkCarListWaybillCarVo> pageInfo = new PageInfo<>(list);
        if(paramsDto.getCurrentPage() > pageInfo.getPages()){
            pageInfo.setList(null);
        }
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<WaybillVo> get(Long waybillId) {
        WaybillVo waybillVo = waybillDao.findVoById(waybillId);
        List<WaybillCarVo> waybillCarVo = waybillCarDao.findVoByWaybillId(waybillId);
        if(waybillCarVo != null){
            waybillVo.setList(waybillCarVo);
        }
        return BaseResultUtil.success(waybillVo);
    }

    @Override
    public ResultVo<List<WaybillCarVo> > getCarByType(Long orderCarId, Integer waybillType) {
       List<WaybillCarVo> list =waybillCarDao.findVoByType(orderCarId, waybillType);
        return BaseResultUtil.success(list);
    }



}
