package com.cjyc.salesman.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.ICarSeriesDao;
import com.cjyc.common.model.dao.IOrderCarDao;
import com.cjyc.common.model.dao.IWaybillCarDao;
import com.cjyc.common.model.dto.salesman.mine.StockCarDetailDto;
import com.cjyc.common.model.dto.salesman.mine.StockCarDto;
import com.cjyc.common.model.entity.WaybillCar;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.salesman.mine.StockCarDetailVo;
import com.cjyc.common.model.vo.salesman.mine.StockCarVo;
import com.cjyc.common.system.config.LogoImgProperty;
import com.cjyc.salesman.api.service.IMineService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MineServiceImpl extends ServiceImpl<IWaybillCarDao, WaybillCar> implements IMineService {

    @Resource
    private IOrderCarDao orderCarDao;
    @Resource
    private ICarSeriesDao carSeriesDao;

    @Override
    public ResultVo<PageVo<StockCarVo>> findStockCar(StockCarDto dto) {
        String logoImg = LogoImgProperty.logoImg;
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<StockCarVo> stockCarVos = orderCarDao.findStockCar(dto);
        if(!CollectionUtils.isEmpty(stockCarVos)){
            for(StockCarVo vo : stockCarVos){
                logoImg += carSeriesDao.getLogoImgByBraMod(vo.getBrand(), vo.getModel());
                vo.setLogoImg(logoImg);
            }
        }
        Integer carStockCount = orderCarDao.stockCarCount(dto);
        Map<String,Object> map = new HashMap<String,Object>(1);
        map.put("carStockCount",carStockCount == null ? 0 : carStockCount);
        PageInfo<StockCarVo> pageInfo = new PageInfo(stockCarVos);
        return BaseResultUtil.success(pageInfo,map);
    }

    @Override
    public ResultVo<StockCarDetailVo> findStockCarDetail(StockCarDetailDto dto) {
        String logoImg = LogoImgProperty.logoImg;
        //订单车辆信息
        StockCarDetailVo orderCarVo = orderCarDao.findStockCarDetail(dto);

        return null;
    }
}