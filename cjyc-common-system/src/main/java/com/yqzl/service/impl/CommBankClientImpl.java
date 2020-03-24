package com.yqzl.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.yqzl.model.Head;
import com.yqzl.model.OutTransferBody;
import com.yqzl.model.request.FundAccountQueryRequest;
import com.yqzl.model.request.FundTransferQueryRequest;
import com.yqzl.model.request.FundTransferRequest;
import com.yqzl.model.response.FundAccountQueryResponse;
import com.yqzl.model.response.FundTransferQueryResponse;
import com.yqzl.model.response.FundTransferResponse;
import com.yqzl.service.FundBankClient;
import com.yqzl.util.SocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
        // 转账参数校验
        ResultVo validateResult = validateParams(fundTransferRequest);
        if (ResultEnum.SUCCESS.getCode() != validateResult.getCode()) {
            return validateResult;
        }
        // 组装请求银行参数
        String bankRequestParams = fillTransferRequestParams(fundTransferRequest);
        // 请求银行
        String bankResponse = SocketUtil.doSocket(bankRequestParams);
        // 银行返回报文解析 + 组装返回参数
        JSONObject jsonObject = (JSONObject) JSONObject.parse(bankResponse);
        FundTransferResponse fundTransferResponse = fillTransferResponseParams(jsonObject);
        return BaseResultUtil.success(fundTransferResponse);
    }

    @Override
    public ResultVo<FundTransferQueryResponse> doTransferQuery(FundTransferQueryRequest fundTransferQueryRequest) {
        return null;
    }

    @Override
    public ResultVo<FundAccountQueryResponse> doAccountQuery(FundAccountQueryRequest fundAccountQueryRequest) {
        return null;
    }

    /**
     * 银行返回报文解析 + 组装返回参数
     *
     * @param jsonObject
     * @return
     */
    private FundTransferResponse fillTransferResponseParams(JSONObject jsonObject) {
        FundTransferResponse fundTransferResponse = new FundTransferResponse();
        return fundTransferResponse;
    }

    /**
     * 组装请求银行参数
     *
     * @param fundTransferRequest
     * @return
     */
    private String fillTransferRequestParams(FundTransferRequest fundTransferRequest) {
        Head head = new Head("210201", "", "", "", "", "", "");
        StringBuilder msg = new StringBuilder(head.getHead(head));
        OutTransferBody outTransferBody = new OutTransferBody("", "", "", "", "",
                "", "", "", new BigDecimal(0), "", "", "", "");
        msg.append(outTransferBody.getOutTransferBody(outTransferBody));
        return msg.toString();
    }

    /**
     * 转账参数校验
     *
     * @param fundTransferRequest
     * @return
     */
    private ResultVo validateParams(FundTransferRequest fundTransferRequest) {

        if (StringUtils.isEmpty(fundTransferRequest.getBankProCode())) {
            return BaseResultUtil.fail("账务中心支持的银行产品编码不能为空！");
        }
        if (StringUtils.isEmpty(fundTransferRequest.getTrCode())) {
            return BaseResultUtil.fail("交易码不能为空！");
        }
        if (StringUtils.isEmpty(fundTransferRequest.getCorpNo())) {
            return BaseResultUtil.fail("企业代码不能为空！");
        }
        if (StringUtils.isEmpty(fundTransferRequest.getUserNo())) {
            return BaseResultUtil.fail("企业用户号不能为空！");
        }
        if (StringUtils.isEmpty(fundTransferRequest.getPayAcno())) {
            return BaseResultUtil.fail("付款人帐号不能为空！");
        }
        if (StringUtils.isEmpty(fundTransferRequest.getPayAcname())) {
            return BaseResultUtil.fail("付款人户名不能为空！");
        }
        if (StringUtils.isEmpty(fundTransferRequest.getRcvBankName())) {
            return BaseResultUtil.fail("收款方行名不能为空！");
        }
        if (StringUtils.isEmpty(fundTransferRequest.getRcvAcno())) {
            return BaseResultUtil.fail("收款人帐号不能为空！");
        }
        if (StringUtils.isEmpty(fundTransferRequest.getRcvAcname())) {
            return BaseResultUtil.fail("收款人户名不能为空！");
        }
        if (StringUtils.isEmpty(fundTransferRequest.getRcvExgCode())) {
            return BaseResultUtil.fail("收款方交换号不能为空！");
        }
        if (StringUtils.isEmpty(fundTransferRequest.getCurCode())) {
            return BaseResultUtil.fail("币种不能为空！");
        }
        if (fundTransferRequest.getAmt() == null) {
            return BaseResultUtil.fail("金额不能为空！");
        }
        if (StringUtils.isEmpty(fundTransferRequest.getCertNo())) {
            return BaseResultUtil.fail("企业凭证编号不能为空！");
        }
        if (StringUtils.isEmpty(fundTransferRequest.getBankFlag())) {
            return BaseResultUtil.fail("银行标志不能为空！");
        }
        return BaseResultUtil.success();
    }
}
