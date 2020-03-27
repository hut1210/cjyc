package com.yqzl.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.XML;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.yqzl.model.Head;
import com.yqzl.model.OutAccountQueryBody;
import com.yqzl.model.OutTransferBody;
import com.yqzl.model.OutTransferQueryBody;
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

/**
 * 交通银行实现类
 *
 * @author RenPL 2020-3-15
 */
@Service("commBankClientImpl")
@Slf4j
public class CommBankClientImpl implements FundBankClient {

    /**
     * 转账交易吗
     */
    private static String TRANSFER_TRANS_CODE = "210201";

    /**
     * 转账查询交易吗
     */
    private static String TRANSFER_TRANS_QUERY_CODE = "310204";

    /**
     * 账户交易吗
     */
    private static String TRANSFER_ACCOUNT_QUERY_CODE = "310101";

    @Override
    public ResultVo doTransfer(Object obj) {
        FundTransferRequest request = null;
        if (obj instanceof FundTransferRequest) {
            request = (FundTransferRequest) obj;
        }
        // 转账参数校验
        ResultVo validateResult = validateTransferParams(request);
        if (ResultEnum.SUCCESS.getCode() != validateResult.getCode()) {
            return validateResult;
        }
        // 组装请求银行参数
        String bankRequestParams = fillTransferRequestParams(request);
        // 请求银行
        String bankResponse = SocketUtil.doSocket(bankRequestParams);
        // 银行返回报文解析 + 组装返回参数
        if (!StringUtils.isEmpty(bankResponse)) {
            bankResponse = bankResponse.substring(bankResponse.indexOf("<ap>"));
        }
        JSONObject jsonObject = XML.toJSONObject(bankResponse);
        return fillTransferResponseParams(jsonObject);
    }

    @Override
    public ResultVo doTransferQuery(Object obj) {
        FundTransferQueryRequest request = null;
        if (obj instanceof FundTransferQueryRequest) {
            request = (FundTransferQueryRequest) obj;
        }
        // 转账查询参数校验
        ResultVo validateResult = validateTransferQueryParams(request);
        if (ResultEnum.SUCCESS.getCode() != validateResult.getCode()) {
            return validateResult;
        }
        // 组装请求银行参数
        String bankRequestParams = fillTransferQueryRequestParams(request);
        // 请求银行
        String bankResponse = SocketUtil.doSocket(bankRequestParams);
        // 银行返回报文解析 + 组装返回参数
        if (!StringUtils.isEmpty(bankResponse)) {
            bankResponse = bankResponse.substring(bankResponse.indexOf("<ap>"));
        }
        JSONObject jsonObject = XML.toJSONObject(bankResponse);
        return fillTransferQueryResponseParams(jsonObject);
    }

    /**
     * 转账查询-银行返回报文解析 + 组装返回参数
     *
     * @param jsonObject
     * @return
     */
    private ResultVo fillTransferQueryResponseParams(JSONObject jsonObject) {
        JSONObject apObject = (JSONObject) jsonObject.get("ap");
        JSONObject headObject = (JSONObject) apObject.get("head");
        JSONObject bodyObject = (JSONObject) apObject.get("body");
        // 交易码
        String transCode = (String) headObject.get("particular_code");
        // 交易返回信息
        String transName = (String) headObject.get("particular_info");
        if (!"0000".equals(transCode)) {
            return BaseResultUtil.fail(transName);
        }
        // 返回数据
        String oglSerialNo = (String) bodyObject.get("ogl_serial_no");
        Integer stat = (Integer) bodyObject.get("stat");
        String errMsg = (String) bodyObject.get("err_msg");
        String payAcno = (String) bodyObject.get("pay_acno");
        String payBankNo = (String) bodyObject.get("pay_bank_no");
        String rcvAcno = (String) bodyObject.get("rcv_acno");
        String rcvBankNo = (String) bodyObject.get("rcv_bank_no");
        Double amt = (Double) bodyObject.get("amt");
        Long certNo = (Long) bodyObject.get("cert_no");
        if (stat != 1) {
            log.info("请求出错,错误码：{}， 错误信息：{}", stat, errMsg);
            return BaseResultUtil.fail(errMsg);
        }
        FundTransferQueryResponse response = new FundTransferQueryResponse(oglSerialNo, stat, errMsg, payAcno, payBankNo, rcvAcno, rcvBankNo, amt, certNo);
        return BaseResultUtil.success(response);
    }

