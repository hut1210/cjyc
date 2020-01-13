package com.cjyc.common.system.service;

import com.cjyc.common.model.dto.web.pingxx.SalesPrePayDto;
import com.cjyc.common.model.dto.web.pingxx.WebOutOfStockDto;
import com.cjyc.common.model.dto.web.pingxx.WebPrePayDto;
import com.cjyc.common.model.dto.customer.pingxx.SweepCodeDto;
import com.cjyc.common.model.dto.customer.pingxx.ValidateSweepCodeDto;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.order.ValidateSweepCodePayVo;
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

    ResultVo<ValidateSweepCodePayVo> validateCarPayState(ValidateSweepCodeDto validateSweepCodeDto, boolean b);

    Charge sweepSalesCode(SweepCodeDto sweepCodeDto) throws RateLimitException, APIException, ChannelException, InvalidRequestException,
            APIConnectionException, AuthenticationException, FileNotFoundException;

    ResultVo allinpayToCarrier(Long waybillId) throws AuthenticationException, InvalidRequestException, APIConnectionException, APIException, ChannelException, RateLimitException, FileNotFoundException ;
    void allinpayToCooperator(Long orderId);

    Charge prePay(WebPrePayDto prePayDto) throws RateLimitException, APIException, ChannelException, InvalidRequestException,
            APIConnectionException, AuthenticationException, FileNotFoundException;

    Charge getOutOfStockQrCode(WebOutOfStockDto webOutOfStockDto) throws RateLimitException, APIException, ChannelException, InvalidRequestException,
            APIConnectionException, AuthenticationException, FileNotFoundException;

    Charge salesPrePay(SalesPrePayDto salesPrePayDto) throws RateLimitException, APIException, ChannelException, InvalidRequestException,
            APIConnectionException, AuthenticationException, FileNotFoundException;


}
