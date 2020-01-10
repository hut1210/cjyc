package com.cjyc.salesman.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.salesman.BaseSalesDto;
import com.cjyc.common.model.dto.salesman.mine.AchieveDto;
import com.cjyc.common.model.dto.salesman.mine.OrderCarDto;
import com.cjyc.common.model.dto.salesman.mine.SalesAchieveDto;
import com.cjyc.common.model.dto.salesman.mine.StockCarDto;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.entity.Dictionary;
import com.cjyc.common.model.entity.WaybillCar;
import com.cjyc.common.model.entity.defined.BizScope;
import com.cjyc.common.model.enums.BizScopeEnum;
import com.cjyc.common.model.enums.waybill.WaybillTypeEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.util.QRCodeUtil;
import com.cjyc.common.model.util.TimeStampUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.salesman.mine.QRCodeVo;
import com.cjyc.common.model.vo.salesman.mine.StockCarDetailVo;
import com.cjyc.common.model.vo.salesman.mine.StockCarVo;
import com.cjyc.common.model.vo.salesman.mine.StockTaskVo;
import com.cjyc.common.system.config.LogoImgProperty;
import com.cjyc.common.system.service.ICsStoreService;
import com.cjyc.common.system.service.sys.ICsSysService;
import com.cjyc.salesman.api.service.IMineService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MineServiceImpl extends ServiceImpl<IWaybillCarDao, WaybillCar> implements IMineService {

    @Resource
    private IOrderCarDao orderCarDao;
    @Resource
    private ICarSeriesDao carSeriesDao;
    @Resource
    private ITaskDao taskDao;
    @Resource
    private IDictionaryDao dictionaryDao;
    @Resource
    private IAdminDao adminDao;
    @Resource
    private IWaybillCarDao waybillCarDao;
    @Resource
    private ICsSysService csSysService;

    @Override
    public ResultVo<PageVo<StockCarVo>> findStockCar(StockCarDto dto) {
        // 根据登录ID查询当前业务员所在业务中心ID
        BizScope bizScope = csSysService.getBizScopeByLoginId(dto.getLoginId(), true);
        // 判断当前登录人是否有权限访问
        if (BizScopeEnum.NONE.code == bizScope.getCode()) {
            return BaseResultUtil.fail("您没有访问权限!");
        }
        // 获取业务中心ID
        dto.setStoreIds(bizScope.getStoreIds());
        if(dto.getEndTime() != null && dto.getEndTime() != 0){
            dto.setEndTime(TimeStampUtil.addDays(dto.getEndTime(),1));
        }
        String logoImg = LogoImgProperty.logoImg;
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<StockCarVo> stockCarVos = orderCarDao.findStockCar(dto);
        if(!CollectionUtils.isEmpty(stockCarVos)){
            for(StockCarVo vo : stockCarVos){
                String carLogoImg = carSeriesDao.getLogoImgByBraMod(vo.getBrand(), vo.getModel());
                vo.setLogoImg(logoImg+carLogoImg);
            }
        }
        PageInfo<StockCarVo> pageInfo = new PageInfo(stockCarVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<StockCarDetailVo> findStockCarDetail(OrderCarDto dto) {
        String logoImg = LogoImgProperty.logoImg;
        //订单车辆信息
        StockCarDetailVo orderCarVo = orderCarDao.findOrderCar(dto);
        if(orderCarVo != null){
            String carLogoImg = carSeriesDao.getLogoImgByBraMod(orderCarVo.getBrand(), orderCarVo.getModel());
            orderCarVo.setLogoImg(logoImg+carLogoImg);
        }
        List<StockTaskVo> listStockTask = taskDao.findListStockTask(dto);
        orderCarVo.setStockVos(listStockTask);
        return BaseResultUtil.success(orderCarVo);
    }

    @Override
    public ResultVo<Map<String,Object>> achieveCount(AchieveDto dto) {
        Admin admin = adminDao.selectById(dto.getLoginId());
        if(admin == null){
            return BaseResultUtil.fail("该用户不存在，请检查");
        }
        //转成localDate
        LocalDate localDate = LocalDateTimeUtil.convertToLocalDate(dto.getDate(), TimePatternConstant.DATE);
        //获取下一个月起始日
        LocalDate nextMonth = LocalDateTimeUtil.getNextMouth(localDate,1);
        Long thisMonthTime = LocalDateTimeUtil.convertToLong(localDate);
        Long nextMonthTime = TimeStampUtil.addDays(LocalDateTimeUtil.convertToLong(nextMonth),1);
        SalesAchieveDto achieveDto = new SalesAchieveDto();
        achieveDto.setThisMonthTime(thisMonthTime);
        achieveDto.setNextMonthTime(nextMonthTime);
        Long userId = admin.getUserId();
        Long driverId = admin.getId();
        Map<String,Object> mapCount = new HashMap<>(5);
        //我下的订单的台数
        mapCount.put("orderCarCount",orderCarDao.orderCarCount(0,userId));
        //我确认过的全部订单的车辆
        mapCount.put("confirmedCarCount",orderCarDao.orderCarCount(1,userId));
        //我下的确认过的订单送车任务交付客户
        mapCount.put("deliveredCarCount",waybillCarDao.deliveredCarCount(userId));
        //我已经完成提车的台数
        mapCount.put("pickCarCount",waybillCarDao.waybillCarCount(1, driverId));
        //我已完成送车的台数
        mapCount.put("backCarCount",waybillCarDao.waybillCarCount(3, driverId));
        return BaseResultUtil.success(mapCount);
    }

    @Override
    public ResultVo<QRCodeVo> findQrCode(BaseSalesDto dto) {
        Admin admin = adminDao.selectById(dto.getLoginId());
        if(admin == null){
            return BaseResultUtil.fail("该业务员不存在，请检查");
        }
        Dictionary dictionary = dictionaryDao.selectOne(new QueryWrapper<Dictionary>().lambda()
                .eq(Dictionary::getItem, "system_picture_qrcode")
                .eq(Dictionary::getState, 1)
                .select(Dictionary::getItemValue));
        String imagePath = "D:\\2.jpg";
        String logo = dictionary.getItemValue();
        String content = "http://customertest.yunchelogistics.com/cjyc/customer/swagger-ui.html?phone=18513107259";
        try{
            QRCodeUtil.encodeimage(imagePath, "JPEG", content, 430, 430 , logo);
        }catch (Exception e){
            return BaseResultUtil.fail("异常");
        }
        QRCodeVo vo = new QRCodeVo();
        vo.setSalesmanId(admin.getId());
        vo.setPhone(admin.getPhone());
        vo.setQrCodeUrl(imagePath+"?"+admin.getPhone());
        return BaseResultUtil.success(vo);
    }

}