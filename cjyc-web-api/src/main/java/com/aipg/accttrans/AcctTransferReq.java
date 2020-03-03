package com.aipg.accttrans;

public class AcctTransferReq {
	private String PAYEECUSID;
	private String AMOUNT;
	private String MEMO;
	public String getPAYEECUSID() {
		return PAYEECUSID;
	}
	public void setPAYEECUSID(String pAYEECUSID) {
		PAYEECUSID = pAYEECUSID;
	}
	public String getAMOUNT() {
		return AMOUNT;
	}
	public void setAMOUNT(String aMOUNT) {
		AMOUNT = aMOUNT;
	}
	public String getMEMO() {
		return MEMO;
	}
	public void setMEMO(String mEMO) {
		MEMO = mEMO;
	}
}
