package com.aipg.payresp;

import com.aipg.common.InfoReq;
import com.aipg.common.InfoRsp;
import com.aipg.common.XStreamEx;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test {
	private static Logger log=LoggerFactory.getLogger(Test.class);
	public static void main(String[] args) {

		// String strTemp = "1234,56745,a1,b1";
		// log.debug(strTemp.substring(0, strTemp.indexOf(",", 0)));
		// ProcessServlet s = new ProcessServlet( );

//		String strSendData = "<?xml version=\"1.0\" encoding=\"GBK\"?><AIPG><INFO><TRX_CODE>100001</TRX_CODE><VERSION>02</VERSION></INFO></AIPG>";

//		XStream xstream = new XStreamEx( );
		XStream xstream = new XStreamEx(new DomDriver());
		xstream.alias("AIPG", AIPG.class);
		xstream.alias("INFO", InfoReq.class);
	
		
		xstream.alias("RET_DETAIL", Ret_Detail.class);
		xstream.aliasField("RET_DETAILS", Body.class, "details");

		
//		xstream.aliasField("TRX->CODE1", Info.class, "TRX_CODE");
		
		AIPG g = new AIPG( );
		InfoRsp info = new InfoRsp( );
		info.setTRX_CODE("-----");
		info.setVERSION("03");
		g.setINFO(info);
		
		Body body = new Body( );
//		Trans_Sum transsum = new Trans_Sum( );
		Ret_Detail ret_detail=new Ret_Detail();
//		transsum.setBUSINESS_CODE("00200");
		ret_detail.setSN("speed");
		body.addDetail(ret_detail);
		ret_detail=new Ret_Detail();
		ret_detail.setSN("time");
		body.addDetail(ret_detail);

		
		g.setBODY(body);
		log.debug(xstream.toXML(g).replaceAll("__", "_"));
		
		
	}
}