    /**
     * 转账查询-组装请求银行参数
     *
     * @param request
     * @return
     */
    private String fillTransferQueryRequestParams(FundTransferQueryRequest request) {
        Head head = new Head(request.getCorpNo(), request.getUserNo(), TRANSFER_TRANS_QUERY_CODE, "", "", "", "", "", "");
        StringBuilder msg = new StringBuilder(head.getHead(head));
        OutTransferQueryBody outTransferQueryBody = new OutTransferQueryBody(request.getQueryFlag(), request.getOglSerialNo());
        msg.append(outTransferQueryBody.getOutTransferQueryBody(outTransferQueryBody));
        return msg.toString();
    }

    /**
     * 转账查询-参数校验
     *
     * @param request
     * @return
     */
    private ResultVo validateTransferQueryParams(FundTransferQueryRequest request) {
        if (request.getQueryFlag() == null) {
            return BaseResultUtil.fail("查询标志不能为空！");
        }
        if (StringUtils.isEmpty(request.getOglSerialNo())) {
            return BaseResultUtil.fail("原流水号不能为空！");
        }
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo doAccountQuery(Object obj) {
        FundAccountQueryRequest request = null;
        if (obj instanceof FundAccountQueryRequest) {
            request = (FundAccountQueryRequest) obj;
        }
        // 账户信息查询参数校验
        ResultVo validateResult = validateAccountQueryParams(request);
        if (ResultEnum.SUCCESS.getCode() != validateResult.getCode()) {
            return validateResult;
        }
        // 组装请求银行参数
        String bankRequestParams = fillAccountQueryRequestParams(request);
        // 请求银行
        String bankResponse = SocketUtil.doSocket(bankRequestParams);
        // 银行返回报文解析 + 组装返回参数
        if (!StringUtils.isEmpty(bankResponse)) {
            bankResponse = bankResponse.substring(bankResponse.indexOf("<ap>"));
        }
        JSONObject jsonObject = XML.toJSONObject(bankResponse);
        return fillAccountQueryResponseParams(jsonObject);
    }

    /**
     * 账户查询-银行返回报文解析 + 组装返回参数
     *
     * @param jsonObject
     * @return
     */
    private ResultVo fillAccountQueryResponseParams(JSONObject jsonObject) {
        JSONObject apObject = (JSONObject) jsonObject.get("ap");
        JSONObject headObject = (JSONObject) apObject.get("head");
        JSONObject bodyObject = (JSONObject) apObject.get("body");
        // 交易码
        String transCode = (String) headObject.get("particular_code");
        // 交易返回信息
        String transName = (String) headObject.get("particular_info");
        if (!"0000".equals(transCode)) {
            log.info("交易码：{}， 交易信息：{}", transCode, transName);
            return BaseResultUtil.fail(transName);
        }
        // 返回数据
        String data = (String) bodyObject.get("serial_record");
        if (!StringUtils.isEmpty(data)) {
            String[] arr = data.split("\\|");
            if (arr != null && arr.length > 19 && !"0".equals(arr[19])) {
                log.info("错误码：{}， 错误信息：{}", arr[19], arr[18]);
                return BaseResultUtil.fail(arr[18]);
            }
        }
        FundAccountQueryResponse fundAccountQueryResponse = new FundAccountQueryResponse(data);
        return BaseResultUtil.success(fundAccountQueryResponse);
    }

    /**
     * 账户查询-组装请求银行参数
     *
     * @param request
     * @return
     */
    private String fillAccountQueryRequestParams(FundAccountQueryRequest request) {
        Head head = new Head(request.getCorpNo(), request.getUserNo(), TRANSFER_ACCOUNT_QUERY_CODE, "", "", "", "", "", "");
        StringBuilder msg = new StringBuilder(head.getHead(head));
        OutAccountQueryBody outAccountQueryBody = new OutAccountQueryBody(request.getAcno());
        msg.append(outAccountQueryBody.getOutAccountQueryBody(outAccountQueryBody));
        return msg.toString();
    }

    /**
     * 账户信息查询参数校验
     *
     * @param request
     * @return
     */
    private ResultVo validateAccountQueryParams(FundAccountQueryRequest request) {
        if (StringUtils.isEmpty(request.getAcno())) {
            return BaseResultUtil.fail("账号不能为空！");
        }
        return BaseResultUtil.success();
    }

    /**
     * 转账-银行返回报文解析 + 组装返回参数
     *
     * @param jsonObject
     * @return
     */
    private ResultVo fillTransferResponseParams(JSONObject jsonObject) {
        JSONObject apObject = (JSONObject) jsonObject.get("ap");
        JSONObject headObject = (JSONObject) apObject.get("head");
        // 交易码
        String transCode = (String) headObject.get("particular_code");
        // 交易返回信息
        String transName = (String) headObject.get("particular_info");
        Integer ansCode = (Integer) headObject.get("ans_code");
        String ansInfo = (String) headObject.get("ans_info");
        // 交易时间
        Integer trTime = (Integer) headObject.get("tr_time");
        // 交易流水号
        String serialNo = (String) headObject.get("serial_no");
        if (!"0000".equals(transCode)) {
            log.info("交易码：{}， 交易信息：{}", transCode, transName);
            return BaseResultUtil.fail(transName);
        }
        if (ansCode != 0) {
            log.info("错误码：{}， 错误信息：{}", ansCode, ansInfo);
            return BaseResultUtil.fail(ansInfo);

        }
        FundTransferResponse fundTransferResponse = new FundTransferResponse(trTime, serialNo);
        return BaseResultUtil.success(fundTransferResponse);
    }

    /**
     * 转账-组装请求银行参数
     *
     * @param request
     * @return
     */
    private String fillTransferRequestParams(FundTransferRequest request) {
        Head head = new Head(request.getCorpNo(), request.getUserNo(), TRANSFER_TRANS_CODE, "", "", "", "", "", "");
        StringBuilder msg = new StringBuilder(head.getHead(head));
        OutTransferBody outTransferBody = new OutTransferBody(request.getPayAcno(), request.getPayAcname(), request.getRcvBankName(), request.getRcvAcno(), request.getRcvAcname(),
                request.getRcvExgCode(), request.getRcvBankNo(), request.getCurCode(), request.getAmt().toString(), request.getCertNo(), request.getSummary(), request.getBankFlag(), request.getAreaFlag());
        msg.append(outTransferBody.getOutTransferBody(outTransferBody));
        return msg.toString();
    }

    /**
     * 转账-参数校验
     *
     * @param request
     * @return
     */
    private ResultVo validateTransferParams(FundTransferRequest request) {

        if (StringUtils.isEmpty(request.getPayAcno())) {
            return BaseResultUtil.fail("付款人帐号不能为空！");
        }
        if (StringUtils.isEmpty(request.getPayAcname())) {
            return BaseResultUtil.fail("付款人户名不能为空！");
        }
        if (StringUtils.isEmpty(request.getRcvBankName())) {
            return BaseResultUtil.fail("收款方行名不能为空！");
        }
        if (StringUtils.isEmpty(request.getRcvAcno())) {
            return BaseResultUtil.fail("收款人帐号不能为空！");
        }
        if (StringUtils.isEmpty(request.getRcvAcname())) {
            return BaseResultUtil.fail("收款人户名不能为空！");
        }
        if (StringUtils.isEmpty(request.getCurCode())) {
            return BaseResultUtil.fail("币种不能为空！");
        }
        if (request.getAmt() == null) {
            return BaseResultUtil.fail("金额不能为空！");
        }
        if (StringUtils.isEmpty(request.getCertNo())) {
            return BaseResultUtil.fail("企业凭证编号不能为空！");
        }
        if (StringUtils.isEmpty(request.getBankFlag())) {
            return BaseResultUtil.fail("银行标志不能为空！");
        }
        return BaseResultUtil.success();
    }
}
