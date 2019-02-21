package com.sumavision.tvpay.connector.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("packet")
public class ErrPacket {
	@XStreamAlias("ErrorCode")
	private String errorCode;
	@XStreamAlias("ErrorMsg")
	private String errorMsg;
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
}
