package com.cjyc.salesman.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.salesman.dispatch.DispatchListDto;
import com.cjyc.common.model.dto.salesman.dispatch.HistoryDispatchRecordDto;
import com.cjyc.common.model.dto.salesman.dispatch.WaitCountDto;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.entity.defined.BizScope;
import com.cjyc.common.model.enums.BizScopeEnum;
import com.cjyc.common.model.enums.waybill.WaybillCarStateEnum;
import com.cjyc.common.model.enums.waybill.WaybillCarrierTypeEnum;
import com.cjyc.common.model.enums.waybill.WaybillStateEnum;
import com.cjyc.common.model.enums.waybill.WaybillTypeEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.TimeStampUtil;
import com.cjyc.common.model.vo.ListVo;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.salesman.dispatch.*;
import com.cjyc.common.model.vo.web.dispatch.WaitCountLineVo;
import com.cjyc.common.model.vo.web.dispatch.WaitCountVo;
import com.cjyc.common.system.config.LogoImgProperty;
import com.cjyc.common.system.service.sys.ICsSysService;
import com.cjyc.salesman.api.service.IDispatchService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 调度业务接口实现
 * @Author Liu Xing Xiang
 * @Date 2019/12/11 13:35
 **/
@Service
public class DispatchServiceImpl implements IDispatchService {
    /**
     * 调度列表干线路径空白字符填充
     */
    private static final String BLANK_TEXT_FILL = "V_NULL_V";
    @Autowired
    private ICsSysService csSysService;
    @Resource
    private IOrderCarDao orderCarDao;
    @Resource
    private IOrderDao orderDao;
    @Resource
    private IWaybillCarDao waybillCarDao;
    @Resource
    private ICarSeriesDao carSeriesDao;
    @Resource
    private IWaybillDao waybillDao;
    @Resource
    private ICarrierDao carrierDao;
    @Resource
    private IAdminDao adminDao;
    @Resource
    private ICustomerDao customerDao;
    @Resource
    private ITaskDao taskDao;
    @Resource
    private ILineDao lineDao;

    @Override
    public ResultVo getCityCarCount(Long loginId) {
        // 根据登录ID查询当前业务员所在业务中心ID
        BizScope bizScope = csSysService.getBizScopeByLoginIdNew(loginId, true);

        // 判断当前登录人是否有权限访问
        if (BizScopeEnum.NONE.code == bizScope.getCode()) {
            return BaseResultUtil.fail("您没有访问权限!");
        }

        // 查询 出发地 以及车辆数量
        List<CityCarCountVo> list = orderCarDao.selectStartCityCarCount(bizScope.getStoreIds());
        // 查询出 发地-目的地 以及车辆数量
        Map<String,Object> map = new HashMap<>(2);
        map.put("storeIds",bizScope.getStoreIds());
        for (CityCarCountVo cityCarCountVo : list) {
            map.put("startCityCode",cityCarCountVo.getStartCityCode());
            List<StartAndEndCityCountVo> startAndEndCityCountList = orderCarDao.selectStartAndEndCityCarCount(map);
            cityCarCountVo.setStartAndEndCityCountList(startAndEndCityCountList);
        }
        return BaseResultUtil.success(list);
    }

