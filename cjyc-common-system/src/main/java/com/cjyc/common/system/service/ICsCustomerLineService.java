package com.cjyc.common.system.service;

import com.cjyc.common.model.dto.CommonDto;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.customerLine.CustomerLineVo;

public interface ICsCustomerLineService {

    /**
     * 根据客户id查询历史线路
     * @param dto
     * @return
     */
    ResultVo<PageVo<CustomerLineVo>> queryLinePage(CommonDto dto);


    /**
     * 根据客户手机号和登录id查询历史线路
     * @param dto
     * @return
     */
    ResultVo<PageVo<CustomerLineVo>> findCustomerLine(CommonDto dto);

    //void asyncSaveBatch(Order order);
}
