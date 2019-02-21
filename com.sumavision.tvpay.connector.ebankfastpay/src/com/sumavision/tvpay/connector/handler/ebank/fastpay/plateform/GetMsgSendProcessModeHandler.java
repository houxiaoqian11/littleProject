package com.sumavision.tvpay.connector.handler.ebank.fastpay.plateform;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tvpay.connector.Handler;
import com.sumavision.tvpay.connector.HandlerExtends;
import com.sumavision.tvpay.connector.util.Constant;


@HandlerExtends(method = "PLA-CON-035", code = 34L, description = "短信发送处理模式", validate = "off")
public class GetMsgSendProcessModeHandler implements Handler {


	final Logger logger = LoggerFactory.getLogger(this.getClass());

	private Long code;

	private String method;

	@Override
	public long getCode() {
		return code;
	}

	@Override
	public String getMethod() {
		return method;
	}

	@Override
	public void setCode(long l) {
		this.code = l;
	}

	@Override
	public void setMethod(String s) {
		this.method = s;
	}

	@Override
	public Map<String, Object> process(Map<String, Object> params) {
		logger.info("获取短信发送处理模式平台传入参数" + params);

		Map<String, Object> resultMap = null;
		try {
			resultMap = businessProcess(params);
		} catch (Exception e) {
			logger.info("获取短信发送处理模式出现异常，", e.getMessage());
			resultMap.put(Constant.ACTION_RESULT, 1);
			resultMap.put(Constant.ERROR_CODE, "310013601");
			resultMap.put(Constant.ERROR_MESSAGE, "获取短信发送处理模式出现异常");
		}
		logger.info("2获取短信发送处理模式返回平台参数" + resultMap);
		return resultMap;
	}

	private Map<String, Object> businessProcess(Map<String, Object> params) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put(Constant.SMS_SEND_FLG, 1);
		resultMap.put(Constant.ACTION_RESULT, 0);

		return resultMap;
	}


}
