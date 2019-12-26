package com.cjyc.common.system.service;

import com.pingplusplus.model.Order;
import com.Pingxx.model.PingOrderModel;
import com.pingplusplus.exception.*;

import java.io.FileNotFoundException;

public interface ICsPingxxService {

    Order createOrderByModel(PingOrderModel pm) throws RateLimitException, APIException, ChannelException, InvalidRequestException, APIConnectionException, AuthenticationException, FileNotFoundException;

    Order payOrderByModel(PingOrderModel pm) throws FileNotFoundException, InvalidRequestException, APIException, ChannelException, RateLimitException, APIConnectionException, AuthenticationException;


}
