package com.yqzl.service.impl;

import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.yqzl.model.Head;
import com.yqzl.model.OutTransferBody;
import com.yqzl.model.request.FundTransferRequest;
import com.yqzl.model.response.FundTransferResponse;
import com.yqzl.service.FundBankClient;
import com.yqzl.util.SocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 交通银行实现类
 *
 * @author RenPL 2020-3-15
 */
@Service
@Slf4j
public class CommBankClientImpl implements FundBankClient {

    @Override
    public ResultVo<FundTransferResponse> doTransfer(FundTransferRequest fundTransferRequest) {
        // 参数校验 + 组装参数
        ResultVo validatResult = validateParams(fundTransferRequest);
        if(ResultEnum.SUCCESS.getCode() != validatResult.getCode()){
            return validatResult;
        }
        Head head =new Head("210201","","","","","","");
        StringBuilder msg = new StringBuilder(head.getHead(head));
        OutTransferBody outTransferBody =  new OutTransferBody("","","","","",
                "","","",new BigDecimal(0),"","","","");
        msg.append(outTransferBody.getOutTransferBody(outTransferBody));
        // 请求银行
        SocketUtil.doSocket(msg.toString());
        // 银行返回报文解析 + 组装返回参数
        return BaseResultUtil.success(new FundTransferResponse());
    }

    private ResultVo validateParams(FundTransferRequest fundTransferRequest) {
    }
}
