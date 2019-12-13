package com.cjyc.salesman.api.service;

import com.cjyc.common.model.dto.salesman.dispatch.DispatchListDto;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.salesman.dispatch.DispatchListVo;

/**
 * @Description 调度业务接口
 * @Author Liu Xing Xiang
 * @Date 2019/12/11 13:34
 **/
public interface IDispatchService {
    /**
     * 功能描述: 查询所有出发城市-目的地城市的车辆数量
     * @author liuxingxiang
     * @date 2019/12/11
     * @param loginId
     * @return com.cjyc.common.model.vo.ResultVo
     */
    ResultVo getCityCarCount(Long loginId);

    /**
     * 调度列表
     * @return
     */
    ResultVo<PageVo<DispatchListVo>> getPageList(DispatchListDto dto);

    /**
     * 功能描述: 根据车辆编号查询车辆明细
     * @author liuxingxiang
     * @date 2019/12/13
     * @param carNo
     * @return com.cjyc.common.model.vo.ResultVo
     */
    ResultVo getCarDetail(String carNo);
}