    @Override
    public ResultVo<PageVo<DispatchListVo>> getPageList(DispatchListDto dto) {
        // 根据登录ID查询当前业务员所在业务中心ID
        BizScope bizScope = csSysService.getBizScopeByLoginIdNew(dto.getLoginId(), true);

        // 判断当前登录人是否有权限访问
        if (BizScopeEnum.NONE.code == bizScope.getCode()) {
            return BaseResultUtil.fail("无权访问");
        }
        dto.setBizScope(bizScope.getStoreIds());
        Page<DispatchListVo> page = new Page<>();
        page.setCurrent(dto.getCurrentPage());
        page.setSize(dto.getPageSize());
        List<DispatchListVo> list = waybillCarDao.getDispatchList(page, dto);
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(v -> {
                if (!StringUtils.isEmpty(v.getTrunkMode())
                        && !StringUtils.isEmpty(v.getTrunkState())) {
                    String[] modes = v.getTrunkMode().split(",");
                    String[] states = v.getTrunkState().split(",");
                    List<String> modeList = new ArrayList<>();
                    List<String> stateList = new ArrayList<>();
                    for (int i = 0; i < modes.length; i++) {
                        if (!BLANK_TEXT_FILL.equals(modes[i])) {
                            modeList.add(modes[i]);
                            stateList.add(states[i]);
                        }
                    }
                    v.setTrunkModeList(modeList);
                    v.setTrunkStateList(stateList);
                }
                //品牌logo图片：LogoImgProperty
                String logoImg = carSeriesDao.getLogoImgByBraMod(v.getBrand(), v.getModel());
                if (!StringUtils.isEmpty(logoImg)) {
                    v.setLogoImgPath(LogoImgProperty.logoImg + logoImg);
                }
            });
        }
        return BaseResultUtil.success(PageVo.<DispatchListVo>builder()
                .totalRecords(page.getTotal())
                .totalPages(page.getTotal() % page.getSize() == 0?
                        (int)(page.getTotal()/page.getSize()): (int)(page.getTotal()/page.getSize()+1))
                .pageSize((int)page.getSize())
                .currentPage((int)page.getCurrent())
                .list(list).build());
    }


    @Override
    public ResultVo<PageVo<WaitDispatchCarListVo>> waitList(DispatchListDto dto) {
        // 根据登录ID查询当前业务员所在业务中心ID
        BizScope bizScope = csSysService.getBizScopeByLoginIdNew(dto.getLoginId(), true);
        // 判断当前登录人是否有权限访问
        if (BizScopeEnum.NONE.code == bizScope.getCode()) {
            return BaseResultUtil.fail("无权访问");
        }
        dto.setBizScope(bizScope.getStoreIds());
        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize(), true);
        List<WaitDispatchCarListVo> list = orderCarDao.findWaitDispatchCarListForApp(dto);
        if(!CollectionUtils.isEmpty(list)){
            for (WaitDispatchCarListVo vo : list) {
                List<String> nodes = orderCarDao.findTrunkNodes(vo.getOrderCarId());
                vo.setTrunkNodes(nodes == null ? Lists.newArrayList() : nodes);
            }
        }

        PageInfo<WaitDispatchCarListVo> pageInfo = new PageInfo<>(list);
        if(pageInfo.getPages() < dto.getCurrentPage()){
            pageInfo.setList(null);
        }
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo getWaybillDetail(Long waybillId) {
        WaybillDetailVo detail = new WaybillDetailVo();
        // 查询运单信息
        Waybill waybill = waybillDao.selectById(waybillId);
        if (waybill == null) {
            return BaseResultUtil.fail("运单ID错误");
        }
        BeanUtils.copyProperties(waybill,detail);

        // 查询承运商管理员手机号
        String carrierPhone = getCarrierPhone(waybill.getCarrierId(), waybill.getCarrierType());
        detail.setLinkmanPhone(carrierPhone);

        // 查询任务单，获取司机id
        List<Task> taskList = taskDao.selectList(new QueryWrapper<Task>().lambda().eq(Task::getWaybillId, waybill.getId()));
        if (!CollectionUtils.isEmpty(taskList) && taskList.size() == 1) {
            detail.setDriverId(taskList.get(0).getDriverId());
        }

        // 查询车辆信息
        LambdaQueryWrapper<WaybillCar> queryWrapper = new QueryWrapper<WaybillCar>().lambda()
                .eq(WaybillCar::getWaybillId, waybillId).le(WaybillCar::getState, WaybillCarStateEnum.UNLOADED.code);
        if (waybill.getState() > WaybillStateEnum.FINISHED.code) {
            queryWrapper = new QueryWrapper<WaybillCar>().lambda()
                    .eq(WaybillCar::getWaybillId, waybillId);
        }

        List<WaybillCar> waybillCarList = waybillCarDao.selectList(queryWrapper);
        List<WaybillCarDetailVo> carDetailVoList = new ArrayList<>(10);
        WaybillCarDetailVo carDetailVo = null;
        if (!CollectionUtils.isEmpty(waybillCarList)) {
            for (WaybillCar waybillCar : waybillCarList) {
                carDetailVo = new WaybillCarDetailVo();
                BeanUtils.copyProperties(waybillCar,carDetailVo);

                // 给详细地址拼接市区
                carDetailVo.setStartAddress(waybillCar.getStartCity()+waybillCar.getStartArea()+waybillCar.getStartAddress());
                carDetailVo.setEndAddress(waybillCar.getEndCity()+waybillCar.getEndArea()+waybillCar.getEndAddress());


                // 查询干线线路费
                if (waybillCar.getLineId() != null) {
                    Line line = lineDao.selectById(waybillCar.getLineId());
                    carDetailVo.setLineFreightFee(line == null ? new BigDecimal(0) : line.getDefaultFreightFee());
                }

                // 指导路线
                carDetailVo.setGuideLine(waybillCar.getStartCity() + "-" + waybillCar.getEndCity());

                // 获取车辆运输图片
                getCarPhotoImg(carDetailVo,waybillCar,waybill);

                // 查询品牌车系信息
                OrderCar orderCar = orderCarDao.selectById(waybillCar.getOrderCarId());
                BeanUtils.copyProperties(orderCar,carDetailVo);

                // 查询提车，送车方式
                Order order = orderDao.selectById(orderCar.getOrderId());
                carDetailVo.setPickType(orderCar.getPickType());
                carDetailVo.setBackType(orderCar.getBackType());
                carDetailVo.setPayType(order.getPayType());


                // 查询车辆logo图片
                String logoImg = carSeriesDao.getLogoImgByBraMod(carDetailVo.getBrand(),carDetailVo.getModel());
                carDetailVo.setLogoPhotoImg(LogoImgProperty.logoImg+logoImg);

                carDetailVo.setId(waybillCar.getId());
                carDetailVo.setWaybillCarState(waybillCar.getState());
                carDetailVoList.add(carDetailVo);
            }
        }
        detail.setCarDetailVoList(carDetailVoList);

        return BaseResultUtil.success(detail);
    }

    private void getCarPhotoImg(WaybillCarDetailVo carDetailVo,WaybillCar waybillCar,Waybill waybill) {
        // 查询车辆历史图片
        StringBuilder sb = getCarHistoryPhotoImg(waybillCar,waybill);

        // 封装当前车辆图片
        fillCarPhotoImg(waybillCar, sb);

        // 封装历史图片
        carDetailVo.setHistoryLoadPhotoImg(sb.toString());
    }

    private void fillCarPhotoImg(WaybillCar waybillCar, StringBuilder sb) {
        // 提车图片
        String loadPhotoImg = waybillCar.getLoadPhotoImg();
        if (sb.length() > 0 && !StringUtils.isEmpty(loadPhotoImg)) {
            sb.append(",");
        }
        if (!StringUtils.isEmpty(loadPhotoImg)) {
            sb.append(loadPhotoImg);
        }

        // 交车图片
        String unloadPhotoImg = waybillCar.getUnloadPhotoImg();
        if (sb.length() > 0 && !StringUtils.isEmpty(unloadPhotoImg)) {
            sb.append(",");
        }
        if (!StringUtils.isEmpty(unloadPhotoImg)) {
            sb.append(unloadPhotoImg);
        }
    }

    private StringBuilder getCarHistoryPhotoImg(WaybillCar waybillCar,Waybill waybill) {
        /*LambdaQueryWrapper<WaybillCar> query = new QueryWrapper<WaybillCar>().lambda()
                .eq(WaybillCar::getOrderCarId, waybillCar.getOrderCarId())
                .lt(WaybillCar::getId, waybillCar.getId());*/

        Map<String,Object> map = new HashMap<>(3);
        map.put("orderCarId",waybillCar.getOrderCarId());
        map.put("waybillCarId",waybillCar.getId());
        map.put("waybillType",waybill.getType());
        List<WaybillCar> waybillCarList = waybillCarDao.selectWaybillCarList(map);
        StringBuilder sb = new StringBuilder();
        if (!CollectionUtils.isEmpty(waybillCarList)) {
            for (WaybillCar car : waybillCarList) {
                String loadPhotoImg = car.getLoadPhotoImg();
                String unloadPhotoImg = car.getUnloadPhotoImg();
                if (sb.length() > 0 && !StringUtils.isEmpty(loadPhotoImg)) {
                    sb.append(",");
                }
                if (!StringUtils.isEmpty(loadPhotoImg)) {
                    sb.append(loadPhotoImg);
                }
                if (sb.length() > 0 && !StringUtils.isEmpty(unloadPhotoImg)) {
                    sb.append(",");
                }
                if (!StringUtils.isEmpty(unloadPhotoImg)) {
                    sb.append(unloadPhotoImg);
                }
            }
        }
        return sb;
    }

    @Override
    public ResultVo<ListVo<WaitCountVo>> waitCountList(WaitCountDto paramsDto) {
        // 根据登录ID查询当前业务员所在业务中心ID
        BizScope bizScope = csSysService.getBizScopeByLoginIdNew(paramsDto.getLoginId(), true);
        // 判断当前登录人是否有权限访问
        if (BizScopeEnum.NONE.code == bizScope.getCode()) {
            return BaseResultUtil.fail("无权访问");
        }
        paramsDto.setBizScope(bizScope.getStoreIds());

        //查询统计
        Map<String, Object> countInfo = null;
        List<WaitCountVo> list = orderCarDao.findWaitDispatchCarCountListForApp(paramsDto);
        if(!CollectionUtils.isEmpty(list)){
            countInfo = orderCarDao.countTotalWaitDispatchCarCountListForApp(paramsDto);

            for (WaitCountVo vo: list) {
                paramsDto.setCityCode(vo.getStartCityCode());
                List<WaitCountLineVo> child = orderCarDao.findWaitDispatchCarCountLineListForApp(paramsDto);
                vo.setList(child);
            }
        }

        return BaseResultUtil.success(list, countInfo);
    }


    private String getCarrierPhone(Long carrierId,Integer carrierType) {
        int code1 = WaybillCarrierTypeEnum.TRUNK_INDIVIDUAL.code;
        int code2 = WaybillCarrierTypeEnum.TRUNK_ENTERPRISE.code;
        int code3 = WaybillCarrierTypeEnum.LOCAL_ADMIN.code;
        int code4 = WaybillCarrierTypeEnum.LOCAL_PILOT.code;
        int code5 = WaybillCarrierTypeEnum.LOCAL_CONSIGN.code;
        int code6 = WaybillCarrierTypeEnum.SELF.code;
        String phone = "";
        boolean b = carrierType == code1 || carrierType == code2 || carrierType == code4 || carrierType == code5;
        if (b) {
            Carrier carrier = carrierDao.selectById(carrierId);
            if (carrier != null) {
                phone = carrier.getLinkmanPhone();
            }
        }
        if (carrierType == code3) {
            Admin admin = adminDao.selectById(carrierId);
            if (admin != null) {
                phone = admin.getPhone();
            }
        }
        if (carrierType == code6) {
            Customer customer = customerDao.selectById(carrierId);
            if (customer != null) {
                phone = customer.getContactPhone();
            }
        }
        return phone;
    }

    @Override
    public ResultVo getCarDetail(String carNo) {
        // 查询车辆信息
        DispatchCarDetailVo detail = new DispatchCarDetailVo();
        OrderCar orderCar = orderCarDao.selectOne(new QueryWrapper<OrderCar>().lambda().eq(OrderCar::getNo, carNo));
        String logoImg = carSeriesDao.getLogoImgByBraMod(orderCar.getBrand(), orderCar.getModel());
        detail.setLogoImg(LogoImgProperty.logoImg+logoImg);
        BeanUtils.copyProperties(orderCar,detail);
        detail.setOrderCarNo(orderCar.getNo());

        // 查询订单信息
        Order order = orderDao.selectById(orderCar.getOrderId());
        BeanUtils.copyProperties(order,detail);

        // 查询调度记录
        List<DispatchRecordVo> dispatchRecordVoList = waybillCarDao.selectWaybillRecordList(orderCar.getId());
        // 查询承运商手机号
        if (!CollectionUtils.isEmpty(dispatchRecordVoList)) {
            for (DispatchRecordVo dispatchRecordVo : dispatchRecordVoList) {
                String carrierPhone = getCarrierPhone(dispatchRecordVo.getCarrierId(), dispatchRecordVo.getCarrierType());
                dispatchRecordVo.setLinkmanPhone(carrierPhone);
            }
        }

        detail.setDispatchRecordVoList(dispatchRecordVoList);
        return BaseResultUtil.success(detail);
    }

    @Override
    public ResultVo getHistoryRecord(HistoryDispatchRecordDto dto) {
        if (dto.getCreateTimeE() != null && dto.getCreateTimeE() != 0) {
            dto.setCreateTimeE(TimeStampUtil.convertEndTime(dto.getCreateTimeE()));
        }
        // 根据登录ID查询当前业务员所在业务中心ID
        BizScope bizScope = csSysService.getBizScopeByLoginIdNew(dto.getLoginId(), true);

        // 判断当前登录人是否有权限访问
        if (BizScopeEnum.NONE.code == bizScope.getCode()) {
            return BaseResultUtil.fail("无权访问");
        }
        dto.setBizStoreIds(bizScope.getStoreIds());
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<HistoryDispatchRecordVo> list = waybillDao.selectHistoryDispatchRecord(dto);
        PageInfo<HistoryDispatchRecordVo> pageInfo = new PageInfo(list);
        return BaseResultUtil.success(pageInfo);
    }
}
