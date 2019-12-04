package com.cjyc.customer.api.service;

import com.cjyc.common.model.dto.customer.pingxx.PrePayDto;
import com.cjyc.common.system.entity.PingCharge;
import com.cjyc.customer.api.dto.OrderModel;
import com.pingplusplus.exception.*;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.Order;

import java.io.FileNotFoundException;

/**
 * @Author:Hut
 * @Date:2019/11/20 14:57
 */
public interface IPingPayService {

    Charge sweepDriveCode(OrderModel om) throws RateLimitException, APIException, ChannelException,InvalidRequestException,
            APIConnectionException, AuthenticationException,FileNotFoundException;

    Order pay(PrePayDto reqDto);

    Charge sweepSalesmanCode(OrderModel om) throws RateLimitException, APIException, ChannelException,InvalidRequestException,
            APIConnectionException, AuthenticationException,FileNotFoundException;

    PingCharge prePay(PrePayDto reqDto);

    void cancelOrderRefund(String orderCode);

}
