package com.cjyc.common.system.service;

import com.cjyc.common.model.dto.customer.pingxx.SweepCodeDto;
import com.pingplusplus.exception.*;
import com.pingplusplus.model.Charge;

import java.io.FileNotFoundException;

/**
 * @Author: Hut
 * @Date: 2019/12/9 19:27
 */
public interface ICsPingPayService {
    Charge sweepDriveCode(SweepCodeDto sweepCodeDto) throws RateLimitException, APIException, ChannelException, InvalidRequestException,
            APIConnectionException, AuthenticationException, FileNotFoundException;
}
