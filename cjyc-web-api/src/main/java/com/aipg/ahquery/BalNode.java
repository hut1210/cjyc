package com.aipg.ahquery;

import com.alibaba.fastjson.annotation.JSONField;

public class BalNode {
	private String PERIODDAY;
	private String BALANCE;
	private String OPENINGBAL;
	private String CLOSINGBAL;
	@JSONField(name = "PERIODDAY")
	public String getPERIODDAY() {
		return PERIODDAY;
	}
	public void setPERIODDAY(String pERIODDAY) {
		PERIODDAY = pERIODDAY;
	}
	@JSONField(name = "BALANCE")
	public String getBALANCE() {
		return BALANCE;
	}
	public void setBALANCE(String bALANCE) {
		BALANCE = bALANCE;
	}
	@JSONField(name = "OPENINGBAL")
	public String getOPENINGBAL() {
		return OPENINGBAL;
	}
	public void setOPENINGBAL(String oPENINGBAL) {
		OPENINGBAL = oPENINGBAL;
	}
	@JSONField(name = "CLOSINGBAL")
	public String getCLOSINGBAL() {
		return CLOSINGBAL;
	}
	public void setCLOSINGBAL(String cLOSINGBAL) {
		CLOSINGBAL = cLOSINGBAL;
	}
}
