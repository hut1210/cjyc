package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.carrier.*;
import com.cjyc.common.model.vo.ResultVo;
/**
 *  @author: zj
 *  @Date: 2019/10/18 15:22
 *  @Description:承运商管理接口
 */
public interface ICarrierService {

    /**
     * 根据手机号判断该承运商是否已添加
     * @param linkmanPhone
     * @return
     */
    ResultVo existCarrier(String linkmanPhone);
    /**
     * 添加承运商
     * @param dto
     * @return
     */
    ResultVo saveCarrier(CarrierDto dto);

    /**
     * 根据承运商id和联系人手机号查看是否在该承运商下
     * @param carrierId
     * @param linkmanPhone
     * @return
     */
    ResultVo existCarrierDriver(Long carrierId,String linkmanPhone);

    /**
     * 更新承运商
     * @param dto
     * @return
     */
    ResultVo modifyCarrier(CarrierDto dto);

    /**
     * 根据条件查询承运商
     * @param dto
     * @return
     */
    ResultVo findCarrier(SeleCarrierDto dto);

    /**
     * 根据承运商id进行审核通过/拒绝/冻结
     * @param dto
     * @return
     */
    ResultVo verifyCarrier(OperateDto dto);

    /**
     * 根据承运商carrierId查看承运商信息
     * @param carrierId
     * @return
     */
    ResultVo showBaseCarrier(Long carrierId);

    /**
     * 重置承运商超级管理员密码
     * @param id
     * @return
     */
    ResultVo resetPwd(Long id);

    /**
     * 调度承运商信息
     * @param dto
     * @return
     */
    ResultVo dispatchCarrier(DispatchCarrierDto dto);

    /**
     * 调度中心中提车干线调度中代驾和拖车列表
     * @param dto
     * @return
     */
    ResultVo trailDriver(TrailCarrierDto dto);
}
