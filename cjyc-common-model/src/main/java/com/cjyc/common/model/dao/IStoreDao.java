package com.cjyc.common.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.dto.customer.freightBill.FindStoreDto;
import com.cjyc.common.model.entity.Store;
import com.cjyc.common.model.vo.customer.customerLine.BusinessStoreVo;
import com.cjyc.common.model.vo.store.StoreVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 韵车业务中心信息表 Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
public interface IStoreDao extends BaseMapper<Store> {


    List<Store> findByCityCode(String cityCode);

    List<Store> findByAreaCode(String areaCode);

    List<String> findAreaBizScope(Long id);

    List<Store> findBelongByAreaCode(String areaCode);

    Store findOneBelongByAreaCode(String areaCode);

    List<Store> findAll();

    List<StoreVo> findVoAll();

    List<Long> findAllDeptId();

    List<Store> findByIds(@Param("set") Set<Long> storeIds);

    List<StoreVo> findVoByIds(@Param("set") Set<Long> storeIds);

    List<Store> findByName(@Param("storeName") String storeName);

    List<Store> findByNameAndIds(@Param("storeName") String storeName, @Param("storeIds") Set<Long> storeIds);

    List<BusinessStoreVo> findStore(FindStoreDto dto);


}
