package com.sumavision.tvpay.connector.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("packet")
public class XMLPacket {
	@XStreamAlias("transName")
	private String transName;
	@XStreamAlias("Plain")
	private String Plain;
	@XStreamAlias("Signature")
	private String Signature;

	public String getTransName() {
		return transName;
	}

	public void setTransName(String transName) {
		this.transName = transName;
	}

	public String getPlain() {
		return Plain;
	}

	public void setPlain(String plain) {
		Plain = plain;
	}

	public String getSignature() {
		return Signature;
	}

	public void setSignature(String signature) {
		Signature = signature;
	}
	
}
