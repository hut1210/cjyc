package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.web.BaseWebDto;
import com.cjyc.common.model.dto.web.dispatch.LineWaitCountDto;
import com.cjyc.common.model.dto.web.order.*;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.entity.defined.BizScope;
import com.cjyc.common.model.entity.defined.FullCity;
import com.cjyc.common.model.entity.defined.FullWaybillCar;
import com.cjyc.common.model.enums.BizScopeEnum;
import com.cjyc.common.model.enums.ClientEnum;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.enums.SendNoTypeEnum;
import com.cjyc.common.model.enums.city.CityLevelEnum;
import com.cjyc.common.model.enums.order.OrderCarStateEnum;
import com.cjyc.common.model.enums.order.OrderStateEnum;
import com.cjyc.common.model.exception.ParameterException;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.MoneyUtil;
import com.cjyc.common.model.vo.ListVo;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.OrderCarVo;
import com.cjyc.common.model.vo.web.order.*;
import com.cjyc.common.model.vo.web.waybill.WaybillCarVo;
import com.cjyc.common.system.service.*;
import com.cjyc.common.system.service.sys.ICsSysService;
import com.cjyc.web.api.service.IOrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * <p>
 * 订单表(客户下单) 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-09
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class OrderServiceImpl extends ServiceImpl<IOrderDao, Order> implements IOrderService {

    @Resource
    private IOrderDao orderDao;
    @Resource
    private ICsOrderService csOrderService;
    @Resource
    private IOrderCarDao orderCarDao;
    @Resource
    private IOrderChangeLogDao orderChangeLogDao;
    @Resource
    private IWaybillCarDao waybillCarDao;
    @Resource
    private IStoreDao storeDao;
    @Resource
    private ICsLineNodeService csLineNodeService;
    @Resource
    private ICsLineService csLineService;
    @Resource
    private ICsCityService csCityService;
    @Resource
    private ICsSysService csSysService;
    @Resource
    private ICsAdminService csAdminService;
    @Autowired
    private ICsSendNoService csSendNoService;
    @Resource
    private ICsCustomerService csCustomerService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importCustomerOrder(List<ImportCustomerOrderDto> orderList,
                                    List<ImportCustomerOrderCarDto> carList, Admin admin) {
        //保存订单及车辆信息
        if (!CollectionUtils.isEmpty(orderList) && !CollectionUtils.isEmpty(carList)) {
            Map<Integer, ImportCustomerOrderDto> orderMap = new HashMap<>();
            Map<Integer, List<ImportCustomerOrderCarDto>> carMap = new HashMap<>();
            orderList.forEach(o -> {
                if (!orderMap.containsKey(o.getOrderNo())) {
                    orderMap.put(o.getOrderNo(), o);
                }
            });
            carList.forEach(c -> {
                if (carMap.containsKey(c.getOrderNo())) {
                    carMap.get(c.getOrderNo()).add(c);
                } else {
                    carMap.put(c.getOrderNo(), Lists.newArrayList(c));
                }
            });
            //生成订单号并批量保存
            orderMap.forEach((integer, dto) -> {
                String orderNo = csSendNoService.getNo(SendNoTypeEnum.ORDER);

                List<ImportCustomerOrderCarDto> carList1 = carMap.get(dto.getOrderNo());
                if (!CollectionUtils.isEmpty(carList1)) {
                    //保存订单、车辆信息
                    BigDecimal totalFee = BigDecimal.ZERO;
                    for (ImportCustomerOrderCarDto c: carList1) {
                        totalFee = totalFee.add(c.getPickFee())
                                .add(c.getBackFee()).add(c.getTrunkFee()).add(c.getAddInsuranceFee());
                    }

                    Order order = packCustomerOrderForImport(dto, orderNo, admin, carList1.size(), totalFee);
                    orderDao.insert(order);
                    Long orderId = order.getId();
                    for (int i = 1; i < carList1.size() + 1; i++) {
                        String carNo = csSendNoService.formatNo(orderNo, i, 3);
                        OrderCar car = packCustomerCarForImport(order, carNo, carList1.get(i-1));
                        orderCarDao.insert(car);
                    }
                }
            });
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importKeyCustomerOrder(List<ImportKeyCustomerOrderDto> orderList, List<ImportKeyCustomerOrderCarDto> carList, Admin admin) {
        //保存订单及车辆信息
        if (!CollectionUtils.isEmpty(orderList)&&!CollectionUtils.isEmpty(carList)) {
            Map<Integer, ImportKeyCustomerOrderDto> orderMap = new HashMap<>();
            Map<Integer, List<ImportKeyCustomerOrderCarDto>> carMap = new HashMap<>();
            orderList.forEach(o -> {
                if (!orderMap.containsKey(o.getOrderNo())) {
                    orderMap.put(o.getOrderNo(), o);
                }
            });
            carList.forEach(c -> {
                if (carMap.containsKey(c.getOrderNo())) {
                    carMap.get(c.getOrderNo()).add(c);
                } else {
                    carMap.put(c.getOrderNo(), Lists.newArrayList(c));
                }
            });
            //生成订单号并批量保存
            orderMap.forEach((integer, dto) -> {
                String orderNo = csSendNoService.getNo(SendNoTypeEnum.ORDER);
                List<ImportKeyCustomerOrderCarDto> carList1 = carMap.get(dto.getOrderNo());
                if (!CollectionUtils.isEmpty(carList1)) {
                    BigDecimal totalFee = BigDecimal.ZERO;
                    for (ImportKeyCustomerOrderCarDto c: carList1) {
                        totalFee = totalFee.add(c.getTrunkFee());
                    }
                    //导入大客户订单信息
                    Order order = packKeyCustomerOrderForImport(dto, orderNo, admin, carList1.size(), totalFee);
                    orderDao.insert(order);
                    Long orderId = order.getId();
                    for (int i = 1; i < carList1.size() + 1; i++) {
                        String carNo = csSendNoService.formatNo(orderNo, i, 3);
                        OrderCar car = packKeyCustomerCarForImport(order,
                                carNo, carList1.get(i-1));
                        orderCarDao.insert(car);
                    }
                }
            });
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importPatCustomerOrder(List<ImportPatCustomerOrderDto> orderList,
                                       List<ImportPatCustomerOrderCarDto> carList, Admin admin) {
        if (!CollectionUtils.isEmpty(orderList) && !CollectionUtils.isEmpty(carList)) {
            Map<Integer, ImportPatCustomerOrderDto> orderMap = new HashMap<>();
            Map<Integer, List<ImportPatCustomerOrderCarDto>> carMap = new HashMap<>();
            orderList.forEach(o -> {
                if (!orderMap.containsKey(o.getOrderNo())) {
                    orderMap.put(o.getOrderNo(), o);
                }
            });
            carList.forEach(c -> {
                if (carMap.containsKey(c.getOrderNo())) {
                    carMap.get(c.getOrderNo()).add(c);
                } else {
                    carMap.put(c.getOrderNo(), Lists.newArrayList(c));
                }
            });
            //生成订单号并批量保存
            orderMap.forEach((integer, dto) -> {
                //保存合伙人及车辆信息
                String orderNo = csSendNoService.getNo(SendNoTypeEnum.ORDER);
                List<ImportPatCustomerOrderCarDto> carList1 = carMap.get(dto.getOrderNo());
                if (!CollectionUtils.isEmpty(carList1)) {
                    Order order = packPatCustomerOrderForImport(dto, orderNo, admin,
                            carList1.size(), dto.getOrderFee());
                    orderDao.insert(order);
                    for (int i = 1; i < carList1.size() + 1; i++) {
                        String carNo = csSendNoService.formatNo(orderNo, i, 3);
                        OrderCar car = packPatCustomerCarForImport(order,
                                carNo, carList1.get(i-1));
                        orderCarDao.insert(car);
                    }
                }
            });
        }
    }


    @Override
    public OrderCarVo getCarVoById(Long orderCarId) {
        OrderCarVo vo = orderCarDao.findVoById(orderCarId);
        if(vo == null){
            return vo;
        }
        Admin admin = csAdminService.findLoop(vo.getEndStoreId());
        if(admin != null){
            vo.setEndStoreLooplinkUserId(admin.getId());
            vo.setEndStoreLooplinkName(admin.getName());
            vo.setEndStoreLooplinkPhone(admin.getPhone());
        }
        return vo;
    }

    @Override
    public ResultVo<PageVo<ListOrderVo>> listForHhr(ListOrderDto paramsDto) {
        //分页查询
        PageHelper.startPage(paramsDto.getCurrentPage(), paramsDto.getPageSize(), true);
        List<ListOrderVo> list = orderDao.findListSelective(paramsDto);
        PageInfo<ListOrderVo> pageInfo = new PageInfo<>(list);
        if (paramsDto.getCurrentPage() > pageInfo.getPages()) {
            pageInfo.setList(null);
        }
        //查询统计
        Map<String, Object> countInfo = orderDao.countForAllTab(paramsDto);
        return BaseResultUtil.success(pageInfo, countInfo);
    }


    @Override
    public ResultVo<DispatchAddCarVo> getWaybillCarEndpoint(ComputeCarEndpointDto reqDto) {
        DispatchAddCarVo dispatchAddCarVo = new DispatchAddCarVo();
        Store store = storeDao.selectById(2L);
        //业务范围
        List<OrderCarVo> list = orderCarDao.findVoListByIds(reqDto.getOrderCarIdList());
        List<WaybillCarVo> childList = new ArrayList<>();
        if(CollectionUtils.isEmpty(list)){
            return BaseResultUtil.fail("车辆不存在");
        }
        for (OrderCarVo orderCarVo : list) {
            if(orderCarVo == null){
                continue;
            }
            WaybillCarVo waybillCarVo = new WaybillCarVo();
            copyOrderCarToWaybillCar(orderCarVo, waybillCarVo);
            Long expectStartTime = orderCarVo.getExpectStartDate();
          /*  if(expectStartTime == null || expectStartTime < LocalDateTimeUtil.getMillisByLDT()){

            }*/
            waybillCarVo.setExpectStartTime(orderCarVo.getExpectStartDate());
            //查询waybillcar最后一条记录，重置
            FullWaybillCar prevWaybillCar = waybillCarDao.findLastPrevByBelongStoreId(orderCarVo.getId(), store.getId());
            if(prevWaybillCar != null){
                copyPrevInfo(prevWaybillCar, waybillCarVo);
                waybillCarVo.setStartFixedFlag(true);
            }else{
                //查询城市数据
                FullCity fullCity = csCityService.findFullCity(orderCarVo.getStartAreaCode(), CityLevelEnum.PROVINCE);
                if(fullCity != null){
                    copyStartToWaybillCarVo(fullCity, waybillCarVo);
                }
                waybillCarVo.setStartFixedFlag(true);
            }
            FullWaybillCar nextWaybillCar = waybillCarDao.findLastNextByBelongStoreId(orderCarVo.getId(), store.getId());
            //查询
            if(nextWaybillCar != null){
                copyNextInfo(nextWaybillCar, waybillCarVo);
                waybillCarVo.setEndFixedFlag(true);
            }else{
                //查询城市数据
                FullCity fullCity = csCityService.findFullCity(orderCarVo.getEndAreaCode(), CityLevelEnum.PROVINCE);
                if(fullCity != null){
                    copyEndToWaybillCarVo(fullCity, waybillCarVo);
                }
                waybillCarVo.setEndFixedFlag(false);
            }
            //查询线路价卡
            Line line = csLineService.getLineByCity(waybillCarVo.getStartCityCode(), waybillCarVo.getEndCityCode(), true);
            if(line != null){
                waybillCarVo.setLineFreightFee(line.getDefaultFreightFee());
                waybillCarVo.setLineId(line.getId());
                waybillCarVo.setHasLine(true);
            }
            childList.add(waybillCarVo);
        }
        dispatchAddCarVo.setList(childList);
        Set<String> citySet = new HashSet<>();
        for (WaybillCarVo waybillCarVo : childList) {
            citySet.add(waybillCarVo.getStartCity());
            citySet.add(waybillCarVo.getEndCity());
        }
        //计算推荐线路
        List<String> guideLines = csLineNodeService.getGuideLine(citySet, store.getCity());
        dispatchAddCarVo.setGuideLine(guideLines == null ? store.getCity() : guideLines.get(0));
        return BaseResultUtil.success(dispatchAddCarVo);
    }

    private void copyStartToWaybillCarVo(FullCity fullCity, WaybillCarVo waybillCarVo) {
        waybillCarVo.setStartProvince(fullCity.getProvince());
        waybillCarVo.setStartProvinceCode(fullCity.getProvinceCode());
        waybillCarVo.setStartCity(fullCity.getCity());
        waybillCarVo.setStartCityCode(fullCity.getCityCode());
        waybillCarVo.setStartArea(fullCity.getArea());
        waybillCarVo.setStartAreaCode(fullCity.getAreaCode());
    }

    private void copyEndToWaybillCarVo(FullCity fullCity, WaybillCarVo waybillCarVo) {
        waybillCarVo.setEndProvince(fullCity.getProvince());
        waybillCarVo.setEndProvinceCode(fullCity.getProvinceCode());
        waybillCarVo.setEndCity(fullCity.getCity());
        waybillCarVo.setEndCityCode(fullCity.getCityCode());
        waybillCarVo.setEndArea(fullCity.getArea());
        waybillCarVo.setEndAreaCode(fullCity.getAreaCode());
    }

    private void copyOrderCarToWaybillCar(OrderCarVo orderCarVo, WaybillCarVo waybillCarVo) {
        BeanUtils.copyProperties(orderCarVo, waybillCarVo);
        waybillCarVo.setOrderCarId(orderCarVo.getId());
        waybillCarVo.setOrderCarNo(orderCarVo.getNo());
        waybillCarVo.setFreightFee(orderCarVo.getTrunkFee());
        waybillCarVo.setLineId(orderCarVo.getLineId());
        waybillCarVo.setExpectStartTime(orderCarVo.getExpectStartDate());
        waybillCarVo.setLoadLinkName(orderCarVo.getPickContactName());
        waybillCarVo.setLoadLinkPhone(orderCarVo.getPickContactPhone());
        waybillCarVo.setUnloadLinkName(orderCarVo.getBackContactName());
        waybillCarVo.setUnloadLinkPhone(orderCarVo.getBackContactPhone());
    }

    @Override
    public List<ListOrderChangeLogVo> getChangeLogVoById(ListOrderChangeLogDto paramsDto) {
        return orderChangeLogDao.findList(paramsDto);
    }

    @Override
    public List<TransportInfoOrderCarVo> getTransportInfoVoById(Long orderId) {
        return orderCarDao.findTransportStateByOrderId(orderId);
    }



    private String getStoreAddress(Store sSto) {
        return (sSto.getProvince() == null ? "" : sSto.getProvince()) +
                (sSto.getCity() == null ? "" : sSto.getCity()) +
                (sSto.getAreaCode() == null ? "" : sSto.getAreaCode()) +
                (sSto.getDetailAddr() == null ? "" : sSto.getDetailAddr());
    }

    private void copyPrevInfo(FullWaybillCar prevWaybillCar, WaybillCarVo carFromToGetVo) {
        if(prevWaybillCar == null){
            return;
        }
        carFromToGetVo.setStartProvince(prevWaybillCar.getEndProvince());
        carFromToGetVo.setStartProvinceCode(prevWaybillCar.getEndProvinceCode());
        carFromToGetVo.setStartCity(prevWaybillCar.getEndCity());
        carFromToGetVo.setStartCityCode(prevWaybillCar.getEndCityCode());
        carFromToGetVo.setStartArea(prevWaybillCar.getEndArea());
        carFromToGetVo.setStartAreaCode(prevWaybillCar.getEndAreaCode());
        carFromToGetVo.setStartAddress(prevWaybillCar.getEndAddress());
        carFromToGetVo.setStartStoreId(prevWaybillCar.getEndStoreId());
        carFromToGetVo.setStartStoreName(prevWaybillCar.getEndStoreName());
        //carFromToGetVo.setStartStoreFullAddress(prevWaybillCar.getEndStoreFullAddress());
        carFromToGetVo.setLoadLinkName(prevWaybillCar.getUnloadLinkName());
        carFromToGetVo.setLoadLinkPhone(prevWaybillCar.getUnloadLinkPhone());
        carFromToGetVo.setLoadLinkUserId(prevWaybillCar.getUnloadLinkUserId());
    }
    private void copyNextInfo(FullWaybillCar nextWaybillCar, WaybillCarVo carFromToGetVo) {
        if(nextWaybillCar == null){
            return;
        }
        carFromToGetVo.setEndProvince(nextWaybillCar.getStartProvince());
        carFromToGetVo.setEndProvinceCode(nextWaybillCar.getStartProvinceCode());
        carFromToGetVo.setEndCity(nextWaybillCar.getStartCity());
        carFromToGetVo.setEndCityCode(nextWaybillCar.getStartCityCode());
        carFromToGetVo.setEndArea(nextWaybillCar.getStartArea());
        carFromToGetVo.setEndAreaCode(nextWaybillCar.getStartAreaCode());
        carFromToGetVo.setEndAddress(nextWaybillCar.getStartAddress());
        carFromToGetVo.setEndStoreId(nextWaybillCar.getStartStoreId());
        carFromToGetVo.setEndStoreName(nextWaybillCar.getStartStoreName());
        //carFromToGetVo.setEndStoreFullAddress(nextWaybillCar.getStartStoreFullAddress());
        carFromToGetVo.setUnloadLinkName(nextWaybillCar.getLoadLinkName());
        carFromToGetVo.setUnloadLinkPhone(nextWaybillCar.getLoadLinkPhone());
        carFromToGetVo.setUnloadLinkUserId(nextWaybillCar.getLoadLinkUserId());
    }


    @Override
    public ResultVo<ListVo<Map<String, Object>>> waitDispatchCarCountList() {
        List<Map<String, Object>> list = orderCarDao.countListWaitDispatchCar();
        //查询统计
        Map<String, Object> countInfo = null;
        if (list != null && !list.isEmpty()) {
            countInfo = orderCarDao.countTotalWaitDispatchCar();
        }
        return BaseResultUtil.success(list, countInfo);
    }
    @Override
    public ResultVo<ListVo<Map<String, Object>>> waitDispatchCarCountListV2(BaseWebDto paramsDto) {
        //查询角色业务中心范围
        BizScope bizScope = csSysService.getBizScopeBySysRoleIdNew(paramsDto.getLoginId(), paramsDto.getRoleId(), true);
        if(bizScope == null || bizScope.getCode() == BizScopeEnum.NONE.code){
            return BaseResultUtil.fail("没有数据权限");
        }

        paramsDto.setBizScope(bizScope.getStoreIds());
        List<Map<String, Object>> list = orderCarDao.countListWaitDispatchCarWeb(paramsDto);
        //统计
        Map<String, Object> countInfo = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(list)) {
            Long sum = list.stream().map(m -> m.get("car_num") == null ? 0 : (Long) m.get("car_num")).reduce(Long::sum).get();
            countInfo.put("totalCount", sum);
        }else{
            countInfo.put("totalCount", 0);
        }
        return BaseResultUtil.success(list, countInfo);
    }

    @Override
    public ResultVo<ListVo<Map<String, Object>>> lineWaitDispatchCarCountListV2(LineWaitCountDto paramsDto) {
        //查询角色业务中心范围
        BizScope bizScope = csSysService.getBizScopeBySysRoleIdNew(paramsDto.getLoginId(), paramsDto.getRoleId(), true);
        if(bizScope == null || bizScope.getCode() == BizScopeEnum.NONE.code){
            return BaseResultUtil.fail("没有数据权限");
        }

        paramsDto.setBizScope(bizScope.getStoreIds());
        //查询统计列表
        List<Map<String, Object>> list = orderCarDao.findLineWaitDispatchCarCountListWeb(paramsDto);
        //查询统计
        Map<String, Object> countInfo = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(list)) {
            Long sum = list.stream().map(m -> m.get("carNum") == null ? 0 : (Long) m.get("carNum")).reduce(Long::sum).get();
            countInfo.put("totalCount", sum);
        }else{
            countInfo.put("totalCount", 0);
        }
        return BaseResultUtil.success(list, countInfo);
    }

    @Override
    public ResultVo<ListVo<Map<String, Object>>> waitDispatchTrunkCarCountList(BaseWebDto paramsDto) {
        //查询角色业务中心范围
        BizScope bizScope = csSysService.getBizScopeBySysRoleIdNew(paramsDto.getLoginId(), paramsDto.getRoleId(), true);
        if(bizScope == null || bizScope.getCode() == BizScopeEnum.NONE.code){
            return BaseResultUtil.fail("没有数据权限");
        }
        paramsDto.setBizScope(bizScope.getStoreIds());

        List<Map<String, Object>> list = orderCarDao.countTrunkListWaitDispatchCar(paramsDto);
        //统计
        Map<String, Object> countInfo = Maps.newHashMap();
        countInfo.put("totalCount", CollectionUtils.isEmpty(list) ? 0 : list.size());

        return BaseResultUtil.success(list, countInfo);
    }


    /**
     * 按线路统计待调度车辆（统计列表）
     *
     * @author JPG
     * @since 2019/10/16 10:04
     */
    @Override
    public ResultVo<ListVo<Map<String, Object>>> lineWaitDispatchCarCountList(LineWaitDispatchCountDto paramsDto, List<Long> bizScopeStoreIds) {
        //查询统计列表
        List<Map<String, Object>> list = orderCarDao.findLineWaitDispatchCarCountList(paramsDto);
        //查询统计
        Map<String, Object> countInfo = null;
        if (list != null && !list.isEmpty()) {
            countInfo = orderCarDao.countTotalWaitDispatchCarByStartCity(paramsDto);
        }
        return BaseResultUtil.success(list, countInfo);
    }


    @Override
    public ResultVo<ListVo<Map<String, Object>>> lineWaitDispatchTrunkCarCountList(LineWaitDispatchCountDto paramsDto) {
        //查询角色业务中心范围
        BizScope bizScope = csSysService.getBizScopeBySysRoleIdNew(paramsDto.getLoginId(), paramsDto.getRoleId(), true);
        if(bizScope == null || bizScope.getCode() == BizScopeEnum.NONE.code){
            return BaseResultUtil.fail("没有数据权限");
        }

        paramsDto.setBizScope(bizScope.getStoreIds());

        //查询统计列表
        List<Map<String, Object>> list = orderCarDao.findLineWaitDispatchTrunkCarCountList(paramsDto);
        //统计
        Map<String, Object> countInfo = Maps.newHashMap();
        countInfo.put("totalCount", CollectionUtils.isEmpty(list) ? 0 : list.size());

        return BaseResultUtil.success(list, countInfo);
    }



    @Override
    public ResultVo<PageVo<OrderCarWaitDispatchVo>> waitDispatchCarList(WaitDispatchListOrderCarDto paramsDto) {

        //查询角色业务中心范围
        BizScope bizScope = csSysService.getBizScopeBySysRoleIdNew(paramsDto.getLoginId(), paramsDto.getRoleId(), true);
        if(bizScope == null || bizScope.getCode() == BizScopeEnum.NONE.code){
            return BaseResultUtil.fail("没有数据权限");
        }

        paramsDto.setBizScope(bizScope.getStoreIds());
        PageHelper.startPage(paramsDto.getCurrentPage(), paramsDto.getPageSize(), true);
        List<OrderCarWaitDispatchVo> list = orderCarDao.findWaitDispatchCarListWeb(paramsDto);
        PageInfo<OrderCarWaitDispatchVo> pageInfo = new PageInfo<>(list);
        if (paramsDto.getCurrentPage() > pageInfo.getPages()) {
            pageInfo.setList(null);
        }

        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<List<OrderCarWaitDispatchVo>> waitDispatchCarAllList(WaitDispatchListOrderCarDto dto) {
        //查询角色业务中心范围
        BizScope bizScope = csSysService.getBizScopeBySysRoleIdNew(dto.getLoginId(), dto.getRoleId(), true);
        if(bizScope == null || bizScope.getCode() == BizScopeEnum.NONE.code){
            return BaseResultUtil.fail("没有数据权限");
        }
        dto.setBizScope(bizScope.getStoreIds());
        return BaseResultUtil.success(orderCarDao.findWaitDispatchCarListWeb(dto));
    }

    @Override
    public ResultVo<PageVo<OrderCarWaitDispatchVo>> waitDispatchTrunkCarList(WaitDispatchTrunkDto paramsDto) {
        //查询角色业务中心范围
        BizScope bizScope = csSysService.getBizScopeBySysRoleIdNew(paramsDto.getLoginId(), paramsDto.getRoleId(), true);
        if(bizScope == null || bizScope.getCode() == BizScopeEnum.NONE.code){
            return BaseResultUtil.fail("没有数据权限");
        }
        paramsDto.setBizScope(bizScope.getStoreIds());

        PageHelper.startPage(paramsDto.getCurrentPage(), paramsDto.getPageSize(), true);
        List<OrderCarWaitDispatchVo> list = orderCarDao.findWaitDispatchTrunkCarList(paramsDto);
        PageInfo<OrderCarWaitDispatchVo> pageInfo = new PageInfo<>(list);
        if (paramsDto.getCurrentPage() > pageInfo.getPages()) {
            pageInfo.setList(null);
        }
        return BaseResultUtil.success(pageInfo);
    }


    @Override
    public OrderVo getVoById(Long orderId) {
        OrderVo orderVo = orderDao.findVoById(orderId);
        if(orderVo == null){
            return null;
        }
        List<OrderCar> list = orderCarDao.findListByOrderId(orderId);
        orderVo.setOrderCarList(list);
        return orderVo;
    }

    @Override
    public ResultVo<PageVo<ListOrderVo>> list(ListOrderDto paramsDto) {

        //查询角色业务中心范围
        BizScope bizScope = csSysService.getBizScopeBySysRoleIdNew(paramsDto.getLoginId(), paramsDto.getRoleId(), true);
        if(bizScope == null || BizScopeEnum.NONE.code == bizScope.getCode()){
            return BaseResultUtil.fail("没有数据权限");
        }
        paramsDto.setBizScope(bizScope.getCode() == 0 ? null : bizScope.getStoreIds());

        //分页查询
        PageHelper.startPage(paramsDto.getCurrentPage(), paramsDto.getPageSize(), true);
        List<ListOrderVo> list = orderDao.findListSelective(paramsDto);
        PageInfo<ListOrderVo> pageInfo = new PageInfo<>(list);
        if (paramsDto.getCurrentPage() > pageInfo.getPages()) {
            pageInfo.setList(null);
        }
        //查询统计
        Map<String, Object> countInfo = orderDao.countForAllTab(paramsDto);
        return BaseResultUtil.success(pageInfo, countInfo);
    }


    @Override
    public ResultVo<List<ListOrderVo>> listAll(ListOrderDto dto) {
        //查询角色业务中心范围
        BizScope bizScope = csSysService.getBizScopeBySysRoleIdNew(dto.getLoginId(), dto.getRoleId(), true);
        if(bizScope == null || BizScopeEnum.NONE.code == bizScope.getCode()){
            return BaseResultUtil.fail("没有数据权限");
        }
        dto.setBizScope(bizScope.getCode() == 0 ? null : bizScope.getStoreIds());

        return BaseResultUtil.success(orderDao.findListSelective(dto));
    }

    @Override
    public ResultVo<PageVo<ListOrderCarVo>> carlist(ListOrderCarDto paramsDto) {

        //查询角色业务中心范围
        BizScope bizScope = csSysService.getBizScopeBySysRoleIdNew(paramsDto.getLoginId(), paramsDto.getRoleId(), true);
        if(bizScope == null || bizScope.getCode() == BizScopeEnum.NONE.code){
            return BaseResultUtil.fail("没有数据权限");
        }
        paramsDto.setBizScope(bizScope.getCode() == 0 ? null : bizScope.getStoreIds());

        PageHelper.startPage(paramsDto.getCurrentPage(), paramsDto.getPageSize(), true);
        List<ListOrderCarVo> list = orderCarDao.findListSelective(paramsDto);
        PageInfo<ListOrderCarVo> pageInfo = new PageInfo<>(list);
        if (paramsDto.getCurrentPage() > pageInfo.getPages()) {
            pageInfo.setList(null);
        }

        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public List<ListOrderCarVo> carListAll(ListOrderCarDto dto) {
        List<ListOrderCarVo> list = orderCarDao.findListSelective(dto);
       /* list.forEach(vo -> {
            vo.setTotalFee(MoneyUtil.yuanToFen(vo.getTotalFee()));
        });*/
        return list;
    }

    /**
     * 为Customer包装Order信息
     * @param dto   Order导入主体dto
     * @param orderNo 订单号
     * @param admin 操作员
     * @param totalFee 总费用
     * @return
     */
    private Order packCustomerOrderForImport(ImportCustomerOrderDto dto, String orderNo,
                                             Admin admin, int carNum, BigDecimal totalFee) {

        //验证用户
        Customer customer = csCustomerService.getByPhone(dto.getCustomerPhone(), true);
        if (customer == null) {
            ResultVo<Customer> res = csCustomerService.saveCustomer(dto.getCustomerPhone(), dto.getCustomerName(), admin.getId());
            if(res.getCode() == ResultEnum.SUCCESS.getCode()){
                customer = res.getData();
            }else{
                throw new ParameterException(res.getMsg());
            }
        }
        Order order = new Order();
        order.setNo(orderNo);
        order.setState(OrderStateEnum.WAIT_SUBMIT.code);
        int pickType = 0;
        switch (dto.getPickType()) {
            case "自送":
                pickType = 1;
                break;
            case "代驾提车":
                pickType = 2;
                break;
            case "拖车提车":
                pickType = 3;
                break;
            case "物流提车":
                pickType = 4;
        }
        order.setPickType(pickType);
        order.setPickContactName(dto.getPickPerson());
        order.setPickContactPhone(dto.getPickPhone());
        int backType = 0;
        switch (dto.getDeliveryType()) {
            case "自提":
                backType = 1;
                break;
            case "代驾提车":
                backType = 2;
                break;
            case "拖车提车":
                backType = 3;
                break;
            case "物流提车":
                backType = 4;
        }
        order.setBackType(backType);
        order.setBackContactName(dto.getDeliveryPerson());
        order.setBackContactPhone(dto.getDeliveryPhone());
        //订单来源：1WEB管理后台
        order.setSource(1);
        order.setCarNum(carNum);
        order.setEndArea(dto.getEndArea());
        order.setEndAreaCode(dto.getEndAreaCode());
        order.setEndCity(dto.getEndCity());
        order.setEndCityCode(dto.getEndCityCode());
        order.setEndProvince(dto.getEndProvince());
        order.setEndProvinceCode(dto.getEndProvinceCode());
        order.setStartArea(dto.getStartArea());
        order.setStartAreaCode(dto.getStartAreaCode());
        order.setStartCity(dto.getStartCity());
        order.setStartCityCode(dto.getStartCityCode());
        order.setStartProvince(dto.getStartProvince());
        order.setStartProvinceCode(dto.getStartProvinceCode());
        order.setExpectEndDate(dto.getSendDate().getTime());
        order.setExpectStartDate(dto.getPickDate().getTime());
        order.setRemark("批量导入");
        //创建人信息
        order.setCreateUserId(admin.getId());
        order.setCreateTime(System.currentTimeMillis());
        order.setCreateUserName(admin.getName());
        order.setTotalFee(totalFee);//车辆信息列表：提车费、物流费、送车费总和
        order.setCustomerId(customer.getId());
        order.setCustomerName(customer.getName());
        order.setCustomerPhone(dto.getCustomerPhone());
        order.setCustomerType(customer.getType());
        order.setEndAddress(dto.getDeliveryAddr());
//        order.setEndBelongStoreId();
//        order.setEndStoreId();
//        order.setEndStoreName();
        order.setStartAddress(dto.getPickAddr());
//        order.setFeeShareType();
//        order.setFinishTime();
//        order.setEndLat();
//        order.setEndLng();
//        order.setStartBelongStoreId()
//        order.setAddInsuranceFee();
//        order.setHurryDays();
//        order.setInputStoreId();
//        order.setInputStoreName();
//        order.setInvoiceFlag();
//        order.setInvoiceType();
//        order.setLineId();
        order.setPayType("到付".equals(dto.getPayType())?0: 1);
//        order.setStartStoreId();
//        order.setStartStoreName();
//        order.setWlPayState();
//        order.setWlPayTime();
        //
        Line line = csLineService.getLineByCity(dto.getStartCityCode(), dto.getEndCityCode(), true);
        if(line != null){
            order.setLineId(line.getId());
        }
        csOrderService.fillOrderInputStore(order);
        csOrderService.fillOrderStoreInfo(order, false);
        return order;
    }

    /**
     * Customer car信息包装
     * @param no
     * @param dto
     * @return
     */
    private OrderCar packCustomerCarForImport(Order order, String no,
                                              ImportCustomerOrderCarDto dto) {
        OrderCar car = new OrderCar();
        car.setOrderId(order.getId());
        car.setOrderNo(order.getNo());
        car.setNo(no);
        car.setBrand(dto.getBrand());
        car.setModel(dto.getModel());
        car.setPlateNo(dto.getCarNum());
        car.setVin(dto.getVinCode());
        car.setIsMove("是".equals(dto.getIsMove())?1: 0);
        car.setIsNew("是".equals(dto.getIsNew())?1: 0);
        car.setValuation(dto.getVehicleValue() == null?null: dto.getVehicleValue().intValue());
        car.setState(OrderCarStateEnum.WAIT_ROUTE.code);
        car.setPickFee(dto.getPickFee());
        car.setPickType(order.getPickType());
        car.setBackFee(dto.getBackFee());
        car.setBackType(order.getBackType());
        car.setTrunkFee(dto.getTrunkFee());
        return car;
    }

    /**
     * 大客户导入订单封装
     * @param dto
     * @param orderNo
     * @param admin
     * @param carNum
     * @return
     */
    private Order packKeyCustomerOrderForImport(ImportKeyCustomerOrderDto dto, String orderNo,
                                                Admin admin, int carNum, BigDecimal totalFee) {
        //验证用户
        Customer customer = csCustomerService.getByPhone(dto.getCustomerPhone(), true);
        if (customer == null) {
            ResultVo<Customer> res = csCustomerService.saveCustomer(dto.getCustomerPhone(), dto.getCustomerName(), admin.getId());
            if(res.getCode() == ResultEnum.SUCCESS.getCode()){
                customer = res.getData();
            }else{
                throw new ParameterException(res.getMsg());
            }
        }
        Order order = new Order();
        order.setNo(orderNo);
        order.setState(OrderStateEnum.WAIT_SUBMIT.code);
        int pickType = 0;
        switch (dto.getPickType()) {
            case "自送":
                pickType = 1;
                break;
            case "代驾提车":
                pickType = 2;
                break;
            case "拖车提车":
                pickType = 3;
                break;
            case "物流提车":
                pickType = 4;
        }
        order.setPickType(pickType);
        order.setPickContactName(dto.getPickPerson());
        order.setPickContactPhone(dto.getPickPhone());
        int backType = 0;
        switch (dto.getDeliveryType()) {
            case "自提":
                backType = 1;
                break;
            case "代驾提车":
                backType = 2;
                break;
            case "拖车提车":
                backType = 3;
                break;
            case "物流提车":
                backType = 4;
        }
        order.setBackType(backType);
        order.setBackContactName(dto.getDeliveryPerson());
        order.setBackContactPhone(dto.getDeliveryPhone());
        //订单来源：1WEB管理后台
        order.setSource(ClientEnum.WEB_SERVER.code);
        order.setCarNum(carNum);
        order.setEndArea(dto.getEndArea());
        order.setEndAreaCode(dto.getEndAreaCode());
        order.setEndCity(dto.getEndCity());
        order.setEndCityCode(dto.getEndCityCode());
        order.setEndProvince(dto.getEndProvince());
        order.setEndProvinceCode(dto.getEndProvinceCode());
        order.setStartArea(dto.getStartArea());
        order.setStartAreaCode(dto.getStartAreaCode());
        order.setStartCity(dto.getStartCity());
        order.setStartCityCode(dto.getStartCityCode());
        order.setStartProvince(dto.getStartProvince());
        order.setStartProvinceCode(dto.getStartProvinceCode());
        order.setExpectEndDate(dto.getSendDate().getTime());
        order.setExpectStartDate(dto.getPickDate().getTime());
        //创建人信息
        order.setCreateUserId(admin.getId());
        order.setCreateTime(System.currentTimeMillis());
        order.setCreateUserName(admin.getName());
        order.setTotalFee(totalFee);// 计算规则
        order.setCustomerId(customer.getId());
        order.setCustomerName(customer.getName());
        order.setCustomerPhone(dto.getCustomerPhone());
        order.setCustomerType(customer.getType());
        order.setEndAddress(dto.getDeliveryAddr());
//        order.setEndBelongStoreId();
//        order.setEndStoreId();
//        order.setEndStoreName();
        order.setStartAddress(dto.getPickAddr());
//        order.setFeeShareType();
//        order.setFinishTime();
//        order.setEndLat();
//        order.setEndLng();
//        order.setBackFee();
//        order.setStartBelongStoreId()
//        order.setAddInsuranceFee();
//        order.setHurryDays();
//        order.setInputStoreId();
//        order.setInputStoreName();
//        order.setInvoiceFlag();
//        order.setInvoiceType();
//        order.setLineId();
        order.setPayType("到付".equals(dto.getPayType())?0: 1);
//        order.setStartStoreId();
//        order.setStartStoreName();
//        order.setTrunkFee();
//        order.setWlPayState();
//        order.setWlPayTime();
        Line line = csLineService.getLineByCity(dto.getStartCityCode(), dto.getEndCityCode(), true);
        if(line != null){
            order.setLineId(line.getId());
        }
        csOrderService.fillOrderInputStore(order);
        csOrderService.fillOrderStoreInfo(order, false);
        return order;
    }

    /**
     * 大客户导入车辆封装
     * @param no
     * @param dto
     * @return
     */
    private OrderCar packKeyCustomerCarForImport(Order order, String no,
                                              ImportKeyCustomerOrderCarDto dto) {
        OrderCar car = new OrderCar();
        car.setOrderId(order.getId());
        car.setOrderNo(order.getNo());
        car.setNo(no);
        car.setBrand(dto.getBrand());
        car.setModel(dto.getModel());
        car.setPlateNo(dto.getCarNum());
        car.setVin(dto.getVinCode());
        car.setIsMove("是".equals(dto.getIsMove())?1: 0);
        car.setIsNew("是".equals(dto.getIsNew())?1: 0);
        car.setValuation(dto.getVehicleValue() == null?null: dto.getVehicleValue().intValue());
        car.setState(OrderCarStateEnum.WAIT_ROUTE.code);
        car.setPickType(order.getPickType());
        car.setBackType(order.getBackType());
        car.setTrunkFee(dto.getTrunkFee());
        return car;
    }

    /**
     * 为大客户包装Order信息
     * @param dto   Order导入主体dto
     * @param orderNo 订单号
     * @param admin 操作员
     * @param totalFee 总费用
     * @return
     */
    private Order packPatCustomerOrderForImport(ImportPatCustomerOrderDto dto, String orderNo,
                                             Admin admin, int carNum, BigDecimal totalFee) {
        //验证用户
        Customer customer = csCustomerService.getByPhone(dto.getCustomerPhone(), true);
        if (customer == null) {
            ResultVo<Customer> res = csCustomerService.saveCustomer(dto.getCustomerPhone(), dto.getCustomerName(), admin.getId());
            if(res.getCode() == ResultEnum.SUCCESS.getCode()){
                customer = res.getData();
            }else{
                throw new ParameterException(res.getMsg());
            }
        }
        Order order = new Order();
        order.setNo(orderNo);
        order.setState(OrderStateEnum.WAIT_SUBMIT.code);
        int pickType = 0;
        switch (dto.getPickType()) {
            case "自送":
                pickType = 1;
                break;
            case "代驾提车":
                pickType = 2;
                break;
            case "拖车提车":
                pickType = 3;
                break;
            case "物流提车":
                pickType = 4;
        }
        order.setPickType(pickType);
        order.setPickContactName(dto.getPickPerson());
        order.setPickContactPhone(dto.getPickPhone());
        int backType = 0;
        switch (dto.getDeliveryType()) {
            case "自提":
                backType = 1;
                break;
            case "代驾提车":
                backType = 2;
                break;
            case "拖车提车":
                backType = 3;
                break;
            case "物流提车":
                backType = 4;
        }
        order.setBackType(backType);
        order.setBackContactName(dto.getDeliveryPerson());
        order.setBackContactPhone(dto.getDeliveryPhone());
        //订单来源：1WEB管理后台
        order.setSource(1);
        order.setCarNum(carNum);
        order.setEndArea(dto.getEndArea());
        order.setEndAreaCode(dto.getEndAreaCode());
        order.setEndCity(dto.getEndCity());
        order.setEndCityCode(dto.getEndCityCode());
        order.setEndProvince(dto.getEndProvince());
        order.setEndProvinceCode(dto.getEndProvinceCode());
        order.setStartArea(dto.getStartArea());
        order.setStartAreaCode(dto.getStartAreaCode());
        order.setStartCity(dto.getStartCity());
        order.setStartCityCode(dto.getStartCityCode());
        order.setStartProvince(dto.getStartProvince());
        order.setStartProvinceCode(dto.getStartProvinceCode());
        order.setExpectEndDate(dto.getSendDate().getTime());
        order.setExpectStartDate(dto.getPickDate().getTime());
        //创建人信息
        order.setCreateUserId(admin.getId());
        order.setCreateTime(System.currentTimeMillis());
        order.setCreateUserName(admin.getName());
        order.setTotalFee(totalFee);//车辆信息列表：提车费、物流费、送车费总和
        order.setCustomerId(customer.getId());
        order.setCustomerName(customer.getName());
        order.setCustomerPhone(dto.getCustomerPhone());
        order.setCustomerType(customer.getType());
        order.setEndAddress(dto.getDeliveryAddr());
//        order.setEndBelongStoreId();
//        order.setEndStoreId();
//        order.setEndStoreName();
        order.setStartAddress(dto.getPickAddr());
//        order.setFeeShareType();
//        order.setFinishTime();
//        order.setEndLat();
//        order.setEndLng();
//        order.setBackFee();
//        order.setStartBelongStoreId()
//        order.setAddInsuranceFee();
//        order.setHurryDays();
//        order.setInputStoreId();
//        order.setInputStoreName();
//        order.setInvoiceFlag();
//        order.setInvoiceType();
//        order.setLineId();
        order.setPayType("到付".equals(dto.getPayType())?0: 1);
//        order.setStartStoreId();
//        order.setStartStoreName();
//        order.setTrunkFee();
//        order.setWlPayState();
//        order.setWlPayTime();
        Line line = csLineService.getLineByCity(dto.getStartCityCode(), dto.getEndCityCode(), true);
        if(line != null){
            order.setLineId(line.getId());
        }
        csOrderService.fillOrderInputStore(order);
        csOrderService.fillOrderStoreInfo(order, false);
        return order;
    }

    /**
     * 合伙人 car信息包装
     * @param no
     * @param dto
     * @return
     */
    private OrderCar packPatCustomerCarForImport(Order order, String no,
                                              ImportPatCustomerOrderCarDto dto) {
        OrderCar car = new OrderCar();
        car.setOrderId(order.getId());
        car.setOrderNo(order.getNo());
        car.setNo(no);
        car.setBrand(dto.getBrand());
        car.setModel(dto.getModel());
        car.setPlateNo(dto.getCarNum());
        car.setVin(dto.getVinCode());
        car.setIsMove("是".equals(dto.getIsMove())?1: 0);
        car.setIsNew("是".equals(dto.getIsNew())?1: 0);
        car.setValuation(dto.getVehicleValue() == null?null: dto.getVehicleValue().intValue());
        car.setState(OrderCarStateEnum.WAIT_ROUTE.code);
        car.setPickFee(dto.getPickFee());
        car.setPickType(order.getPickType());
        car.setBackFee(dto.getBackFee());
        car.setBackType(order.getBackType());
        car.setTrunkFee(dto.getTrunkFee());
        return car;
    }
}
