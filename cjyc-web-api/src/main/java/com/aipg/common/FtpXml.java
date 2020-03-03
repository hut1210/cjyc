package com.aipg.common;

import java.util.ArrayList;
import java.util.List;

public class FtpXml {
	private String tranx;
	private String version;
	private InFTP in;
	private List trxData;
	public String getTranx() {
		return tranx;
	}
	public void setTranx(String tranx) {
		this.tranx = tranx;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public InFTP getIn() {
		return in;
	}
	public void setIn(InFTP in) {
		this.in = in;
	}
	public List getTrxData()
	{
		return trxData;
	}

	public void setTrxData(List trxData)
	{
		this.trxData = trxData;
	}
	public void addTrx(Object o)
	{
		if(o==null) return ;
		if(trxData==null) trxData=new ArrayList();
		trxData.add(o);
	}
	public Object findObj(Class clzx)
	{
		if(trxData==null) return null;
		for(Object ox:trxData)
		{
			if(clzx.isInstance(ox)) return ox;
		}
		return trxObj();
	}
	public Object trxObj()
	{
		if(trxData==null) return null;
		if(!trxData.isEmpty()) return trxData.iterator().next();
		return null;
	}
}
