package com.cjyc.salesman.api.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cjyc.common.model.dao.IOrderCarDao;
import com.cjyc.common.model.dao.IWaybillCarDao;
import com.cjyc.common.model.dto.salesman.dispatch.DispatchListDto;
import com.cjyc.common.model.entity.defined.BizScope;
import com.cjyc.common.model.enums.BizScopeEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.salesman.dispatch.CityCarCountVo;
import com.cjyc.common.model.vo.salesman.dispatch.DispatchListVo;
import com.cjyc.common.model.vo.salesman.dispatch.StartAndEndCityCountVo;
import com.cjyc.common.system.service.sys.ICsSysService;
import com.cjyc.salesman.api.service.IDispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    private IWaybillCarDao waybillCarDao;

    @Override
    public ResultVo getAllCityCarCount(Long loginId) {
        // 根据登录ID查询当前业务员所在业务中心ID
        BizScope bizScope = csSysService.getBizScopeByLoginId(loginId, true);

        // 判断当前登录人是否有权限访问
        int code = bizScope.getCode();
        if (BizScopeEnum.NONE.code == code) {
            return BaseResultUtil.fail("无权访问");
        }
        if (BizScopeEnum.CHINA.code == code) {
            return BaseResultUtil.fail("暂不支持全国数据访问");
        }

        List<CityCarCountVo> returnList = new ArrayList<>(10);

        // 获取业务中心ID
        String storeIds = getStoreIds(bizScope);
        // 查询出发地相同的车辆数量：提车，干线，送车均未调度的车辆
        List<CityCarCountVo> notDispatchList = orderCarDao.selectStartCityCarCount(storeIds);
        // 查询出发地，目的地相同的车辆数量
        for (CityCarCountVo cityCarCountVo : notDispatchList) {
            List<StartAndEndCityCountVo> startAndEndCityCountList = orderCarDao.selectStartAndEndCityCarCount(cityCarCountVo.getStartCityCode());
            cityCarCountVo.setStartAndEndCityCountList(startAndEndCityCountList);
        }

        returnList.addAll(notDispatchList);


        return BaseResultUtil.success(returnList);
    }

    @Override
    public PageVo<DispatchListVo> getPageList(DispatchListDto dto) {
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
            });
        }
        return PageVo.<DispatchListVo>builder()
                .totalRecords(page.getTotal())
                .totalPages(page.getTotal() % page.getSize() == 0?
                        (int)(page.getTotal()/page.getSize()): (int)(page.getTotal()/page.getSize()+1))
                .pageSize((int)page.getSize())
                .currentPage((int)page.getCurrent())
                .list(list).build();
    }

    private String getStoreIds(BizScope bizScope) {
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
