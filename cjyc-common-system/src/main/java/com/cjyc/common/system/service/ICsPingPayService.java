package com.cjyc.common.system.service;

import com.cjyc.common.model.dto.customer.pingxx.SweepCodeDto;
import com.cjyc.common.model.dto.customer.pingxx.ValidateSweepCodeDto;
import com.cjyc.common.model.dto.web.pingxx.SalesPrePayDto;
import com.cjyc.common.model.dto.web.pingxx.WebOutOfStockDto;
import com.cjyc.common.model.dto.web.pingxx.WebPrePayDto;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.order.ValidateSweepCodePayVo;
import com.pingplusplus.exception.*;
import com.pingplusplus.model.Charge;

import java.io.FileNotFoundException;
import java.util.List;

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

    void allinpayToCarrier(Long waybillId) throws AuthenticationException, InvalidRequestException, APIConnectionException, APIException, ChannelException, RateLimitException, FileNotFoundException ;

    ResultVo allinpayToCarrierNew(Long waybillId) throws AuthenticationException, InvalidRequestException, APIConnectionException, APIException, ChannelException, RateLimitException, FileNotFoundException ;

    void allinpayToCooperator(Long orderId) throws FileNotFoundException, RateLimitException, APIException, ChannelException, InvalidRequestException, APIConnectionException, AuthenticationException;

    ResultVo allinpayToCooperatorNew(Long orderId) throws FileNotFoundException, RateLimitException, APIException, ChannelException, InvalidRequestException, APIConnectionException, AuthenticationException;

    Charge prePay(WebPrePayDto prePayDto) throws RateLimitException, APIException, ChannelException, InvalidRequestException,
            APIConnectionException, AuthenticationException, FileNotFoundException;

    Charge getOutOfStockQrCode(WebOutOfStockDto webOutOfStockDto) throws RateLimitException, APIException, ChannelException, InvalidRequestException,
            APIConnectionException, AuthenticationException, FileNotFoundException;

    ResultVo salesPrePay(SalesPrePayDto salesPrePayDto) throws RateLimitException, APIException, ChannelException, InvalidRequestException,
            APIConnectionException, AuthenticationException, FileNotFoundException;

    @Deprecated
    ResultVo unlock(String orderNo);

    @Deprecated
    ResultVo unlockQrcode(String orderCarNo);

    void cancelOrderRefund(Long orderId);

    ResultVo unlockPay(List<String> nos);
}
