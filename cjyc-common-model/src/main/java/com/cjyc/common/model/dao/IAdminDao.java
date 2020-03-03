package com.cjyc.common.model.dao;

import com.cjyc.common.model.dto.salesman.order.SalesmanQueryDto;
import com.cjyc.common.model.dto.web.mineStore.ListMineSalesmanDto;
import com.cjyc.common.model.dto.web.salesman.AdminPageDto;
import com.cjyc.common.model.dto.web.salesman.AdminPageNewDto;
import com.cjyc.common.model.dto.web.salesman.TypeSalesmanDto;
import com.cjyc.common.model.entity.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.vo.web.admin.AdminPageVo;
import com.cjyc.common.model.vo.web.admin.AdminVo;
import com.cjyc.common.model.vo.web.admin.TypeSalesmanVo;
import com.cjyc.common.model.vo.web.mineStore.MySalesmanVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 韵车后台管理员表 Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
public interface IAdminDao extends BaseMapper<Admin> {

    Admin findByUserId(@Param("userId") Long userId);

    List<Long> findStoreBizScope(@Param("adminId") Long adminId);

    /**
     * 根据承运商id获取Admin
     * @param carrierId
     * @return
     */
    Admin getByCarrierId(@Param("carrierId") Long carrierId);

    List<Admin> findListByUserIds(@Param("userIds") Set<Long> userIds);

    List<TypeSalesmanVo> deliverySalesman(TypeSalesmanDto dto);

    Admin findByPhone(String phone);

    List<AdminPageVo> findList(AdminPageDto paramsDto, Set<Long> collect);

    AdminVo findVoByPhone(String phone);

    Admin findNext(@Param("startStoreId") Long startStoreId, Long valueOf);


    /************************************韵车集成改版 st***********************************/
    List<AdminPageVo> getPageList(@Param("param") AdminPageNewDto dto);

    /**
     * 我的业务中心-我的业务员
     * @param dto
     * @return
     */
    List<MySalesmanVo> getMineSalesmanList(@Param("param") ListMineSalesmanDto dto);

    List<Admin> findListByStoreId(Long storeId);

    List<TypeSalesmanVo> deliverySalesmanNew(TypeSalesmanDto dto);

    List<AdminPageVo>  getPageListForSalesmanApp(@Param("param")SalesmanQueryDto dto);

    Admin findAdminByPhone(String phone);
    /************************************韵车集成改版 ed***********************************/
}
