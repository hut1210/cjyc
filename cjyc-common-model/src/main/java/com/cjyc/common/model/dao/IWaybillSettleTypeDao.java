package com.cjyc.common.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.dto.salesman.order.SalesmanQueryDto;
import com.cjyc.common.model.dto.web.mineStore.ListMineSalesmanDto;
import com.cjyc.common.model.dto.web.salesman.AdminPageDto;
import com.cjyc.common.model.dto.web.salesman.AdminPageNewDto;
import com.cjyc.common.model.dto.web.salesman.TypeSalesmanDto;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.entity.WaybillSettleType;
import com.cjyc.common.model.vo.web.admin.AdminPageVo;
import com.cjyc.common.model.vo.web.admin.AdminVo;
import com.cjyc.common.model.vo.web.admin.TypeSalesmanVo;
import com.cjyc.common.model.vo.web.mineStore.MySalesmanVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 运单+结算类型中间表 Mapper 接口
 * </p>
 *
 * @author RenPL
 * @since 2020-04-09
 */
public interface IWaybillSettleTypeDao extends BaseMapper<WaybillSettleType> {
}
