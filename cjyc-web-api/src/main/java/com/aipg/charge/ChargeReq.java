package com.aipg.charge;
/**
 * 自定义通联充值对象
 * @author dell
 */
public class ChargeReq {
	private String BUSINESS_CODE;
	private String BANKACCT;
	private String AMOUNT;
	private String CUST_USERID;
	private String SUMMARY;
	private String REMARK;
	
	public String getBANKACCT() {
		return BANKACCT;
	}
	public void setBANKACCT(String bANKACCT) {
		BANKACCT = bANKACCT;
	}
	public String getAMOUNT() {
		return AMOUNT;
	}
	public void setAMOUNT(String aMOUNT) {
		AMOUNT = aMOUNT;
	}
	public String getCUST_USERID() {
		return CUST_USERID;
	}
	public void setCUST_USERID(String cUSTUSERID) {
		CUST_USERID = cUSTUSERID;
	}
	public String getBUSINESS_CODE() {
		return BUSINESS_CODE;
	}
	public void setBUSINESS_CODE(String bUSINESS_CODE) {
		BUSINESS_CODE = bUSINESS_CODE;
	}
	public String getSUMMARY() {
		return SUMMARY;
	}
	public void setSUMMARY(String sUMMARY) {
		SUMMARY = sUMMARY;
	}
	public String getREMARK() {
		return REMARK;
	}
	public void setREMARK(String rEMARK) {
		REMARK = rEMARK;
	}
	
}
