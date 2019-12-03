package com.cjyc.customer.api.service;

import com.cjyc.customer.api.dto.OrderModel;
import com.pingplusplus.exception.*;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.Order;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;

/**
 * @Author:Hut
 * @Date:2019/11/20 14:57
 */
public interface IPingPayService {

    Charge sweepDriveCode(OrderModel om) throws RateLimitException, APIException, ChannelException,InvalidRequestException,
            APIConnectionException, AuthenticationException,FileNotFoundException;

    Order pay(HttpServletRequest request, OrderModel om);

    Charge sweepSalesmanCode(OrderModel om) throws RateLimitException, APIException, ChannelException,InvalidRequestException,
            APIConnectionException, AuthenticationException,FileNotFoundException;
}
