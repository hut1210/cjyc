package com.cjyc.web.api.service.impl;

import com.aipg.acquery.AcNode;
import com.aipg.acquery.AcQueryRep;
import com.aipg.acquery.AcQueryReq;
import com.aipg.common.AipgReq;
import com.aipg.common.AipgRsp;
import com.aipg.common.InfoReq;
import com.aipg.common.XSUtil;
import com.allinpay.XmlTools;
import com.cjyc.web.api.config.TLDFProperty;
import com.cjyc.web.api.service.ITLDFService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Hut
 * @Date: 2020/02/25 11:23
 */
@Service
@Slf4j
public class TLDFServiceImpl implements ITLDFService {

    @Override
    public Map<String, Object> queryAccountMsg() {
        String xml="";
        AipgReq aipg=new AipgReq();
        InfoReq info=makeReq("300000");//交易码
        aipg.setINFO(info);
        AcQueryReq acQueryReq = new AcQueryReq();
        acQueryReq.setACCTNO(TLDFProperty.acctNo);//商户在通联的虚拟账号
        aipg.addTrx(acQueryReq);

        System.out.println(TLDFProperty.acctNo+"   "+aipg.getINFO().getUSER_NAME());
        xml= XmlTools.buildXml(aipg,true);
        System.out.print(xml);
        boolean isTLTFront = false;
        String response = sendToTlt(xml,isTLTFront, TLDFProperty.url);
        return formatAccountMsg(response);
    }

    /**
     * 组装报文头部
     * @param trxcod
     * @return
     *日期：Sep 9, 2012
     */
    private InfoReq makeReq(String trxcod)
    {

        InfoReq info=new InfoReq();
        info.setTRX_CODE(trxcod);
        info.setREQ_SN(TLDFProperty.merchantId+"-"+String.valueOf(System.currentTimeMillis()));
        info.setUSER_NAME(TLDFProperty.all_userName);
        info.setUSER_PASS(TLDFProperty.all_password);
        info.setLEVEL("5");
        info.setDATA_TYPE("2");
        info.setVERSION("04");
        if("300000".equals(trxcod)||"300001".equals(trxcod)||"300003".equals(trxcod)||"REFUND".equals(trxcod)){
            info.setMERCHANT_ID(TLDFProperty.merchantId);
        }
        return info;
    }

    public String sendToTlt(String xml,boolean flag,String url) {
        try{
            if(!flag){
                xml=signMsg(xml);
            }else{
                xml=xml.replaceAll("<SIGNED_MSG></SIGNED_MSG>", "");
            }
            return sendXml(xml,url,flag);
        }catch(Exception e){
            e.printStackTrace();
            if(e.getCause() instanceof ConnectException ||e instanceof ConnectException){
                log.debug("请求链接中断，如果是支付请求，请做交易结果查询，以确认该笔交易是否已被通联受理，避免重复交易");
            }
        }
        return "请求链接中断，如果是支付请求，请做交易结果查询，以确认该笔交易是否已被通联受理，避免重复交易";
    }

    /**
     * 报文签名
     * @param xml
     * @return
     *日期：Sep 9, 2012
     * @throws Exception
     */
    public String signMsg(String xml) throws Exception{
        String pfxPath = this.getClass().getClassLoader().getResource(TLDFProperty.pfxPath).getPath();
        xml= XmlTools.signMsg(xml, pfxPath, TLDFProperty.pfxPassword, false);
        return xml;
    }

    public String sendXml(String xml,String url,boolean isFront) throws UnsupportedEncodingException, Exception{
        log.debug("======================发送报文======================：\n"+xml);
        String resp= XmlTools.send(url,xml);
//		dealRet(resp);
        log.debug("======================响应内容======================") ;
        String tltcerPath = this.getClass().getClassLoader().getResource(TLDFProperty.tltcerPath).getPath();;
        boolean flag= this.verifyMsg(resp, tltcerPath,isFront);
        if(flag){
            log.debug("响应内容验证通过") ;
        }else{
            log.debug("响应内容验证不通过") ;
        }
        return resp;
    }

    /**
     * 验证签名
     * @param msg
     * @return
     *日期：Sep 9, 2012
     * @throws Exception
     */
    public boolean verifyMsg(String msg,String cer,boolean isFront) throws Exception{
        boolean flag= XmlTools.verifySign(msg, cer, false,isFront);
        log.debug("验签结果["+flag+"]") ;
        return flag;
    }

    /**
     * 查询账户信息处理响应
     * @param retXml
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> formatAccountMsg(String retXml){
        String trxcode = null;
        AipgRsp aipgrsp= null;
        //或者交易码
        if (retXml.indexOf("<TRX_CODE>") != -1)
        {
            int end = retXml.indexOf("</TRX_CODE>");
            int begin = end - 6;
            if (begin >= 0) trxcode = retXml.substring(begin, end);
        }
        aipgrsp= XSUtil.parseRsp(retXml);

        List<AcNode> acNodeList = new ArrayList<AcNode>();
        if("300000".equals(trxcode)){//账户信息查询
            if("0000".equals(aipgrsp.getINFO().getRET_CODE())){//提交成功
                List<AcQueryRep> trxData = aipgrsp.getTrxData();
                for(AcQueryRep acQueryRep : trxData){
                    acNodeList = acQueryRep.getDetails();
                }
            } else{
                log.debug("响应码" + aipgrsp.getINFO().getRET_CODE() + "原因：" + aipgrsp.getINFO().getERR_MSG());
            }
        }
        Map<String, Object> retMsg = new HashMap<String,Object>();
        retMsg.put("aipgrsp", aipgrsp);
        if(acNodeList == null || acNodeList.isEmpty()){
            retMsg.put("acNode", new AcNode());
        }else{
            retMsg.put("acNode", acNodeList.get(0));
        }
        return retMsg;
    }

}
