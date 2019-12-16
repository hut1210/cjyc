package com.cjyc.salesman.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.salesman.dispatch.DispatchListDto;
import com.cjyc.common.model.dto.salesman.dispatch.HistoryDispatchRecordDto;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.common.model.entity.defined.BizScope;
import com.cjyc.common.model.enums.BizScopeEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.TimeStampUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.salesman.dispatch.*;
import com.cjyc.common.system.config.LogoImgProperty;
import com.cjyc.common.system.service.sys.ICsSysService;
import com.cjyc.salesman.api.service.IDispatchService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

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

    @Override
    public ResultVo getCityCarCount(Long loginId) {
        // 根据登录ID查询当前业务员所在业务中心ID
        BizScope bizScope = csSysService.getBizScopeByLoginId(loginId, true);

        // 判断当前登录人是否有权限访问
        int code = bizScope.getCode();
        if (BizScopeEnum.NONE.code == code) {
            return BaseResultUtil.fail("您没有访问权限!");
        }

        // 获取业务中心ID
        String storeIds = getStoreIds(bizScope);
        // 查询 出发地 以及车辆数量
        List<CityCarCountVo> list = orderCarDao.selectStartCityCarCount(storeIds);
        // 查询出 发地-目的地 以及车辆数量
        Map<String,Object> map = new HashMap<>(2);
        map.put("storeIds",storeIds);
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
        BizScope bizScope = csSysService.getBizScopeByLoginId(dto.getLoginId(), true);

        // 判断当前登录人是否有权限访问
        int code = bizScope.getCode();
        if (BizScopeEnum.NONE.code == code) {
            return BaseResultUtil.fail("无权访问");
        }
        dto.setBizStoreIds(getStoreIds(bizScope));
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
                    if (CollectionUtils.isEmpty(modeList)) {
                        modeList.add(v.getOrderStartCity()+"-"+v.getOrderEndCity());
                        stateList.add("0");
                    }else {
                        String[] lines = modeList.get(modeList.size() - 1).split("-");
                        if (lines != null && lines.length == 2) {
                            if (!lines[1].equals(v.getOrderEndCity())) {
                                modeList.add(lines[1] + "-" + v.getOrderEndCity());
                                stateList.add("0");
                            }
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
    public ResultVo<PageVo<DispatchListVo>> waitDispatchCarList(DispatchListDto dto) {
        // 根据登录ID查询当前业务员所在业务中心ID
        BizScope bizScope = csSysService.getBizScopeByLoginId(dto.getLoginId(), true);
        // 判断当前登录人是否有权限访问
        if (BizScopeEnum.NONE.code == bizScope.getCode()) {
            return BaseResultUtil.fail("无权访问");
        }
        dto.setBizStoreIds(getStoreIds(bizScope));
        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize(), true);
        List<DispatchListVo> list = orderCarDao.findWaitDispatchCarListForApp(dto);
        return null;

    }

    @Override
    public ResultVo getCarDetail(String carNo) {
        // 查询车辆信息
        DispatchCarDetailVo detail = new DispatchCarDetailVo();
        OrderCar orderCar = orderCarDao.selectOne(new QueryWrapper<OrderCar>().lambda().eq(OrderCar::getNo, carNo));
        String logoImg = carSeriesDao.getLogoImgByBraMod(orderCar.getBrand(), orderCar.getModel());
        detail.setLogoImg(LogoImgProperty.logoImg+logoImg);
        BeanUtils.copyProperties(orderCar,detail);
        // 查询订单信息
        Order order = orderDao.selectById(orderCar.getOrderId());
        BeanUtils.copyProperties(order,detail);
        // 查询调度记录
        List<DispatchRecordVo> dispatchRecordVoList = waybillCarDao.selectWaybillRecordList(orderCar.getId());
        detail.setDispatchRecordVoList(dispatchRecordVoList);
        return BaseResultUtil.success(detail);
    }

    @Override
    public ResultVo getHistoryRecord(HistoryDispatchRecordDto dto) {
        if (dto.getCreateTimeE() != null) {
            dto.setCreateTimeE(TimeStampUtil.convertEndTime(dto.getCreateTimeE()));
        }
        // 根据登录ID查询当前业务员所在业务中心ID
        BizScope bizScope = csSysService.getBizScopeByLoginId(dto.getLoginId(), true);

        // 判断当前登录人是否有权限访问
        int code = bizScope.getCode();
        if (BizScopeEnum.NONE.code == code) {
            return BaseResultUtil.fail("无权访问");
        }
        dto.setBizStoreIds(getStoreIds(bizScope));
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<HistoryDispatchRecordVo> list = waybillDao.selectHistoryDispatchRecord(dto);
        PageInfo pageInfo = new PageInfo(list);
        return BaseResultUtil.success(pageInfo);
    }


    private String getStoreIds(BizScope bizScope) {
        if (bizScope.getCode() == BizScopeEnum.CHINA.code) {
            return null;
        }
        Set<Long> storeIds = bizScope.getStoreIds();
        StringBuilder sb = new StringBuilder();
        for (Long storeId : storeIds) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(storeId);
        }
        return sb.toString();
    }
}
