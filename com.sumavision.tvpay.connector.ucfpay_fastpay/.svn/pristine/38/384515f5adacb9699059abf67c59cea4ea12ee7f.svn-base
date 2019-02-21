package com.sumavision.tvpay.connector.handler.ucfpay.fastpay.plateform;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tvpay.connector.Handler;
import com.sumavision.tvpay.connector.HandlerAutowired;
import com.sumavision.tvpay.connector.HandlerExtends;
import com.sumavision.tvpay.spring.resources.ReloadablePlaceHolderResourceBundleMessageSource;

@HandlerExtends(method = "PLA-CON-060", code = 59L, validate = "off", description = "代扣鉴权" )
public class AuthenticationValidateHandler implements Handler {

	private String method;

	private long code;

	@HandlerAutowired(springName = "propertyConfigurer")
	private ReloadablePlaceHolderResourceBundleMessageSource ppc;


	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void setMethod(String method) {
		this.method = method;
	}

	@Override
	public String getMethod() {
		return method;
	}

	public void setCode(long code) {
		this.code = code;
	}

	public long getCode() {
		return this.code;
	}
	public Map<String, Object> process(Map<String, Object> params) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		return resultMap;
	}


	public ReloadablePlaceHolderResourceBundleMessageSource getPpc() {
		return ppc;
	}

	public void setPpc(ReloadablePlaceHolderResourceBundleMessageSource ppc) {
		this.ppc = ppc;
	}
}
