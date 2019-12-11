package com.cjyc.customer.api.service;

import com.Pingxx.model.Order;
import com.cjyc.common.model.dto.customer.order.CarCollectPayDto;
import com.cjyc.common.model.dto.customer.order.CarPayStateDto;
import com.cjyc.common.model.dto.customer.order.ReceiptBatchDto;
import com.cjyc.common.model.dto.customer.pingxx.PrePayDto;
import com.cjyc.common.model.dto.customer.pingxx.SweepCodeDto;
import com.cjyc.common.model.vo.ResultReasonVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.order.ValidateReceiptCarPayVo;
import com.cjyc.customer.api.dto.OrderModel;
import com.pingplusplus.exception.*;
import com.pingplusplus.model.Charge;

import java.io.FileNotFoundException;

/**
 * @Author:Hut
 * @Date:2019/11/20 14:57
 */
public interface IPingPayService {

    /*Charge sweepDriveCode(SweepCodeDto sweepCodeDto) throws RateLimitException, APIException, ChannelException,InvalidRequestException,
            APIConnectionException, AuthenticationException,FileNotFoundException;*/

    Order pay(PrePayDto reqDto);

    /*Charge sweepSalesmanCode(OrderModel om) throws RateLimitException, APIException, ChannelException,InvalidRequestException,
            APIConnectionException, AuthenticationException,FileNotFoundException;*/

    Order prePay(PrePayDto reqDto);

    void cancelOrderRefund(String orderCode);

    ResultVo<ValidateReceiptCarPayVo> validateCarPayState(CarPayStateDto reqDto, boolean b);

    ResultVo carCollectPay(CarCollectPayDto reqDto);

    ResultVo<ResultReasonVo> receiptBatch(ReceiptBatchDto reqDto);
}
