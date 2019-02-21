package com.sumavision.tvpay.connector.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("body")
public class Body {
	@XStreamAlias("bankCardNo")
	private String bankCardNo ;
	
	@XStreamAlias("accountName")
	private String accountName;
	
	@XStreamAlias("bankCardType")
	private String bankCardType;
	
	@XStreamAlias("certificateType")
	private String certificateType;
	
	@XStreamAlias("certificateNo")
	private String certificateNo;
	
	@XStreamAlias("mobilePhone")
	private String mobilePhone;
	
	@XStreamAlias("valid")
	private String valid;
	
	@XStreamAlias("cvn2")
	private String cvn2;
	
	@XStreamAlias("pin")
	private String pin;
	
	@XStreamAlias("bankBranch")
	private String bankBranch ;
	
	@XStreamAlias("province")
	private String province;
	
	@XStreamAlias("city")
	private String city;
	
	@XStreamAlias("userId")	
	private String userId ;
	
	@XStreamAlias("bankCode")	
	private String bankCode;
	
	@XStreamAlias("bindId")
	private String bindId ;
	
	@XStreamAlias("currency")
	private String currency;
	
	@XStreamAlias("amount")
	private String amount;
	
	@XStreamAlias("productCategory")
	private String productCategory;
	
	@XStreamAlias("productName")
	private String productName;
	
	@XStreamAlias("productDesc")
	private String productDesc;
	
	@XStreamAlias("bindValid")
	private String bindValid;
	
	@XStreamAlias("refundReason")
	private String refundReason;
	
	@XStreamAlias("oriPayMsgId")
	private String oriPayMsgId ;
	
	@XStreamAlias("oriRefundReqMsgId")
	private String oriRefundReqMsgId;
	
	@XStreamAlias("refundFee")
	private String refundFee;
	
	@XStreamAlias("remainFee")
	private String remainFee;

	@XStreamAlias("oriReqMsgId")
	private String oriReqMsgId;
	
	@XStreamAlias("oriAmount")
	private String oriAmount;
	
	@XStreamAlias("payFee")
	private String payFee ;
	
	@XStreamAlias("orderDate")
	private String orderDate;
	
	@XStreamAlias("payedDate")
	private String payedDate;
	
	@XStreamAlias("oriRespType")
	private String oriRespType;
	
	@XStreamAlias("oriRespCode")
	private String oriRespCode;
	
	@XStreamAlias("oriRespMsg")
	private String oriRespMsg ;

	

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getBankCardType() {
		return bankCardType;
	}

	public void setBankCardType(String bankCardType) {
		this.bankCardType = bankCardType;
	}

	public String getCertificateType() {
		return certificateType;
	}

	public void setCertificateType(String certificateType) {
		this.certificateType = certificateType;
	}

	public String getCertificateNo() {
		return certificateNo;
	}

	public void setCertificateNo(String certificateNo) {
		this.certificateNo = certificateNo;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

	public String getCvn2() {
		return cvn2;
	}

	public void setCvn2(String cvn2) {
		this.cvn2 = cvn2;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getBankBranch() {
		return bankBranch;
	}

	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBindId() {
		return bindId;
	}

	public void setBindId(String bindId) {
		this.bindId = bindId;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	public String getBindValid() {
		return bindValid;
	}

	public void setBindValid(String bindValid) {
		this.bindValid = bindValid;
	}

	public String getRefundReason() {
		return refundReason;
	}

	public void setRefundReason(String refundReason) {
		this.refundReason = refundReason;
	}

	public String getOriPayMsgId() {
		return oriPayMsgId;
	}

	public void setOriPayMsgId(String oriPayMsgId) {
		this.oriPayMsgId = oriPayMsgId;
	}

	public String getOriRefundReqMsgId() {
		return oriRefundReqMsgId;
	}

	public void setOriRefundReqMsgId(String oriRefundReqMsgId) {
		this.oriRefundReqMsgId = oriRefundReqMsgId;
	}

	public String getRefundFee() {
		return refundFee;
	}

	public void setRefundFee(String refundFee) {
		this.refundFee = refundFee;
	}

	public String getRemainFee() {
		return remainFee;
	}

	public void setRemainFee(String remainFee) {
		this.remainFee = remainFee;
	}

	public String getOriReqMsgId() {
		return oriReqMsgId;
	}

	public void setOriReqMsgId(String oriReqMsgId) {
		this.oriReqMsgId = oriReqMsgId;
	}

	public String getOriAmount() {
		return oriAmount;
	}

	public void setOriAmount(String oriAmount) {
		this.oriAmount = oriAmount;
	}

	public String getPayFee() {
		return payFee;
	}

	public void setPayFee(String payFee) {
		this.payFee = payFee;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getPayedDate() {
		return payedDate;
	}

	public void setPayedDate(String payedDate) {
		this.payedDate = payedDate;
	}

	public String getOriRespType() {
		return oriRespType;
	}

	public void setOriRespType(String oriRespType) {
		this.oriRespType = oriRespType;
	}

	public String getOriRespCode() {
		return oriRespCode;
	}

	public void setOriRespCode(String oriRespCode) {
		this.oriRespCode = oriRespCode;
	}

	public String getOriRespMsg() {
		return oriRespMsg;
	}

	public void setOriRespMsg(String oriRespMsg) {
		this.oriRespMsg = oriRespMsg;
	}

}
