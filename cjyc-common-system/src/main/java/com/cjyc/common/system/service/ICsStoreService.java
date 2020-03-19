package com.cjyc.common.system.service;

import com.cjyc.common.model.dto.customer.freightBill.FindStoreDto;
import com.cjyc.common.model.dto.salesman.StoreListLoopAdminDto;
import com.cjyc.common.model.entity.Store;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.customerLine.StoreListVo;
import com.cjyc.common.model.vo.salesman.store.StoreLoopAdminVo;
import com.cjyc.common.model.vo.salesman.store.StoreVo;

import java.util.List;

/**
 * 公用业务中心类
 * @author JPG
 */
public interface ICsStoreService {
    /**
     * 查询区县所属业务中心-业务范围
     * @author JPG
     * @since 2019/11/5 9:27
     * @param areaCode
     */
    Store getOneBelongByAreaCode(String areaCode);
    /**
     * 查询区县所属业务中心列表-业务范围
     * @author JPG
     * @since 2019/11/5 9:27
     * @param areaCode
     */
    Store getBelongByAreaCode(String areaCode);

    /**
     * 根据ID查询业务中心
     * @author JPG
     * @since 2019/11/5 17:26
     * @param storeId
     * @param isSearchCache
     */
    Store getById(Long storeId, boolean isSearchCache);

    /**
     * 根据ID查询业务中心覆盖范围
     * @param storeId
     * @return
     */
    List<String> getAreaBizScope(Long storeId);

    /**
     * 查询所有业务中心
     * @author JPG
     * @since 2019/11/7 13:21
     * @param
     */
    List<Store> getAll();
    /**
     * 查询所有业务中心dept_id
     * @author JPG
     * @since 2019/11/7 13:21
     * @param
     */
    List<Long> getAllDeptId();

    Long getBelongStoreId(Long storeId, String areaCode);

    ResultVo<StoreListVo> findStore(FindStoreDto dto);

    Store getOneBelongByCityCode(String cityCode);

    ResultVo<List<StoreVo>> listByAdminId(Long adminId);

    ResultVo<List<StoreLoopAdminVo>> listLoopAdmin(StoreListLoopAdminDto loginId);

    boolean validateStoreParam(Long storeId, String storeName);

}
