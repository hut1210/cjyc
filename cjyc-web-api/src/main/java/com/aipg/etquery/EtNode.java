package com.aipg.etquery;

import com.alibaba.fastjson.annotation.JSONField;

public class EtNode {
	private String ACCTNO;
	private String VOUCHERNO;
	private String CRDR;
	private String AMOUNT;
	private String TRXID;
	private String PAYCODE;
	private String POSTDATE;
	private String MEMO;
	@JSONField(name = "ACCTNO")
	public String getACCTNO() {
		return ACCTNO;
	}
	public void setACCTNO(String aCCTNO) {
		ACCTNO = aCCTNO;
	}
	@JSONField(name = "VOUCHERNO")
	public String getVOUCHERNO() {
		return VOUCHERNO;
	}
	public void setVOUCHERNO(String vOUCHERNO) {
		VOUCHERNO = vOUCHERNO;
	}
	@JSONField(name = "CRDR")
	public String getCRDR() {
		return CRDR;
	}
	public void setCRDR(String cRDR) {
		CRDR = cRDR;
	}
	@JSONField(name = "AMOUNT")
	public String getAMOUNT() {
		return AMOUNT;
	}
	public void setAMOUNT(String aMOUNT) {
		AMOUNT = aMOUNT;
	}
	public String getTRXID() {
		return TRXID;
	}
	public void setTRXID(String tRXID) {
		TRXID = tRXID;
	}
	public String getPAYCODE() {
		return PAYCODE;
	}
	public void setPAYCODE(String pAYCODE) {
		PAYCODE = pAYCODE;
	}
	@JSONField(name = "POSTDATE")
	public String getPOSTDATE() {
		return POSTDATE;
	}
	public void setPOSTDATE(String pOSTDATE) {
		POSTDATE = pOSTDATE;
	}
	public String getMEMO() {
		return MEMO;
	}
	public void setMEMO(String mEMO) {
		MEMO = mEMO;
	}
}
