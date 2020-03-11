package com.cjyc.foreign.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.foreign.api.dto.req.OrderSaveReqDto;

/**
 * 订单
 */
public interface IOrderService extends IService<Order>{
    /**
     * 功能描述: 下单
     * @author liuxingxiang
     * @date 2020/3/11
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo<java.lang.String>
     */
    ResultVo<String> saveOrder(OrderSaveReqDto dto);
}
