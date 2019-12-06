package com.cjyc.customer.api.service;

import com.cjyc.common.model.dto.customer.order.CarCollectPayDto;
import com.cjyc.common.model.dto.customer.order.CarPayStateDto;
import com.cjyc.common.model.dto.customer.order.ReceiptBatchDto;
import com.cjyc.common.model.dto.customer.pingxx.PrePayDto;
import com.cjyc.common.model.vo.ResultReasonVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.order.ValidateReceiptCarPayVo;
import com.cjyc.common.system.entity.PingCharge;
import com.cjyc.customer.api.dto.OrderModel;
import com.pingplusplus.exception.*;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.Order;

import java.io.FileNotFoundException;
import java.util.Map;

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

    ResultVo<ValidateReceiptCarPayVo> validateCarPayState(CarPayStateDto reqDto, boolean b);

    ResultVo<Map<String, Object>> carCollectPay(CarCollectPayDto reqDto);

    ResultVo<ResultReasonVo> receiptBatch(ReceiptBatchDto reqDto);
}
